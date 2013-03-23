package main;

import main.ICore;
import main.plugin.IPlugin;

/**
 * The class PluginTest has been auto-generated by PluginManager
 * @author PluginManager
 */
public class PluginTest implements IPlugin
{
	private String name="PluginTest";
	private ICore core;
	
	/**
	 * Active constructor
	 * @param core : {@link ICore}, The core
	 */
	public PluginTest(ICore core)
	{
		// TODO Auto-generated method stub
		this.core = core;
		new PluginManager(core);
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
		System.out.println("PluginTest");
	}

}
