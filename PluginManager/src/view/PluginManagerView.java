package view;

import java.awt.BorderLayout;
import java.awt.Desktop;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	private JButton addPluginFrom=new JButton("<html><center>Ajouter un plugin depuis le plugin:" +
			"<br/>\"<i>null</i>\"</center</html>");
	private JButton displayFilePlugin=new JButton("<html><center>Afficher le fichier de configuration" +
			"</center></html>");
	private JButton loadPlugin=new JButton("Charger le plugin");
	private JButton refreshPlugin=new JButton("Actualiser config");
	private JButton importPlugin=new JButton("Importer un plugin");
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
		buttonsPanel.setPreferredSize(new Dimension(185,0));
		buttonsPanel.add(this.addPlugin);
		this.addPlugin.setPreferredSize(new Dimension(175,25));
		this.addPlugin.setFont(newButtonFont);
		buttonsPanel.add(this.addPluginFrom);
		this.addPluginFrom.setPreferredSize(new Dimension(175,55));
		this.addPluginFrom.setFont(newButtonFont);
		buttonsPanel.add(this.importPlugin);
		this.importPlugin.setPreferredSize(new Dimension(175,25));
		this.importPlugin.setFont(newButtonFont);
		buttonsPanel.add(this.displayFilePlugin);
		this.displayFilePlugin.setPreferredSize(new Dimension(175,40));
		this.displayFilePlugin.setFont(newButtonFont);
		buttonsPanel.add(this.refreshPlugin);
		this.refreshPlugin.setPreferredSize(new Dimension(175,25));
		this.refreshPlugin.setFont(newButtonFont);
		buttonsPanel.add(this.loadPlugin);
		this.loadPlugin.setPreferredSize(new Dimension(175,25));
		this.loadPlugin.setFont(newButtonFont);

		this.addPlugin.addActionListener(this);
		this.addPluginFrom.addActionListener(this);
		this.displayFilePlugin.addActionListener(this);
		this.refreshPlugin.addActionListener(this);
		this.loadPlugin.addActionListener(this);
		this.importPlugin.addActionListener(this);

		this.displayFilePlugin.setEnabled(false);
		this.addPluginFrom.setEnabled(false);
		this.refreshPlugin.setEnabled(false);
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
			int row=this.pluginList.convertRowIndexToModel(this.pluginList.getSelectedRow());
			String pluginName=(String) this.pluginList.getTableModel().getPlugin(row).getName();
			if(this.pluginList.getSelectedRow()>-1)
			{
				this.addPluginFrom.setText("<html><center>Ajouter un plugin depuis le plugin:<br/>\"<i>"+pluginName+
						"</i>\"</center</html>");
				this.addPluginFrom.setEnabled(true);
				this.refreshPlugin.setEnabled(true);
				this.displayFilePlugin.setEnabled(true);
				this.loadPlugin.setEnabled(true);
				if(!this.pluginList.getTableModel().getPlugin(row).isLoaded())
				{
					this.loadPlugin.setText("Charger le plugin");
				}
				else
				{
					this.loadPlugin.setText("Recharger le plugin");			
				}
			}
			else
			{
				this.addPluginFrom.setEnabled(false);
				this.refreshPlugin.setEnabled(false);
				this.displayFilePlugin.setEnabled(false);
			}
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
		int row=-1;
		if(this.pluginList.getSelectedRow()>-1)
		{
			row=this.pluginList.convertRowIndexToModel(this.pluginList.getSelectedRow());
			parent=this.pluginList.getTableModel().getPlugin(row);
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
		else if(event.getSource()==this.importPlugin)
		{
			final File workspace=new File(this.pluginManager.getWorkspace());
			JFileChooser chooser=new JFileChooser(workspace);
			chooser.setDialogTitle("Sélectionnez un Projet");
			chooser.setMultiSelectionEnabled(false);
			chooser.setDragEnabled(false);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);
		    if(chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
		    {
		    	File choice=chooser.getSelectedFile();
		    	if(!choice.isDirectory())
		    	{
		    		choice=chooser.getCurrentDirectory();
		    	}
		    	String projectName=choice.getName();
		    	if(!this.pluginManager.importProject(choice))
		    	{
					JOptionPane.showMessageDialog(this, "Le projet \""+projectName+"\" n'a pas pu être chargé",
								"Projet invalide", JOptionPane.ERROR_MESSAGE);
		    	}
		    }
		}
		else if(event.getSource()==this.loadPlugin)
		{
			if(parent==null)
			{
				return;
			}
			if(this.pluginManager.loadPlugin(parent, parent.isActive())!=null)
			{
				this.pluginList.setValueAt(true, this.pluginList.getSelectedRow(), 5);
				this.pluginManager.getPluginsDescriptors().get(
						this.pluginManager.getPluginsDescriptors().indexOf(parent)).setLoaded(true);
			}
			this.pluginList.setRowSelectionInterval(this.pluginList.getSelectedRow(),
							this.pluginList.getSelectedRow());
			this.loadPlugin.setText("Recharger le plugin");	
		}
		else if(event.getSource()==this.displayFilePlugin)
		{
			if(parent!=null)
			{
				try
				{
					File configFile=new File(this.pluginManager.getCorePath()+
						parent.getPath()+File.separator+"config.ini");
					Desktop.getDesktop().open(configFile);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else if(event.getSource()==this.refreshPlugin)
		{
			if(parent!=null)
			{
				String pluginName=parent.getName();
				IPluginDescriptor tmp=this.pluginManager.getPluginConfig(pluginName,parent.getPath());
				if(tmp!=null)
				{
					tmp.setPluginInstance(parent.getPluginInstance());
					tmp.setLoaded(parent.isLoaded());
					parent=tmp;
					this.pluginList.getTableModel().updatePlugin(parent, row);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Le fichier de configuration pour \""+
								pluginName+"\" semble invalide",
								"Fichier de configuration invalide", JOptionPane.ERROR_MESSAGE);
				}
			}
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
			int row=this.pluginList.convertRowIndexToModel(this.pluginList.getSelectedRow());
			IPluginDescriptor parent=null;
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
				this.addPluginFrom.setText("<html><center>Ajouter un plugin depuis le plugin:" +
						"<br/>\"<i>"+pluginName+"</i>\"</center></html>");
				this.addPluginFrom.setEnabled(true);
				this.refreshPlugin.setEnabled(true);
				this.displayFilePlugin.setEnabled(true);
				if(!this.pluginList.getTableModel().getPlugin(row).isLoaded())
				{
					this.loadPlugin.setText("Charger le plugin");
				}
				else
				{
					this.loadPlugin.setText("Recharger le plugin");				
				}
			}
			else
			{
				this.addPluginFrom.setEnabled(false);
				this.refreshPlugin.setEnabled(false);
				this.displayFilePlugin.setEnabled(false);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		if(event.getSource()==this.pluginList)
		{
			IPluginDescriptor parent=null;
			int row=this.pluginList.convertRowIndexToModel(this.pluginList.getSelectedRow());
			if(this.pluginList.getSelectedRow()>-1)
			{
				parent=this.pluginList.getTableModel().getPlugin(row);
				this.pluginManager.setPluginConfig(parent);
			}
			else
			{
				return;
			}
		}
	}

}
