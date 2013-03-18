package main.plugin;

import java.util.ArrayList;

public class PluginDescriptor implements IPluginDescriptor
{
	/**
	 * Le nom du plugin
	 */
	private String name;
	/**
	 * La classe (nom de la classe) principale du plugin
	 */
	private String className;
	/**
	 * Les interfaces dont depend le plugin
	 */
	private ArrayList<String> interfaces=new ArrayList<String>();
	/**
	 * Les librairies dont depend le plugin
	 */
	private ArrayList<String> libraries=new ArrayList<String>();
	/**
	 * Le dossier qui contient les fichiers binaires du plugin
	 */
	private String path;
	/**
	 * Definit si le plugin a ete charge ou pas
	 */
	private boolean isLoaded=false;
	/**
	 * Definit si le chargement se fait au lancement du coeur
	 */
	private boolean isDefault;
	/**
	 * Definit si le plugin est actif ou passif vis-a-vis du coeur
	 */
	private boolean isActive;
	/**
	 * Definit si le plugin se fait par un chargement paresseux
	 */
	private boolean isLazy;
	/**
	 * L'instance du plugin
	 */
	private IPlugin pluginInstance;
	/**
	 * Les classes dont ont besoin les plugins qui dépendent de celui-ci
	 */
	private ArrayList<String> dependencies;
	
	/**
	 * Constructor from all attributes
	 * @param name : {@link String}, The plugin name
	 * @param path : {@link String}, The plugin path
	 * @param isDefault : {@link Boolean boolean}, If the plugin is loaded by default
	 * @param isActive : {@link Boolean boolean}, If the plugin is active
	 * @param isLazy : {@link Boolean boolean}, If the plugin is loaded by lazy loading
	 * @param className : {@link String}, The plugin class name
	 * @param interfaces : {@link ArrayList}<{@link String}>, The interfaces dependencies of the plugin
	 * @param libraries : {@link ArrayList}<{@link String}>, The libraries dependencies of the plugin
	 * @param dependencies : {@link ArrayList}<{@link String}>, The libraries dependencies of the plugin
	 */
	public PluginDescriptor(String name, String path,
			boolean isDefault, boolean isActive, boolean isLazy,
			String className, ArrayList<String> interfaces, ArrayList<String> libraries,
			ArrayList<String> dependencies)
	{
		this.name=name;
		this.path=path;
		this.isDefault=isDefault;
		this.isActive=isActive;
		this.isLazy=isLazy;
		this.className=className;
		this.interfaces=interfaces;
		this.libraries=libraries;
		this.dependencies=dependencies;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#getClassName()
	 */
	@Override
	public String getClassName()
	{
		return this.className;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#getInterfaces()
	 */
	@Override
	public ArrayList<String> getInterfaces() 
	{
		return this.interfaces;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setInterfaces(java.util.ArrayList)
	 */
	@Override
	public void setInterfaces(ArrayList<String> interfaces)
	{
		this.interfaces = interfaces;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#getLibraries()
	 */
	@Override
	public ArrayList<String> getLibraries()
	{
		return this.libraries;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setLibraries(java.util.ArrayList)
	 */
	@Override
	public void setLibraries(ArrayList<String> libraries)
	{
		this.libraries = libraries;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#isDefault()
	 */
	@Override
	public boolean isDefault()
	{
		return this.isDefault;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#isActive()
	 */
	@Override
	public boolean isActive()
	{
		return this.isActive;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#isLazy()
	 */
	@Override
	public boolean isLazy()
	{
		return this.isLazy;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#getName()
	 */
	@Override
	public String getName()
	{
		return this.name;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#getPath()
	 */
	@Override
	public String getPath()
	{
		return this.path;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setName(java.lang.String)
	 */
	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setClassName(java.lang.String)
	 */
	@Override
	public void setClassName(String className)
	{
		this.className = className;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setPath(java.lang.String)
	 */
	@Override
	public void setPath(String path)
	{
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setDefault(boolean)
	 */
	@Override
	public void setDefault(boolean isDefault)
	{
		this.isDefault = isDefault;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setActive(boolean)
	 */
	@Override
	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setLazy(boolean)
	 */
	@Override
	public void setLazy(boolean isLazy)
	{
		this.isLazy = isLazy;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setLoaded(boolean)
	 */
	@Override
	public void setLoaded(boolean loaded)
	{
		this.isLoaded=true;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#isLoaded()
	 */
	@Override
	public boolean isLoaded()
	{
		return this.isLoaded;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setPluginInstance(main.plugin.IPlugin)
	 */
	@Override
	public void setPluginInstance(IPlugin p)
	{
		this.pluginInstance=p;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#getPluginInstance()
	 */
	@Override
	public IPlugin getPluginInstance()
	{
		return this.pluginInstance;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#getDependencies()
	 */
	@Override
	public ArrayList<String> getDependencies()
	{
		return this.dependencies;
	}

	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#setDependencies(java.util.ArrayList)
	 */
	@Override
	public void setDependencies(ArrayList<String> dependencies)
	{
		this.dependencies=dependencies;
	}
	
	/* (non-Javadoc)
	 * @see main.plugin.IPluginDescriptor#toString()
	 */
	@Override
	public String toString()
	{
		String out = "main.plugin.Plugin[Name="+this.name+
				",Default="+this.isDefault+
				",Lazy="+this.isLazy+
				",Active="+this.isActive+
				",Path="+this.path;
		out+=	",Interfaces={";
		if(this.interfaces!=null&&this.interfaces.size()>0)
		{
			boolean first=true;
			for(String itfce:this.interfaces)
			{
				out+=itfce;
				if(first)
				{
					first ^=true;
				}
				else
				{
					out+=",";	
				}
			}
		}
		out+=	"}";
		out+=	",Libraries={";
		if(this.libraries!=null&&this.libraries.size()>0)
		{
			boolean first=true;
			for(String lib:this.libraries)
			{
				out+=lib;
				if(first)
				{
					first ^=true;
				}
				else
				{
					out+=",";	
				}
			}
		}
		out+=	"}";
		out+=	",Loaded="+this.isLoaded+
				"]";
		return out;
	}
}
