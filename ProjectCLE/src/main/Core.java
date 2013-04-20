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
import java.util.ConcurrentModificationException;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import main.log.ConsoleLogger;
import main.log.FrameLogger;
import main.log.ILogger;
import main.plugin.IPlugin;
import main.plugin.IPluginDescriptor;
import main.plugin.PluginDescriptor;
import main.view.SplashScreen;

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
	private SplashScreen screen;
	private ArrayList<ILogger> logs=new ArrayList<ILogger>();

	/**
	 * Constructeur
	 */
	public Core()
	{
		this.logs.add(new ConsoleLogger());
		this.logs.add(new FrameLogger("la plateforme"));
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if(this.fileName==null)
		{
			this.setFileName("config.ini");
		}
		if(!this.loadCore())
		{
			return;
		}
		
		this.loadDefaultPlugins();
		
		this.screen.setLabel("Plateforme chargée");
		this.logWrite("Coeur chargé");
		this.splashScreenDestruct();
		this.logDisplay();
		this.consolePluginList();
	}
	
	/**
	 * Charge tous les plugin par défaut
	 */
	private void loadDefaultPlugins()
	{
		this.logWrite("Chargement des plugins par défaut...");
		this.screen.setLabel("Chargement des plugins par défaut...");
		this.screen.setNbElement(this.getNbDefaultPlugins());
		this.screen.setProgress(0);
		int i=1;
		for(IPluginDescriptor plugin:this.getPlugins())
		{
			if(plugin.isDefault())
			{
				this.screen.setLabel("Chargement du plugin "+plugin.getName());
				this.screen.setStateMessage("Loading Plugin");
				this.screen.setNbElement(this.getNbDefaultPlugins());
				this.screen.setProgress(i);
				try
				{
					Thread.sleep(70);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				if(this.loadPlugin(plugin,plugin.isActive())!=null)
				{
			        /* Si la classe a pu etre chargee on definie cette classe pour le plugin dans
			         * son descripteur et on dit qu'il a ete charge
			         */
					plugin.setLoaded(true);
				}
				this.screen.setLabel("Chargement du plugin "+plugin.getName());
				this.screen.setStateMessage("Loading Plugin");
				this.screen.setNbElement(this.getNbDefaultPlugins());
				this.screen.setProgress(i++);
			}
		}
	}

	/**
	 * Permet de charger les plugins manuellement à partir de la console
	 */
	private void consolePluginList()
	{
		if(this.plugins.size()==0)
		{
			return;
		}
		int pluginNumber=0;
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		do
		{
			System.out.println("Type the plugin number to load (0 to quit)\nAvailable plugins:");
			for(int i=0;i<this.plugins.size();i++)
			{
				System.out.printf("\t%d - %s\n",i+1,this.plugins.get(i).getName());
			}
			boolean goodChoice=true;
			try
			{
				pluginNumber=sc.nextInt();
				if(pluginNumber>0&&pluginNumber<=this.plugins.size())
				{
					IPluginDescriptor plugin=this.getPluginDescriptor(this.plugins.get(pluginNumber-1).getName());
					if(plugin.isLoaded())
					{
						System.out.println("This plugin is already loaded");
					}
					else
					{
						this.loadPlugin(plugin, plugin.isActive());
					}
				}
				else
				{
					if(pluginNumber!=0)
					{
						goodChoice=false;
					}
				}
			}
			catch(Exception e)
			{
				goodChoice=false;
				sc=new Scanner(System.in);
			}
			if(!goodChoice)
			{
				System.out.println("Wrong selection");
				pluginNumber=1;
			}
			sc.reset();
			goodChoice=true;
		}while(pluginNumber!=0);
		this.logWrite("Core is leaving...");
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}
		this.logWrite("Core end");
		System.exit(0);		
	}

	/**
	 * Initialisation du coeur
	 * @return {@link Boolean boolean}, Vrai si l'intitialisation s'est déroulée correctement
	 */
	private boolean loadCore()
	{
		this.logWrite("Chargement du coeur...");
		this.splashScreenInit();
		this.screen.setLabel("Chargement de la plateforme");
		try
		{
			Thread.sleep(70);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		if(!this.loadConfigs())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Initialise le SplashScreen
	 */
	private void splashScreenInit()
	{
		ImageIcon myImage = new ImageIcon("img/SplashScreen.jpg");
	    this.screen = new SplashScreen(myImage);
	    this.screen.setLocationRelativeTo(null);
	    this.screen.setProgressMax(100);
	    this.screen.setNbElement(1);
	    this.screen.setProgress(0);
	    this.screen.setScreenVisible(true);
	}
	
	/**
	 * Détruit le SplashScreen
	 */
	private void splashScreenDestruct()
	{
		this.screen.setScreenVisible(false);
		this.screen.dispose();
	}
	
	/**
	 * Retourne l'URL du chemin vers une librairie donnée
	 * @param librarieName : {@link String}, The library name
	 * @return {@link URL}, The URL to library path
	 */
	private URL getLibraryURL(String librarieName)
	{
		this.logWrite("Chargement de la librairie \""+librarieName+"\"");
		this.screen.setLabel("Loading library "+librarieName);
		this.screen.setStateMessage("Loading Library");
		try
		{
			Thread.sleep(70);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		try
		{
			return new URL("file:"+this.path+"libs/"+File.separator+librarieName);
		}
		catch (MalformedURLException mURLe)
		{
			this.logError("Le chemin défini vers \""+librarieName+"\" est incorrect"+
					"\n"+mURLe.getMessage());
			return null;
		}
	}

	/** (non-Javadoc)
	 * @see main.ICore#loadPlugin(main.plugin.IPluginDescriptor, boolean)
	 */
	@Override
	public Object loadPlugin(IPluginDescriptor descriptor, boolean active)
	{
		// On vérifie si le plugin est déjà chargé et que ce n'est pas un singleton
		if(descriptor.isSingleton()&&descriptor.isLoaded())
		{
			this.logError("Le plugin \""+descriptor.getName()+"\" ne peut être chargé qu'une seule fois");
			return null;
		}
		this.logWrite("Chargement ("+(active?"actif":"passif")+") du plugin \""+
						descriptor.getName()+"\"...");
		Class<?> c;
		ArrayList<URL> urls=new ArrayList<URL>();
		ClassLoader superLoader=null;
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
								/*
								 * Si le plugin n'est pas chargé et que son instance est nulle
								 * on demande à le charger avant
								 */
								if(!plugin.isLoaded())
								{
									if(plugin.getPluginInstance()==null)
									{
										this.logError("Le plugin \""+plugin.getName()
												+"\" n'est pas chargé, chargez le avant");
										return null;
									}
								}
								superLoader=plugin.getPluginInstance().getClass().getClassLoader();
								break;
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
		
		if(descriptor.getDependencies().size()>0)
		{
			/*
			 * Pour toutes ses dépendance on ajoute son chemin dans le classloader
			 */
			for(String depd:descriptor.getDependencies())
			{
				for(IPluginDescriptor plugin:this.plugins)
				{
					if(plugin.getInterfaces().size()>0)
					{
						for(String intfce:plugin.getInterfaces())
						{
							if(intfce.equals(depd))
							{
								try
								{
									urls.add(new URL("file:"+this.path+plugin.getPath()+File.separator));
									break;
								}
								catch (MalformedURLException mURLe)
								{
									this.logError("L'URL vers le plugin \""+plugin.getName()
											+"\" est mal définie:\n"+mURLe.getMessage());
									break;
								}
							}
						}
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
				this.screen.setNbElement(descriptor.getLibraries().size());
				this.screen.setProgress(0);
				this.screen.setStateMessage("Loading Plugin");
				int i=1;
				for(String library:descriptor.getLibraries())
				{
					URL lib=this.getLibraryURL(library);
					if(lib!=null)
					{
						urls.add(lib);
					}
					this.screen.setProgress(i++);
				}
			}
		}
		catch (MalformedURLException mURLe)
		{
			this.logError("Le chemin défini vers \""+descriptor.getName()+"\" est incorrect"+
						"\n"+mURLe.getMessage());
			return null;
		}
		ClassLoader ucl;
		if(superLoader!=null)
		{
			ucl=superLoader;
		}
		else
		{
			ucl=new URLClassLoader((URL[]) urls.toArray(new URL[0]),ClassLoader.getSystemClassLoader());			
		}
        try
        {
			c = Class.forName(descriptor.getClassName(),false,ucl);
		}
        catch (ClassNotFoundException cnfe)
        {
        	this.logError("La classe \""+descriptor.getClassName()+"\" pour le plugin \""+
						descriptor.getName()+"\" n'a pas été trouvée");
			return null;
		}
		if(!IPlugin.class.isAssignableFrom(c))
		{
			this.logError("Ce plugin n'est pas valide: La classe n'implémente" +
					" pas l'interface \""+IPlugin.class.getName()+"\"");
			return null;
		}

        return this.getPluginInstance(descriptor.getName(), (Class<?>)c,  active);
	}
	
	

	/** (non-Javadoc)
	 * @see main.ICore#getPluginInstance(java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public Object getPluginInstance(String pluginName, Class<?> classe, boolean active)
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
				this.logError("Le constructeur du plugin \""+
							descriptor.getName()+"\" n'a pas pu être trouvé");
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
				this.logError("Le constructeur du plugin \""+
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
				this.logWrite("Le plugin \""+descriptor.getName()+"\" a été chargé");
				descriptor.setPluginInstance(instance);
				return instance;
			}
			else
			{
				this.logError("Le plugin \""+descriptor.getName()+"\" n'a pas pu être chargé");
				return null;
			}
		}
		catch (InstantiationException ie)
		{
			this.logError("Impossible d'instancier le plugin \""+
					descriptor.getName()+"\""+
					"\n"+ie.getMessage());
	    	String reasons="Reasons: "+ie.getMessage()+"\n";
	    	if(ie.getCause()!=null)
	    	{
		    	for(StackTraceElement e:ie.getCause().getStackTrace())
		    	{
		    		reasons+="\t"+e.toString()+"\n";
		    	}
	    	}
	    	this.logError(reasons);
			if(descriptor.isActive()==active)
			{
				return this.getPluginInstance(pluginName, classe, !active);
			}
			return null;
		}
		catch (IllegalAccessException iae)
		{
			this.logError("Impossible d'instancier le plugin \""+
					descriptor.getName()+"\" (Illegal Access)"+
					"\n"+iae.getMessage());
	    	String reasons="Reasons:";
	    	for(StackTraceElement e:iae.getCause().getStackTrace())
	    	{
	    		reasons+="\t"+e.toString()+"\n";
	    	}
	    	this.logError(reasons);
			if(descriptor.isActive()==active)
			{
				return this.getPluginInstance(pluginName, classe, !active);
			}
			return null;
		}
	    catch (IllegalArgumentException iae)
		{
	    	this.logError("Impossible d'instancier le plugin \""+
					descriptor.getName()+"\" (Illegal Argument)"+
					"\n"+iae.getMessage());
	    	String reasons="Reasons:";
	    	for(StackTraceElement e:iae.getCause().getStackTrace())
	    	{
	    		reasons+="\t"+e.toString()+"\n";
	    	}
	    	this.logError(reasons);
			if(descriptor.isActive()==active)
			{
				return this.getPluginInstance(pluginName, classe, !active);
			}
			return null;
		}
	    catch (InvocationTargetException ite)
		{
	    	this.logError("Impossible d'instancier le plugin \""+
					descriptor.getName()+"\" (Invocation Target)"+
					"\n"+ite.getMessage());
	    	String reasons="Reasons:";
	    	for(StackTraceElement e:ite.getCause().getStackTrace())
	    	{
	    		reasons+="\t"+e.toString()+"\n";
	    	}
	    	this.logError(reasons);
			if(descriptor.isActive()==active)
			{
				return this.getPluginInstance(pluginName, classe, !active);
			}
			return null;
		}
	}

	/** (non-Javadoc)
	 * @see main.ICore#loadConfigs(java.lang.String)
	 */
	@Override
	synchronized public boolean loadConfigs()
	{
		if(this.fileName==null)
		{
			this.logError("Aucun fichier de configuration définit");
			return false;
		}
    	Properties p=new Properties();
    	this.logWrite("Chargement des configurations...");
        this.screen.setLabel("Chargement des configurations...");
		try
	    {
			p.load(new FileReader(this.fileName));
		}
		catch (FileNotFoundException fnfe)
		{
			this.logError("Impossible de charger le ficher \""+this.fileName+"\"\n"+
						fnfe.getMessage());
			return false;
		}
		catch (IOException ioe)
		{
			this.logError("Erreur lors de l'ouverture du ficher \""+this.fileName+"\"\n"
						+ioe.getMessage());
			return false;
		}
		this.path=p.getProperty("pluginsPath");
		this.path=this.path==null?p.getProperty("PluginsPath"):p.getProperty("pluginsPath");
		if(this.path==null)
		{
			this.logError("Le fichier \""+this.fileName+"\" ne contient pas d'attribut \"pluginsPath\"");
			return false;
		}
		if(!new File(this.path).exists())
		{
			this.path=new File("plugins").getAbsolutePath()+File.separator;
			this.logError("Le chemin vers le dossier de plugins n'est pas valide. " +
					"Utilisation du chemin: \""+this.path+"\"");
		}
		int nbPlugins=(p.size()-1);
		this.screen.setNbElement(nbPlugins);
		int i=1;
		this.logWrite(nbPlugins+" plugins présents dans:\n\""+path+"\"");
		this.logPrint("\tClasses:\t\tPath:\n");
		this.logPrint("\t---------------------------------\n");
		for(Object k:p.keySet())
		{
			if(!((String)k).equalsIgnoreCase("pluginsPath"))
			{
				String pluginName=(String)k;
				String pluginPath=(String)p.getProperty((String) k);
				this.screen.setLabel("Opening plugin "+pluginName+" from: "+pluginPath);
				try
				{
					Thread.sleep(70);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				this.screen.setProgress(i++);
				this.screen.setStateMessage("Opening Plugin");
				IPluginDescriptor descriptor=this.getPluginConfig(pluginName, pluginPath);
				if(descriptor!=null)
				{
					this.plugins.add(descriptor);
				}
			}
		}
		this.logWrite("Configurations chargées");
		return true;
	}
	
	/**
	 * Return the number of default plugins
	 * @return {@link Integer int}, Number of default plugins
	 */
	synchronized private int getNbDefaultPlugins()
	{
		if(this.plugins.isEmpty())
		{
			return 0;
		}
		int res=0;
		for(IPluginDescriptor desc:this.plugins)
		{
			if(desc.isDefault())
			{
				res++;
			}
		}
		return res;
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
			this.logError("\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \""+
					propertyName+"\"");
		}
		return property;
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
			this.logError("\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \""+
					propertyName+"\"");
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
			this.logError("\t - Le fichier \""+pluginFileConfig+"\" ne contient pas d'attribut \""+
						propertyName+"\"");
			return null;
		}
		if(property.length()>0)
		{
			array=new ArrayList<String>(Arrays.asList(property.replaceAll(" ", "").split(",")));
		}
		return array;
	}

	/** (non-Javadoc)
	 * @see main.ICore#getPluginConfig(java.lang.String,java.lang.String)
	 */
	public IPluginDescriptor getPluginConfig(String pluginName, String pluginPath)
	{
		this.logPrint(String.format("\t%-22s\t%s\n",pluginName,pluginPath));
		String pluginFileConfig=path+pluginPath+File.separator+"config.ini";
		Properties prop=new Properties();
		try
		{
			prop.load(new FileReader(pluginFileConfig));
		}
		catch (FileNotFoundException e)
		{
			this.logError("\t - Impossible de charger le ficher \""+pluginFileConfig+"\"\n"
								+e.getMessage());
			return null;
		}
		catch (IOException e)
		{
			this.logError("\t - Erreur lors de l'ouverture du ficher \""+pluginFileConfig+"\"\n"
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
		Boolean pluginIsSingleton=this.getBooleanProperty(prop, "singleton", pluginFileConfig);
		if(pluginIsSingleton==null)
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
		this.logPrint(String.format("\t  |-> %-10s %s\n\t  |-> %-10s %s\n\t  '-> %-10s %s\n",
				"Default:",pluginIsDefault?"Oui":"Non",
				"Active:",pluginIsActive?"Oui":"Non",
				"Singleton:",pluginIsSingleton?"Oui":"Non"));
		PluginDescriptor descriptor=new PluginDescriptor(pluginName,pluginPath,pluginIsDefault,
				pluginIsActive,pluginIsSingleton,className,pluginInterfaces,pluginLibraries,pluginDependencies);
		return descriptor;
	}

	/** (non-Javadoc)
	 * @see main.ICore#getPuginsByInterface(java.lang.String)
	 */
	@Override
	synchronized public ArrayList<IPluginDescriptor> getPuginsByInterface(String iplugin)
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
	
	/** (non-Javadoc)
	 * @see main.ICore#getPlugin(java.lang.String)
	 */
	@Override
	synchronized public Object getPlugin(String pluginName)
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

	/** (non-Javadoc)
	 * @see main.ICore#getPluginDescriptor(java.lang.String)
	 */
	@Override
	synchronized public IPluginDescriptor getPluginDescriptor(String pluginName)
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
	
	/** (non-Javadoc)
	 * @see main.ICore#getPlugins()
	 */
	@Override
	synchronized public ArrayList<IPluginDescriptor> getPlugins()
	{
		try
		{
			return this.plugins;
		}
		catch(ConcurrentModificationException cme)
		{
			this.logError("La liste de plugins n'est pas accessinle");
			return null;
		}
	}


	/** (non-Javadoc)
	 * @see main.ICore#removePlugin(IPluginDescriptor)
	 */
	@Override
	synchronized public boolean removePlugin(IPluginDescriptor plugin)
	{
		Properties prop=new Properties();
		try
		{
			prop.load(new FileReader(this.fileName));
		}
		catch (FileNotFoundException fnfe)
		{
			this.logError("Impossible de charger le ficher \""+this.fileName+"\"\n"+
						fnfe.getMessage());
			return false;
		}
		catch (IOException ioe)
		{
			this.logError("Erreur lors de l'ouverture du ficher \""+this.fileName+"\"\n"
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
			this.logError("La configuration du plugin \""+plugin.getName()+
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
			this.logError("Impossible d'enregistrer les configuration de la plateforme:\n"+
					ioe.getMessage());
		}
		
		return this.plugins.remove(plugin);
	}


	/** (non-Javadoc)
	 * @see main.ICore#unload(java.lang.String)
	 */
	@Override
	public boolean unload(String pluginName)
	{
		for(IPluginDescriptor plugin:this.plugins)
		{
			/*
			 * Trying to found the plugin
			 */
			if(plugin.getName().equalsIgnoreCase(pluginName))
			{
				/*
				 * We will unload the dependencies plugins
				 */
				if(plugin.getDependencies()!=null&&plugin.getDependencies().size()>0)
				{
					for(String depd:plugin.getDependencies())
					{
						for(IPluginDescriptor desc:this.plugins)
						{
							if(desc.getInterfaces()!=null&&desc.getInterfaces().size()>0)
							{
								for(String intfe:desc.getInterfaces())
								{
									/*
									 * If the interface of this plugin equals the dependency we unload it
									 */
									if(intfe.equalsIgnoreCase(depd))
									{
										this.unload(desc.getName());
									}
								}
							}
						}
					}
				}
				plugin.setLoaded(false);
				plugin.setPluginInstance(null);
				this.logWrite("Plugin \""+plugin.getName()+"\" déchargé");
				return true;
			}
		}
		return false;
	}

	/** (non-Javadoc)
	 * @see main.ICore#getDescriptor(java.lang.String, java.lang.String, boolean, boolean, boolean, java.lang.String, java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public IPluginDescriptor getDescriptor(String name, String path,
			boolean isDefault, boolean isActive, boolean isSingleton,
			String className, ArrayList<String> interfaces,
			ArrayList<String> libraries, ArrayList<String> dependencies)
	{
		return new PluginDescriptor(name, path, isDefault, isActive, isSingleton, className,
				interfaces, libraries, dependencies);
	}
	
	/** (non-Javadoc)
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
	// A METTRE EN PRIVATE
//	private void setFileName(String fileName)
	public void setFileName(String fileName)
	{
		this.fileName=fileName;
	}
	// A DETRUIRE
	public void setPath(String path)
	{
		this.path=path;
	}
	

	/** (non-Javadoc)
	 * @see main.ICore#logWrite(java.lang.String)
	 */
	@Override
	public void logWrite(String message)
	{
		for(ILogger log:this.logs)
		{
			log.write(message);
		}
	}

	/** (non-Javadoc)
	 * @see main.ICore#logPrint(java.lang.String)
	 */
	@Override
	public void logPrint(String message)
	{
		for(ILogger log:this.logs)
		{
			log.print(message);
		}
	}

	/** (non-Javadoc)
	 * @see main.ICore#logError(java.lang.String)
	 */
	@Override
	public void logError(String error)
	{
		for(ILogger log:this.logs)
		{
			log.error(error);
		}
	}

	/** (non-Javadoc)
	 * @see main.ICore#logDisplay()
	 */
	@Override
	public void logDisplay()
	{
		for(ILogger log:this.logs)
		{
			log.display();
		}
	}

	/**
	 * The main function
	 * @param args
	 */
	public static void main(String[] args)
	{
		new Core();
	}
}