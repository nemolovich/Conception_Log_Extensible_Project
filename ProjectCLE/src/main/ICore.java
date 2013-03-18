package main;

import java.util.ArrayList;

import main.plugin.IPlugin;
import main.plugin.IPluginDescriptor;

/**
 * Interface ICore: The core interface
 * @author Brian GOHIER
 */

public interface ICore
{

	/**
	 * Load a plugin from his {@link IPluginDescriptor descriptor}.
	 * @param descriptor - {@link IPluginDescriptor descriptor}, The
	 * plugin descriptor
	 * @param active - {@link Boolean boolean}, Set if the plugin
	 * must be loaded with active status
	 * @return {@link Boolean boolean}, True if the plugin have been 
	 * successfully loaded
	 */
	public abstract boolean loadPlugin(IPluginDescriptor descriptor,
			boolean active);

	/**
	 * Load a config file from core.
	 * @param fileName - {@link String}, The file name to load
	 * @return {@link Boolean boolean}, True if the config have been 
	 * successfully loaded
	 */
	public abstract boolean loadConfigs(String fileName);

	/**
	 * Get the plugins list that implement the given interface name.
	 * @param iplugin : {@link String}, The interface name
	 * @return {@link ArrayList}<{@link String}>, The plugin list
	 */
	public abstract ArrayList<IPluginDescriptor> getPuginsByInterface(String iplugin);

	/**
	 * Get the plugin by {@link IPlugin#getName() name} from
	 * the plugins loaded in core.
	 * @param pluginName - {@link String}, The plugin name
	 * @return {@link IPlugin}, The plugin
	 */
	public abstract IPlugin getPlugin(String pluginName);

	/**
	 * Get the plugin descriptor by {@link IPluginDescriptor#getName() name}.
	 * from the plugins loaded in core.
	 * @param pluginName - {@link String}, The plugin name
	 * @return {@link IPluginDescriptor}, The plugin descriptor
	 */
	public abstract IPluginDescriptor getPluginDescriptor(String pluginName);

	/**
	 * Get all the {@link IPluginDescriptor plugin descriptors} loaded in
	 * the core.
	 * @return {@link ArrayList}<{@link IPluginDescriptor}>, The plugin descriptor
	 */
	public abstract ArrayList<IPluginDescriptor> getPlugins();

	/**
	 * Get the {@link IPluginDescriptor descriptors} created by attributes.
	 * @param name : {@link String}, The plugin name
	 * @param path : {@link String}, The plugin path
	 * @param isDefault : {@link Boolean boolean}, If the plugin is loaded by default
	 * @param isActive : {@link Boolean boolean}, If the plugin is active
	 * @param isLazy : {@link Boolean boolean}, If the plugin is loaded by lazy loading
	 * @param className : {@link String}, The plugin class name
	 * @param interfaces : {@link ArrayList}<{@link String}>, The interfaces dependencies of the plugin
	 * @param libraries : {@link ArrayList}<{@link String}>, The libraries dependencies of the plugin
	 * @return {@link IPluginDescriptor}, The plugin descriptor
	 */
	public abstract IPluginDescriptor getDescriptor(String name, String path,
			boolean isDefault, boolean isActive, boolean isLazy,
			String className, ArrayList<String> interfaces,
			ArrayList<String> libraries, ArrayList<String> dependencies);

	/**
	 * Return the plugin directory path.
	 * @return {@link String} The plugin path
	 */
	public abstract String getPath();

}