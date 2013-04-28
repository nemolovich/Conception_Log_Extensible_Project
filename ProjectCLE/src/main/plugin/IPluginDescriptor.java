package main.plugin;

import java.util.ArrayList;

/**
 * Cette interface correspond à un descripteur de plugin. On y regroupe
 * toutes les informations sur ce dernier afin d'avoir une trace dans
 * le code.
 * @author Brian GOHIER
 */
public interface IPluginDescriptor
{

	/**
	 * Retourne le nom de la classe du plugin.
	 * @return {@link String} : La class name
	 */
	public abstract String getClassName();

	/**
	 * Retourne la liste de noms d'interfaces qu'implémente le plugin.
	 * @return {@link ArrayList}<{@link String}>, La interfaces liste de noms 
	 */
	public abstract ArrayList<String> getInterfaces();

	/**
	 * Définit la liste de noms des interfaces qu'implémente le plugin.
	 * @param interfaces {@link ArrayList}<{@link String}>, La liste des noms d'interfaces
	 */
	public abstract void setInterfaces(ArrayList<String> interfaces);

	/**
	 * Retourne la liste des noms de librairies que le plugin implémente.
	 * @return {@link ArrayList}<{@link String}>, La liste des noms de librairies que le plugin implémente
	 */
	public abstract ArrayList<String> getLibraries();

	/**
	 * Définit la liste des noms de librairies que le plugin implémente.
	 * @param libraries {@link ArrayList}<{@link String}>, La liste de noms de libraries 
	 */
	public abstract void setLibraries(ArrayList<String> libraries);

	/**
	 * Renvoie vrai si le plugin est chargé par default.
	 * @return {@link Boolean boolean} : Vrai si le plugin est chargé par default
	 */
	public abstract boolean isDefault();

	/**
	 * Renvoie vrai si le plugin est un chargement actif.
	 * @return {@link Boolean boolean} : Vrai si le plugin est un chargement actif
	 */
	public abstract boolean isActive();

	/**
	 * Renvoie vrai si le plugin ne peut avoir qu'une instance.
	 * @return {@link Boolean boolean} : Vrai si le plugin est un singleton
	 */
	public abstract boolean isSingleton();

	/**
	 * Retourne Le nom du plugin.
	 * @return {@link String} : Le nom du plugin
	 */
	public abstract String getName();

	/**
	 * Retourne Le chemin du plugin.
	 * @return {@link String} : Le chemin du plugin
	 */
	public abstract String getPath();

	/**
	 * Définit le nom du plugin.
	 * @param name {@link String} : Le nom du plugin
	 */
	public abstract void setName(String name);

	/**
	 * Définit le nom de la classe du plugin.
	 * @param className {@link String} : Le nom de la classe du plugin
	 */
	public abstract void setClassName(String className);

	/**
	 * Définit le chemin du plugin.
	 * @param path {@link String} : Le chemin du plugin
	 */
	public abstract void setPath(String path);

	/**
	 * Définit si le plugin est chargé par default.
	 * @param isDefault {@link Boolean boolean} : Vrai si le plugin est chargé par default
	 */
	public abstract void setDefault(boolean isDefault);

	/**
	 * Définit si le plugin est un chargement actif.
	 * @param isActive {@link Boolean boolean} : Vrai si le plugin est un chargement actif
	 */
	public abstract void setActive(boolean isActive);

	/**
	 * Définit si le plugin ne peut être chargé qu'une fois.
	 * @param isSingleton {@link Boolean boolean} : Vrai si le plugin est un singleton
	 */
	public abstract void setSingleton(boolean isSingleton);

	/**
	 * Définit si le plugin est chargé par la plateforme.
	 * @param loaded {@link Boolean boolean} : Vrai si le plugin est chargé par la plateforme
	 */
	public abstract void setLoaded(boolean loaded);

	/**
	 * Renvoie vrai si le plugin est chargé par la plateforme.
	 * @return {@link Boolean boolean} : Vrai si le plugin est chargé par la plateforme
	 */
	public abstract boolean isLoaded();

	/**
	 * Définit l'instance du plugin.
	 * @param o {@link Object} : L'instance de plugin
	 */
	public abstract void setPluginInstance(Object o);

	/**
	 * Retourne l'instance de plugin.
	 * @return {@link Object} : L'instance de plugin
	 */
	public abstract Object getPluginInstance();

	/**
	 * Retourne la liste des noms de class dont auraient besoin des plugins
	 * qui dépendraient de celui-ci.
	 * @return {@link ArrayList}<{@link String}>, La liste des noms de classes
	 */
	public abstract ArrayList<String> getDependencies();

	/**
	 * Définit la liste des noms de class dont auraient besoin des plugins
	 * qui dépendraient de celui-ci.
	 * @param dependencies {@link ArrayList}<{@link String}>, La liste des noms de classes
	 */
	public abstract void setDependencies(ArrayList<String> dependencies);

	/**
	 * Méthode pour avoir le format {@link String texte} depuis un {@link IPluginDescriptor}.
	 * @return {@link String} : Le format texte
	 */
	public abstract String toString();

	/**
	 * Définit si les {@link IPluginDescriptor plugins} sont égaux.
	 * @param desc {@link IPluginDescriptor} : Le plugin à comparer
	 * @return {@link Boolean boolean} : Vrai si les plugins sont égaux
	 */
	boolean equals(IPluginDescriptor desc);

}