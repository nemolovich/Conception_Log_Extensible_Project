package view.table;

import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.table.AbstractTableModel;

import main.plugin.IPluginDescriptor;

public class PluginTableModel extends AbstractTableModel
{
    /**
	 * ID
	 */
	private static final long serialVersionUID = -2058107310824938138L;
 
    private final String[] header = {"Nom", "Défaut", "Lazy", "Actif", "Chemin", "Chargé"};
    
    private final ArrayList<IPluginDescriptor> datas = new ArrayList<IPluginDescriptor>();
 
    public PluginTableModel()
    {
        super();
    }

    @Override
    public int getRowCount()
    {
        return this.datas.size();
    }
 
    @Override
    public int getColumnCount()
    {
        return header.length;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        return this.header[columnIndex];
    }
	
	public void addPlugin(IPluginDescriptor plugin)
	{
	    this.datas.add(this.datas.size(),plugin);
	
	    fireTableRowsInserted(this.datas.size()-1, this.datas.size()-1);
	}
	
	public void removePlugin(int rowIndex)
	{
	    this.datas.remove(rowIndex);
	
	    fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
	    if(this.getValueAt(rowIndex, columnIndex) instanceof JCheckBox
	    		&&columnIndex!=this.header.length-1)
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}
	
	public IPluginDescriptor getPlugin(int row)
	{
		return this.datas.get(row);
	}

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch(columnIndex)
        {
	        case 0:
	            return this.datas.get(rowIndex).getName();
	        case 1:
	            return this.getCheckbox(this.datas.get(rowIndex).isDefault());
	        case 2:
	            return this.getCheckbox(this.datas.get(rowIndex).isLazy());
	        case 3:
	            return this.getCheckbox(this.datas.get(rowIndex).isActive());
	        case 4:
	            return this.datas.get(rowIndex).getPath();
	        case 5:
	            return this.getCheckbox(this.datas.get(rowIndex).isLoaded());
	        default:
	            return null;
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        switch(columnIndex)
        {
	        case 0:
	            ((IPluginDescriptor)this.datas.get(rowIndex)).setName((String)value);
	            break;
	        case 1:
	        	((IPluginDescriptor)this.datas.get(rowIndex)).setDefault((Boolean)value);
	            break;
	        case 2:
	        	((IPluginDescriptor)this.datas.get(rowIndex)).setLazy((Boolean)value);
	            break;
	        case 3:
	        	((IPluginDescriptor)this.datas.get(rowIndex)).setActive((Boolean)value);
	            break;
	        case 4:
	            ((IPluginDescriptor)this.datas.get(rowIndex)).setPath((String)value);
	            break;
	        case 5:
	            ((IPluginDescriptor)this.datas.get(rowIndex)).setLoaded((Boolean)value);
	            break;
        }
//        this.fireTableDataChanged();
        this.fireTableRowsUpdated(rowIndex, rowIndex);
    }

    /**
     * Update a plugin from a {@link IPluginDescriptor plugin descriptor}
     * @param plugin : {@link IPluginDescriptor}, The plugin descriptor
     * @param rowIndex : {@link Integer int}, The row index
     */
	public void updatePlugin(IPluginDescriptor plugin, int rowIndex)
	{
		((IPluginDescriptor)this.datas.get(rowIndex)).setName(plugin.getName());
		((IPluginDescriptor)this.datas.get(rowIndex)).setDefault(plugin.isDefault());
		((IPluginDescriptor)this.datas.get(rowIndex)).setLazy(plugin.isLazy());
		((IPluginDescriptor)this.datas.get(rowIndex)).setActive(plugin.isActive());
		((IPluginDescriptor)this.datas.get(rowIndex)).setLoaded(plugin.isLoaded());
        this.fireTableRowsUpdated(rowIndex, rowIndex);
	}
    
    private JCheckBox getCheckbox(boolean selected)
    {
    	JCheckBox check=new JCheckBox();
    	check.setSelected(selected);
    	return check;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Class getColumnClass(int columnIndex)
    {
    	switch(columnIndex)
    	{
			case 1:
				return JCheckBox.class;
			case 2:
				return JCheckBox.class;
    		case 3:
    			return JCheckBox.class;
    		case 5:
    			return JCheckBox.class;
    		default:
    			return Object.class;
    	}
    }

	public int getIndexOf(IPluginDescriptor existPlugin)
	{
		for(int i=0;i<this.getRowCount();i++)
		{
			if(this.datas.get(i).getName().equalsIgnoreCase(existPlugin.getName()))
			{
				return i;
			}
		}
		return -1;
	}

}
