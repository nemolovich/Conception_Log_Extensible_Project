package view.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class PluginBooleanEditor extends AbstractCellEditor implements TableCellEditor, ActionListener
{
    /**
	 * ID
	 */
	private static final long serialVersionUID = 3190517555512091757L;
	private boolean checked;
    private JButton bouton;
 
    public PluginBooleanEditor()
    {
        super();
        bouton = new JButton();
        bouton.addActionListener(this);
        bouton.setBorderPainted(false);
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        checked ^= true;
 
        fireEditingStopped();
    }
 
    @Override
    public Object getCellEditorValue()
    {
        return this.checked;
    }
 
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
    		boolean isSelected, int row, int column)
    {
        this.checked = ((JCheckBox)value).isSelected();
 
        return this.bouton;
    }
}
