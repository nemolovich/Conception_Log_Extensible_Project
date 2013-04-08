package ext.plugin.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;


public class PluginFormPoint extends IForm
{
	public static final String pluginName="PluginFormPoint";
	private String name=PluginFormPoint.pluginName;
	
	public PluginFormPoint()
	{
		super();
		this.fill=2;
		this.formName="Point";
		this.foregroundColor=Color.decode("#000000");
		this.isDraggable=false;
		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		this.setIcon(new ImageIcon("ressources/img/point_icon_24.png"));
	}

	@Override
	public void draw(Graphics g)
	{
		g.setColor(this.foregroundColor);
		if(this.drawMode==IForm.FULL_BACK_DRAW)
		{
			Color color=this.foregroundColor;
			if(this.backgroundColor!=null)
			{
				color=this.backgroundColor;
			}
			g.setColor(color);
		}
		((Graphics2D)g).setStroke(new BasicStroke(this.getFill()));
		g.fillRect(this.x-(this.fill/2), this.y-(this.fill/2), this.fill, this.fill);
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void setName(String name)
	{
		this.name=name;
	}

	@Override
	public boolean isDefaultItem()
	{
		return true;
	}

}
