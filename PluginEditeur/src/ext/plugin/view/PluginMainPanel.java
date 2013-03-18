package ext.plugin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;

import main.plugin.IPluginDescriptor;
import ext.plugin.controller.Controller;
import ext.plugin.view.panel.EditPanel;
import ext.plugin.view.panel.OptionPanel;

public class PluginMainPanel extends JFrame
//		implements MouseListener, MouseMotionListener
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
	JLabel iconePoint= new JLabel("Icone point ");
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
		this.setTitle("Edition d'images");
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
		JLabel plugLabel;
		this.optionPanel = optionPanel;			
		this.optionPanel.setMinimumSize(new Dimension(250,this.frameHeight-50));
		this.getContentPane().add(this.optionPanel, BorderLayout.WEST);
		this.split.setLeftComponent(this.optionPanel);
		this.optionPanel.setPreferredSize(new Dimension(this.frameWidth-(this.frameWidth-300),this.frameHeight-50));
		
		if(plugins!=null){
			for(int i=0;i<plugins.size();i++){
				plugLabel= new JLabel(plugins.get(i).getName());
				this.optionPanel.add(plugLabel);
			}
		}
	}
	
	public void setMenuList()
	{
		
	}
	
	public void setPlugins(ArrayList<IPluginDescriptor> plugins)
	{
		this.plugins=plugins;
	}

}
