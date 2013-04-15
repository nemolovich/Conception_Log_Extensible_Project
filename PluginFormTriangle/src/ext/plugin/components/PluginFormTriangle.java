package ext.plugin.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import ext.plugin.components.interfaces.IForm;

public class PluginFormTriangle extends IForm
{
	
	private String name="PluginFormTriangle";
	
	public PluginFormTriangle()
	{
		super();
		this.formName="Triangle";
		this.fill=10;
		this.isDraggable=true;
		BufferedImage img=this.getCursorImage();
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setPaint(IForm.CURSOR_COLOR);
		g2d.drawLine(10, 10, 16, 19);
		g2d.drawLine(10, 10, 4, 19);
		g2d.drawLine(4, 19, 16, 19);
		this.setCursorImage(img, this.formName);
		this.setIcon(new ImageIcon("ressources/img/triangle_icon_24.png"));
	}

	@Override
	public void draw(Graphics g)
	{
		Point p1 = new Point(this.x+this.width/2, this.y);
		Point p2 = new Point(this.x+this.width, this.y+this.height);
		Point p3 = new Point(this.x, this.y+this.height);
		int[] xPoints = { p1.x, p2.x, p3.x };
		int[] yPoints = { p1.y, p2.y, p3.y };
		g.setColor(this.foregroundColor);
		if(this.drawMode==IForm.NORMAL_DRAW)
		{
			((Graphics2D)g).setStroke(new BasicStroke(this.getFill()));
			g.drawPolygon(xPoints, yPoints, xPoints.length);
			((Graphics2D)g).setStroke(new BasicStroke());
		}
		else if(this.drawMode==IForm.FILL_DRAW)
		{
			if(this.backgroundColor!=null)
			{
				g.setColor(this.backgroundColor);
				g.fillPolygon(xPoints, yPoints, xPoints.length);
			}
			g.setColor(this.foregroundColor);
			((Graphics2D)g).setStroke(new BasicStroke(this.getFill()));
			g.drawPolygon(xPoints, yPoints, xPoints.length);
			((Graphics2D)g).setStroke(new BasicStroke());
		}
		else if(this.drawMode==IForm.FULL_FORE_DRAW)
		{
			g.fillPolygon(xPoints, yPoints, xPoints.length);
		}
		else if(this.drawMode==IForm.FULL_BACK_DRAW)
		{
			Color color=this.foregroundColor;
			if(this.backgroundColor!=null)
			{
				color=this.backgroundColor;
			}
			g.setColor(color);
			g.fillPolygon(xPoints, yPoints, xPoints.length);
			
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
	
	public static void main(String[] args)
	{
		System.out.println("PluginFormTriangle");
	}
}
