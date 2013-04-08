import interfaces.IPlugin_Affichage;
import Affichage.PluginAfficheMajuscule;
import Affichage.PluginAfficheMinuscule;
import main.plugin.*;

public class PluginAffichageMain implements IPlugin
{
	
	private String name="PluginAffichage";

	public PluginAffichageMain()
	{
		IPlugin_Affichage ia;
		IPlugin_Affichage ia2;
		
		ia= new PluginAfficheMajuscule();
		ia2=new PluginAfficheMinuscule();
		ia.affiche("Bonjour les terriens ");
		ia2.affiche("Bonjour les terriens ");
	}
	
	public static void main(String[] args)
	{
		System.out.println("PluginAffichage");
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void setName(String name)
	{
		this.name=name;
	}

}
