package main;

import main.plugin.IPlugin;

/**
 * The class PluginTest has been auto-generated by PluginManager
 * @author PluginManager
 */
public class PluginTest implements IPlugin
{
	private String name="PluginTest";

	/**
	 * Passive constructor
	 */
	public PluginTest()
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
		System.out.println("PluginTest");
	}

}
