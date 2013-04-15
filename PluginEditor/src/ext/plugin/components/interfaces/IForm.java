package ext.plugin.components.interfaces;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public abstract class IForm implements IItem
{
	public static final int NORMAL_DRAW = 0;
	public static final int FULL_FORE_DRAW = 1;
	public static final int FULL_BACK_DRAW = 2;
	public static final int FILL_DRAW = 3;
	public static final Color CURSOR_COLOR = Color.decode("#7A8A99");
	
	protected int x=0;
	protected int y=0;
	protected int fill=2;
	protected int width=0;
	protected int height=0;
	protected ImageIcon icon;
	protected boolean isDraggable=false;
	protected Color foregroundColor=Color.decode("#000000");
	protected Color backgroundColor=Color.decode("#FFFFFF");
	protected String formName="unnamed";
	protected JButton button=null;
	protected Cursor cursor;
	protected int drawMode=NORMAL_DRAW;
	public abstract void draw(Graphics g);

	public BufferedImage getCursorImage()
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		BufferedImage cursorImg=new BufferedImage(16,20, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d=(Graphics2D) cursorImg.createGraphics();
		toolkit.getBestCursorSize(16, 20);
		g2d.setPaint(IForm.CURSOR_COLOR);
		Image img=new ImageIcon("ressources/img/cursor.png").getImage();
		g2d.drawImage(img,0,0,null);
		return cursorImg;
	}
	
	public void setCursorImage(BufferedImage img, String cursorName)
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		this.cursor=toolkit.createCustomCursor(img, new Point(0, 0), cursorName);		
	}
	
	/**
	 * @return the icon
	 */
	@Override
	public ImageIcon getIcon()
	{
		return this.icon;
	}

	/**
	 * @param icon the icon to set
	 */
	@Override
	public void setIcon(ImageIcon icon)
	{
		this.icon = icon;
	}

	/**
	 * @return the isDraggable
	 */
	public boolean isDraggable()
	{
		return this.isDraggable;
	}

	/**
	 * @param isDraggable the isDraggable to set
	 */
	public void setDraggable(boolean isDraggable)
	{
		this.isDraggable = isDraggable;
	}

	/**
	 * @return the x
	 */
	public int getX()
	{
		return this.x;
	}

	/**
	 * @return the y
	 */
	public int getY()
	{
		return this.y;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	/**
	 * Set the IForm size
	 * @param width
	 * @param height
	 */
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
	
	/**
	 * @return the foregroundColor
	 */
	public Color getForegroundColor()
	{
		return this.foregroundColor;
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
		return this.backgroundColor;
	}
	
	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * @return the itemName
	 */
	public String getItemName()
	{
		return this.formName;
	}
	
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String formName)
	{
		this.formName=formName;
	}
	
	/**
	 * @return the button
	 */
	public JButton getButton()
	{
		return button;
	}
	
	/**
	 * @param button the button to set
	 */
	public void setButton(JButton button)
	{
		this.button = button;
		this.button.setToolTipText("Créer une forme "+this.formName);
	}

	/**
	 * @return the cursor
	 */
	public Cursor getCursor()
	{
		return cursor;
	}

	/**
	 * @param cursor the cursor to set
	 */
	public void setCursor(Cursor cursor)
	{
		this.cursor = cursor;
	}

	/**
	 * @return the drawMode
	 */
	public int getDrawMode()
	{
		return this.drawMode;
	}
	
	/**
	 * @param drawMode the drawMode to set
	 */
	public void setDrawMode(int drawMode)
	{
		this.drawMode=drawMode;
	}
}
