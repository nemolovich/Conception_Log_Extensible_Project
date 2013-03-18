package ext.plugin.view.panel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionPanel extends JPanel
{
	/**
	 * ID
	 */
	//ImageIcon iconePoint= new ImageIcon("../images/point.jpeg");
	JLabel iconePoint= new JLabel("Icone point ");
	List<JLabel> _menu= new ArrayList<JLabel>();
	
	
	
	private static final long serialVersionUID = 2019551761497423458L;

	public OptionPanel()
	{
		super();
		this.setBackground(Color.decode("#F0F0F0"));
		addItemMenu(iconePoint);
		displayMenu();
	}
	
	/*
	 * set _menu with the given value
	 */
	public void setMenu(ArrayList<JLabel> men){	
		this._menu=men;
	}
	
	/*
	 * add an item (shape) to _menu 
	 */
	public void addItemMenu(JLabel figure){
		this._menu.add(figure);
	}
	
	/*
	 * display all the shapes available
	 */
	public void displayMenu(){
		
		for(int i=0;i<_menu.size();i++){
			this.add(_menu.get(i));
		}
	}
}
