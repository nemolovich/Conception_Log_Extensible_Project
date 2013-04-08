package main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class SplashScreen extends JFrame
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = 1837517758659548861L;
	private ImageIcon imageIcon;
	private JPanel mainPanel = new JPanel()
	{
		/**
		 * ID
		 */
		private static final long serialVersionUID = 1193749300327137199L;

		@Override
	    protected void paintComponent(Graphics g)
		{
	        g.drawImage(imageIcon.getImage(), 0, 0, null);
	    }
	};
	private JProgressBar progressBar = new JProgressBar();
	private int nbElement=0;
	private JLabel label=new JLabel("Loading plateform");
	private String stateMessage="Loading plateform";

	public SplashScreen(ImageIcon imageIcon)
	{
		this.imageIcon = imageIcon;
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.setUndecorated(true);
		try
		{
			this.screenInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void screenInit() throws Exception
	{
		this.getContentPane().setLayout(new BorderLayout());
		JPanel northPanel=new JPanel();
		northPanel.setBackground(Color.decode("#000000"));
		northPanel.setOpaque(false);
		northPanel.setPreferredSize(new Dimension(this.imageIcon.getIconWidth(),
				this.imageIcon.getIconHeight()-55));
		this.mainPanel.setLayout(new FlowLayout());
		this.mainPanel.setPreferredSize(new Dimension(this.imageIcon.getIconWidth(),
				this.imageIcon.getIconHeight()));
		this.mainPanel.add(northPanel, BorderLayout.SOUTH);
		this.label.setForeground(Color.decode("#C0C0C0"));
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		this.mainPanel.add(this.progressBar, null);
		this.mainPanel.add(this.label, null);
		this.mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,
				Color.decode("#220022"),Color.decode("#442244")));
		this.progressBar.setPreferredSize(new Dimension(this.imageIcon.getIconWidth()-30,20));
//		this.progressBar.setOpaque(false);
		this.getContentPane().add(this.mainPanel, BorderLayout.NORTH);
		this.pack();
	}

	public void setProgressMax(int maxProgress)
	{
		progressBar.setMaximum(maxProgress);
	}

	public void setProgress(int progress)
	{
		final int newProgress = (progress*100/(nbElement==0?1:nbElement));;
		final int oldProgress = this.progressBar.getValue();
		if(newProgress<oldProgress)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					progressBar.setValue(newProgress);
					setMessage(stateMessage+" - "+newProgress+"%");
					return;
				}
			});
		}
		else
		{
			for(int i=oldProgress;i<=newProgress;i++)
			{
				final int value=i;
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						progressBar.setValue(value);
						setMessage(stateMessage+" - "+value+"%");
					}
				});
				try
				{
					Thread.sleep(35);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void setLabel(String label)
	{
		this.label.setText(label);
		this.pack();
	}

	public void setProgress(String message, int progress)
	{
		final int theProgress = (progress*100/(this.nbElement==0?1:this.nbElement));
		final String theMessage = message+" - "+theProgress+"%";
		setProgress(progress);
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				progressBar.setValue(theProgress);
				setMessage(theMessage);
			}
		});
	}

	public void setScreenVisible(boolean b)
	{
		final boolean boo = b;
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				setVisible(boo);
			}
		});
	}

	private void setMessage(String message)
	{
		if (message==null)
		{
			message = "";
			progressBar.setStringPainted(false);
		}
		else
		{
			progressBar.setStringPainted(true);
		}
		progressBar.setString(message);
	}

	public void setNbElement(int nbElement)
	{
		this.nbElement=nbElement;
	}

	public int getNbElement()
	{
		return this.nbElement;
	}

	/**
	 * @return the stateMessage
	 */
	public String getStateMessage()
	{
		return stateMessage;
	}

	/**
	 * @param stateMessage the stateMessage to set
	 */
	public void setStateMessage(String stateMessage)
	{
		this.stateMessage = stateMessage;
	}

}