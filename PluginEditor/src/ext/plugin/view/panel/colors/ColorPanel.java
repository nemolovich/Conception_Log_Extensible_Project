package ext.plugin.view.panel.colors;

import static ext.plugin.view.panel.colors.Colors.getHexaColor;
import static ext.plugin.view.panel.colors.Colors.getStringColorFormat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ext.plugin.view.panel.OptionPanel;

public class ColorPanel extends JPanel implements MouseListener, KeyListener
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = 2285240304183069926L;

	private static final String FOREGROUND_TEXT = "Couleur primaire";
	private static final String BACKGROUND_TEXT = "Couleur secondaire";

	private Color foregroundColor=Color.decode("#C0C0C0");
	private Color backgroundColor=Color.decode("#C0C0C0");
	private JPanel foregroundPanel=new JPanel();
	private JPanel backgroundPanel=new JPanel();
	private JLabel selectedColorLabel=new JLabel(ColorPanel.FOREGROUND_TEXT+":");
	private Color selectedColor=this.foregroundColor;
	private JTextField inputColor=new JTextField(getHexaColor(this.foregroundColor));
	private JButton switchButton=new JButton();
	private OptionPanel optionPanel;

	/**
	 * Constructeur
	 * @param optionPanel 
	 */
	public ColorPanel(OptionPanel optionPanel, Color foreground, Color background)
	{
		super();
		this.setLayout(null);
		this.optionPanel=optionPanel;
		this.foregroundColor=foreground;
		this.backgroundColor=background;
		this.foregroundPanel.setBackground(foreground);
		this.backgroundPanel.setBackground(background);
		this.foregroundPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#808080")));
		this.backgroundPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#808080")));
		this.foregroundPanel.setToolTipText(ColorPanel.FOREGROUND_TEXT);
		this.backgroundPanel.setToolTipText(ColorPanel.BACKGROUND_TEXT);
		Image img = null;
		try
		{
			img = ImageIO.read(new File("ressources/img/arrow_16.png"));
		}
		catch (IOException ioe)
		{
			System.err.println("[INFO] L'image du bouton n'a pas pu être chargé:\n"+ioe.getMessage());
		}
		this.switchButton.setIcon(new ImageIcon(img));
		this.switchButton.setToolTipText("Intervertir les couleurs");

		Font newLabelFont=new Font(this.selectedColorLabel.getFont().getName(),
				0,12);
		this.selectedColorLabel.setFont(newLabelFont);
		
		this.add(this.backgroundPanel);
		this.add(this.foregroundPanel);
		this.add(this.selectedColorLabel);
		this.add(this.inputColor);
		this.add(this.switchButton);

		this.setComponentZOrder(this.foregroundPanel, 0);
		this.setComponentZOrder(this.backgroundPanel, 1);
		this.backgroundPanel.addMouseListener(this);
		this.foregroundPanel.addMouseListener(this);
		this.switchButton.addMouseListener(this);
		this.inputColor.addKeyListener(this);

		this.selectedColor=this.foregroundColor;
		this.inputColor.setText(getHexaColor(this.foregroundColor));
		this.center(200);
	}

	public void center(int width)
	{
		this.foregroundPanel.setBounds(2+(width-160)/2, 10, 25, 15);
		this.backgroundPanel.setBounds(18+(width-160)/2, 20, 25, 15);
		this.selectedColorLabel.setBounds(50+(width-160)/2, 2, 150, 15);
		this.inputColor.setBounds(50+(width-160)/2, 18, 75, 23);
		this.switchButton.setBounds(130+(width-160)/2, 17, 24, 24);
		this.setSize(width, 25);
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

	/**
	 * @return the foregroundPanel
	 */
	public JPanel getForegroundPanel()
	{
		return foregroundPanel;
	}

	/**
	 * @param foregroundPanel the foregroundPanel to set
	 */
	public void setForegroundPanel(JPanel foregroundPanel)
	{
		this.foregroundPanel = foregroundPanel;
	}

	/**
	 * @return the backgroundPanel
	 */
	public JPanel getBackgroundPanel()
	{
		return backgroundPanel;
	}

	/**
	 * @param backgroundPanel the backgroundPanel to set
	 */
	public void setBackgroundPanel(JPanel backgroundPanel)
	{
		this.backgroundPanel = backgroundPanel;
	}

	private void updateInput()
	{
		Color newColor=getStringColorFormat(this.inputColor,this.selectedColor);
		if(newColor==null)
		{
			return;
		}
		if(this.foregroundColor.equals(this.selectedColor))
		{
			this.foregroundColor=newColor;
			this.selectedColor=this.foregroundColor;
		}
		else if(this.backgroundColor.equals(this.selectedColor))
		{
			this.backgroundColor=newColor;
			this.selectedColor=this.backgroundColor;
		}
		
		this.backgroundPanel.setBackground(this.backgroundColor);
		this.foregroundPanel.setBackground(this.foregroundColor);
		this.inputColor.setText(getHexaColor(this.selectedColor));
		this.repaint();
		this.optionPanel.updateColors();
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		if(event.getSource().equals(this.backgroundPanel))
		{
			if(event.getClickCount()>1)
			{
				Color newColor=JColorChooser.showDialog(this, "Sélectionnez la "+
						ColorPanel.BACKGROUND_TEXT,
						this.backgroundColor);
				if(newColor!=null)
				{
					this.backgroundColor=newColor;
				}
				this.inputColor.setText(getHexaColor(this.backgroundColor));
			}
			else
			{
				this.inputColor.setText(getHexaColor(this.backgroundColor));
				this.selectedColorLabel.setText(ColorPanel.BACKGROUND_TEXT+":");
			}
			this.selectedColor=this.backgroundColor;
			this.updateInput();
		}
		else if(event.getSource().equals(this.foregroundPanel))
		{
			if(event.getClickCount()>1)
			{
				Color newColor=JColorChooser.showDialog(this, "Sélectionnez la "+
						ColorPanel.FOREGROUND_TEXT,
						this.foregroundColor);
				if(newColor!=null)
				{
					this.foregroundColor=newColor;
				}
				this.inputColor.setText(getHexaColor(this.foregroundColor));
			}
			else
			{
				this.inputColor.setText(getHexaColor(this.foregroundColor));
				this.selectedColorLabel.setText(ColorPanel.FOREGROUND_TEXT+":");
			}
			this.selectedColor=this.foregroundColor;
			this.updateInput();
		}
		else if(event.getSource().equals(this.switchButton))
		{
			if(this.selectedColor.equals(this.foregroundColor))
			{
				this.foregroundColor=this.backgroundColor;
				this.backgroundColor=this.selectedColor;
				this.selectedColor=this.foregroundColor;
			}
			else if(this.selectedColor.equals(this.backgroundColor))
			{
				this.backgroundColor=this.foregroundColor;
				this.foregroundColor=this.selectedColor;
				this.selectedColor=this.backgroundColor;
			}
			this.inputColor.setText(getHexaColor(this.selectedColor));
			this.updateInput();
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
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{	
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		if(event.getSource().equals(this.inputColor)&&
				event.getKeyCode()==10)				
		{
			this.updateInput();
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}
}
