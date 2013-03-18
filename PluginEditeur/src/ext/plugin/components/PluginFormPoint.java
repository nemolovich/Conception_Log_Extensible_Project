package ext.plugin.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class PluginFormPoint extends IForm
{
	
	public PluginFormPoint()
	{
		super();
		this.fill=3;
		this.isDraggable=false;
	}

	@Override
	public void draw(Graphics g)
	{
		g.setColor(this.foreGroundColor);
		((Graphics2D)g).setStroke(new BasicStroke(this.getFill()));

		g.drawRect(this.x, this.y,1,1);
		((Graphics2D)g).setStroke(new BasicStroke());
	}

	@Override
	public void drawFill(Graphics g)
	{
		draw(g);
	}

	@Override
	public void drawFullFore(Graphics g)
	{
		g.setColor(this.foreGroundColor);
		draw(g);
	}

	@Override
	public void drawFullBack(Graphics g)
	{
		Color color=this.foreGroundColor;
		if(this.backGroundColor!=null)
		{
			color=this.backGroundColor;
		}
		g.setColor(color);
		g.drawOval(this.x, this.y,1,1);
		((Graphics2D)g).setStroke(new BasicStroke());
	}

}
