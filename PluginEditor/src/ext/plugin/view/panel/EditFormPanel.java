package ext.plugin.view.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;

import main.ICore;
import main.plugin.IPlugin;
import ext.plugin.components.IForm;
import ext.plugin.components.IItem;
import ext.plugin.components.PluginFormPoint;


public class EditFormPanel extends EditPanel
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = -3748095472208214156L;

	public static final Cursor notAllowedCursor=Toolkit.getDefaultToolkit().createCustomCursor(
			Toolkit.getDefaultToolkit().getImage("img/not-allowed_cursor.png"),
			new Point(0,0), "not-allowed");
	
	private ArrayList<IForm> forms=new ArrayList<IForm>();
	private IForm currentForm=null; 
	private int[] lastPos=new int[]{0,0};
	private ICore core=null;
	private Color backgroundColor;
	private Color foregroundColor;
	private int drawType=IForm.NORMAL_DRAW;

	public EditFormPanel(ICore core)
	{
		super();
		this.core=core;
		super.addMouseListener(this);
		super.addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		g.setColor(Color.decode("#FFFFFF"));
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		for(IForm f:this.forms)
		{
			f.draw(g);			
		}
	}
	
	public void setForegroundColor(Color foregroundColor)
	{
		this.foregroundColor=foregroundColor;
	}

	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor=backgroundColor;
	}

	@Override
	public void setCurrentItem(IItem form)
	{
		this.currentForm=(IForm)form;
	}

	@Override
	public Object getNewInstance(String itemName,
			Class<IPlugin> itemClass, JButton itemButton)
	{
		Object o=this.core.getPluginInstance(itemName,itemClass,false);
		if(o==null)
		{
			o=new PluginFormPoint();
		}
		((IForm)o).setButton(itemButton);
		((IForm)o).setForegroundColor(this.foregroundColor);
		((IForm)o).setBackgroundColor(this.backgroundColor);
		((IForm)o).setDrawMode(this.drawType);
		((IForm)o).setFill(this.size);
		return o;
	}
	
	/**
	 * @return the drawType
	 */
	public int getDrawType()
	{
		return drawType;
	}

	/**
	 * @param drawType the drawType to set
	 */
	public void setDrawType(int drawType)
	{
		this.drawType = drawType;
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		if(this.currentForm!=null)
		{
			this.setCursor(this.currentForm.getCursor());
		}
		else
		{
			if(this.forms.isEmpty())
			{
				this.setCursor(EditFormPanel.notAllowedCursor);
			}
			else
			{
				this.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(MouseEvent event)
	{
		if(this.currentForm!=null)
		{
			this.currentForm=(IForm) this.getNewInstance(this.currentForm.getName(),
					(Class<IPlugin>)this.currentForm.getClass(),this.currentForm.getButton());
			this.lastPos=new int[]{event.getX(),event.getY()};
			this.currentForm.setX(event.getX());
			this.currentForm.setY(event.getY());
			this.forms.add(this.currentForm);
			this.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		if(this.currentForm!=null)
		{
			this.setCursor(this.currentForm.getCursor());
		}
		else
		{
			if(this.forms.isEmpty())
			{
				this.setCursor(EditFormPanel.notAllowedCursor);
			}
			else
			{
				this.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		if(this.currentForm!=null&&this.currentForm.isDraggable())
		{
			int width=Math.abs(event.getX()-lastPos[0]);
			int height=Math.abs(event.getY()-lastPos[1]);
			if(lastPos[0]>event.getX())
			{
				this.currentForm.setX(event.getX());
			}
			else
			{
				this.currentForm.setX(lastPos[0]);
			}
			if(lastPos[1]>event.getY())
			{
				this.currentForm.setY(event.getY());
			}
			else
			{
				this.currentForm.setY(lastPos[1]);
			}
			boolean overX=event.getX()>lastPos[0]+5;
			boolean underX=event.getX()<lastPos[0]-5;
			boolean overY=event.getY()>lastPos[1]+5;
			boolean underY=event.getY()<lastPos[1]-5;
			Cursor cursor=null;
			if(this.currentForm!=null)
			{
				cursor=this.currentForm.getCursor();
			}
			else
			{
				cursor=Cursor.getDefaultCursor();
			}
			if(overX)
			{
				cursor=Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
				if(overY)
				{
					cursor=Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
				}
				else if(underY)
				{
					cursor=Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
				}
			}
			else if(underX)
			{
				cursor=Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
				if(overY)
				{
					cursor=Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
				}
				else if(underY)
				{
					cursor=Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
				}
			}
			else
			{
				if(overY)
				{
					cursor=Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
				}
				else if(underY)
				{
					cursor=Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
				}
			}
			this.setCursor(cursor);
			this.currentForm.setSize(width,height);
			this.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent event)
	{
		
	}
}
