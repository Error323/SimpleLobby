package com.spring.tasclient.simplelobby.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.javadocking.dockable.DraggableContent;
import com.javadocking.drag.DragListener;
import com.spring.tasclient.simplelobby.ChatUserModel;
import com.spring.tasclient.simplelobby.interfaces.IChatWinInterface;

public class ChatWindow extends JPanel implements DraggableContent, 
		ChangeListener, ActionListener, IChatWinInterface {
	private Hashtable<String, Channel> mChannels;
	private Channel mActive;
	private JTextField mInput;
	private JTabbedPane mTabs;
	private SimpleDateFormat mFormat;
	
	public ChatWindow() {
		super(new FlowLayout());
		mTabs = new JTabbedPane();
		mChannels = new Hashtable<String, Channel>();
		mFormat = new SimpleDateFormat("[HH:mm]");
		mInput = new JTextField();
		
		mTabs.addChangeListener(this);
		mInput.addActionListener(this);
		
		
		setLayout(new BorderLayout());
		add(mTabs, BorderLayout.CENTER);
		add(mInput, BorderLayout.SOUTH);
	}
	
	private class Channel extends JComponent {
		private String mName;
		private JTextPane mOutput;
		private StyledDocument mDoc;
		private JTextField mTitle;
		private JTable mUserTable;
		
		public Channel(String name, ChatUserModel model) {
			setLayout(new BorderLayout());
			
			mName = name;
			
			mTitle = new JTextField("Topic field");
			mOutput = new JTextPane();
			mUserTable = new JTable();
			
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
			mTitle.setEditable(false);
			mOutput.setEditable(false);
			mOutput.setAutoscrolls(true);
	        JScrollPane tableScp = new JScrollPane(mUserTable);
	        tableScp.setVerticalScrollBarPolicy(
	                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        JScrollPane paneScrollPane = new JScrollPane(mOutput);
	        paneScrollPane.setVerticalScrollBarPolicy(
	                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			add(paneScrollPane, BorderLayout.CENTER);
			add(tableScp, BorderLayout.EAST);
			add(mTitle, BorderLayout.NORTH);
			mDoc = mOutput.getStyledDocument();
			AddStyles(mDoc);
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
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void CreateChannel(String channel, ChatUserModel cum) {
		mActive = new Channel(channel, cum);
		mChannels.put(channel, mActive);
		mTabs.add(mActive);
		mTabs.setSelectedComponent(mActive);
	}
}
