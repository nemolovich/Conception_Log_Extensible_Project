package main;

import static main.utils.Files.REPLACE_EXISTING;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import main.plugin.IPlugin;
import main.plugin.IPluginDescriptor;
import main.utils.DirectoryNotEmptyException;
import main.utils.Files;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.lang.StringUtils;

import view.PluginManagerView;

/**
 * @author Brian GOHIER
 *
 */
public class PluginManager implements IPlugin
{
	private ICore core;
	private String name="PluginManager";

	private String pluginManagerPath="/";
	private String workspace="/";
	
	/**
	 * The PluginManager active constructor
	 * @param core : {@link ICore}, The core
	 */
	public PluginManager(ICore core)
	{
		this.core=core;
		this.workspace=System.getProperty("user.dir");
		this.workspace=this.workspace.substring(0,this.workspace.lastIndexOf(File.separator)+1);
		if(this.core!=null)
		{
			IPluginDescriptor desc=this.core.getPluginDescriptor(this.name);
			if(desc!=null)
			{
				this.pluginManagerPath=this.core.getPath()+desc.getPath()+File.separator;
			}
		}
		new PluginManagerView(this,core!=null?core.getPlugins():null);
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Copy a file from a path to another
	 * @param pluginPath : {@link String}, The destination path
	 * @param tempFolder : {@link String}, The source path
	 * @param fileName : {@link String}, The file name
	 * @return {@link Boolean boolean}, If the file has been correctly copied
	 */
	private boolean copyFile(String pluginPath, String tempFolder, String fileName)
	{
		try
		{
			Files.copy(new File(tempFolder+fileName), 
						new File(pluginPath+fileName),REPLACE_EXISTING);
			
			return true;
		}
		catch (IOException ioe)
		{
			this.core.logError("Impossible de copier le fichier \""+fileName+"\"\n"+ioe.getMessage());
			return false;
		}
	}
	
	/**
	 * Return the lines list from a string buffer
	 * @param buffer : {@link String}, The buffer
	 * @return {@link ArrayList}<{@link String}>, The lines list
	 */
	private ArrayList<String> getLines(String buffer)
	{
		ArrayList<String> lines=new ArrayList<String>();
		String line="";
		for(int i=0;i<buffer.length();i++)
		{
			if(buffer.charAt(i)!='\n')
			{
				line+=buffer.charAt(i);
			}
			else
			{
				lines.add(line);
				line="";
			}
		}
		return lines;
	}
	
	/**
	 * Return the string lines from a lines list
	 * @param lines : {@link ArrayList}<{@link String}>, The lines list
	 * @param name : {@link String}, The plugin name
	 * @param start : {@link Integer int}, The start line number
	 * @param end : {@link Integer int}, The end line number
	 * @return {@link String}, The string lines
	 */
	private String getLinesFrom(ArrayList<String> lines, String name, int start, int end)
	{
		String result="";
		int i=1;
		for(String line:lines)
		{
			if(i>=start&&i<=end)
			{
				String tmpLine=line;
				if(tmpLine.contains("PluginName"))
				{
					tmpLine=tmpLine.replace("PluginName", name);
				}
				result+=tmpLine+"\n";
			}
			i++;
		}
		return result;
	}
	
	/**
	 * Create a class file in a path from a plugin name
	 * @param path : {@link String}, The path to create the class
	 * @param className : {@link String}, The plugin name
	 * @param active : {@link Boolean boolean}, If the plugin is active
	 * @return {@link Boolean boolean}, If the class has been correctly created
	 */
	private boolean createMainClass(String path, String className, boolean active)
	{
		this.core.logWrite("Création de la classe \""+className+".java\"...");
		FileInputStream fi=null;
		FileOutputStream fo=null;
		File tempFile=new File(this.pluginManagerPath+"temp"+File.separator+"example.java");
		File classFile=new File(path+className+".java");
		try
		{
			fi=new FileInputStream(tempFile);
			fo=new FileOutputStream(classFile);
			byte[] buf = new byte[(int)tempFile.length()];
			while ((fi.read(buf)) > 0)
			{
			}
			String buffer="";
			for(byte b:buf)
			{
				buffer+=(char)b;
			}
			ArrayList<String> lines=this.getLines(buffer);
			buffer=this.getLinesFrom(lines, className, 1, 2);
			if(active)
			{
				buffer+=this.getLinesFrom(lines, className, 3, 3);
			}
			buffer+=getLinesFrom(lines, className, 4, 12);
			if(active)
			{
				buffer+=getLinesFrom(lines, className, 13, 24);
			}
			else
			{
				buffer+=getLinesFrom(lines, className, 24, 32);
			}
			buffer+=this.getLinesFrom(lines, className, 33, lines.size());
			fo.write(buffer.getBytes());
			fi.close();
			fo.close();
		}
		catch (FileNotFoundException fnfe)
		{
			this.core.logError("Impossible de créer la classe \""+className+"\"\n"
						+fnfe.getMessage());
			return false;
		}
		catch (IOException ioe)
		{
			this.core.logError("Impossible de créer la classe \""+className+"\"\n"
					+ioe.getMessage());
			return false;
		}
		this.core.logWrite("Classe \""+className+".java\" créée");
		
		return true;
	}
	
	/**
	 * Create a directory
	 * @param folder : {@link String}, The directory name
	 * @return {@link Boolean boolean}, If the directory has been correctly created
	 */
	private boolean makeDir(String folder)
	{
		if(new File(folder).mkdir())
		{
			this.core.logWrite("Création du dossier \""+folder+"\"...");
			return true;
		}
		else
		{
			this.core.logWrite("Le dossier \""+folder+"\" existe déjà");
			return false;
//			return true;
		}
	}
	
	/**
	 * Return true if the project exists
	 * @param projectName : {@link String}, The project name
	 * @return {@link Boolean boolean}, If the project exists
	 */
	public boolean isExistingProject(String projectName)
	{
		if(new File(this.workspace+projectName).isDirectory())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Delete a directory from path name
	 * @param pathName : {@link String}, The path name
	 * @return {@link Boolean boolean}, If the directory has been correctly deleted
	 * @throws IOException
	 */
	private boolean deleteDirectory(String pathName) throws IOException
	{
		File path=new File(pathName);
		try
		{
			if(path.isDirectory())
			{
				Files.deleteIfExists(path);
				this.core.logWrite("Dossier \""+path.getName()+"\" supprimé");
				return true;
			}
		}
		catch(DirectoryNotEmptyException dnee)
		{
			for(File f:path.listFiles())
			{
				if(f.isDirectory())
				{
					if(!this.deleteDirectory(f.getAbsolutePath()))
					{
						this.core.logError("Impossible de supprimer le dossier \""+
									f.getName()+"\"");
						return false;
					}
				}
				else
				{
					if(!Files.deleteIfExists(f))
					{
						this.core.logError("Impossible de supprimer le fichier \""+
								f.getName()+"\"");
						return false;
					}
				}
			}
			this.deleteDirectory(pathName);
		}
		return true;
	}
	
	/**
	 * Add a plugin into eclipse workspace
	 * @param pluginName : {@link String}, The plugin name
	 * @param active : {@link Boolean boolean}, If the plugin is active
	 * @return
	 */
	@SuppressWarnings("resource")
	public boolean addPlugin(String pluginName, boolean isDefault, boolean isActive, boolean isSingleton,
			ArrayList<IPluginDescriptor> parents, boolean erase)
	{
		this.core.logWrite("Adding plugin \""+pluginName+"\"...");
		String pluginDir=this.workspace+pluginName+File.separator;

		if(erase)
		{
			try
			{
				this.core.logWrite("Suppression du dossier \""+pluginDir+"\" et de son contenu...");
				if(this.deleteDirectory(pluginDir))
				{
					this.core.logWrite("Dossier \""+pluginDir+"\" supprimé");	
				}
				else
				{
					this.core.logError("Le dossier \""+pluginDir+"\" n'a pas pu être supprimé");
					return false;
				}
			}
			catch (IOException ioe)
			{
				this.core.logError("Le dossier \""+pluginDir+"\" n'a pas pu être supprimé\n"+
							ioe.getMessage());
				return false;
			}
		}
		
		if(!this.makeDir(pluginDir))
		{
			return false;
		}
		if(!this.makeDir(pluginDir+"src"+File.separator))
		{
			return false;
		}
		if(!this.makeDir(pluginDir+"src"+File.separator+"main"+File.separator))
		{
			return false;
		}
		if(!this.makeDir(pluginDir+"bin"+File.separator))
		{
			return false;
		}
		if(!this.makeDir(pluginDir+".settings"+File.separator))
		{
			return false;
		}
		if(!this.copyFile(pluginDir+".settings"+File.separator, this.pluginManagerPath+
				".settings"+File.separator, "org.eclipse.jdt.core.prefs"))
		{
			return false;
		}
		if(!this.makeDir(pluginDir+"lib"+File.separator))
		{
			return false;
		}
		if(!this.copyFile(pluginDir+"lib"+File.separator, this.pluginManagerPath+
				"lib"+File.separator, "core_lib.jar"))
		{
			return false;
		}
		
		if(!this.createMainClass(pluginDir+"src"+File.separator+"main"+File.separator, pluginName, isActive))
		{
			return false;
		}
		
		this.core.logWrite("Loading config files...");
		XMLConfiguration config=null;
		try
		{
			config = new XMLConfiguration(this.pluginManagerPath+"temp/.project");
		}
		catch (ConfigurationException ce)
		{
			this.core.logError("La configuration du fichier \"temp/.project\" semble invalide\n"+
						ce.getMessage());
			return false;
		}
		Node root = config.getRoot();
		ConfigurationNode name=root.getChild(0);
		name.setValue(pluginName);
		try
		{
			config.save(new File(pluginDir+".project"));
		}
		catch (ConfigurationException ce)
		{
			this.core.logError("La configuration du fichier \""+pluginDir+".project\" semble invalide\n"+
					ce.getMessage());
			return false;
		}
		
		try
		{
			config = new XMLConfiguration(this.pluginManagerPath+"temp/.classpath");
		}
		catch (ConfigurationException ce)
		{
			this.core.logError("La configuration du fichier \"temp/.classpath\" semble invalide\n"+
						ce.getMessage());
		}
		
		Properties prop=new Properties();
		prop.setProperty("className","main."+pluginName);
		prop.setProperty("active",isActive?"true":"false");
		prop.setProperty("default",isDefault?"true":"false");
		prop.setProperty("singleton",isSingleton?"true":"false");
		
		String interfaces="";
		String libraries="";
		if(parents!=null&&parents.size()>0)
		{
			for(IPluginDescriptor parent:parents)
			{
				if(parent!=null&&parent.getDependencies()!=null&&parent.getDependencies().size()>0)
				{					
					/*
					 * The dependencies libraries
					 */
					FileOutputStream jar_lib = null;
					try
					{
						jar_lib = new FileOutputStream(pluginDir+File.separator+"lib"+File.separator+
										parent.getName()+"_lib.jar");
					}
					catch (FileNotFoundException fnfe)
					{
						this.core.logError("Fichier \""+parent.getName()+"_lib.jar\" introuvable");
						return false;
					}
		    		ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(jar_lib));
		    		
					root = config.getRoot();
					ConfigurationNode entry = new Node();
					entry.setName("classpathentry");
					ConfigurationNode kind = new Node();
					kind.setAttribute(true);
					kind.setName("kind");
					kind.setValue("lib");
					entry.addAttribute(kind);
					ConfigurationNode path = new Node();
					path.setName("path");
					path.setValue("lib"+File.separator+parent.getName()+"_lib.jar");
					entry.addAttribute(path);
					root.addChild(entry);
					try
					{
						config.save(new File(pluginDir+".classpath"));
					}
					catch (ConfigurationException ce)
					{
						this.core.logError("La configuration du fichier \""+pluginDir+".classpath\" semble invalide\n"+
								ce.getMessage());
						return false;
					}
		    		
					for(String intfce:parent.getDependencies())
					{
						interfaces+=intfce+",";
						String parentPath=this.core.getPluginDescriptor(
								parent.getName()).getPath();
						File[] allFiles;
						if(intfce.endsWith("*"))
						{
							allFiles=new File(this.core.getPath()+parentPath+File.separator+
									intfce.replaceAll("\\.", File.separator).
									replaceAll("\\*", "")).listFiles();
						}
						else
						{
							allFiles=new File[]{new File(this.core.getPath()+parentPath+File.separator+
									intfce.replaceAll("\\.", File.separator)+".class")};
						}
						
						for(File interfaceFile:allFiles)
						{
							if(!interfaceFile.isFile())
							{
								this.core.logError("L'interface \""+interfaceFile.getName()+"\" " +
										"n'a pas pu être trouvée");
								try
								{
									zip.close();
								}
								catch (IOException e)
								{
									this.core.logError("Lors de la fermeture du fichier zip " +
											"\""+parent.getName()+"_lib.jar\": "+e.getMessage());
								}
								return false;
							}
							else
							{
								FileInputStream in = null;
								try
								{
									in = new FileInputStream(interfaceFile);
								}
								catch (FileNotFoundException fnfe)
								{
									this.core.logError("Fichier \""+interfaceFile.getName()+
											"\" introuvable");
									try
									{
										zip.close();
									}
									catch (IOException ioe)
									{
										this.core.logError("Lors de la fermeture du fichier zip " +
												"\""+parent.getName()+"_lib.jar\": "+ioe.getMessage());
										ioe.printStackTrace();
									}
									return false;
								}
								byte buffer[] = new byte[(int)interfaceFile.length()];
								String classPath=interfaceFile.getPath();
								String className=interfaceFile.getName();
								classPath=classPath.substring(0,classPath.lastIndexOf(File.separator)+1);
								String[] folds=classPath.substring(classPath.lastIndexOf(parentPath)+
										parentPath.length()+1, classPath.length()).split("/");
								classPath="";
								for(int i=0;i<folds.length;i++)
								{
									classPath+=folds[i]+File.separator;
									try
									{
										zip.putNextEntry(new ZipEntry(classPath));
									}
									catch (IOException ioe)
									{
										this.core.logPrint("[WARNING] Impossible d'ajouter le dossier \"" +
												classPath+"\" dans l'archive:\n"+ioe.getMessage()+"\n");
									}
								}
								try
								{
									zip.putNextEntry(new ZipEntry(classPath+className));
								}
								catch (IOException ioe)
								{
									this.core.logError("Impossible d'ajouter la classe \"" +
											interfaceFile.getName()+"\" dans l'archive"+ioe.getMessage());
									try
									{
										zip.close();
									}
									catch (IOException e)
									{
										this.core.logError("Lors de la fermeture du fichier zip " +
												"\""+parent.getName()+"_lib.jar\": "+e.getMessage());
									}
									try
									{
										in.close();
									}
									catch (IOException e)
									{
										this.core.logError("Lors de la fermeture du fichier \""+
												interfaceFile.getName()+"\": "+e.getMessage());
									}
									return false;
								}
								int len;
								try
								{
									while((len=in.read(buffer))>0)
									{
										zip.write(buffer,0,len);
									}
								}
								catch (IOException ioe)
								{
									this.core.logError("Impossible d'écrire dans le fichier \""+
											interfaceFile.getName()+"\""+ioe.getMessage());
									return false;
								}
								try
								{
									in.close();
								}
								catch (IOException e)
								{
									this.core.logError("Lors de la fermeture du fichier \""+
											interfaceFile.getName()+"\": "+e.getMessage());
									return false;
								}
							}
						}
					}
					try
					{
						zip.close();
					}
					catch (IOException e)
					{
						this.core.logError("Lors de la fermeture du fichier zip " +
								"\""+parent.getName()+"_lib.jar\": "+e.getMessage());
						return false;
					}
					interfaces=interfaces.substring(0, interfaces.length()-1);
				}


				if(parent!=null&&parent.getLibraries()!=null&&parent.getLibraries().size()>0)
				{
					/*
					 * The libraries of parent
					 */
					for(String lib:parent.getLibraries())
					{
						libraries+=lib+",";
						if(!this.copyFile(pluginDir+"lib"+File.separator, this.core.getPath()+
								"libs"+File.separator, lib))
						{
							this.core.logError("La librairie \""+lib+"\" n'a pas " +
									"pû être exportée vers le plugin \""+pluginName+"\"");
							return false;
						}
					}
					libraries=libraries.substring(0, libraries.length()-1);
				}
			}
		}
		prop.setProperty("libraries",libraries);
		prop.setProperty("interfaces",interfaces);
		prop.setProperty("dependencies","");
		try
		{
			config.save(new File(pluginDir+".classpath"));
		}
		catch (ConfigurationException ce)
		{
			this.core.logError("La configuration du fichier \""+pluginDir+".classpath\" semble invalide\n"+
					ce.getMessage());
			return false;
		}
		try
		{
			prop.store(new FileOutputStream(new File(pluginDir+"config.ini"))," "+pluginName+
					" config (auto-generated by "+this.getName()+")");
		}
		catch (FileNotFoundException fnfe)
		{
			this.core.logError("Fichier de configuration non trouvé\n"
					+fnfe.getMessage());
			return false;
		}
		catch (IOException ioe)
		{
			this.core.logError("Impossible d'enregistrer le fichier de configuration\n"
					+ioe.getMessage());
			return false;
		}
		this.core.logWrite("Config file loaded");
		
		this.core.logWrite("Plugin \""+pluginName+"\" added");
		
		return true;
	}
	
	/**
	 * Return the eclipse workspace
	 * @return {@link String}, The workspace
	 */
	public String getWorkspace()
	{
		return this.workspace;
	}
	
	/**
	 * Returns the {@link Object} from his {@link IPluginDescriptor1#getName() name}
	 * @param pluginName
	 * @return {@link String}
	 */
	public Object getPlugin(String pluginName)
	{
		return this.core.getPlugin(pluginName);
	}
	
	/**
	 * Load a plugin on {@link Core}
	 * @param descriptor : {@link IPluginDescriptor1}, The plugin descriptor
	 * @param active : {@link Boolean boolean}, If the loading is active
	 * @return {@link Object}, The plugin if it has been successfully loaded
	 */
	public Object loadPlugin(IPluginDescriptor descriptor, boolean active)
	{
		return this.core.loadPlugin(descriptor, active);
	}

	/**
	 * Return the PluginManager path
	 * @return {@link String}, The PluginManager path
	 */
	public String getPluginManagerPath()
	{
		return this.pluginManagerPath;
	}

	/**
	 * Set the PluginManager path
	 * @param {@link String}, The PluginManager path
	 */
	public void setPluginManagerPath(String pluginManagerPath)
	{
		this.pluginManagerPath = pluginManagerPath;
	}
	
	/**
	 * Return the {@link ICore} path
	 * @return {@link String}, The core path
	 */
	public String getCorePath()
	{
		return this.core.getPath();
	}

	/**
	 * Retourne la liste des {@link IPluginDescriptor descripteurs de plugin}
	 * @return {@link ArrayList}<{@link IPluginDescriptor}>, La liste des plugins
	 */
	public ArrayList<IPluginDescriptor> getPluginsDescriptors()
	{
		return this.core.getPlugins();
	}

	/**
	 * Return the {@link IPluginDescriptor plugin descriptor} from his file
	 * @param pluginName : {@link String}, The plugin name
	 * @param pluginPath : {@link String}, The plugin path
	 * @return {@link IPluginDescriptor}, The plugin descriptor
	 */
	public IPluginDescriptor getPluginConfig(String pluginName, String pluginPath)
	{
		return this.core.getPluginConfig(pluginName, pluginPath);
	}
	
	/**
	 * Set the plugin attributes from his {@link IPluginDescriptor descriptor}
	 * @param plugin : {@link IPluginDescriptor}, The plugin descriptor
	 * @return {@link Boolean boolean}, If the plugin has been found
	 */
	public boolean setPluginConfig(IPluginDescriptor plugin)
	{
		for(IPluginDescriptor desc:this.core.getPlugins())
		{
			if(desc.getName().equals(plugin.getName()))
			{
				String pluginFileConfig=this.core.getPath()+plugin.getPath()+File.separator+"config.ini";
				Properties prop=new Properties();
				prop.setProperty("className", plugin.getClassName());
				prop.setProperty("active",plugin.isActive()?"true":"false");
				prop.setProperty("default",plugin.isDefault()?"true":"false");
				prop.setProperty("singleton",plugin.isSingleton()?"true":"false");
				String[] interfacesArray=plugin.getInterfaces().toArray(new String[0]);
				String interfaces=StringUtils.join(interfacesArray,",");
				prop.setProperty("interfaces",interfaces);
				String[] dependenciesArray=plugin.getDependencies().toArray(new String[0]);
				String dependencies=StringUtils.join(dependenciesArray,",");
				prop.setProperty("dependencies",dependencies);
				String[] librariesArray=plugin.getLibraries().toArray(new String[0]);
				String libraries=StringUtils.join(librariesArray,",");
				prop.setProperty("libraries",libraries);
				try
				{
					prop.store(new FileOutputStream(pluginFileConfig)," "+plugin.getName()+
							" configs (auto-generated by "+this.getName()+")");
					return true;
				}
				catch (FileNotFoundException fnfe)
				{
					this.core.logError("Le fichier \""+pluginFileConfig+"\" n'a pas été trouvé");
				}
				catch (IOException ioe)
				{
					this.core.logError("Erreur lors de l'enregistrement de \""+
								pluginFileConfig+"\":\n"+ioe.getMessage());
				}
			}
		}
		return false;
	}
	
	/**
	 * Get the plugin descriptor by {@link IPluginDescriptor#getName() name}.
	 * from the plugins loaded in core.
	 * @param pluginName - {@link String}, The plugin name
	 * @return {@link IPluginDescriptor}, The plugin descriptor null if does not exist
	 */
	public IPluginDescriptor getPluginDescriptor(String pluginName)
	{
		return this.core.getPluginDescriptor(pluginName);
	}

	/**
	 * Import a plugin from his eclipse project path
	 * @param folder : {@link File}, The project path
	 * @return {@link Boolean boolean}, If the project has been correctly imported
	 */
	public String importProject(File folder)
	{
		String path=folder.getPath().toString()+File.separator;
		String error=null;
		if(!new File(path+"config.ini").isFile())
		{
			error="Le projet ne contient pas de fichier de configuration";
			this.core.logError(""+error);
			return error;
		}
		String pluginName=folder.getName();
		this.makeDir(this.core.getPath()+pluginName+File.separator);
		try
		{
			Files.copy(new File(path+"config.ini"),new File(this.core.getPath()+
					pluginName+File.separator+"config.ini"),REPLACE_EXISTING);
		}
		catch (IOException ioe)
		{
			error="Erreur lors de la copie du fichier de configuration:\n"
					+ioe.getMessage();
			this.core.logError(""+error);
			return error;
		}
		File binaries=new File(path+"bin"+File.separator);
		if(!binaries.exists()||binaries.listFiles().length<1)
		{
			error="Le projet ne semble pas avoir été compilé";
			this.core.logError(""+error);
			return error;
		}

		Properties plugProp=new Properties();
		try
		{
			plugProp.load(new FileReader(path+"config.ini"));
		}
		catch (FileNotFoundException fnfe)
		{
			error="Impossible d'accéder au fichier de configuration du plugin";
			this.core.logError(""+error);
			return error;
		}
		catch (IOException ioe)
		{
			error="Erreur lors de la récupération des configuration du plugin:\n"
					+ioe.getMessage();
			this.core.logError(""+error);
			return error;
		}
		String libraries=plugProp.getProperty("libraries");
		if(libraries==null)
		{
			error="Le fichier de configuration ne contient pas d'attribut \"libraries\"";
			this.core.logError(""+error);
			return error;
		}
		ArrayList<String> libs=new ArrayList<String>(Arrays.asList(libraries.split(",")));
		String librariesPath=this.core.getPath()+"libs"+File.separator;
		for(String lib:libs)
		{
			if(lib.length()>0)
			{
				try
				{
					Files.copy(new File(path+"lib"+File.separator+lib),
							new File(librariesPath+lib),REPLACE_EXISTING);
				}
				catch (IOException ioe)
				{
					error="Impossible de copier a librairie \""+lib+"\" " +
							"sur la plateforme:\n"+ioe.getMessage();
					this.core.logError(""+error);
					return error;
				}
			}
		}
		
		Properties prop=new Properties();
		try
		{
			prop.load(new FileReader(this.core.getPath().substring(0,
					this.core.getPath().lastIndexOf("plugins"))+"config.ini"));
			prop.setProperty(pluginName, pluginName);
			prop.store(new FileOutputStream(this.core.getPath().substring(0,
					this.core.getPath().lastIndexOf("plugins"))+"config.ini"),
					" Core config (auto-generated by "+this.getName()+")");
		}
		catch (FileNotFoundException fnfe)
		{
			error="Le fichier de configuration de la plateforme n'a" +
			" pas été trouvé";
			this.core.logError(""+error);
			return error;
		}
		catch (IOException ioe)
		{
			error="Erreur lors du chargement des configurations de la" +
			" plateforme:\n"+ioe.getMessage();
			this.core.logError(""+error);
			return error;
		}
		
		
		if(this.copyFolder(binaries,new File(this.core.getPath()+
				pluginName+File.separator)))
		{
			return error;
		}
		else
		{
			error="Impossible de copier le dossier "+this.core.getPath()+pluginName;
			return error;
		}
	}
	
	/**
	 * Copy a folder and its content from a source to destination
	 * @param source : {@link File}, The source folder
	 * @param destination : {@link File}, The destination folder
	 * @return {@link Boolean boolean}, If the folder has been correctly copied
	 */
	private boolean copyFolder(File source, File destination)
	{
		this.makeDir(destination.toString());
		for(File file:source.listFiles())
		{
			if(file.isFile())
			{
				try
				{
					Files.copy(file, new File(destination.toString()+File.separator+
							file.getName()),REPLACE_EXISTING);
				}
				catch (IOException e)
				{

					this.core.logError("Erreur lors dela copie du fichier \""+
							destination.toString()+File.separator+file.getName()+"\"");
				}
			}
			else if(file.isDirectory())
			{
				this.copyFolder(file, new File(destination.toString()+File.separator+
						file.getName()));
			}
		}
		return true;
	}

	/**
	 * Remove the plugin from plugin list and platform plugins
	 * @param plugin : {@link IPluginDescriptor}, The plugin
	 * @return {@link Boolean boolean}, If the plugin 
	 */
	public boolean removePlugin(IPluginDescriptor plugin)
	{
		if(this.core.getPlugins().contains(plugin))
		{
			String pluginPath=this.getCorePath()+plugin.getPath()+File.separator;
			try
			{
				if(!this.deleteDirectory(pluginPath))
				{
					this.core.logError("Le dossier du plugin \""+plugin.getName()+
							"\" n'a pas été supprimé");
					return false;
				}
			}
			catch(IOException ioe)
			{
				this.core.logError("Erreur lors de la suppression du dossier du plugin " +
						"\""+plugin.getName()+"\"");
				return false;
			}
			return this.core.removePlugin(plugin);
		}
		this.core.logError("Le plugin \""+plugin.getName()+"\" n'a pas été trouvé");
		return false;
	}

	/**
	 * Unload this plugin
	 * @return {@link Boolean boolean}, True if the plugin has been unloaded
	 */
	public boolean unload()
	{
		return this.core.unload(this.name);
	}
	
	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("PluginManager");
//		Core core=new Core();
//		core.setFileName("/home/nemo/Documents/Info/Java/Projets/ProjectCLE/config.ini");
//		core.setPath("/home/nemo/Documents/Info/Java/Projets/ProjectCLE/plugins/");
//		core.loadConfigs();
//		core.logDisplay();
//		new PluginManager(core);
	}

}
