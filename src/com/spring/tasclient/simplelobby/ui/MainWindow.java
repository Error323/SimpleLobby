package com.spring.tasclient.simplelobby.ui;

import java.awt.BorderLayout;

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
import com.javadocking.model.FloatDockModel;
import com.spring.tasclient.simplelobby.SimpleLobby;

@SuppressWarnings("serial")
public class MainWindow extends JPanel {
	private TabDock mTabDock;
	private Position mPos;
	private JLabel mStatusLabel;
	
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
		
		mStatusLabel = new JLabel();
		mStatusLabel.setSize(main.getWidth()-100, 20);
		mStatusLabel.setBorder(BorderFactory.createEmptyBorder(2, 1, 2, 2));
		
		add(splitDock, BorderLayout.CENTER);
		add(mStatusLabel, BorderLayout.SOUTH);
		SetStatus("Disconnected");
	}
	
	public void AddDockable(String name, JComponent component) {
		Dockable d = new DefaultDockable(name, component, name, null, DockingMode.ALL);
		mTabDock.addDockable(d, mPos);
	}
	
	public void SetStatus(String status) {
		mStatusLabel.setText(SimpleLobby.HUMAN_NAME + "    --    " + status);
	}
}
