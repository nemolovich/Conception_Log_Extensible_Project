package Affichage;

import interfaces.IPlugin_Affichage;

public class PluginAfficheMajuscule implements IPlugin_Affichage {

	public void affiche(String s) {
		System.out.println(s.toUpperCase());
	}

}
