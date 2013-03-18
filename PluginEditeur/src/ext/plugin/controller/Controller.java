package ext.plugin.controller;

import java.util.ArrayList;

import main.ICore;
import main.plugin.IPlugin;
import main.plugin.IPluginDescriptor;
import ext.plugin.view.PluginMainPanel;
import ext.plugin.view.panel.EditFormPanel;
import ext.plugin.view.panel.OptionPanel;

public class Controller implements IPlugin
{
	private PluginMainPanel pluginMainPanel=new PluginMainPanel();
	private String name="PluginEditeur";
	private ICore core;
	private ArrayList<IPluginDescriptor> availableFormPlugins;
	public static final String pn = "PluginEditeur";
	
	public Controller(ICore core)
	{
		this.core=core;
		this.availableFormPlugins= new ArrayList<IPluginDescriptor>();
		System.out.println("Opening Plugin \""+this.name+"\"");
		EditFormPanel editPanel=new EditFormPanel();
		OptionPanel optionPanel=new OptionPanel();
		this.pluginMainPanel.setEditPanel(editPanel);
		this.pluginMainPanel.setOptionPanel(optionPanel);
		
		if(this.core!=null)
		{
			this.availableFormPlugins=core.getPuginsByInterface("IForm");
			this.pluginMainPanel.setPlugins(this.availableFormPlugins);
		}
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void setName(String name)
	{
		this.name=name;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		System.out.println("PluginEditor");
//		new Controller(null);
	}

}
