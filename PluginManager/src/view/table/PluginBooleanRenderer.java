package view.table;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PluginBooleanRenderer extends DefaultTableCellRenderer 
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = -3537438891642386840L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
    		boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		JCheckBox check=(JCheckBox)value;
		check.setHorizontalAlignment(JCheckBox.CENTER);
		if(isSelected)
		{
			check.setBackground(table.getSelectionBackground());
			check.setForeground(table.getForeground());
		}
		else
		{
			check.setBackground(table.getBackground());
			check.setForeground(table.getForeground());
		}
		
		return check;
	}
}
