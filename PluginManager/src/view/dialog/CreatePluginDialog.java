package view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.plugin.IPluginDescriptor;
import view.PluginManagerView;

public class CreatePluginDialog extends JDialog implements ActionListener
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = -80941485217196005L;
	private JTextField pluginName=new JTextField();
	private JCheckBox isActive=new JCheckBox("Actif");
	private JCheckBox isLazy=new JCheckBox("Lazy");
	private JCheckBox isDefault=new JCheckBox("Par défaut");
	private JButton validateButton=new JButton("Créer le plugin");
	private JButton cancelButton=new JButton("Annuler");
	private boolean erase=false;
	private boolean validated=false;
	private PluginManagerView parent=null;

	public CreatePluginDialog(PluginManagerView parent, IPluginDescriptor pluginParent)
	{
		super(parent,"Créer un plugin",true);
		
		this.parent=parent;
		if(pluginParent!=null)
		{
			this.setTitle(this.getTitle()+" depuis \""+pluginParent.getName()+"\"");
		}
		
		this.setSize(350, 190);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(CreatePluginDialog.DISPOSE_ON_CLOSE);
		
		JPanel namePanel=new JPanel();
		namePanel.setLayout(new GridLayout());
		namePanel.setBorder(BorderFactory.createTitledBorder("Nom du plugin"));
		namePanel.add(this.pluginName);
		
		JPanel loadingPanel=new JPanel();
		loadingPanel.setLayout(new GridLayout(3, 1));
		loadingPanel.setBorder(BorderFactory.createTitledBorder("Type de chargement"));
		loadingPanel.add(this.isActive);
		loadingPanel.add(this.isLazy);
		loadingPanel.add(this.isDefault);

		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setAlignmentX(RIGHT_ALIGNMENT);
		buttonsPanel.add(this.cancelButton);
		buttonsPanel.add(this.validateButton);

		this.cancelButton.addActionListener(this);
		this.validateButton.addActionListener(this);

		this.getContentPane().add(namePanel,BorderLayout.NORTH);
		this.getContentPane().add(loadingPanel,BorderLayout.CENTER);
		this.getContentPane().add(buttonsPanel,BorderLayout.SOUTH);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource().equals(this.validateButton))
		{
			if(this.getVerifName()!=null)
			{
				this.validated=true;
				JOptionPane.showMessageDialog(this, "<html><b> Le plugin \""+this.getPluginName()+
				"\" a bien été créé.</b><br/>Pour l'ajouter en tant que projet à eclipse:<br/>" +
				"<ul><li><i>File</i> -> <i>Import...</i> -> <i>Existing Project into Workspace</i></li>" +
				"<li><i>Next ></i></li>" +
				"<li><i>Browse...</i></li>" +
				"<li>Selectionnez le dossier dans le workspace et validez</li>" +
				"</ul></html>", "Plugin \""+this.getPluginName()+"\" créé",
				JOptionPane.INFORMATION_MESSAGE);
				this.dispose();
			}
			else
			{
				this.validated=false;
			}
		}
		else if(event.getSource().equals(this.cancelButton))
		{
			this.validated=false;
			this.dispose();
		}
	}
	
	public String getPluginName()
	{
		return this.pluginName.getText();
	}
	
	public boolean isActive()
	{
		return this.isActive.isSelected();
	}
	
	public boolean isLazy()
	{
		return this.isLazy.isSelected();
	}
	
	public boolean isDefault()
	{
		return this.isDefault.isSelected();
	}
	
	public boolean isErased()
	{
		return this.erase;
	}
	
	public boolean isValidated()
	{
		return this.validated;
	}
	
	public String getVerifName()
	{
		String pluginName=this.pluginName.getText();
		if(pluginName.length()<7)
		{
			JOptionPane.showMessageDialog(this, "Le nom du plugin doit contenir au moins 7 caractères",
					"Nom du plugin incorrecte", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if(pluginName.length()>28)
		{
			JOptionPane.showMessageDialog(this, "Le nom du plugin ne doit pas excéder 28 caractères",
					"Nom du plugin incorrecte", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if(this.parent.getPluginManager().isExistingProject(pluginName))
		{
			int res=JOptionPane.showConfirmDialog(this, "Le plugin semble exister, voulez-vous l'écraser?",
					"Nom de plugin existant", JOptionPane.YES_OPTION,JOptionPane.WARNING_MESSAGE);
			if(res==1)
			{
				this.erase=false;
				return null;
			}
			else
			{
				this.erase=true;
			}
		}
		return pluginName;
	}
}
