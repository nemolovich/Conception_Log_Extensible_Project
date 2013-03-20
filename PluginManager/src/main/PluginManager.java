package main;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import main.plugin.IPlugin;
import main.plugin.IPluginDescriptor;

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
			Files.copy(new File(tempFolder+fileName).toPath(), 
						new File(pluginPath+fileName).toPath(),REPLACE_EXISTING);
			return true;
		}
		catch (IOException ioe)
		{
			System.out.println("[ERROR] Impossible de copier le fichier \""+fileName+"\"\n"+ioe.getMessage());
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
		System.out.println("[INFO] Création de la classe \""+className+".java\"...");
		FileInputStream fi=null;
		FileOutputStream fo=null;
		File tempFile=new File("temp"+File.separator+"example.java");
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
			System.out.println("[ERROR] Impossible de créer la classe \""+className+"\"\n"
						+fnfe.getMessage());
			return false;
		}
		catch (IOException ioe)
		{
			System.out.println("[ERROR] Impossible de créer la classe \""+className+"\"\n"
					+ioe.getMessage());
			return false;
		}
		System.out.println("[INFO] Classe \""+className+".java\" créée");
		
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
			System.out.println("[INFO] Création du dossier \""+folder+"\"...");
			return true;
		}
		else
		{
			System.out.println("[INFO] Le dossier \""+folder+"\" existe déjà");
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
				Files.deleteIfExists(path.toPath());
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
						return false;
					}
				}
				else
				{
					if(!Files.deleteIfExists(f.toPath()))
					{
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
	public boolean addPlugin(String pluginName, boolean isDefault, boolean isActive, boolean isLazy,
			ArrayList<IPluginDescriptor> parents, boolean erase)
	{
		System.out.println("[INFO] Adding plugin \""+pluginName+"\"...");
		String pluginDir=this.workspace+pluginName+File.separator;

		if(erase)
		{
			try
			{
				System.out.println("[INFO] Suppression du dossier \""+pluginDir+"\" et de son contenu...");
				if(this.deleteDirectory(pluginDir))
				{
					System.out.println("[INFO] Dossier \""+pluginDir+"\" supprimé");	
				}
				else
				{
					System.out.println("[ERROR] Le dossier \""+pluginDir+"\" n'a pas pu être supprimé");
					return false;
				}
			}
			catch (IOException ioe)
			{
				System.out.println("[ERROR] Le dossier \""+pluginDir+"\" n'a pas pu être supprimé\n"+
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
		
		System.out.println("[INFO] Loading config files...");
		XMLConfiguration config=null;
		try
		{
			config = new XMLConfiguration("temp/.project");
		}
		catch (ConfigurationException ce)
		{
			System.out.println("[ERROR] La configuration du fichier \"temp/.project\" semble invalide\n"+
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
			System.out.println("[ERROR] La configuration du fichier \""+pluginDir+".project\" semble invalide\n"+
					ce.getMessage());
			return false;
		}
		
		try
		{
			config = new XMLConfiguration("temp/.classpath");
		}
		catch (ConfigurationException ce)
		{
			System.out.println("[ERROR] La configuration du fichier \"temp/.classpath\" semble invalide\n"+
						ce.getMessage());
		}
		
		Properties prop=new Properties();
		prop.setProperty("className","main."+pluginName);
		prop.setProperty("active",isActive?"true":"false");
		prop.setProperty("default",isDefault?"true":"false");
		prop.setProperty("lazy",isLazy?"true":"false");
		
		String interfaces="";
		if(parents!=null&&parents.size()>0)
		{
			for(IPluginDescriptor parent:parents)
			{
				if(parent!=null&&parent.getDependencies()!=null&&parent.getDependencies().size()>0)
				{
					FileOutputStream jar_lib = null;
					try
					{
						jar_lib = new FileOutputStream(pluginDir+File.separator+"lib"+File.separator+
										parent.getName()+"_lib.jar");
					}
					catch (FileNotFoundException fnfe)
					{
						System.out.println("[ERROR] Fichier \""+parent.getName()+"_lib.jar\" introuvable");
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
		    		
					for(String intfce:parent.getDependencies())
					{
						interfaces+=intfce+",";
						String parentPath=this.core.getPluginDescriptor(
								parent.getName()).getPath();
						File interfaceFile=new File(this.core.getPath()+parentPath+File.separator+
								intfce.replaceAll("\\.", File.separator)+".class");
						
						if(!interfaceFile.isFile())
						{
							System.out.println("[ERROR] L'interface \""+intfce+".class\" n'a pas pu être trouvée");
							try
							{
								zip.close();
							}
							catch (IOException e)
							{
								System.out.println("[ERROR] Lors de la fermeture du fichier zip " +
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
								System.out.println("[ERROR] Fichier \""+intfce.replaceAll("\\.", File.separator)+
										".class\" introuvable");
								try
								{
									zip.close();
								}
								catch (IOException ioe)
								{
									System.out.println("[ERROR] Lors de la fermeture du fichier zip " +
											"\""+parent.getName()+"_lib.jar\": "+ioe.getMessage());
									ioe.printStackTrace();
								}
								return false;
							}
							byte buffer[] = new byte[(int)interfaceFile.length()];
							String classPath=interfaceFile.getPath();
							String className=(intfce+".class").substring(intfce.lastIndexOf(".")+1, intfce.length()+6);
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
									System.out.println("[ERROR] Impossible d'ajouter le dossier \"" +
											classPath+"\" dans l'archive"+ioe.getMessage());
									try
									{
										zip.close();
									}
									catch (IOException e)
									{
										System.out.println("[ERROR] Lors de la fermeture du fichier zip " +
												"\""+parent.getName()+"_lib.jar\": "+e.getMessage());
									}
									try
									{
										in.close();
									}
									catch (IOException e)
									{
										System.out.println("[ERROR] Lors de la fermeture du fichier \""+
											intfce.replaceAll("\\.", File.separator)+".class\": "+e.getMessage());
									}
									return false;
								}
							}
							try
							{
								zip.putNextEntry(new ZipEntry(classPath+className));
							}
							catch (IOException ioe)
							{
								System.out.println("[ERROR] Impossible d'ajouter la classe \"" +
										classPath+className+"\" dans l'archive"+ioe.getMessage());
								try
								{
									zip.close();
								}
								catch (IOException e)
								{
									System.out.println("[ERROR] Lors de la fermeture du fichier zip " +
											"\""+parent.getName()+"_lib.jar\": "+e.getMessage());
								}
								try
								{
									in.close();
								}
								catch (IOException e)
								{
									System.out.println("[ERROR] Lors de la fermeture du fichier \""+
										intfce.replaceAll("\\.", File.separator)+".class\": "+e.getMessage());
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
								System.out.println("[ERROR] Impossible d'écrire dans le fichier \""+
										intfce.replaceAll("\\.", File.separator)+".class\""+ioe.getMessage());
								return false;
							}
							try
							{
								in.close();
							}
							catch (IOException e)
							{
								System.out.println("[ERROR] Lors de la fermeture du fichier \""+
									intfce.replaceAll("\\.", File.separator)+".class\": "+e.getMessage());
								return false;
							}
						}
					}
					try
					{
						zip.close();
					}
					catch (IOException e)
					{
						System.out.println("[ERROR] Lors de la fermeture du fichier zip " +
								"\""+parent.getName()+"_lib.jar\": "+e.getMessage());
						return false;
					}
					interfaces=interfaces.substring(0, interfaces.length()-1);
				}
			}
		}
		prop.setProperty("interfaces",interfaces);
		prop.setProperty("dependencies","");
		prop.setProperty("libraries","");
		try
		{
			config.save(new File(pluginDir+".classpath"));
		}
		catch (ConfigurationException ce)
		{
			System.out.println("[ERROR] La configuration du fichier \""+pluginDir+".classpath\" semble invalide\n"+
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
			System.out.println("[ERROR] Impossible le fichier de configuration\n"
					+fnfe.getMessage());
			return false;
		}
		catch (IOException ioe)
		{
			System.out.println("[ERROR] Impossible le fichier de configuration\n"
					+ioe.getMessage());
			return false;
		}
		System.out.println("[INFO] Config file loaded");
		
		System.out.println("[INFO] Plugin \""+pluginName+"\" added");
		
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
	 * Returns the {@link IPlugin} from his {@link IPluginDescriptor1#getName() name}
	 * @param pluginName
	 * @return {@link String}
	 */
	public IPlugin getPlugin(String pluginName)
	{
		return this.core.getPlugin(pluginName);
	}
	
	/**
	 * Load a plugin on {@link Core}
	 * @param descriptor : {@link IPluginDescriptor1}, The plugin descriptor
	 * @param active : {@link Boolean boolean}, If the loading is active
	 * @return {@link IPlugin}, The plugin if it has been successfully loaded
	 */
	public IPlugin loadPlugin(IPluginDescriptor descriptor, boolean active)
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
				prop.setProperty("lazy",plugin.isLazy()?"true":"false");
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
					System.out.println("[ERROR] Le fichier \""+pluginFileConfig+"\" n'a pas été trouvé");
				}
				catch (IOException ioe)
				{
					System.out.println("[ERROR] Erreur lors de l'enregistrement de \""+
								pluginFileConfig+"\":\n"+ioe.getMessage());
				}
			}
		}
		return false;
	}

	/**
	 * Import a plugin from his eclipse project path
	 * @param folder : {@link File}, The project path
	 * @return {@link Boolean boolean}, If the project has been correctly imported
	 */
	public boolean importProject(File folder)
	{
		String path=folder.getPath().toString()+File.separator;
		if(!new File(path+"config.ini").isFile())
		{
			System.out.println("[ERROR] Le projet ne contient pas de fichier de configuration");
			return false;
		}
		String pluginName=folder.getName();
		this.makeDir(this.core.getPath()+pluginName+File.separator);
		try
		{
			Files.copy(new File(path+"config.ini").toPath(),new File(this.core.getPath()+
					pluginName+File.separator+"config.ini").toPath(),REPLACE_EXISTING);
		}
		catch (IOException ioe)
		{
			System.out.println("[ERROR] Erreur lors de la copie du fichier de configuration");
			return false;
		}
		File binaries=new File(path+"bin"+File.separator);
		if(!binaries.exists()||binaries.listFiles().length<1)
		{
			System.out.println("[ERROR] Le projet ne semble pas avoir été compilé");
			return false;
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
		catch (FileNotFoundException e)
		{
			System.out.println("[ERROR] Le fichier de configuration de la plateforme n'a" +
					" pas été trouvé");
			return false;
		}
		catch (IOException e)
		{
			System.out.println("[ERROR] Erreur lors du chargement des configurations de la" +
					" plateforme");
			return false;
		}
		
		return this.copyFolder(binaries,new File(this.core.getPath()+
				pluginName+File.separator));
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
					Files.copy(file.toPath(), new File(destination.toString()+File.separator+
							file.getName()).toPath(),REPLACE_EXISTING);
				}
				catch (IOException e)
				{

					System.out.println("[ERROR] Erreur lors dela copie du fichier \""+
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
	 * Main function
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("PluginManager");
//		Core core=new Core();
//		core.setFileName("config.ini");
//		core.setPath("/home/nemo/Documents/Info/Java/Projets/ProjectCLE/plugins/");
//		core.loadConfigs();
//		new PluginManager(core);
//		new PluginManager(null);
	}

}
