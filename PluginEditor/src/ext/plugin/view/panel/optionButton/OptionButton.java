package ext.plugin.view.panel.optionButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public abstract class OptionButton extends JPanel implements MouseListener
{

	/**
	 * ID
	 */
	private static final long serialVersionUID = -4684957809044339449L;
	protected boolean selected;
	protected int value;
	private ArrayList<OptionButton> groupButtons=new ArrayList<OptionButton>();
	
	/**
	 * Constructor
	 */
	public OptionButton(int value)
	{
		super();
		this.value=value;
		this.setPreferredSize(new Dimension(30,20));
		this.setBorder(BorderFactory.createLineBorder(Color.decode("#7A8A99")));
		this.addMouseListener(this);
	}
	
	public OptionButton(int value, String infos)
	{
		super();
		this.value=value;
		this.setToolTipText(infos);
		this.setPreferredSize(new Dimension(30,20));
		this.setBorder(BorderFactory.createLineBorder(Color.decode("#7A8A99")));
		this.addMouseListener(this);
	}
	
	/**
	 * @return the value
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value)
	{
		this.value = value;
	}
	
	public boolean isSelected()
	{
		return this.selected;
	}
	
	public void setSelected(boolean selected)
	{
		if(selected)
		{
			if(!this.groupButtons.isEmpty())
			{
				for(OptionButton button:this.groupButtons)
				{
					button.setSelected(false);
				}
			}
		}
		else
		{
			this.setBorder(BorderFactory.createLineBorder(Color.decode("#7A8A99")));			
		}
		this.selected=selected;
	}
	
	public abstract void paintContent(Graphics g);
	
	public void addExcludedButton(OptionButton button)
	{
		if(!this.groupButtons.contains(button))
		{
			if(!this.groupButtons.isEmpty())
			{
				for(OptionButton excludedbutton:this.groupButtons)
				{
					if(button!=excludedbutton)
					{
						button.addExcludedButton(excludedbutton);
					}
				}
			}
			this.groupButtons.add(button);
			button.groupButtons.add(this);
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		this.paintContent(g);
		GradientPaint gp = new GradientPaint(
			    0, 0, Color.decode("#E6EEF6"),
			    0, 7, Color.decode("#FFFFFF") );
		((Graphics2D) g).setPaint( gp );
		g.fillRect(0, 0, 30, 7);
		gp = new GradientPaint(
			    0, 0, Color.decode("#FFFFFF"),
			    0, 24, Color.decode("#BED3E7") );
		((Graphics2D) g).setPaint( gp );
		g.fillRect(0, 7, 30, 13);
		this.paintContent(g);
		if(this.selected)
		{
			this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,
					Color.decode("#7A8A99"),Color.decode("#475766")));
		}
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		this.setSelected(true);
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#B8CFE5")),
					BorderFactory.createLineBorder(Color.decode("#7A8A99"))),
				BorderFactory.createLineBorder(Color.decode("#B8CFE5"))));
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
		this.setBorder(BorderFactory.createLineBorder(Color.decode("#7A8A99")));
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		this.setBackground(Color.decode("#B8CFE5"));
		Graphics g=this.getGraphics();
		g.setColor(Color.decode("#B8CFE5"));
		g.fillRect(2, 2, 26, 16);
		this.paintContent(g);
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		this.repaint();
	}

}
