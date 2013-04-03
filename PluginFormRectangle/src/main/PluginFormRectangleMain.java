package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import main.plugin.IPlugin;

import ext.plugin.components.IForm;

public class PluginFormRectangleMain extends IForm implements IPlugin
{
	private String name="PluginFormRectangle";
	
	public PluginFormRectangleMain() 
	{
		super();
		System.out.println("Opening Plugin \""+this.name+"\"");
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
	
	public static void main(String args[]){
		System.out.println("Forme rectangle ");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String arg0) {
		this.name=name;
		
	}

}
