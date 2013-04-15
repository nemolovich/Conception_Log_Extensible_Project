package ext.plugin.view.panel.size;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import ext.plugin.view.panel.OptionPanel;

public class SizePanel extends JPanel implements KeyListener, MouseMotionListener, ActionListener
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = 624725829733208466L;
	private JTextField sizeInput=new JTextField();
	private OptionPanel optionPanel;
	public static final int MAX_SIZE=100;
	private JSlider slider=new JSlider(JSlider.HORIZONTAL, 1, SizePanel.MAX_SIZE, 2);
	private boolean specialKeyLock;
	private JButton plusButton=new JButton();
	private JButton minusButton=new JButton();

	public SizePanel(OptionPanel optionPanel, int size)
	{
		super();
		this.setLayout(new BorderLayout());
		this.optionPanel=optionPanel;
		
		JLabel label=new JLabel("Taille: ");
		label.setFont(new Font(label.getFont().getName(),0,12));
		label.setPreferredSize(new Dimension(45,15));
		
		this.slider.setValue(optionPanel.getCurrentSize());
		
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		this.sizeInput.setText(String.valueOf(size));
		panel.add(this.sizeInput,BorderLayout.EAST);
		JPanel plusOrMinus=new JPanel();
		plusOrMinus.setLayout(new GridLayout(2,1));
		plusOrMinus.setPreferredSize(new Dimension(12,24));
		
		this.plusButton.setPreferredSize(new Dimension(12,12));
		this.plusButton.setToolTipText("Augmenter la taille");
		this.plusButton.setIcon(new ImageIcon("ressources/img/plus.png"));
		
		this.minusButton.setPreferredSize(new Dimension(12,12));
		this.minusButton.setToolTipText("Diminuer la taille");
		this.minusButton.setIcon(new ImageIcon("ressources/img/minus.png"));
		
		Font littleFont=new Font(this.plusButton.getFont().getName(),0,6);
		this.plusButton.setFont(littleFont);
		this.minusButton.setFont(littleFont);
		
		plusOrMinus.add(this.plusButton);
		plusOrMinus.add(this.minusButton);
		panel.add(plusOrMinus,BorderLayout.WEST);

		this.sizeInput.addKeyListener(this);
		this.sizeInput.setPreferredSize(new Dimension(35,20));
		this.slider.addMouseMotionListener(this);
		this.minusButton.addActionListener(this);
		this.plusButton.addActionListener(this);
		
		this.add(label,BorderLayout.WEST);
		this.add(this.slider,BorderLayout.CENTER);
		this.add(panel,BorderLayout.EAST);
	}

	private int getDigitFormat(JTextField inputField, int defaultValue)
	{
		String input=inputField.getText();
		boolean good_format=true;
		if(input.length()==0)
		{
			good_format=false;
		}
		for(int i=0;i<input.length();i++)
		{
			if(!Character.isDigit(input.charAt(i)))
			{
				good_format=false;
				break;
			}
		}
		if(!good_format)
		{
			return defaultValue;
		}
		else
		{
			return (Integer.valueOf(input)>SizePanel.MAX_SIZE?SizePanel.MAX_SIZE:
				Integer.valueOf(input)<1?1:Integer.parseInt(input));
		}
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		if(event.getKeyCode()==KeyEvent.VK_CONTROL||
			event.getKeyCode()==KeyEvent.VK_ALT||
			event.getKeyCode()==KeyEvent.VK_WINDOWS||
			event.getKeyCode()==KeyEvent.VK_ALT_GRAPH||
			event.getKeyCode()==KeyEvent.VK_CONTEXT_MENU)
		{
			this.specialKeyLock=true;
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		if(Character.isDigit(event.getKeyChar())||(event.getKeyChar()=='\n'
				||event.getKeyChar()=='\r'))
		{
			this.optionPanel.updateSize(this.getDigitFormat(this.sizeInput,
					this.sizeInput.getText().length()>0?Integer.parseInt(this.sizeInput.getText()):1));
			this.sizeInput.setText(""+this.optionPanel.getCurrentSize());
			this.slider.setValue(this.optionPanel.getCurrentSize());
		}
		else
		{
			if(event.getKeyCode()!=1&&event.getKeyCode()!=8&&
					event.getKeyCode()!=127&&!(event.getKeyCode()>=35&&
					event.getKeyCode()<=40)&&!this.specialKeyLock)
			{
				this.sizeInput.setText(String.valueOf(this.optionPanel.getCurrentSize()));
			}
			if(event.getKeyCode()==1||event.getKeyCode()==8||
					event.getKeyCode()==127)
			{
				this.optionPanel.updateSize(this.getDigitFormat(this.sizeInput,
						this.sizeInput.getText().length()>0?Integer.parseInt(this.sizeInput.getText()):1));
				this.sizeInput.setText(""+this.optionPanel.getCurrentSize());
				this.slider.setValue(this.optionPanel.getCurrentSize());
			}
			if(event.getKeyCode()==KeyEvent.VK_CONTROL||
					event.getKeyCode()==KeyEvent.VK_ALT||
					event.getKeyCode()==KeyEvent.VK_WINDOWS||
					event.getKeyCode()==KeyEvent.VK_ALT_GRAPH||
					event.getKeyCode()==KeyEvent.VK_CONTEXT_MENU)
			{
				this.specialKeyLock=false;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		this.sizeInput.setText(""+this.slider.getValue());
		this.optionPanel.updateSize(this.slider.getValue());
	}

	@Override
	public void mouseMoved(MouseEvent event)
	{	
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource().equals(this.plusButton))
		{
			if(this.optionPanel.getCurrentSize()<SizePanel.MAX_SIZE)
			{
				this.optionPanel.updateSize(this.slider.getValue()+1);
			}
		}
		else if(event.getSource().equals(this.minusButton))
		{
			if(this.optionPanel.getCurrentSize()>1)
			{
				this.optionPanel.updateSize(this.slider.getValue()-1);
			}
		}
		this.slider.setValue(this.optionPanel.getCurrentSize());
		this.sizeInput.setText(""+this.optionPanel.getCurrentSize());
	}
}
