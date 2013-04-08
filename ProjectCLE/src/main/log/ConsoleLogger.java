<<<<<<< HEAD
package main.log;

public class ConsoleLogger implements ILogger
{

	@Override
	public void write(String message)
	{
		System.out.println("[LOG] "+message);
	}

	@Override
	public void error(String error)
	{
		System.err.println("[ERROR] "+error);
	}

	@Override
	public void print(String message)
	{
		System.out.print(""+message);
	}

	@Override
	public void display()
	{
	}
	
}
=======
package main.log;

public class ConsoleLogger implements ILogger
{

	@Override
	public void write(String message)
	{
		System.out.println("[LOG] "+message);
	}

	@Override
	public void error(String error)
	{
		System.err.println("[ERROR] "+error);
	}

	@Override
	public void print(String message)
	{
		System.out.print(""+message);
	}

	@Override
	public void display()
	{
	}
	
}
>>>>>>> Update some PluginManager bugs
