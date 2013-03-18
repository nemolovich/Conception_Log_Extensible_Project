package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.PluginManager;
import main.plugin.IPluginDescriptor;
import view.dialog.CreatePluginDialog;
import view.table.PluginTable;
import view.table.PluginTableModel;

public class PluginManagerView extends JFrame implements ActionListener, MouseListener
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = -8256310508582962335L;
	private int frameWidth=920;
	private int frameHeight=600;
	private JButton addPlugin=new JButton("Ajouter un plugin");
	private JButton addPluginFrom=new JButton("<html>Ajouter un plugin depuis le plugin:<br/>\"<i>null</i>\"</html>");
	private JButton displayFilePlugin=new JButton("<html><center>Afficher le fichier de configuration</center></html>");
	private JButton loadPlugin=new JButton("Charger le plugin");
	private PluginTable pluginList;
	private PluginManager pluginManager;
	
	public PluginManagerView(PluginManager pluginManager, ArrayList<IPluginDescriptor> descriptors)
	{
		this.pluginManager=pluginManager;
		ArrayList<IPluginDescriptor> plugins=descriptors!=null?descriptors:new ArrayList<IPluginDescriptor>();
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		this.frameWidth=width/2;
		this.frameHeight=height/2;
		this.setSize(this.frameWidth, this.frameHeight);
		this.setMinimumSize(new Dimension(650,200));
		this.setTitle("Plugin Manager");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		final PluginManagerView pm = this;
		final String pn = this.pluginManager.getName();
		this.addWindowListener(new WindowAdapter() 
			{
				public void windowClosing(WindowEvent e) 
				{
					System.out.println("Close "+pn+" view");
					pm.dispose();
				}
			});
//		this.setResizable(false);
//		this.setIconImage(new ImageIcon("img/icon.png").getImage());
		
		JPanel pluginsPanel=new JPanel();
		pluginsPanel.setLayout(new BorderLayout());
		pluginsPanel.setBorder(BorderFactory.createTitledBorder("Liste des plugins"));

		this.pluginList=new PluginTable(new PluginTableModel());
		this.pluginList.setSurrendersFocusOnKeystroke(false);
		this.pluginList.addMouseListener(this);
		JScrollPane scroll=new JScrollPane(this.pluginList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pluginsPanel.add(scroll);
		
		JPanel buttonsPanel=new JPanel();
		Font newButtonFont=new Font(this.addPlugin.getFont().getName(),
				this.addPlugin.getFont().getStyle(),12); 
		buttonsPanel.setPreferredSize(new Dimension(170,0));
		buttonsPanel.add(this.addPlugin);
		this.addPlugin.setPreferredSize(new Dimension(160,25));
		this.addPlugin.setFont(newButtonFont);
		buttonsPanel.add(this.addPluginFrom);
		this.addPluginFrom.setPreferredSize(new Dimension(160,55));
		this.addPluginFrom.setFont(newButtonFont);
		buttonsPanel.add(this.displayFilePlugin);
		this.displayFilePlugin.setPreferredSize(new Dimension(160,45));
		this.displayFilePlugin.setFont(newButtonFont);
		buttonsPanel.add(this.loadPlugin);
		this.loadPlugin.setPreferredSize(new Dimension(160,25));
		this.loadPlugin.setFont(newButtonFont);

		this.addPlugin.addActionListener(this);
		this.addPluginFrom.addActionListener(this);
		this.displayFilePlugin.addActionListener(this);
		this.loadPlugin.addActionListener(this);

		this.displayFilePlugin.setEnabled(false);
		this.addPluginFrom.setEnabled(false);
		this.loadPlugin.setEnabled(false);
		
		addPlugins(plugins);

		this.getContentPane().add(pluginsPanel,BorderLayout.CENTER);
		this.getContentPane().add(buttonsPanel,BorderLayout.EAST);
		this.setVisible(true);
	}
	
	private void addPlugins(ArrayList<IPluginDescriptor> plugins)
	{
        if(plugins!=null&&plugins.size()>0)
        {
	        for(IPluginDescriptor plugin:plugins)
	        {
	        	this.pluginList.getTableModel().addPlugin(plugin);
	        }
	        this.pluginList.setRowSelectionInterval(0, 0);
			this.addPluginFrom.setEnabled(true);
			String pluginName=(String) this.pluginList.getTableModel().getPlugin(
									this.pluginList.getSelectedRow()).getName();
			if(pluginName!=null)
			{
				this.addPluginFrom.setText("<html>Ajouter un plugin depuis le plugin:<br/>\"<i>"+pluginName+
						"</i>\"</html>");
				this.addPluginFrom.setEnabled(true);
				if(!this.pluginList.getTableModel().getPlugin(
									this.pluginList.getSelectedRow()).isLoaded())
				{
					this.loadPlugin.setEnabled(true);
				}
				else
				{
					this.loadPlugin.setEnabled(false);					
				}
			}
			else
			{
				this.addPluginFrom.setEnabled(false);
			}
			this.displayFilePlugin.setEnabled(true);
        }
	}
	
	public void addPlugin(IPluginDescriptor plugin)
	{
		this.pluginList.getTableModel().addPlugin(plugin);
	}

	public String getWorkspace()
	{
		return this.pluginManager.getWorkspace();
	}

	public PluginManager getPluginManager()
	{
		return this.pluginManager;
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		IPluginDescriptor parent=null;
		if(this.pluginList.getSelectedRow()>-1)
		{
			parent=this.pluginList.getTableModel().
					getPlugin(this.pluginList.getSelectedRow());
		}
		if(event.getSource()==this.addPlugin)
		{
			CreatePluginDialog createPlugin=new CreatePluginDialog(this,null);
			if(createPlugin.isValidated())
			{
				String pluginName=createPlugin.getPluginName();
				boolean isActive=createPlugin.isActive();
				boolean isLazy=createPlugin.isLazy();
				boolean isDefault=createPlugin.isDefault();
				boolean isErased=createPlugin.isErased();
	        	this.pluginManager.addPlugin(pluginName, isDefault, isActive, isLazy, null, isErased);
			}
		}
		else if(event.getSource()==this.addPluginFrom)
		{
			if(parent==null)
			{
				return;
			}
			CreatePluginDialog createPlugin=new CreatePluginDialog(this,parent);
			if(createPlugin.isValidated())
			{
				String pluginName=createPlugin.getPluginName();
				boolean isActive=createPlugin.isActive();
				boolean isLazy=createPlugin.isLazy();
				boolean isDefault=createPlugin.isDefault();
				boolean isErased=createPlugin.isErased();
	        	this.pluginManager.addPlugin(pluginName, isDefault, isActive, isLazy,
	        			new ArrayList<IPluginDescriptor>(Arrays.asList(parent)), isErased);
			}
		}
		else if(event.getSource()==this.loadPlugin)
		{
			if(parent==null)
			{
				return;
			}
			int row=this.pluginList.getSelectedRow();
			if(this.pluginManager.loadPlugin(parent, parent.isActive()))
			{
				this.pluginList.setValueAt(true, row, 5);
			}
			this.pluginList.setRowSelectionInterval(row, row);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		if(event.getSource()==this.pluginList)
		{
			IPluginDescriptor parent=null;
			int row=this.pluginList.getSelectedRow();
			if(row>-1)
			{
				parent=this.pluginList.getTableModel().getPlugin(row);
			}
			else
			{
				return;
			}
			String pluginName=(String) parent.getName();
			if(pluginName!=null)
			{
				this.addPluginFrom.setText("<html>Ajouter un plugin depuis le plugin:<br/>\"<i>"+pluginName+"</i>\"</html>");
				this.addPluginFrom.setEnabled(true);
				if(!this.pluginList.getTableModel().getPlugin(row).isLoaded())
				{
					this.loadPlugin.setEnabled(true);
				}
				else
				{
					this.loadPlugin.setEnabled(false);					
				}
			}
			else
			{
				this.addPluginFrom.setEnabled(false);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
	}

}