package ext.plugin.view.panel;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public abstract class EditPanel extends JPanel implements MouseListener, MouseMotionListener
{

	/**
	 * ID
	 */
	private static final long serialVersionUID = 1706578324645332146L;
	
	public EditPanel()
	{
		super();
	}
	
	public abstract void paintComponent(Graphics g);
	
	
}
