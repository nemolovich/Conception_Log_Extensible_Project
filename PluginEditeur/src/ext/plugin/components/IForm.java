package ext.plugin.components;

import java.awt.Color;
import java.awt.Graphics;

public abstract class IForm
{
	protected int x=0;
	protected int y=0;
	protected int width=0;
	protected int height=0;
	protected int fill=1;
	protected boolean isDraggable=false;
	protected Color foreGroundColor=Color.decode("#000000");
	protected Color backGroundColor=null;
	public abstract void draw(Graphics g);
	public abstract void drawFill(Graphics g);
	public abstract void drawFullFore(Graphics g);
	public abstract void drawFullBack(Graphics g);

	public void setX(int x)
	{
		this.x=x;
	}

	public void setY(int y)
	{
		this.y=y;
	}
	
	public void setSize(int width, int height)
	{
		this.width=width;
		this.height=height;
	}

	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return this.height;
	}
	
	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}
	
	/**
	 * @return the fill
	 */
	public int getFill()
	{
		return this.fill;
	}
	
	/**
	 * @param fill the fill to set
	 */
	public void setFill(int fill)
	{
		this.fill = fill;
	}
	
	public boolean isDraggable()
	{
		return this.isDraggable;
	}
	
	/**
	 * @return the foreGroundColor
	 */
	public Color getForeGroundColor()
	{
		return this.foreGroundColor;
	}
	
	/**
	 * @param foreGroundColor the foreGroundColor to set
	 */
	public void setForeGroundColor(Color foreGroundColor)
	{
		this.foreGroundColor = foreGroundColor;
	}
	
	/**
	 * @return the backGroundColor
	 */
	public Color getBackGroundColor()
	{
		return this.backGroundColor;
	}
	
	/**
	 * @param backGroundColor the backGroundColor to set
	 */
	public void setBackGroundColor(Color backGroundColor)
	{
		this.backGroundColor = backGroundColor;
	}
}
