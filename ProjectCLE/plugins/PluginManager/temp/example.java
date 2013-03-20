package main;

import main.ICore;
import main.plugin.IPlugin;

/**
 * The class PluginName has been auto-generated by PluginManager
 * @author PluginManager
 */
public class PluginName implements IPlugin
{
	private String name="PluginTest";
	private ICore core;
	
	/**
	 * Active constructor
	 * @param core : {@link ICore}, The core
	 */
	public PluginName(ICore core)
	{
		// TODO Auto-generated method stub
		this.core = core;
	}

	/**
	 * Passive constructor
	 */
	public PluginName()
	{
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPlugin#getName()
	 */
	@Override
	public String getName()
	{
		return this.name;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPlugin#setName(java.lang.String)
	 */
	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		System.out.println("PluginName");
	}

}
