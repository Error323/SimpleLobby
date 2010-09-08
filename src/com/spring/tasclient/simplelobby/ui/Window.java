package com.spring.tasclient.simplelobby.ui;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class Window extends JPanel {
	protected JTextPane mOutput;
	protected StyledDocument mDoc;

	public Window() {
		mOutput = new JTextPane();
		mDoc = mOutput.getStyledDocument();
		AddStyles(mDoc);
		JScrollPane jsp = new JScrollPane(mOutput);
		add(jsp);
		setSize(super.getSize());
		setMinimumSize(super.getMinimumSize());
		setPreferredSize(super.getPreferredSize());
	}
		
	public void Say(String txt, String style) {
		try {
			mDoc.insertString(mDoc.getLength(), txt, mDoc.getStyle(style));
		} catch (BadLocationException e) {
		}
	}

	private void AddStyles(StyledDocument doc) {
		Style def = StyleContext.getDefaultStyleContext().
						getStyle(StyleContext.DEFAULT_STYLE);
		
        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "DejaVu Sans Mono");
        StyleConstants.setFontSize(def, 11);
        StyleConstants.setForeground(def, Color.BLACK);
		
        Style s = doc.addStyle("sayex", regular);
        StyleConstants.setForeground(s, Color.PINK);
        
        s = doc.addStyle("time", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Color.DARK_GRAY);
        
        s = doc.addStyle("alert", regular);
        StyleConstants.setForeground(s, Color.ORANGE);
        
        s = doc.addStyle("error", regular);
        StyleConstants.setForeground(s, Color.RED);

        s = doc.addStyle("system", regular);
        StyleConstants.setItalic(s, true);
        StyleConstants.setForeground(s, Color.GRAY);
        
        s = doc.addStyle("channel", regular);
        StyleConstants.setForeground(s, Color.GRAY);
        
        s = doc.addStyle("topic", regular);
        StyleConstants.setItalic(s, true);
        StyleConstants.setForeground(s, Color.BLUE);
	}
}
