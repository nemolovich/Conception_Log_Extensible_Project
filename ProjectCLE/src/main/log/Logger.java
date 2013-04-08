package main.log;

public interface Logger
{
	public void write(String message);
	public void print(String message);
	public void error(String error);
	public void display();
}
