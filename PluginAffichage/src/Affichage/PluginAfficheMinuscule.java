package Affichage;

import interfaces.IPlugin_Affichage;

public class PluginAfficheMinuscule implements IPlugin_Affichage{
	
	public void affiche(String s) {
		System.out.println(s.toLowerCase());
	}

}
