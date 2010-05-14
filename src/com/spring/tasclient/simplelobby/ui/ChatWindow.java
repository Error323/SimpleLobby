package com.spring.tasclient.simplelobby.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.javadocking.dockable.DraggableContent;
import com.javadocking.drag.DragListener;
import com.spring.tasclient.simplelobby.IChatListener;

@SuppressWarnings("serial")
public class ChatWindow extends JPanel implements DraggableContent, IChatListener, ChangeListener {
	
	private static final Color bg = new Color(0xEEEEEE);
	
	/**
	 * This class represents a channel and a sidepane with users 
	 */
	private class Channel extends JComponent {
		private String mName;
		private JTextPane mOutput;
		private SimpleDateFormat mFormat;
		private StyledDocument mDoc;
		
		public Channel(String name) {
			mName = name;
			mFormat = new SimpleDateFormat("[HH:mm]");
			
			setLayout(new BorderLayout());
			mOutput = new JTextPane();
			mOutput.setEditable(false);
			mOutput.setAutoscrolls(true);
			mOutput.setBackground(bg);
	        JScrollPane paneScrollPane = new JScrollPane(mOutput);
	        paneScrollPane.setVerticalScrollBarPolicy(
	                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        paneScrollPane.setMinimumSize(new Dimension(800, 600));
			add(mOutput, BorderLayout.CENTER);
			mDoc = mOutput.getStyledDocument();
			AddStyles(mDoc);
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

		public void SayUser(String user, String msg) {
			Say("<"+user+"> "+msg, "regular");
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
	}

	private Hashtable<String, Channel> mChannels;
	private Channel mActive, mSystem;
	private JTextField mInput;
	private JTabbedPane mTabs;

	public ChatWindow(ActionListener al) {
		super(new FlowLayout());
		mTabs = new JTabbedPane();
		mInput = new JTextField();
		mChannels = new Hashtable<String, Channel>();
		mSystem = CreateChannel("system", true);
		
		mTabs.setMinimumSize(new Dimension(640, 480));
		mTabs.addChangeListener(this);
		mInput.addActionListener(al);
		
		JLabel inputLabel = new JLabel("Say: ");
		inputLabel.setLabelFor(mInput);
		
		JPanel inputPane = new JPanel();
		inputPane.setLayout(new BorderLayout());
		inputPane.add(inputLabel, BorderLayout.WEST);
		inputPane.add(mInput, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(mTabs, BorderLayout.CENTER);
		add(inputPane, BorderLayout.SOUTH);
	}
	
	public void Channel(String channel, String usercount) {
		// TODO Auto-generated method stub
		
	}

	public void ChannelMsg(String channel, String msg) {
		mChannels.get(channel).Say(msg, "channel");
	}

	public void ChannelTopic(String channel, String author, String changedtime,
			String topic) {
		CreateChannel(channel, true);
		mChannels.get(channel).Say(topic + " set by " + author, "topic");
	}

	public void Clients(String channel, String clients) {
		// TODO Auto-generated method stub
		
	}

	private Channel CreateChannel(String channel, boolean isActive) {
		if (mChannels.containsKey(channel))
			return mChannels.get(channel);
		
		Channel chan = new Channel(channel);
		mChannels.put(channel, chan);
		mTabs.addTab("#"+channel, chan);
		if (isActive) {
			mTabs.setSelectedComponent(chan);
			mActive = chan;
		}
		
		return chan;
	}

	public void EndOfChannels() {
		// TODO Auto-generated method stub
		
	}

	public void ForceLeaveChannel(String channel, String username, String reason) {
		mSystem.Say("Kicked from " + channel + " by " + username + " (" + reason + ")", "error");
	}

	public void Joined(String channel, String username) {
		mChannels.get(channel).Say(username + " has joined #" + channel, "topic");
	}

	public void JoinFailed(String channel, String reason) {
		mSystem.Say("Failed to join #" + channel + " " + "(" + reason + ")", "error");
	}

	public void JoinSucceeded(String channel) {
		CreateChannel(channel, true);
		mSystem.Say("Joined channel #" + channel, "topic");
	}

	public void Left(String channel, String username, String reason) {
		mChannels.get(channel).Say(
			username + " has left #" + channel + " (" + reason + ")",
			"topic"
		);
	}

	public void Motd(String msg) {
		mSystem.Say(msg, "topic");
	}

	public void Said(String channel, String username, String msg) {
		mChannels.get(channel).SayUser(username, msg);
	}

	public void SaidPrivate(String username, String msg) {

	}

	public void SayPrivate(String username, String msg) {

	}

	public void ServerMsg(String msg) {
		mSystem.Say(msg, "system");
	}
	
	public void SaidEx(String channel, String username, String msg) {
		mChannels.get(channel).Say("* " + username + " " + msg, "sayex");
	}

		/*
		if (e.getSource() == mInput) {
			String s = mInput.getText();
			if (s.length() > 0 && s.charAt(0) == '/') {
				
				String splitted[] = s.substring(1).split(" ");
				if (splitted[0].equals("join") || splitted[0].equals("j")) {
					if (splitted.length > 1) {
						splitted[1].replace("#", "");
						if (splitted.length == 2)
							mChatOut.Join(splitted[1], "");
						else
						if (splitted.length == 3)
							mChatOut.Join(splitted[1], splitted[2]);
					}
				} else
					
				if (splitted[0].equals("leave")) {
					mChatOut.Leave(mActive.mName);
					mChannels.remove(mActive.mName);
					mTabs.remove(this);
				} else
				
				if (splitted[0].equals("me")) {
					mChatOut.SayEx(mActive.mName, s.substring(4));
				}
				
			}
			else {
				mChatOut.Say(mActive.mName, s);
			}
			mInput.setText("");
		}
		*/
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == mTabs) {
			mActive = (Channel) mTabs.getSelectedComponent();
			mTabs.setTitleAt(mTabs.getSelectedIndex(), "#" + mActive.mName);
		}
	}

	public void addDragListener(DragListener dragListener) {
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);
//		label.addMouseListener(dragListener);
//		label.addMouseMotionListener(dragListener);
	}
}
