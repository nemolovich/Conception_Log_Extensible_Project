package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import main.ICore;
import main.plugin.IPlugin;
import main.plugin.IPluginDescriptor;
import main.plugin.PluginDescriptor;

public class Core implements ICore
{

	/**
	 * Tous les {@link IPluginDescriptor descripteurs de plugin} lus dans le fichier de config
	 * file from the core.
	 */
	private ArrayList<IPluginDescriptor> plugins=new ArrayList<IPluginDescriptor>();
	/**
	 * The core path.
	 */
	private String path;
	private String fileName;
	
	/**
	 * Retourne l'URL du chemin vers une librairie donnée
	 * @param librarieName : {@link String}, The library name
	 * @return {@link URL}, The URL to library path
	 */
	private URL getLibraryURL(String librarieName)
	{
		System.out.println("[LOG] Chargement de la librairie \""+librarieName+"\"");
		try
		{
			return new URL("file:"+this.path+"libs/"+File.separator+librarieName);
		}
		catch (MalformedURLException mURLe)
		{
			System.out.println("[ERROR] Le chemin défini vers \""+librarieName+"\" est incorrect"+
					"\n"+mURLe.getMessage());
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see main.ICore#loadPlugin(main.plugin.IPluginDescriptor, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object loadPlugin(IPluginDescriptor descriptor, boolean active)
	{
		System.out.println("[LOG] Chargement ("+(active?"actif":"passif")+") du plugin \""+
						descriptor.getName()+"\"...");
		Class<?> c;
		ArrayList<URL> urls=new ArrayList<URL>();
		if(descriptor.getInterfaces().size()>0)
		{
			/*
			 * Pour toutes les interfaces dont dépend le plugin
			 */
			for(String intfce:descriptor.getInterfaces())
			{
				/*
				 * On recherche le plugin qui possède cette interface
				 */
				for(IPluginDescriptor plugin:this.plugins)
				{
					boolean found=false;
					if(plugin.getDependencies().size()>0)
					{
						/*
						 * Pour chaque interface que possède le plugin
						 */
						for(String depd:plugin.getDependencies())
						{
							/*
							 * Si l'interface est celle désirée, on ajoute son path dans le classloader
							 */
							if(depd.equals(intfce))
							{
								found=true;
								try
								{
									urls.add(new URL("file:"+this.path+plugin.getPath()+File.separator));
									break;
								}
								catch (MalformedURLException mURLe)
								{
									System.out.println("[ERROR] L'URL vers le plugin \""+plugin.getName()
											+"\" est mal définie:\n"+mURLe.getMessage());
									break;
								}
							}
						}
					}
					if(found)
					{
						break;
					}
				}
			}
		}
		try
		{
			urls.add(new URL("file:"+this.path+descriptor.getPath()+File.separator));
			if(descriptor.getLibraries()!=null&&descriptor.getLibraries().size()>0)
			{
				/* Si le plugin depend de librairies on les ajoute au classloader
				 * (Ces libraries doivent être placées dans le dossier "libs" du
				 * path du coeur)
				 */
				for(String library:descriptor.getLibraries())
				{
					URL lib=this.getLibraryURL(library);
					if(lib!=null)
					{
						urls.add(lib);
					}
				}
			}
		}
		catch (MalformedURLException mURLe)
		{
			System.out.println("[ERROR] Le chemin défini vers \""+descriptor.getName()+"\" est incorrect"+
						"\n"+mURLe.getMessage());
			return null;
		}
		URLClassLoader ucl=new URLClassLoader((URL[]) urls.toArray(new URL[0]),ClassLoader.getSystemClassLoader());
        try
        {
			c = Class.forName(descriptor.getClassName(),false,ucl);
		} 
        catch (ClassNotFoundException cnfe)
        {
			System.out.println("[ERROR] La classe \""+descriptor.getClassName()+"\" pour le plugin \""+
						descriptor.getName()+"\" n'a pas été trouvée");
			return null;
		}
		if(!IPlugin.class.isAssignableFrom(c))
		{
			System.out.println("[ERROR] Ce plugin n'est pas valide: La classe n'implémente" +
					" pas l'interface \""+IPlugin.class.getName()+"\"");
			return null;
		}

        return this.getPluginInstance(descriptor.getName(), (Class<IPlugin>)c,  active);
	}

	/* (non-Javadoc)
	 * @see main.ICore#getPluginInstance(java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public Object getPluginInstance(String pluginName, Class<IPlugin> classe, boolean active)
	{
		IPluginDescriptor descriptor=this.getPluginDescriptor(pluginName);
		if(descriptor==null)
		{
			return null;
		}
		@SuppressWarnings("rawtypes")
		Constructor constructor=null;
		if(active)
		{
			Class<?>[] args=new Class<?>[]{ICore.class};
			try
			{
				constructor=classe.getConstructor(args);
			}
			catch (NoSuchMethodException nsme)
			{
				System.out.println("[ERROR] Le constructeur du plugin \""+
							descriptor.getName()+"\" n'a pas pu étre trouvé");
				/* NOTE: Ces trois lignes essayent de charger le plugin avec
				 * le parametre de chargement actif inverse de celui qu'il
				 * possede actuellement. Ce qui permet de le charge quand
				 * meme, meme si le fichier de configuration est mauvais.
				 */
				if(descriptor.isActive()==active)
				{
					return this.getPluginInstance(pluginName, classe, !active);
				}
				return null;
			}
			catch (SecurityException se)
			{
				System.out.println("[ERROR] Le constructeur du plugin \""+
						descriptor.getName()+"\" n'a pas pu être invoqué"+
						"\n"+se.getMessage());
				if(descriptor.isActive()==active)
				{
					return this.getPluginInstance(pluginName, classe, !active);
				}
				return null;
			}
		}
	    try
		{
	    	Object instance=null;
			if(active)
			{
				instance=constructor.newInstance(this);
			}
			else
			{
				instance=classe.newInstance();
			}
			if(instance!=null)
			{
				descriptor.setPluginInstance(instance);
				return instance;
			}
			else
			{
				System.out.println("[ERROR] L'instance du plugin n'a pas pu être crée");
				return null;
			}
		}
		catch (InstantiationException ie)
		{
			System.out.println("[ERROR] Impossible d'instancier le plugin \""+
					descriptor.getName()+"\""+
					"\n"+ie.getMessage());
			if(descriptor.isActive()==active)
			{
				return this.getPluginInstance(pluginName, classe, !active);
			}
			return null;
		}
		catch (IllegalAccessException iae)
		{
			System.out.println("[ERROR] Impossible d'instancier le plugin \""+
					descriptor.getName()+"\" (Illegal Access)"+
					"\n"+iae.getMessage());
			if(descriptor.isActive()==active)
			{
				return this.getPluginInstance(pluginName, classe, !active);
			}
			return null;
		}
	    catch (IllegalArgumentException iae)
		{
			System.out.println("[ERROR] Impossible d'instancier le plugin \""+
					descriptor.getName()+"\" (Illegal Argument)"+
					"\n"+iae.getMessage());
			if(descriptor.isActive()==active)
			{
				return this.getPluginInstance(pluginName, classe, !active);
			}
			return null;
		}
	    catch (InvocationTargetException ite)
		{
			System.out.println("[ERROR] Impossible d'instancier le plugin \""+
					descriptor.getName()+"\" (Invocation Target)"+
					"\n"+ite.getMessage());
			ite.printStackTrace();
			if(descriptor.isActive()==active)
			{
				return this.getPluginInstance(pluginName, classe, !active);
			}
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see main.ICore#loadConfigs(java.lang.String)
	 */
	@Override
	public boolean loadConfigs()
	{
    	Properties p=new Properties();
        System.out.println("[LOG] Chargement des configurations...");
		try
	    {
			p.load(new FileReader(this.fileName));
		}
		catch (FileNotFoundException fnfe)
		{
			System.out.println("[ERROR] Impossible de charger le ficher \""+this.fileName+"\"\n"+
						fnfe.getMessage());
			return false;
		}
		catch (IOException ioe)
		{
			System.out.println("[ERROR] Erreur lors de l'ouverture du ficher \""+this.fileName+"\"\n"
						+ioe.getMessage());
			return false;
		}
		this.path=p.getProperty("pluginsPath");
		this.path=this.path==null?p.getProperty("PluginsPath"):p.getProperty("pluginsPath");
		if(this.path==null)
		{
			System.out.println("[ERROR] Le fichier \""+this.fileName+"\" ne contient pas d'attribut \"pluginsPath\"");
			return false;
		}
		System.out.println("[LOG] Plugins présents dans: \""+path+"\"");
		System.out.println("\tClasses:\t\tPath:");
		System.out.println("\t---------------------------------");
		for(Object k:p.keySet())
		{
			if(!((String)k).equalsIgnoreCase("pluginsPath"))
			{
				String pluginName=(String)k;
				String pluginPath=(String)p.getProperty((String) k);
				
				IPluginDescriptor descriptor=this.getPluginConfig(pluginName, pluginPath);
				if(descriptor!=null)
				{
					this.plugins.add(descriptor);
				}
			}
		}
		return true;
	}

	/**
	 * Return the boolean property from a config file
	 * @param prop : {@link Properties}, The properties
	 * @param propertyName : {@link String}, The property name
	 * @param pluginFileConfig : {@link String}, The plugin file config
	 * @return {@link Boolean}, The boolean property
	 */
	private Boolean getBooleanProperty(Properties prop, String propertyName,
			String pluginFileConfig)
	{
		String property=prop.getProperty(propertyName);
		if(property==null)
		{
			System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"lazy\"");
			return null;
		}
		return (property.equalsIgnoreCase("true"));
	}

	/**
	 * Return the array property from a config file
	 * @param prop : {@link Properties}, The properties
	 * @param propertyName : {@link String}, The property name
	 * @param pluginFileConfig : {@link String}, The plugin file config
	 * @return {@link ArrayList}<{@link String}>, The array property
	 */
	private ArrayList<String> getArrayProperty(Properties prop, String propertyName,
			String pluginFileConfig)
	{
		String property=prop.getProperty(propertyName);
		ArrayList<String> array=new ArrayList<String>();
		if(property==null)
		{
			System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"interfaces\"");
			return null;
		}
		if(property.length()>0)
		{
			array=new ArrayList<String>(Arrays.asList(property.replaceAll(" ", "").split(",")));
		}
		return array;
	}

	/* (non-Javadoc)
	 * @see main.ICore#getPluginConfig(java.lang.String,java.lang.String)
	 */
	public IPluginDescriptor getPluginConfig(String pluginName, String pluginPath)
	{
		System.out.printf("\t%-22s\t%s\n",pluginName,pluginPath);
		String pluginFileConfig=path+pluginPath+File.separator+"config.ini";
		Properties prop=new Properties();
		try
		{
			prop.load(new FileReader(pluginFileConfig));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("[ERROR]\t - Impossible de charger le ficher \""+pluginFileConfig+"\"\n"
								+e.getMessage());
			return null;
		}
		catch (IOException e)
		{
			System.out.println("[ERROR]\t - Erreur lors de l'ouverture du ficher \""+pluginFileConfig+"\"\n"
								+e.getMessage());
			return null;
		}
		String className=this.getStringProperty(prop, "className", pluginFileConfig);
		if(className==null)
		{
			return null;
		}
		ArrayList<String> pluginInterfaces=this.getArrayProperty(prop, "interfaces", pluginFileConfig);
		if(pluginInterfaces==null)
		{
			return null;
		}
		ArrayList<String> pluginLibraries=this.getArrayProperty(prop, "libraries", pluginFileConfig);
		if(pluginLibraries==null)
		{
			return null;
		}
		ArrayList<String> pluginDependencies=this.getArrayProperty(prop, "dependencies", pluginFileConfig);
		if(pluginDependencies==null)
		{
			return null;
		}
		Boolean pluginIsLazy=this.getBooleanProperty(prop, "lazy", pluginFileConfig);
		if(pluginIsLazy==null)
		{
			return null;
		}
		Boolean pluginIsActive=this.getBooleanProperty(prop, "active", pluginFileConfig);
		if(pluginIsActive==null)
		{
			return null;
		}
		Boolean pluginIsDefault=this.getBooleanProperty(prop, "default", pluginFileConfig);
		if(pluginIsDefault==null)
		{
			return null;
		}
		System.out.printf("\t  |-> %-10s %s\n\t  |-> %-10s %s\n\t  '-> %-10s %s\n",
				"Default:",pluginIsDefault?"Oui":"Non",
				"Active:",pluginIsActive?"Oui":"Non",
				"Lazy:",pluginIsLazy?"Oui":"Non");
		PluginDescriptor descriptor=new PluginDescriptor(pluginName,pluginPath,pluginIsDefault,
				pluginIsActive,pluginIsLazy,className,pluginInterfaces,pluginLibraries,pluginDependencies);
		return descriptor;
	}

	/**
	 * Return the string property from a config file
	 * @param prop : {@link Properties}, The properties
	 * @param propertyName : {@link String}, The property name
	 * @param pluginFileConfig : {@link String}, The plugin file config
	 * @return {@link String}, The string property
	 */
	private String getStringProperty(Properties prop, String propertyName, String pluginFileConfig)
	{
		String property=prop.getProperty(propertyName);
		if(property==null)
		{
			System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"className\"");
		}
		return property;
	}

	/* (non-Javadoc)
	 * @see main.ICore#getPuginsByInterface(java.lang.String)
	 */
	@Override
	public ArrayList<IPluginDescriptor> getPuginsByInterface(String iplugin)
	{
		ArrayList<IPluginDescriptor> pluglist=new ArrayList<IPluginDescriptor>();
		for(IPluginDescriptor plugin:this.plugins)
		{
			if(plugin.getInterfaces().contains(iplugin))
			{
				pluglist.add(plugin);
			}
		}
		
		return (ArrayList<IPluginDescriptor>) pluglist;
	}
	
	/* (non-Javadoc)
	 * @see main.ICore#getPlugin(java.lang.String)
	 */
	@Override
	public Object getPlugin(String pluginName)
	{
		for(IPluginDescriptor plugin:this.plugins)
		{
			if(plugin.getName().equalsIgnoreCase(pluginName))
			{
				return plugin.getPluginInstance();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see main.ICore#getPluginDescriptor(java.lang.String)
	 */
	@Override
	public IPluginDescriptor getPluginDescriptor(String pluginName)
	{
		for(IPluginDescriptor plugin:this.plugins)
		{
			if(plugin.getName().equalsIgnoreCase(pluginName))
			{
				return plugin;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see main.ICore#getPlugins()
	 */
	@Override
	public ArrayList<IPluginDescriptor> getPlugins()
	{
		return this.plugins;
	}


	/* (non-Javadoc)
	 * @see main.ICore#removePlugin(IPluginDescriptor)
	 */
	@Override
	public boolean removePlugin(IPluginDescriptor plugin)
	{
		Properties prop=new Properties();
		try
		{
			prop.load(new FileReader(this.fileName));
		}
		catch (FileNotFoundException fnfe)
		{
			System.out.println("[ERROR] Impossible de charger le ficher \""+this.fileName+"\"\n"+
						fnfe.getMessage());
			return false;
		}
		catch (IOException ioe)
		{
			System.out.println("[ERROR] Erreur lors de l'ouverture du ficher \""+this.fileName+"\"\n"
						+ioe.getMessage());
			return false;
		}
		String pluginName=plugin.getName();
		boolean found=false;
		if(!prop.containsKey(pluginName))
		{
			for(Object key:prop.keySet())
			{
				if(((String)key).equalsIgnoreCase(pluginName))
				{
					pluginName=(String)key;
					found=true;
					break;
				}
			}
		}
		else
		{
			found=true;
		}
		if(!found)
		{
			System.out.println("[ERROR] La configuration du plugin \""+plugin.getName()+
					"\" n'a pas été trouvée");
			return false;
		}
		try
		{
			prop.remove(pluginName);
			prop.store(new FileWriter(this.fileName), " Core config (auto-generated by the Core)");
		}
		catch (IOException ioe)
		{
			System.out.println("[ERROR] Impossible d'enregistrer les configuration de la plateforme:\n"+
					ioe.getMessage());
		}
		
		return this.plugins.remove(plugin);
	}

	/* (non-Javadoc)
	 * @see main.ICore#getDescriptor(java.lang.String, java.lang.String, boolean, boolean, boolean, java.lang.String, java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public IPluginDescriptor getDescriptor(String name, String path,
			boolean isDefault, boolean isActive, boolean isLazy,
			String className, ArrayList<String> interfaces,
			ArrayList<String> libraries, ArrayList<String> dependencies)
	{
		return new PluginDescriptor(name, path, isDefault, isActive, isLazy, className,
				interfaces, libraries, dependencies);
	}
	
	/* (non-Javadoc)
	 * @see main.ICore#getPath()
	 */
	@Override
	public String getPath()
	{
		return this.path;
	}

	/**
	 * Définit le fichier de configuration de la plateforme
	 * @param fileName : {@link String}, Le nom du fichier de configuration
	 */
//	private void setFileName(String fileName)
	public void setFileName(String fileName)
	{
		this.fileName=fileName;
	}
	
	public void setPath(String path)
	{
		this.path=path;
	}

	
	/**
	 * The main function
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("[LOG] Chargement du coeur...");
		Core core=new Core();
		core.setFileName("config.ini");
		if(core.loadConfigs())
		{
			System.out.println("[LOG] Configurations chargées");			
		}
		System.out.println("[LOG] Chargement des plugins par défaut...");
		for(IPluginDescriptor plugin:core.getPlugins())
		{
			if(plugin.isDefault())
			{
				if(core.loadPlugin(plugin,plugin.isActive())!=null)
				{
					System.out.println("[LOG] Le plugin \""+plugin.getName()+"\" a été chargé");
			        
			        /* Si la classe a pu etre chargee on definie cette classe pour le plugin dans
			         * son descripteur et on dit qu'il a ete charge
			         */
					plugin.setLoaded(true);
				}
				else
				{
					System.out.println("[LOG] Le plugin \""+plugin.getName()+"\" n'a pas pu être chargé");
				}
			}
		}
		System.out.println("[LOG] Fin d'exécution");
        
	}
}
