package main.plugin;

/**
 * Cette interface définit un plugin en tant qu'objet 
 * @author Brian GOHIER
 */
public interface IPlugin
{
	/**
	 * Permet de récupérer le nom du plugin.
	 * @return {@link String} : Le nom du plugin
	 */
	public String getName();
	
	/**
	 * Permet de définir le nom du plugin.
	 * @param name {@link String} : Le nom du plugin
	 */
	public void setName(String name);
	
}
