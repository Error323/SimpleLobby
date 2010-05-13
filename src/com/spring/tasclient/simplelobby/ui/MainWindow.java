package com.spring.tasclient.simplelobby.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

@SuppressWarnings("serial")
public class MainWindow extends JPanel {
	private TabDock mTabDock;
	private Position mPos;
	
	public MainWindow(JFrame main) {
		super(new BorderLayout());
		
		FloatDockModel dockModel = new FloatDockModel();
		dockModel.addOwner(main.getName(), main);

		// Give the dock model to the docking manager.
		DockingManager.setDockModel(dockModel);
		
		mTabDock = new TabDock();
		SplitDock splitDock = new SplitDock();
		
		mPos = new Position(0);
		
		dockModel.addRootDock("tabs", splitDock, main);
		splitDock.addChildDock(mTabDock, mPos);
		
		add(splitDock, BorderLayout.CENTER);
	}
	
	public void AddDockable(String name, JComponent component) {
		Dockable d = new DefaultDockable(name, component, name, null, DockingMode.ALL);
		mTabDock.addDockable(d, mPos);
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
}
