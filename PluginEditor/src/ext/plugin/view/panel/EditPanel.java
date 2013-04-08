package ext.plugin.view.panel;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.plugin.IPlugin;
import ext.plugin.components.interfaces.IItem;

public abstract class EditPanel extends JPanel implements MouseListener, MouseMotionListener
{

	/**
	 * ID
	 */
	private static final long serialVersionUID = 1706578324645332146L;
	protected int size=2;
	
	public EditPanel()
	{
		super();
	}
	
	public abstract void paintComponent(Graphics g);

	public abstract void setCurrentItem(IItem item);

	public abstract Object getNewInstance(String itemName, Class<IPlugin> itemClass,
			JButton button);
	
	public void setSize(int size)
	{
		this.size=size;
	}
}
