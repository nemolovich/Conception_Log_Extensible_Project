package ext.plugin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import main.plugin.IPlugin;
import main.plugin.IPluginDescriptor;
import ext.plugin.components.IItem;
import ext.plugin.components.PluginFormPoint;
import ext.plugin.controller.Controller;
import ext.plugin.view.panel.EditFormPanel;
import ext.plugin.view.panel.EditPanel;
import ext.plugin.view.panel.OptionPanel;

public class PluginMainPanel extends JFrame implements ActionListener
{

	/**
	 * ID
	 */
	private static final long serialVersionUID = -2519517014648965665L;
	private int frameWidth=1024;
	private int frameHeight=640;
	private EditPanel editPanel;
	private OptionPanel optionPanel;
	private JSplitPane split;
	private ArrayList<IPluginDescriptor> plugins;
	
	public PluginMainPanel()
	{
		/* For multi desktops */
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		this.frameWidth=width-300;
		this.frameHeight=height-150;
		this.setSize(this.frameWidth, this.frameHeight);
		this.setBackground(Color.decode("#F0F0F0"));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		final PluginMainPanel pmp = this;
		final String pn=Controller.pn;
		this.addWindowListener(new WindowAdapter() 
			{
				public void windowClosing(WindowEvent e) 
				{
					System.out.println("Close "+pn+" view");
					pmp.dispose();
				}
			});
		this.setResizable(false);
//		this.setIconImage(new ImageIcon("img/icon.png").getImage());
		
		/* Barre de séparation */
		this.split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,this.optionPanel,this.editPanel);
		this.split.setOneTouchExpandable(true);
		this.split.setEnabled(true);
		this.split.setAutoscrolls(true);
		this.split.setOneTouchExpandable(true);
		this.split.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
				new PropertyChangeListener()
				{
					@Override
					public void propertyChange(PropertyChangeEvent pce)
					{
						if(optionPanel!=null)
						{
							optionPanel.setPanelSize(split.getDividerLocation(), frameHeight,
									plugins.size()+1*30+30);
						}
					}
				});
		this.add(this.split);
		
		this.setVisible(true);
	}

	/**
	
	 * @return the editPanel
	 */
	public EditPanel getEditPanel()
	{
		return editPanel;
	}

	/**
	 * @param editPanel the editPanel to set
	 */
	public void setEditPanel(EditPanel editPanel)
	{
		this.editPanel = editPanel;
		this.editPanel.setMinimumSize(new Dimension(400,this.frameHeight-50));
		this.getContentPane().add(this.editPanel, BorderLayout.EAST);
		this.split.setRightComponent(this.editPanel);
		this.editPanel.setPreferredSize(new Dimension(this.frameWidth-310,this.frameHeight-50));
		if(this.editPanel instanceof EditFormPanel)
		{
			this.setTitle("Edition de formes");
		}
	}

	/**
	 * @return the optionPanel
	 */
	public OptionPanel getOptionPanel()
	{
		return optionPanel;
	}

	
	/**
	 * @param optionPanel the optionPanel to set
	 */
	public void setOptionPanel(OptionPanel optionPanel)
	{
		this.optionPanel = optionPanel;
		int nbItem=0;
		if(this.plugins!=null)
		{
			for(IPluginDescriptor plugin:this.plugins)
			{
				IItem item=(IItem)plugin.getPluginInstance();
				JButton button= new JButton(item.getItemName());
				button.setPreferredSize(new Dimension(this.frameWidth-(this.frameWidth-240), 25));
				nbItem++;
				if(item.getIcon()!=null)
				{
					button.setIcon(item.getIcon());
					button.setText(null);
					button.setPreferredSize(new Dimension(26, 26));
				}
				button.addActionListener(this);
				item.setButton(button);
				this.optionPanel.getItemPanel().add(button);
				if(item.isDefaultItem())
				{
					this.editPanel.setCurrentItem(item);
				}
			}
			if(this.editPanel instanceof EditFormPanel)
			{
				if(this.getPluginByName(PluginFormPoint.pluginName)==null)
				{
					IItem item=(IItem)new PluginFormPoint();
					JButton button= new JButton(item.getItemName());
					button.setPreferredSize(new Dimension(this.frameWidth-(this.frameWidth-240), 25));
					if(item.getIcon()!=null)
					{
						button.setIcon(item.getIcon());
						button.setText(null);
						button.setPreferredSize(new Dimension(26, 26));
					}
					nbItem++;
					button.addActionListener(this);
					item.setButton(button);
					this.optionPanel.getItemPanel().add(button);
					if(item.isDefaultItem())
					{
						this.editPanel.setCurrentItem(item);
					}
				}
			}
		}
		this.optionPanel.setOptions(this);
		this.setSize(this.getSize());
		if(this.editPanel instanceof EditFormPanel)
		{
			this.optionPanel.setPanelSize(this.frameWidth, this.frameHeight, (nbItem/6+(nbItem%6>0?1:0))*30+30);
		}
		else
		{
			this.optionPanel.setPanelSize(this.frameWidth, this.frameHeight, nbItem+1*30);
		}
		this.getContentPane().add(this.optionPanel, BorderLayout.WEST);
		this.split.setLeftComponent(this.optionPanel);
	}
	

	/**
	 * Search a plugin with the given name and returns it
	 * @param name
	 * @return
	 */
	public IPluginDescriptor getPluginByName(String name)
	{
		for(IPluginDescriptor plugin: plugins)
		{
			if(plugin.getName().equals(name))
			{
				return plugin;
			}
		}
		return null;
	}
	
	public void setMenuList()
	{
		
	}
	
	/**
	 * set the plugin list
	 * @param plugins
	 */
	public void setPlugins(ArrayList<IPluginDescriptor> plugins)
	{
		this.plugins=plugins;
	}

	public void addItem(IItem item)
	{
		JButton button=new JButton(item.getItemName());
		button.setPreferredSize(new Dimension(this.frameHeight-(this.frameHeight-250), 25));
		button.addActionListener(this);
		this.optionPanel.add(button);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source=event.getSource();
		if(source!=null&&source instanceof JButton)
		{
			for(IPluginDescriptor plugin:this.plugins)
			{
				IItem item=(IItem)(plugin.getPluginInstance());
				if(item!=null&&item.getButton()!=null&&item.getButton().equals(source))
				{
					IItem newItem=(IItem) this.editPanel.getNewInstance(
							plugin.getName(),(Class<IPlugin>)item.getClass(),item.getButton());
					this.editPanel.setCurrentItem(newItem);
					return;
				}
			}
		}
		else
		{
//			System.out.println(source.getClass().getName());
		}
	}
}
