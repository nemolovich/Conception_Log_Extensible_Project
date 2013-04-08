package ext.plugin.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import ext.plugin.controller.Controller;

public class PluginFormRectangle extends IForm
{

	private String name="PluginFormRectangle";

	public PluginFormRectangle()
	{
		super();
		this.formName="Rectangle";
		this.fill=10;
		this.isDraggable=true;
		BufferedImage img=this.getCursorImage();
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setPaint(IForm.CURSOR_COLOR);
		g2d.drawRect(5, 10 ,10, 8);
		this.setCursorImage(img, this.formName);
		this.setIcon(new ImageIcon(Controller.EDITOR_PATH+"img/rectangle_icon_24.png"));
	}

	@Override
	public void draw(Graphics g)
	{
		g.setColor(this.foregroundColor);
		if(this.drawMode==IForm.NORMAL_DRAW)
		{
			((Graphics2D)g).setStroke(new BasicStroke(this.getFill()));
			g.drawRect(this.x, this.y, this.width, this.height);
			((Graphics2D)g).setStroke(new BasicStroke());
		}
		else if(this.drawMode==IForm.FILL_DRAW)
		{
			if(this.backgroundColor!=null)
			{
				g.setColor(this.backgroundColor);
				g.fillRect(this.x, this.y, this.width, this.height);
			}
			g.setColor(this.foregroundColor);
			((Graphics2D)g).setStroke(new BasicStroke(this.getFill()));
			g.drawRect(this.x, this.y, this.width, this.height);
			((Graphics2D)g).setStroke(new BasicStroke());
		}
		else if(this.drawMode==IForm.FULL_FORE_DRAW)
		{
			g.fillRect(this.x, this.y, this.width, this.height);
		}
		else if(this.drawMode==IForm.FULL_BACK_DRAW)
		{
			Color color=this.foregroundColor;
			if(this.backgroundColor!=null)
			{
				color=this.backgroundColor;
			}
			g.setColor(color);
			g.fillRect(this.x, this.y, this.width, this.height);	
		}
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
		return false;
	}

}
