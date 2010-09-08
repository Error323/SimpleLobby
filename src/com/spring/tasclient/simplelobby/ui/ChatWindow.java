package com.spring.tasclient.simplelobby.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
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
import com.spring.tasclient.simplelobby.SimpleLobby;

public class ChatWindow extends JPanel implements DraggableContent, 
		ChangeListener, ActionListener {
	private class Channel extends Window {
		private String mName;
		private JTable mUserTable;
		
		public Channel(String name, ChatUserModel model) {
			setLayout(new BorderLayout());
			mName = name;
			mUserTable = new JTable();

			mOutput.setEditable(false);
			
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
	        JScrollPane jspRight = new JScrollPane(mUserTable);
	        	        
	        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspLeft, jspRight);
	        add(jsp, BorderLayout.CENTER);
	        jsp.setDividerLocation(SimpleLobby.WIDTH-200);
	        jsp.setResizeWeight(0.8);
		}
		
		@Override
		public void Say(String msg, String style) {
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
			mOutput.setCaretPosition(mDoc.getLength());
		}
	}
	private Hashtable<String, Channel> mChannels;
	private Channel mActive, mSystem;
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
		setSize(super.getSize());
		setMinimumSize(super.getMinimumSize());
		setPreferredSize(super.getPreferredSize());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mInput) {
			mChatHandler.Say(mInput.getText(), mActive.mName);
			mInput.setText("");
		}
	}
	
	@Override
	public void addDragListener(DragListener dragListener) {
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);
	}
	
	public void Channel(String channel, String usercount) {
		// TODO Auto-generated method stub
	}

	public void ChannelMsg(String channel, String msg) {
		mChannels.get(channel).Say(msg, "channel");
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

	public Channel CreateChannel(String channel, ChatUserModel cum) {
		mActive = new Channel(channel, cum);
		mChannels.put(mActive.mName, mActive);
		mTabs.add(mActive);
		mTabs.setSelectedComponent(mActive);
		mTabs.setTitleAt(mTabs.getSelectedIndex(), "#" + mActive.mName);
		if (mSystem != null)
			mSystem.Say("Joined #" + channel, "system");
		return mActive;
	}

	public void CreateSystemChannel(String channel, ChatUserModel cum) {
		mSystem = CreateChannel(channel, cum);
		mTabs.setTitleAt(mTabs.getSelectedIndex(), mSystem.mName);
	}

	public void ForceLeaveChannel(String channel, String username, String reason) {
		if (reason.length() > 0)
			reason = " - " + reason;
		mSystem.Say("Left #" + channel + reason, "error");
	}

	public void Joined(String channel, String username) {
		mChannels.get(channel).Say(username + " has joined", "channel");
	}

	public void JoinFailed(String channel, String reason) {
		if (reason.length() > 0)
			reason = " - " + reason;
		mSystem.Say("Could not join #" + channel + reason, "error");
	}

	public void Leave(String channel) {
		mTabs.remove(mChannels.remove(channel));
		mSystem.Say("Left #" + channel, "system");
	}

	public void Left(String channel, String username, String reason) {
		if (reason.length() > 0)
			reason = " (" + reason + ")";
		mChannels.get(channel).Say(username + " has left" + reason, "channel");
	}

	public void LinkHandler(ChatHandler ch) {
		mChatHandler = ch;
		mChatHandler.AttachChatWindow(this);
	}

	public void Motd(String msg) {
		mSystem.Say(msg, "regular");
	}

	public void Said(final String channel, final String username, final String msg) {
		Channel c = mChannels.get(channel);
		
		if (mActive != c) {
			for (int i = 0; i < mTabs.getComponentCount(); i++) {
				if (mTabs.getComponentAt(i) == c) {
					if (msg.regionMatches(0, "Error323", 0, 8)) {
						mTabs.setBackgroundAt(i, Color.RED);
						c.Say("<" + username + "> " + msg, "alert");
						break;
					}
					else if (mTabs.getBackgroundAt(i) != Color.RED) {
						mTabs.setBackgroundAt(i, Color.ORANGE);
					}
					c.Say("<" + username + "> " + msg, "regular");
					break;
				}
			}
		}
		else c.Say("<" + username + "> " + msg, "regular");
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
		mSystem.Say("**" + msg, "system");
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == mTabs) {
			mActive = (Channel) mTabs.getSelectedComponent();
			if (mActive == mSystem)
				mTabs.setTitleAt(mTabs.getSelectedIndex(), mActive.mName);
			else
				mTabs.setBackgroundAt(mTabs.getSelectedIndex(), mTabs.getBackground());
			mActive.add(mInput, BorderLayout.SOUTH);
		}
		mInput.requestFocus();
	}
}
