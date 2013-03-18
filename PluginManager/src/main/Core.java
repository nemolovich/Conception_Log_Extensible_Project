package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

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
	@Override
	@SuppressWarnings("unchecked")
	public boolean loadPlugin(IPluginDescriptor descriptor, boolean active)
	{
		System.out.println("[LOG] Chargement ("+(active?"actif":"passif")+") du plugin \""+descriptor.getName()+"\"...");
		Class<IPlugin> c;
		ArrayList<URL> urls=new ArrayList<URL>();
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
			return false;
		}
		URLClassLoader ucl=new URLClassLoader((URL[]) urls.toArray(new URL[0]),ClassLoader.getSystemClassLoader());
        try
        {
			c = (Class<IPlugin>) Class.forName(descriptor.getClassName(),false,ucl);
		} 
        catch (ClassNotFoundException cnfe)
        {
			System.out.println("[ERROR] La classe \""+descriptor.getClassName()+"\" pour le plugin \""+
						descriptor.getName()+"\" n'a pas été trouvée");
			return false;
		}
        
        /* Si la classe a pu etre chargee on definie cette classe pour le plugin dans
         * son descripteur et on dit qu'il a ete charge
         */
		descriptor.setLoaded(true);

		@SuppressWarnings("rawtypes")
		Constructor constructor=null;
		if(active)
		{
			Class<?>[] args=new Class<?>[]{ICore.class};
			try
			{
				constructor=c.getConstructor(args);
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
					return this.loadPlugin(descriptor, !active);
				}
				return false;
			}
			catch (SecurityException se)
			{
				System.out.println("[ERROR] Le constructeur du plugin \""+
						descriptor.getName()+"\" n'a pas pu être invoqué"+
						"\n"+se.getMessage());
				if(descriptor.isActive()==active)
				{
					return this.loadPlugin(descriptor, !active);
				}
				return false;
			}
		}
	    try
		{
			if(active)
			{
				descriptor.setPluginInstance((IPlugin) constructor.newInstance(this));
			}
			else
			{
				descriptor.setPluginInstance(c.newInstance());
			}
		}
		catch (InstantiationException ie)
		{
			System.out.println("[ERROR] Impossible d'instancier le plugin \""+
					descriptor.getName()+"\""+
					"\n"+ie.getMessage());
			if(descriptor.isActive()==active)
			{
				return this.loadPlugin(descriptor, !active);
			}
			return false;
		}
		catch (IllegalAccessException iae)
		{
			System.out.println("[ERROR] Impossible d'instancier le plugin \""+
					descriptor.getName()+"\" (Illegal Access)"+
					"\n"+iae.getMessage());
			if(descriptor.isActive()==active)
			{
				return this.loadPlugin(descriptor, !active);
			}
			return false;
		}
	    catch (IllegalArgumentException iae)
		{
			System.out.println("[ERROR] Impossible d'instancier le plugin \""+
					descriptor.getName()+"\""+
					"\n"+iae.getMessage());
			if(descriptor.isActive()==active)
			{
				return this.loadPlugin(descriptor, !active);
			}
			return false;
		}
	    catch (InvocationTargetException ite)
		{
			System.out.println("[ERROR] Impossible d'instancier le plugin \""+
					descriptor.getName()+"\""+
					"\n"+ite.getMessage());
			if(descriptor.isActive()==active)
			{
				return this.loadPlugin(descriptor, !active);
			}
			return false;
		}
        return true;
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
		catch (FileNotFoundException e)
		{
			System.out.println("[ERROR] Impossible de charger le ficher \""+this.fileName+"\"\n"+e.getMessage());
			return false;
		}
		catch (IOException e)
		{
			System.out.println("[ERROR] Erreur lors de l'ouverture du ficher \""+this.fileName+"\"\n"+e.getMessage());
			return false;
		}
		this.path=p.getProperty("pluginsPath");
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
			if(!((String)k).equals("pluginsPath"))
			{
				String pluginName=(String)k;
				String pluginPath=(String)p.getProperty((String) k);
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
					continue;
				}
				catch (IOException e)
				{
					System.out.println("[ERROR]\t - Erreur lors de l'ouverture du ficher \""+pluginFileConfig+"\"\n"
										+e.getMessage());
					continue;
				}
				String className=prop.getProperty("className");
				if(className==null)
				{
					System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"className\"");
					continue;
				}
				String interfaces=prop.getProperty("interfaces");
				ArrayList<String> pluginInterfaces=new ArrayList<String>();
				if(interfaces==null)
				{
					System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"interfaces\"");
					continue;
				}
				if(interfaces.length()>0)
				{
					pluginInterfaces=new ArrayList<String>(Arrays.asList(interfaces.replaceAll(" ", "").split(",")));
				}
				String libraries=prop.getProperty("libraries");
				ArrayList<String> pluginLibraries=new ArrayList<String>();
				if(libraries==null)
				{
					System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"libraries\"");
					continue;
				}
				if(libraries.length()>0)
				{
					pluginLibraries=new ArrayList<String>(Arrays.asList(libraries.replaceAll(" ", "").split(",")));
				}
				String dependencies=prop.getProperty("dependencies");
				ArrayList<String> pluginDependencies=new ArrayList<String>();
				if(dependencies==null)
				{
					System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"dependencies\"");
					continue;
				}
				if(dependencies.length()>0)
				{
					pluginDependencies=new ArrayList<String>(Arrays.asList(dependencies.replaceAll(" ", "").split(",")));
				}
				String pluginLazy=prop.getProperty("lazy");
				if(pluginLazy==null)
				{
					System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"lazy\"");
					continue;
				}
				boolean pluginIsLazy=(pluginLazy.equalsIgnoreCase("true"));
				String pluginActive=prop.getProperty("active");
				if(pluginActive==null)
				{
					System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"active\"");
					continue;
				}
				boolean pluginIsActive=(pluginActive.equalsIgnoreCase("true"));
				String pluginDefault=prop.getProperty("default");
				if(pluginDefault==null)
				{
					System.out.println("[ERROR]\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \"default\"");
					continue;
				}
				boolean pluginIsDefault=(pluginDefault.equalsIgnoreCase("true"));
				System.out.printf("\t  |-> %-10s %s\n\t  |-> %-10s %s\n\t  '-> %-10s %s\n",
						"Default:",pluginIsDefault?"Oui":"Non",
						"Active:",pluginIsActive?"Oui":"Non",
						"Lazy:",pluginIsLazy?"Oui":"Non");
				PluginDescriptor descriptor=new PluginDescriptor(pluginName,pluginPath,pluginIsDefault,
						pluginIsActive,pluginIsLazy,className,pluginInterfaces,pluginLibraries,pluginDependencies);
				this.plugins.add(descriptor);
			}
		}
		return true;
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
	public IPlugin getPlugin(String pluginName)
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
				if(core.loadPlugin(plugin,plugin.isActive()))
				{
					System.out.println("[LOG] Le plugin \""+plugin.getName()+"\" a été chargé");
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