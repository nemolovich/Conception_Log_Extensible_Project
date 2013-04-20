package view.table;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class PluginTable extends JTable
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = 7400055648553993614L;
	private PluginTableModel tableModel;
	
	public PluginTable(PluginTableModel model)
	{
		super(model);
		this.tableModel=model;
		
		/* Affichage et edition des booleens */
		this.setDefaultRenderer(JCheckBox.class, new PluginBooleanRenderer());
		this.setDefaultEditor(JCheckBox.class, new PluginBooleanEditor());
		
		/* Tri des lignes */
		this.getTableHeader().setReorderingAllowed(true);
		this.setAutoCreateRowSorter(true);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.getModel());   
		this.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		sorter.setComparator(1, new JCheckBoxComparator());
		sorter.setComparator(2, new JCheckBoxComparator());
		sorter.setComparator(3, new JCheckBoxComparator());
		sorter.setSortable(4, false);
		sorter.setComparator(5, new JCheckBoxComparator());
		sorter.toggleSortOrder(0);
		sorter.sort();
		
		/* Taille du header */
		this.getTableHeader().setResizingAllowed(false);
		this.getTableHeader().setReorderingAllowed(false);
		this.setColonneSize(0, 150);
		this.setColonneSize(1, 65);
		this.setColonneSize(2, 80);
		this.setColonneSize(3, 55);
		this.getColumnModel().getColumn(4).setMinWidth(55);
		this.setColonneSize(5, 70);
		
		// Selection de plugin
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private void setColonneSize(int column, int size)
	{
		this.getColumnModel().getColumn(column).setPreferredWidth(size);
		this.getColumnModel().getColumn(column).setMaxWidth(size);
		this.getColumnModel().getColumn(column).setMinWidth(size);
		this.getColumnModel().getColumn(column).setMinWidth(size);
	}

	/**
	 * @return the model
	 */
	public PluginTableModel getTableModel()
	{
		return tableModel;
	}

	/**
	 * @param model the model to set
	 */
	public void setTableModel(PluginTableModel tableModel)
	{
		this.tableModel = tableModel;
	}
}
