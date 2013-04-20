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
import javax.swing.UIManager;

import main.plugin.IPlugin;
import main.plugin.IPluginDescriptor;
import ext.plugin.components.interfaces.IItem;
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
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
					pmp.editPanel.unload(pn);
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
							optionPanel.setPanelSize(split.getDividerLocation(), frameHeight);
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
		if(this.editPanel==null)
		{
			System.err.println("[ERROR] Le panneau d'édition n'est pas défini");
			return;
		}
		this.optionPanel = optionPanel;

		this.addOptionItem(this.editPanel.getNewInstanceOfDefaultItem());
		if(this.plugins!=null)
		{
			for(IPluginDescriptor plugin:this.plugins)
			{
				this.addOptionItem((IItem)plugin.getPluginInstance());
			}
		}
		
		this.optionPanel.setOptions(this);
		this.setSize(this.getSize());
		if(this.editPanel instanceof EditFormPanel)
		{
			this.optionPanel.setPanelSize(this.frameWidth, this.frameHeight);
		}
		else
		{
			this.optionPanel.setPanelSize(this.frameWidth, this.frameHeight);
		}
		this.getContentPane().add(this.optionPanel, BorderLayout.WEST);
		this.split.setLeftComponent(this.optionPanel);
	}
	
	/**
	 * Add an {@link IItem item} in the item list selection
	 * @param item : {@link IItem}, The item to add
	 */
	private void addOptionItem(IItem item)
	{
		try
		{
			JButton button= new JButton(item.getItemName());
			button.setPreferredSize(new Dimension(this.frameWidth-(this.frameWidth-240), 25));
			if(item.getIcon()!=null)
			{
				button.setIcon(item.getIcon());
				button.setText(null);
				button.setPreferredSize(new Dimension(30, 30));
			}
			button.addActionListener(this);
			item.setButton(button);
			this.optionPanel.getItemPanel().add(button);
			if(item.isDefaultItem())
			{
				this.editPanel.setCurrentItem(item);
			}
		}
		catch(Exception e)
		{
			System.err.println("[ERROR] Impossible de convertir le plugin \""+((IPlugin)item).getName()+
					"\" en item compatible:\n"+e.getMessage());
			e.printStackTrace();
		}
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
			IItem newItem=(IItem) this.editPanel.getNewInstanceOfDefaultItem();
			this.editPanel.setCurrentItem(newItem);
			this.editPanel.setDefaultItem(newItem);
		}
	}
}
