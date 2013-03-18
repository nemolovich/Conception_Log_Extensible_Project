package ext.plugin.view.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import ext.plugin.components.IForm;
import ext.plugin.components.PluginFormPoint;


public class EditFormPanel extends EditPanel
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = -3748095472208214156L;
	private ArrayList<IForm> forms=new ArrayList<IForm>();
//	private Class<?> currentForm;
	private IForm currentForm;
	private int[] lastPos=new int[]{0,0};

	public EditFormPanel()
	{
		super();
		super.addMouseListener(this);
		super.addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		g.setColor(Color.decode("#FFFFFF"));
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
		boolean pair=true;
		int i=0;
		for(IForm f:forms)
		{
			if(pair)
			{
				f.setForeGroundColor(Color.decode("#0000FF"));
				f.setBackGroundColor(Color.decode("#F88800"));
			}
			else
			{
				f.setForeGroundColor(Color.decode("#FF0000"));
				f.setBackGroundColor(Color.decode("#00FF00"));
			}
			if(i++%2==0)
			{
				f.draw(g);
			}
			else if(i++%3==0)
			{
				f.drawFullFore(g);
			}
			else if(i++%4==0)
			{
				f.drawFullBack(g);
			}
			else
			{
				f.drawFill(g);
			}
			
			pair=!pair;
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		if(!this.currentForm.isDraggable())
		{
			this.currentForm.setX(event.getX());
			this.currentForm.setY(event.getY());
			this.forms.add(this.currentForm);
			this.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{

	}

	@Override
	public void mouseExited(MouseEvent event)
	{

	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		this.currentForm=new PluginFormPoint(); //Modifier ici
		if(this.currentForm.isDraggable())
		{
			this.lastPos=new int[]{event.getX(),event.getY()};
			this.currentForm.setX(event.getX());
			this.currentForm.setY(event.getY());
			this.forms.add(this.currentForm);
		}
		else
		{
			this.mouseClicked(event);
		}
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
			int cursor=Cursor.DEFAULT_CURSOR;
			if(overX)
			{
				cursor=Cursor.W_RESIZE_CURSOR;
				if(overY)
				{
					cursor=Cursor.SE_RESIZE_CURSOR;
				}
				else if(underY)
				{
					cursor=Cursor.NE_RESIZE_CURSOR;
				}
			}
			else if(underX)
			{
				cursor=Cursor.E_RESIZE_CURSOR;
				if(overY)
				{
					cursor=Cursor.SW_RESIZE_CURSOR;
				}
				else if(underY)
				{
					cursor=Cursor.NW_RESIZE_CURSOR;
				}
			}
			else
			{
				if(overY)
				{
					cursor=Cursor.S_RESIZE_CURSOR;
				}
				else if(underY)
				{
					cursor=Cursor.N_RESIZE_CURSOR;
				}
			}
			this.setCursor(Cursor.getPredefinedCursor(cursor));
			this.currentForm.setSize(width,height);
			this.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent event)
	{
		
	}
}
