package main.log;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class FrameLogger extends JFrame implements ILogger, ActionListener
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = -4414582820533659661L;
	
	private JTextPane log=new JTextPane();
	private StyledDocument style=this.log.getStyledDocument();
	private JScrollPane scroll;
	private JButton copyButton=new JButton("Copy");
	private JButton clearButton=new JButton("Clear");
	private JButton quitButton=new JButton("Quit");
    private Style errorStyle = this.log.addStyle("ErrorStyle", null);
    private Style normalStyle = this.log.addStyle("NormalStyle", null);
	
	public FrameLogger(String name)
	{
		super("Journal de "+name);
	    StyleConstants.setForeground(this.errorStyle, Color.red);
	    StyleConstants.setForeground(this.normalStyle, this.log.getForeground());
		this.setSize(500,300);
		this.setPreferredSize(new Dimension(500,300));
		this.setResizable(false);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		final FrameLogger fl = this;
		final String fln = "FrameLogger";
		this.addWindowListener(new WindowAdapter() 
			{
				public void windowClosing(WindowEvent e) 
				{
					System.out.println("Close "+fln+" view");
					fl.dispose();
				}
			});

		JLabel label=new JLabel("Etat de "+name+":");
		label.setPreferredSize(new Dimension(460,15));
		label.setMaximumSize(new Dimension(460,15));

		this.log.setEditable(false);
		this.log.setAutoscrolls(true);
		this.log.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		this.scroll = new JScrollPane(this.log);
		this.scroll.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.scroll.setPreferredSize(new Dimension(490,200));

		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setPreferredSize(new Dimension(490,35));
		buttonPanel.add(this.copyButton);
		buttonPanel.add(this.clearButton);
		buttonPanel.add(this.quitButton);
		this.clearButton.addActionListener(this);
		this.copyButton.addActionListener(this);
		this.quitButton.addActionListener(this);
		
		this.getContentPane().add(label,BorderLayout.NORTH);
		this.getContentPane().add(this.scroll,BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel,BorderLayout.SOUTH);
		// To have the caretPosition of log
		this.setVisible(true);
		this.setVisible(false);
	}
	
	@Override
	public void write(String message)
	{
		this.print(message+"\n");
	}

	@Override
	public void print(String message)
	{
		try
		{
			this.style.insertString(this.style.getLength(), message, this.normalStyle);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
		this.log.setCaretPosition(this.style.getLength());
	}

	@Override
	public void error(String error)
	{
		try
		{
			this.style.insertString(this.style.getLength(), error+"\n", this.errorStyle);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
		this.log.setCaretPosition(this.style.getLength());
	}

	@Override
	public void display()
	{
		this.setVisible(true);
		this.repaint();
		this.log.setCaretPosition(this.style.getLength());
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource().equals(this.clearButton))
		{
			this.log.setText("");
		}
		else if(event.getSource().equals(this.copyButton))
		{
			StringSelection selection=new StringSelection(this.log.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clipboard.setContents(selection, null);
		}
		else if(event.getSource().equals(this.quitButton))
		{
			this.dispose();
		}
	}
	
}
