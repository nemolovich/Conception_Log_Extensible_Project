package ext.plugin.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PluginFormRectangle extends IForm
{
	
	public PluginFormRectangle()
	{
		super();
		this.fill=10;
		this.isDraggable=true;
	}

	@Override
	public void draw(Graphics g)
	{
		g.setColor(this.foreGroundColor);
		((Graphics2D)g).setStroke(new BasicStroke(this.getFill()));
		g.drawRect(this.x, this.y, this.width, this.height);
		((Graphics2D)g).setStroke(new BasicStroke());
	}

	@Override
	public void drawFill(Graphics g)
	{
		if(this.backGroundColor!=null)
		{
			g.setColor(this.backGroundColor);
			g.fillRect(this.x, this.y, this.width, this.height);
		}
		g.setColor(this.foreGroundColor);
		((Graphics2D)g).setStroke(new BasicStroke(this.getFill()));
		g.drawRect(this.x, this.y, this.width, this.height);
		((Graphics2D)g).setStroke(new BasicStroke());
	}

	@Override
	public void drawFullFore(Graphics g)
	{
		g.setColor(this.foreGroundColor);
		g.fillRect(this.x, this.y, this.width, this.height);
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
		g.fillRect(this.x, this.y, this.width, this.height);
	}

}
