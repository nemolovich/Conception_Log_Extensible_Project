package ext.plugin.controller;

import java.io.File;
import java.util.ArrayList;

import javax.swing.UIManager;

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
		try
		{
			UIManager.setLookAndFeel(UIManager.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		this.core=core;
		this.availableFormPlugins= new ArrayList<IPluginDescriptor>();
		System.out.println("Opening Plugin \""+this.name+"\"");
		EditFormPanel editPanel=new EditFormPanel(core);
		OptionPanel optionPanel=new OptionPanel();
		Controller.EDITOR_PATH=this.core.getPath()+this.core.getPluginDescriptor(this.name).getPath()
				+File.separator;
		
		if(this.core!=null)
		{
			this.availableFormPlugins=core.getPuginsByInterface("IForm");
			for(IPluginDescriptor plugin:this.availableFormPlugins)
			{
				if(!plugin.isLoaded())
				{
					this.core.loadPlugin(plugin, plugin.isActive());
					plugin.setLoaded(true);
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
