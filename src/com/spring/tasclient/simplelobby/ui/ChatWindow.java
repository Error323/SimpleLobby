package com.spring.tasclient.simplelobby.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.javadocking.dockable.DraggableContent;
import com.javadocking.drag.DragListener;
import com.spring.tasclient.simplelobby.ChatHandler;
import com.spring.tasclient.simplelobby.ChatUserModel;

public class ChatWindow extends JPanel implements DraggableContent, 
		ChangeListener, ActionListener {
	private Hashtable<String, Channel> mChannels;
	private Channel mActive;
	private JTextField mInput;
	private JTabbedPane mTabs;
	private SimpleDateFormat mFormat, mTopic;
	private ChatHandler mChatHandler;
	
	public ChatWindow() {
		super(new FlowLayout());
		mTabs = new JTabbedPane();
		mChannels = new Hashtable<String, Channel>();
		mFormat = new SimpleDateFormat("[HH:mm]");
		mTopic = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
		mInput = new JTextField();
		
		mTabs.addChangeListener(this);
		mInput.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(mTabs, BorderLayout.CENTER);
	}
	
	public void AttachHandler(ChatHandler ch) {
		mChatHandler = ch;
	}
	
	private class Channel extends JComponent {
		private String mName;
		private JTextPane mOutput;
		private StyledDocument mDoc;
		private JTable mUserTable;
		
		public Channel(String name, ChatUserModel model) {
			setLayout(new BorderLayout());
			mName = name;
			
			mOutput = new JTextPane();
			mUserTable = new JTable();

			mOutput.setEditable(false);
			mOutput.setAutoscrolls(true);
			mDoc = mOutput.getStyledDocument();
			AddStyles(mDoc);
			
			mUserTable.setEnabled(false);
			mUserTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			mUserTable.setAutoCreateColumnsFromModel(false);
			mUserTable.setModel(model);
			mUserTable.setAutoCreateRowSorter(true);
			mUserTable.setAutoscrolls(true);
			for (ChatUserModel.Column c : ChatUserModel.Column.values()) {
				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				TableColumn column = new TableColumn(c.ordinal(), 5, renderer, null);
				mUserTable.addColumn(column);
			}
	        JScrollPane jspLeft = new JScrollPane(mOutput);
	        jspLeft.setVerticalScrollBarPolicy(
	                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	        JScrollPane jspRight = new JScrollPane(mUserTable);
	        jspRight.setVerticalScrollBarPolicy(
	                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        	        
	        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspLeft, jspRight);	        
	        add(jsp, BorderLayout.CENTER);
	        jspLeft.setMinimumSize(new Dimension(800, super.getHeight()));
	        jspRight.setMinimumSize(new Dimension(150, super.getHeight()));
	        jsp.setDividerLocation(jsp.getSize().width
                    - jsp.getInsets().right
                    - jsp.getDividerSize()
                    - 200);
		}
		
		public void Say(String msg, String style) {
			if (mActive != this) {
				for (int i = 0; i < mTabs.getComponentCount(); i++) {
					if (mTabs.getComponentAt(i) == this) {
						mTabs.setTitleAt(i, "!#" + mName);
					}
				}
			}
			
			String lines[] = msg.split("\\\\n");
			for (int i = 0; i < lines.length; i++) {
				try {
					mDoc.insertString(mDoc.getLength(), 
							mFormat.format(Calendar.getInstance().getTime()), 
							mDoc.getStyle("time"));
					
					mDoc.insertString(mDoc.getLength(), 
							" " + lines[i] + "\n", 
							mDoc.getStyle(style));
				} catch (BadLocationException e) {
					System.err.println("Couldn't insert initial text into text pane.");
				}
			}
		}
	}
	
	private void AddStyles(StyledDocument doc) {
		Style def = StyleContext.getDefaultStyleContext().
						getStyle(StyleContext.DEFAULT_STYLE);
		
        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        StyleConstants.setForeground(def, Color.BLACK);
		
        Style s = doc.addStyle("sayex", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Color.BLUE);
        
        s = doc.addStyle("time", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Color.DARK_GRAY);
        
        s = doc.addStyle("error", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Color.RED);

        s = doc.addStyle("system", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setItalic(s, true);
        StyleConstants.setForeground(s, Color.RED);
        
        s = doc.addStyle("topic", regular);
        StyleConstants.setForeground(s, Color.GRAY);
        
        s = doc.addStyle("channel", regular);
        StyleConstants.setItalic(s, true);
        StyleConstants.setForeground(s, Color.BLUE);
	}
	
	@Override
	public void addDragListener(DragListener dragListener) {
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == mTabs) {
			mActive = (Channel) mTabs.getSelectedComponent();
			mTabs.setTitleAt(mTabs.getSelectedIndex(), "#" + mActive.mName);
			mActive.add(mInput, BorderLayout.SOUTH);
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mInput) {
			mChatHandler.Say(mInput.getText(), mActive.mName);
			mInput.setText("");
		}
	}

	public void CreateChannel(String channel, ChatUserModel cum) {
		mActive = new Channel(channel, cum);
		mChannels.put(mActive.mName, mActive);
		mTabs.add(mActive);
		mTabs.setSelectedComponent(mActive);
		mTabs.setTitleAt(mTabs.getSelectedIndex(), "#" + mActive.mName);
		mInput.setFocusable(true);
	}

	public void Channel(String channel, String usercount) {
		// TODO Auto-generated method stub
	}

	public void ChannelMsg(String channel, String msg) {
		Channel c = mChannels.get(channel);
		c.Say(msg, "channel");
	}

	public void ChannelTopic(String channel, String author, long changedtime,
			String topic) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(changedtime);
		Channel c = mChannels.get(channel);
		c.Say("**Channel topic:", "topic");
		c.Say(topic, "topic");
		c.Say("**Set by " + author + " at " + mTopic.format(calendar.getTime()), "topic");
	}

	public void ForceLeaveChannel(String channel, String username, String reason) {
		mChannels.get(channel).Say(
				username + " left (" + reason + ")",
				"channel");
	}

	public void JoinFailed(String channel, String reason) {
		mActive.Say(
				"Could not join " + channel,
				"error");
	}

	public void Motd(String msg) {
//		mActive.Say(msg, "regular");
	}

	public void Said(String channel, String username, String msg) {
		mChannels.get(channel).Say(
				"<" + username + "> " + msg,
				"regular");		
	}

	public void SaidEx(String channel, String username, String msg) {
		mChannels.get(channel).Say(
				"* " + username + " " + msg,
				"sayex");
	}

	public void SaidPrivate(String username, String msg) {
		// TODO Auto-generated method stub
		
	}

	public void SayPrivate(String username, String msg) {
		// TODO Auto-generated method stub
		
	}

	public void ServerMsg(String msg) {
		mActive.Say(msg, "system");
	}

	public void Leave(String channel) {
		mTabs.remove(mChannels.remove(channel));
	}
}
