package main.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Files
{
	public final static int REPLACE_EXISTING=1;
	
	public static void copy(File sourceFile, File destFile, int OPTIONS) throws IOException
	{
    	String fileName=destFile.getName();
	    if(!destFile.exists()&&OPTIONS==1)
	    {
	        try
	        {
				destFile.createNewFile();
			}
	        catch (IOException ioe)
	        {
				System.err.println("[ERROR] Impossible de remplacer le fichier \""+fileName+
						"\":\n"+ioe.getMessage());
			}
	    }

	    FileChannel source = null;
	    FileChannel destination = null;
	    try
	    {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();

	        long count = 0;
	        long size = source.size();              
	        while((count += destination.transferFrom(source, count, size-count))<size);
	    }
	    catch (FileNotFoundException fnfe)
	    {
			System.err.println("[ERROR] Erreur lors de la copie du fichier \""+fileName
					+"\". Fichier non trouvé:\n"+fnfe.getMessage());
		}
	    finally
	    {
	        if(source != null)
	        {
	            source.close();
	        }
	        if(destination != null)
	        {
				destination.close();
	        }
	    }
	}

	/**
	 * Supprime un fichier ou un dossier si il existe
	 * @param file : {@link File}, Le fichier/dossier
	 * @return {@link Boolean boolean}, Si le fichier/dossier a bien été supprimé
	 * @throws DirectoryNotEmptyException : {@link DirectoryNotEmptyException}, Si
	 * le dossier a supprimer n'est pas vide
	 */
	public static boolean deleteIfExists(File file) throws IOException
	{
		if(file.exists())
		{
			if(file.isDirectory())
			{
				if(file.listFiles().length>0)
				{
					throw new DirectoryNotEmptyException();
				}
			}
			return file.delete();
		}
		return false;
	}
}
