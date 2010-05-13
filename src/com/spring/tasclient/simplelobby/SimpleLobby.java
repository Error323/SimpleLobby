package com.spring.tasclient.simplelobby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.javadocking.DockingManager;
import com.javadocking.dock.Position;
import com.javadocking.dock.SplitDock;
import com.javadocking.dock.TabDock;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockingMode;
import com.javadocking.dockable.DraggableContent;
import com.javadocking.drag.DragListener;
import com.javadocking.model.FloatDockModel;
import com.spring.tasclient.simplelobby.ui.Menu;


@SuppressWarnings("serial")
public class SimpleLobby extends JPanel {
	public static final String NAME    = "SimpleLobby";
	public static final String VERSION = "1.0.0";

	private TabDock mTabDock;
	private Position mPos;
	
	public SimpleLobby(JFrame main) {
		super(new BorderLayout());
		
		FloatDockModel dockModel = new FloatDockModel();
		dockModel.addOwner(main.getName(), main);

		// Give the dock model to the docking manager.
		DockingManager.setDockModel(dockModel);
		
		mTabDock = new TabDock();
		SplitDock splitDock = new SplitDock();
		
		// Create the content components.
		TextPanel textPanel1 = new TextPanel("I am window 1.");
		TextPanel textPanel2 = new TextPanel("I am window 2.");
		TextPanel textPanel3 = new TextPanel("I am window 3.");
		TextPanel textPanel4 = new TextPanel("I am window 4.");
		TextPanel textPanel5 = new TextPanel("I am window 5.");
		
		// Create the dockables around the content components.
		Dockable dockable1 = new DefaultDockable("Window1", textPanel1, "Window 1", null, DockingMode.ALL);
		Dockable dockable2 = new DefaultDockable("Window2", textPanel2, "Window 2", null, DockingMode.ALL);
		Dockable dockable3 = new DefaultDockable("Window3", textPanel3, "Window 3", null, DockingMode.ALL);
		Dockable dockable4 = new DefaultDockable("Window4", textPanel4, "Window 4", null, DockingMode.ALL);
		Dockable dockable5 = new DefaultDockable("Window5", textPanel5, "Window 5", null, DockingMode.ALL);
		
		mPos = new Position(0);
		mTabDock.addDockable(dockable1, mPos);
		mTabDock.addDockable(dockable2, mPos);
		mTabDock.addDockable(dockable3, mPos);
		mTabDock.addDockable(dockable4, mPos);
		mTabDock.addDockable(dockable5, mPos);
		
		dockModel.addRootDock("tabs", splitDock, main);
		splitDock.addChildDock(mTabDock, mPos);
		
		add(splitDock, BorderLayout.CENTER);
	}
	
	/**
	 * This is the class for the content.
	 */
	private class TextPanel extends JPanel implements DraggableContent
	{
		
		private JLabel label; 
		
		public TextPanel(String text)
		{
			super(new FlowLayout());
			
			// The panel.
			setMinimumSize(new Dimension(80,80));
			setPreferredSize(new Dimension(150,150));
			setBackground(Color.white);
			setBorder(BorderFactory.createLineBorder(Color.lightGray));
			
			// The label.
			label = new JLabel(text);
			label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			add(label);
		}
		
		// Implementations of DraggableContent.

		public void addDragListener(DragListener dragListener)
		{
			addMouseListener(dragListener);
			addMouseMotionListener(dragListener);
			label.addMouseListener(dragListener);
			label.addMouseMotionListener(dragListener);
		}
	}

	private static void createAndShowGUI() {
        try {
//        	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        JFrame main = new JFrame(NAME + " v" + VERSION);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Menu menu = new Menu();
        main.setJMenuBar(menu);
        
        //Instantiate the controlling class.
        SimpleLobby simplelobby = new SimpleLobby(main);
        main.getContentPane().add(simplelobby);
        
        //Display the window.
        main.pack();
        main.setVisible(true);
		main.setLocation(100, 100);
        main.setSize(1024, 768);
	}
	
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
}
