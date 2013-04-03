package main.view;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class SplashScreenMain
{

	SplashScreen screen;

	public SplashScreenMain()
	{
		// initialize the splash screen
		splashScreenInit();
		// do something here to simulate the program doing something that
		// is time consuming
		for (int i = 0; i <= 100; i++)
		{
			try {
				new Thread().sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// run either of these two -- not both
			screen.setProgress("Loading " + i, i);  // progress bar with a message
			//screen.setProgress(i);           // progress bar with no message
		}
		splashScreenDestruct();
		System.exit(0);
	}

	private void splashScreenDestruct()
	{
		screen.setScreenVisible(false);
	}

	private void splashScreenInit()
	{
		ImageIcon myImage = new ImageIcon("img/SplashScreen.jpg");
	    screen = new SplashScreen(myImage);
	    screen.setLocationRelativeTo(null);
	    screen.setProgressMax(100);
	    screen.setScreenVisible(true);
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new SplashScreenMain();
	}

}