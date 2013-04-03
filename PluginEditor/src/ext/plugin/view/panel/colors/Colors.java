package ext.plugin.view.panel.colors;

import java.awt.Color;

import javax.swing.JTextField;

public class Colors
{


	
	public static String getHexaValue(int value)
	{
		return (Integer.toHexString(value).length()==1?"0":"")+Integer.toHexString(value).toUpperCase();
	}
	
	public static String getHexaColor(Color color)
	{
		return "#"+Colors.getHexaValue(color.getRed())+Colors.getHexaValue(color.getGreen())+
				Colors.getHexaValue(color.getBlue());
	}
	
	public static Color getStringColorFormat(JTextField inputField, Color defaultColor)
	{
		String input=inputField.getText();
		boolean good_format=true;
		if(input.length()>7||input.charAt(0)!='#')
		{
			good_format=false;
		}
		else if(input.length()<7)
		{
			if(input.length()==4)
			{
				input="#"+input.charAt(1)+"0"+input.charAt(2)+"0"+input.charAt(3)+"0";
			}
			else
			{
				good_format=false;
			}
		}
		if(!good_format)
		{
			return defaultColor;
		}
		else
		{
			try
			{
				return Color.decode(input);
			}
			catch(NumberFormatException nfe)
			{
				return defaultColor;
			}
		}
	}
}
