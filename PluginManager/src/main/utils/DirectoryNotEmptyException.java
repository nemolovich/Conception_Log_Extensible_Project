package main.utils;

import java.io.IOException;

public class DirectoryNotEmptyException extends IOException
{
	/**
	 * ID
	 */
	private static final long serialVersionUID = 3319104840592492440L;
	
	public DirectoryNotEmptyException()
	{
		super("Erreur le dossier n'est pas vide");
	}

}
