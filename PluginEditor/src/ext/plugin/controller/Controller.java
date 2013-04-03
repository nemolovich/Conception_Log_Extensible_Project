package ext.plugin.controller;

import java.util.ArrayList;

import main.Core;
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
	private String name=Controller.pn;
	
	public Controller(ICore core)
	{
		this.core=core;
		this.availableFormPlugins= new ArrayList<IPluginDescriptor>();
		System.out.println("Opening Plugin \""+this.name+"\"");
		EditFormPanel editPanel=new EditFormPanel(core);
		OptionPanel optionPanel=new OptionPanel();
		
		
//		IPluginDescriptor editorDesc=this.core.getPluginDescriptor(this.name);
//		String editorPath=this.core.getPath()+editorDesc.getPath();
//		IPluginDescriptor pointDesc = this.core.getDescriptor("PluginFormPoint", editorPath,
//				false, false, false, "ext.plugins.components.PluginFormPoint", 
//				editorDesc.getDependencies(), editorDesc.getLibraries(), null);
//		pointDesc.setPluginInstance(new PluginFormPoint());
//		IPluginDescriptor rectDesc = this.core.getDescriptor("PluginFormRectangle", editorPath,
//				false, false, false, "ext.plugins.components.PluginFormRectangle", 
//				editorDesc.getDependencies(), editorDesc.getLibraries(), null);
//		rectDesc.setPluginInstance(new PluginFormRectangle());
//		this.core.getPlugins().add(pointDesc);
//		this.core.getPlugins().add(rectDesc);
		
		if(this.core!=null)
		{
			this.availableFormPlugins=core.getPuginsByInterface("IForm");
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
//		new Controller(null);
		Core core=new Core();
		core.setFileName("/home/nemo/Documents/Info/Java/Projets/ProjectCLE/config.ini");
		core.loadConfigs();
		core.setPath("/home/nemo/Documents/Info/Java/Projets/ProjectCLE/plugins/");
		new Controller(core);
	}

}
