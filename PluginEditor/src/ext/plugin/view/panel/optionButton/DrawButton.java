package ext.plugin.view.panel.optionButton;

import java.awt.Color;
import java.awt.Graphics;

public class DrawButton extends OptionButton
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3959886016268159082L;
	protected Color foregroundColor;
	protected Color backgroundColor;

	/**
	 * Constructor
	 * @param fullBackDraw 
	 */
	public DrawButton(int value)
	{
		this(value,null);
	}
	
	/**
	 * Constrcutor
	 * @param value
	 * @param infos
	 */
	public DrawButton(int value, String infos)
	{
		super(value,infos);
		this.foregroundColor=Color.decode("#000000");
		this.backgroundColor=Color.decode("#FFFFFF");
	}
	
	public void paintContent(Graphics g)
	{
	}

	/**
	 * @return the foregroundColor
	 */
	public Color getForegroundColor()
	{
		return foregroundColor;
	}

	/**
	 * @param foregroundColor the foregroundColor to set
	 */
	public void setForegroundColor(Color foregroundColor)
	{
		this.foregroundColor = foregroundColor;
	}

	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}

	
}
