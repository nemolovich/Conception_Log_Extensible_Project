package ext.plugin.components;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public interface IItem
{
	public abstract String getItemName();
	public abstract void setItemName(String itemName);
	public abstract JButton getButton();
	public abstract void setButton(JButton button);
	public abstract boolean isDefaultItem();
	public abstract ImageIcon getIcon();
	public abstract void setIcon(ImageIcon icon);
}
