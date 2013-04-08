package ext.plugin.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ext.plugin.view.PluginMainPanel;
import ext.plugin.view.panel.colors.ColorPanel;
import ext.plugin.view.panel.optionButton.DrawTypePanel;
import ext.plugin.view.panel.size.SizePanel;


public class OptionPanel extends JPanel implements MouseListener
{
	/**
	 * ID
	 */
	//ImageIcon iconePoint= new ImageIcon("../images/point.jpeg");
	
	private static final long serialVersionUID = 2019551761497423458L;
	private int size=2;

	private ColorPanel colorPanel=null;
	private DrawTypePanel drawTypePanel;
	private SizePanel sizeInput=new SizePanel(this,this.size);
	private JPanel configurationPanel=new JPanel();
	private JPanel itemPanel=new JPanel();
	private EditPanel editPanel;

	public OptionPanel()
	{
		super();
		this.setLayout(new BorderLayout());
		this.setBackground(Color.decode("#F0F0F0"));
		this.configurationPanel.setLayout(new FlowLayout());
		this.configurationPanel.add(this.sizeInput);
		this.configurationPanel.setBorder(BorderFactory.createTitledBorder("Configurations"));
		this.itemPanel.setLayout(new FlowLayout());
		this.itemPanel.setBorder(BorderFactory.createTitledBorder("Objets"));
		JSplitPane split=new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		split.setTopComponent(this.configurationPanel);
		split.setBottomComponent(this.itemPanel);
		split.setOneTouchExpandable(true);
		split.setEnabled(true);
		split.setAutoscrolls(true);
		split.setOneTouchExpandable(true);
		this.add(split);
		this.sizeInput.addMouseListener(this);
		this.addMouseListener(this);
	}


	public void updateColors()
	{
		this.drawTypePanel.setForegroundColor(this.colorPanel.getForegroundColor());
		this.drawTypePanel.setBackgroundColor(this.colorPanel.getBackgroundColor());
		((EditFormPanel)this.editPanel).setForegroundColor(this.colorPanel.getForegroundColor());
		((EditFormPanel)this.editPanel).setBackgroundColor(this.colorPanel.getBackgroundColor());
	}
	
	public void updateDrawType(int drawType)
	{
		try
		{
			((EditFormPanel)this.editPanel).setDrawType(drawType);
		}
		catch(NullPointerException npe)
		{
			JOptionPane.showMessageDialog(null, "Erreur lors de la configuration de l'editeur. " +
					"L'application va fermer", "Erreur au lancement", JOptionPane.ERROR_MESSAGE);
			Container component=this;
			while(component!=null&&!component.getClass().equals(PluginMainPanel.class))
			{
				component=component.getParent();
			}
			if(component!=null)
			{
				System.out.println(component.getClass().getName());
				((PluginMainPanel)component).dispose();
			}
		}
	}
	
	public void setPanelSize(int optionWidth, int frameHeight, int heightItems)
	{
		this.setPreferredSize(new Dimension(optionWidth,frameHeight-50));
		this.setMinimumSize(new Dimension(250,frameHeight-50));
		this.sizeInput.setMinimumSize(new Dimension(200, 35));
		this.sizeInput.setPreferredSize(new Dimension(optionWidth-15, 25));
		int confHeight=60;
		if(this.colorPanel!=null)
		{
			this.colorPanel.setPreferredSize(new Dimension(optionWidth-15, 40));
			this.colorPanel.center(optionWidth-10);
			confHeight+=50;
		}
		if(this.drawTypePanel!=null)
		{
			this.drawTypePanel.setPreferredSize(new Dimension(optionWidth-15, 40));
			confHeight+=50;
		}
		this.configurationPanel.setPreferredSize(new Dimension(optionWidth,confHeight));
//		this.itemPanel.setPreferredSize(new Dimension(optionWidth,heightItems));
	}
	
	public Color getBackgroundColor()
	{
		return this.colorPanel.getBackgroundColor();
	}
	
	public Color getForegroundColor()
	{
		return this.colorPanel.getForegroundColor();
	}
	
	public void setOptions(PluginMainPanel pmp)
	{
		this.editPanel=pmp.getEditPanel();
		if(this.editPanel instanceof EditFormPanel)
		{
			this.colorPanel=new ColorPanel(this,Color.decode("#000000"),Color.decode("#FFFFFF"));
			this.drawTypePanel=new DrawTypePanel(this);
			this.configurationPanel.add(this.colorPanel);
			this.configurationPanel.add(this.drawTypePanel);
			this.colorPanel.addMouseListener(this);
			this.drawTypePanel.updateButtons();
			((EditFormPanel)pmp.getEditPanel()).setForegroundColor(this.colorPanel.getForegroundColor());
			((EditFormPanel)pmp.getEditPanel()).setBackgroundColor(this.colorPanel.getBackgroundColor());
			((EditFormPanel)pmp.getEditPanel()).setSize(this.size);
		}
	}
	
	/**
	 * @return the itemPanel
	 */
	public JPanel getItemPanel()
	{
		return itemPanel;
	}

	public void updateSize(int size)
	{
		this.size=size;
		if(this.editPanel instanceof EditFormPanel)
		{
			this.editPanel.setSize(this.size);
		}
	}


	public int getCurrentSize()
	{
		return this.size;
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
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
}
