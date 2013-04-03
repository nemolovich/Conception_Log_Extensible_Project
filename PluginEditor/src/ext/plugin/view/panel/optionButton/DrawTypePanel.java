package ext.plugin.view.panel.optionButton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ext.plugin.components.IForm;
import ext.plugin.view.panel.OptionPanel;

public class DrawTypePanel extends JPanel
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = 2285240304183069926L;

	private DrawButton normalDrawButton;
	private DrawButton fillDrawButton;
	private DrawButton fullForeDrawButton;
	private DrawButton fullBackDrawButton;
	private Color foregroundColor;
	private Color backgroundColor;

	/**
	 * Constructeur
	 * @param optionPanel 
	 */
	public DrawTypePanel(OptionPanel optionPanel)
	{
		super();
		this.foregroundColor=Color.decode("#000000");
		this.backgroundColor=Color.decode("#FFFFFF");
		JLabel label=new JLabel("Type: ");
		label.setFont(new Font(label.getFont().getName(),0,12));
		this.add(label);
		final OptionPanel op=optionPanel;
		this.normalDrawButton=new DrawButton(IForm.NORMAL_DRAW, "Contours de la forme")
		{
			/**
			 * ID
			 */
			private static final long serialVersionUID = 3654079729185986402L;

			@Override
			public void paintContent(Graphics g)
			{
				super.paintComponents(g);
				((Graphics2D) g).setStroke(new BasicStroke(2));
				g.setColor(this.foregroundColor);
				g.drawRect(4, 4, 22, 12);
				op.updateDrawType(this.getValue());
			}
		};
		this.fillDrawButton=new DrawButton(IForm.FILL_DRAW, "Forme remplie")
		{
			/**
			 * ID
			 */
			private static final long serialVersionUID = 1023412581945531155L;

			@Override
			public void paintContent(Graphics g)
			{
				super.paintComponents(g);
				((Graphics2D) g).setStroke(new BasicStroke(2));
				g.setColor(this.backgroundColor);
				g.fillRect(4, 4, 22, 12);
				g.setColor(this.foregroundColor);
				((Graphics2D) g).setStroke(new BasicStroke(2));
				g.drawRect(4, 4, 22, 12);
				op.updateDrawType(this.getValue());
			}
		};
		this.fullForeDrawButton=new DrawButton(IForm.FULL_FORE_DRAW, "De la couleur principale")
		{
			/**
			 * ID
			 */
			private static final long serialVersionUID = -6097079383990806441L;

			@Override
			public void paintContent(Graphics g)
			{
				super.paintComponents(g);
				((Graphics2D) g).setStroke(new BasicStroke(2));
				g.setColor(this.foregroundColor);
				g.fillRect(4, 4, 22, 12);
				op.updateDrawType(this.getValue());
			}
		};
		this.fullBackDrawButton=new DrawButton(IForm.FULL_BACK_DRAW, "De la couleur secondaire")
		{

			/**
			 * ID
			 */
			private static final long serialVersionUID = -3959886016268159082L;

			@Override
			public void paintContent(Graphics g)
			{
				super.paintComponents(g);
				((Graphics2D) g).setStroke(new BasicStroke(2));
				g.setColor(this.backgroundColor);
				g.fillRect(4, 4, 22, 12);
				op.updateDrawType(this.getValue());
			}
		};
		this.add(this.normalDrawButton);
		this.add(this.fillDrawButton);
		this.add(this.fullForeDrawButton);
		this.add(this.fullBackDrawButton);
		this.normalDrawButton.addExcludedButton(this.fillDrawButton);
		this.normalDrawButton.addExcludedButton(this.fullForeDrawButton);
		this.normalDrawButton.addExcludedButton(this.fullBackDrawButton);
		this.normalDrawButton.setSelected(true);
		
		this.setSize(200, 25);
	}
	
	public void updateButtons()
	{
		this.repaint();
		this.normalDrawButton.setForegroundColor(this.foregroundColor);
		this.normalDrawButton.setBackgroundColor(this.backgroundColor);
		this.fillDrawButton.setForegroundColor(this.foregroundColor);
		this.fillDrawButton.setBackgroundColor(this.backgroundColor);
		this.fullForeDrawButton.setForegroundColor(this.foregroundColor);
		this.fullForeDrawButton.setBackgroundColor(this.backgroundColor);
		this.fullBackDrawButton.setForegroundColor(this.foregroundColor);
		this.fullBackDrawButton.setBackgroundColor(this.backgroundColor);
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
		this.updateButtons();
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
		this.updateButtons();
	}

	public int getValue()
	{
		if(this.normalDrawButton.isSelected())
		{
			return this.normalDrawButton.getValue();
		}
		else if(this.fillDrawButton.isSelected())
		{
			return this.fillDrawButton.getValue();
		}
		else if(this.fullForeDrawButton.isSelected())
		{
			return this.fullForeDrawButton.getValue();
		}
		else if(this.fullBackDrawButton.isSelected())
		{
			return this.fullBackDrawButton.getValue();
		}
		else
		{
			return -1;
		}
	}
}
