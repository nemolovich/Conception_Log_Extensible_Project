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
	 * @return {@link Object}, The plugin if it has been successfully
	 * loaded else null
	 */
	public abstract Object loadPlugin(IPluginDescriptor descriptor,
			boolean active);

	/**
	 * Get the plugin instance from his name and his class
	 * @param pluginName : {@link String}, The plugin name
	 * @param classe : {@link Class}<{@link IPlugin}>, The plugin class
	 * @param active : {@link Boolean boolean}, If the plugin is active
	 * @return {@link Object}, The plugin instance
	 */
	public Object getPluginInstance(String pluginName, Class<?> classe, boolean active);

	/**
	 * Load a config file from core.
	 * @return {@link Boolean boolean}, True if the config have been 
	 * successfully loaded
	 */
	public abstract boolean loadConfigs();

	/**
	 * Get the {@link IPluginDescriptor plugin descriptor} from a plugin name
	 * @param pluginName : {@link String}, The plugin name
	 * @param pluginPath : {@link String}, The plugin path
	 * @return {@link IPluginDescriptor}, The plugin descriptor
	 */
	public abstract IPluginDescriptor getPluginConfig(String pluginName, String pluginPath);
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
	 * @return {@link Object}, The plugin
	 */
	public abstract Object getPlugin(String pluginName);

	/**
	 * Get the plugin descriptor by {@link IPluginDescriptor#getName() name}.
	 * from the plugins loaded in core.
	 * @param pluginName - {@link String}, The plugin name
	 * @return {@link IPluginDescriptor}, The plugin descriptor, null if does not exist
	 */
	public abstract IPluginDescriptor getPluginDescriptor(String pluginName);

	/**
	 * Get all the {@link IPluginDescriptor plugin descriptors} loaded in
	 * the core.
	 * @return {@link ArrayList}<{@link IPluginDescriptor}>, The plugin descriptor
	 */
	public abstract ArrayList<IPluginDescriptor> getPlugins();

	/**
	 * Remove the plugin from the list
	 * @param plugin : {@link IPluginDescriptor}, The plugin
	 * @return {@link Boolean boolean}, If the plugin has been removed
	 */
	public abstract boolean removePlugin(IPluginDescriptor plugin);

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
	 * Unload the plugin. If some plugins depend of this one they will be unload too. 
	 * @param pluginName : {@link String}, The plugin name
	 * @return {@link Boolean boolean}, True if the plugin has been correctly unloaded
	 */
	public abstract boolean unload(String pluginName);
	
	/**
	 * Return the plugin directory path.
	 * @return {@link String} The plugin path
	 */
	public abstract String getPath();
	
	/**
	 * Write in all {@link Core#logs loggers}
	 * @param message : {@link String}, The message to write as LOG
	 */
	public abstract void logWrite(String message);

	/**
	 * Print in all {@link Core#logs loggers}
	 * @param message : {@link String}, The message to print in a logger
	 */
	public abstract void logPrint(String message);
	
	/**
	 * Write an error in all {@link Core#logs loggers}
	 * @param error : {@link String}, The error to write as ERROR
	 */
	public abstract void logError(String error);
	
	/**
	 * Display all hidden {@link Core#logs loggers}
	 */
	public abstract void logDisplay();
	

}