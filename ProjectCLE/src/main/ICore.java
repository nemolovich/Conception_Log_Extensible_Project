package main;

import java.util.ArrayList;

import main.plugin.IPlugin;
import main.plugin.IPluginDescriptor;

/**
 * Interface ICore: L'interface qu'implémente le coeur.
 * @author Brian GOHIER
 * @see Core
 */

public interface ICore
{
	/**
	 * Charge un plugin depuis son {@link IPluginDescriptor descripteur} et retourne
	 * son instance, "null" si une erreur est arrivée.
	 * @param descriptor - {@link IPluginDescriptor} : Le descripteur du plugin
	 * @param active - {@link Boolean boolean} : Définit si le plugin doit
	 * être chargé activement (dépendant de la plateforme) ou non
	 * @return {@link Object} : L'instance du plugin si il a été chargé
	 * correctement "null" sinon
	 */
	public abstract Object loadPlugin(IPluginDescriptor descriptor,
			boolean active);

	/**
	 * Retourne l'instance d'un plugin à partir de son nom et de la classe
	 * qui le définit (qui peut être actif ou passif).
	 * @param pluginName {@link String} : Le nom du plugin
	 * @param classe {@link Class}<{@link IPlugin}> : La classe du plugin
	 * @param active - {@link Boolean boolean} : Si le plugin est actif
	 * @return {@link Object} : L'instance du plugin, "null" si erreur
	 */
	public Object getPluginInstance(String pluginName, Class<?> classe, boolean active);

	/**
	 * Charge les configurations du coeur à partir de son fichier de 
	 * configurations, ainsi pour chaque plugin défini dans ce fichier, on va
	 * ouvrir son fichier de configuration et enregistrer ses paramètres dans
	 * un nouveau {@link IPluginDescriptor descripteur}.
	 * @return {@link Boolean boolean} : Vrai si les configurations ont été
	 * correctement chargées
	 */
	public abstract boolean loadConfigs();

	/**
	 * Retourne le {@link IPluginDescriptor plugin descripteur} d'un plugin
	 * depuis son nom et son dossier.
	 * @param pluginName {@link String} : Le nom du plugin
	 * @param pluginPath {@link String} : Le chemin vers le dossier du plugin
	 * @return {@link IPluginDescriptor} : Le descripteur du plugin, "null" si erreur
	 */
	public abstract IPluginDescriptor getPluginConfig(String pluginName, String pluginPath);
	/**
	 * Retourne la {@link ArrayList liste} des {@link IPluginDescriptor plugins}
	 *  que connaît la plateforme et qui implémentent (depuis leur fichier de
	 *  configurations) l'interface qui porte le nom donné.
	 * @param iplugin {@link String} : Le nom de l'interface
	 * @return {@link ArrayList}<{@link IPluginDescriptor}> : La liste des plugins
	 * qui implémentent cette interface
	 */
	public abstract ArrayList<IPluginDescriptor> getPuginsByInterface(String iplugin);

	/**
	 * Retourne l'instance d'un plugin depuis son {@link IPlugin#getName() nom}
	 * à partir des plugins connus par la plateforme.
	 * @param pluginName - {@link String} : Le nom du plugin
	 * @return {@link Object} : L'instance du plugin, "null" si non trouvé
	 */
	public abstract Object getPlugin(String pluginName);

	/**
	 * Retourne le {@link IPluginDescriptor descripteur} d'un plugin
	 * depuis son {@link IPluginDescriptor#getName() nom} à partir de la
	 * liste des plugins connus par la plateforme.
	 * @param pluginName - {@link String} : Le nom du plugin
	 * @return {@link IPluginDescriptor} : Le descripteur du plugin,
	 * "null" si il n'existe pas
	 */
	public abstract IPluginDescriptor getPluginDescriptor(String pluginName);

	/**
	 * Retourne tous les {@link IPluginDescriptor descripteurs} de plugin que
	 * connaît la plateforme.
	 * @return {@link ArrayList}<{@link IPluginDescriptor}> : La liste des plugins
	 */
	public abstract ArrayList<IPluginDescriptor> getPlugins();

	/**
	 * Supprime un plugin de la liste des plugins de la plateforme.
	 * @param plugin {@link IPluginDescriptor} : Le plugin
	 * @return {@link Boolean boolean} : Vrai si le plugin a été correctement
	 * supprimé
	 */
	public abstract boolean removePlugin(IPluginDescriptor plugin);

	/**
	 * Retourne un {@link IPluginDescriptor descripteur} créé à partir de ses attributs.
	 * @param name {@link String} : Le nom du plugin
	 * @param path {@link String} : Le chemin de son dossier
	 * @param isDefault {@link Boolean boolean} : Si le plugin est chargé par défaut
	 * @param isActive {@link Boolean boolean} : Si le plugin est actif
	 * @param isSingleton {@link Boolean boolean} : Si le plugin est une instance unique ou non
	 * @param className {@link String} : Le nom de la classe principale du plugin
	 * @param interfaces {@link ArrayList}<{@link String}> : Les interfaces dont dépend le plugin
	 * @param libraries {@link ArrayList}<{@link String}> : Les librairies dont dépend le plugin
	 * @return {@link IPluginDescriptor} : Le descripteur du plugin
	 */
	public abstract IPluginDescriptor getDescriptor(String name, String path,
			boolean isDefault, boolean isActive, boolean isSingleton,
			String className, ArrayList<String> interfaces,
			ArrayList<String> libraries, ArrayList<String> dependencies);

	/**
	 * Décharge un plugin. Si d'autre plugins dépendent de lui ils seront également déchargés.
	 * @param pluginName {@link String} : Le nom du plugin
	 * @return {@link Boolean boolean} : Vrai si le plugin a été correctement déchargé
	 */
	public abstract boolean unload(String pluginName);
	
	/**
	 * Retourne le chemin vers le dossier qui contient tous les plugins.
	 * @return {@link String} Le chemin vers les plugins
	 */
	public abstract String getPath();
	
	/**
	 * Écrit dans tous les {@link Core#logs loggers}.
	 * @param message {@link String} : Le message à écrire en tant que LOG
	 */
	public abstract void logWrite(String message);

	/**
	 * Imprime dans tous les {@link Core#logs loggers}.
	 * @param message {@link String} : Le message à imprimer dans les loggers
	 */
	public abstract void logPrint(String message);
	
	/**
	 * Écrit une erreur dans tous les {@link Core#logs loggers}.
	 * @param error {@link String} : L'erreur à écrire en tant que ERROR
	 */
	public abstract void logError(String error);
	
	/**
	 * Affiche tous les {@link Core#logs loggers} cachés.
	 */
	public abstract void logDisplay();
	

}