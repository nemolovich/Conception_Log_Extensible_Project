package ext.plugin.controller;

import java.io.File;
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
	private ICore core;
	private ArrayList<IPluginDescriptor> availableFormPlugins;
	public static final String pn = "PluginEditor";
	public static String EDITOR_PATH="";
	private String name=Controller.pn;
	
	public Controller(ICore core)
	{
		this.core=core;
		this.availableFormPlugins= new ArrayList<IPluginDescriptor>();
		System.out.println("Opening Plugin \""+this.name+"\"");
		EditFormPanel editPanel=new EditFormPanel(core);
		OptionPanel optionPanel=new OptionPanel();
		Controller.EDITOR_PATH=this.core.getPath()+this.core.getPluginDescriptor(this.name).getPath()
				+File.separator;
		this.core.getPluginDescriptor(this.name).setLoaded(true);
		this.core.getPluginDescriptor(this.name).setPluginInstance(this);
		if(this.core!=null)
		{
			IPluginDescriptor controllerPlugin=this.core.getPluginDescriptor(this.name);
			this.availableFormPlugins=core.getPuginsByInterface(controllerPlugin.getDependencies().get(0));
			for(IPluginDescriptor plugin:this.availableFormPlugins)
			{
				if(!plugin.isLoaded())
				{
					Object o=this.core.loadPlugin(plugin, plugin.isActive());
					if(o!=null)
					{
						plugin.setLoaded(true);
						plugin.setPluginInstance(o);
					}
				}
			}
			this.pluginMainPanel.setPlugins(this.availableFormPlugins);
		}
		this.pluginMainPanel.setEditPanel(editPanel);
		this.pluginMainPanel.setOptionPanel(optionPanel);
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
//		Core core=new Core();
//		core.setFileName("/home/nemo/Documents/Info/Java/Projets/ProjectCLE/config.ini");
//		core.loadConfigs();
//		core.setPath("/home/nemo/Documents/Info/Java/Projets/ProjectCLE/plugins/");
//		new Controller(core);
	}

}
