package fr.tse.fi2.hpp.labs.queries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class WritingThread implements Runnable
{
	private BufferedWriter outputWriter;
	private final BlockingQueue<String> writingEvents;
	private final int id;
	
	WritingThread (BlockingQueue<String> q, int identifiant)
	{
		this.writingEvents = q;
		this.id = identifiant;
		
		try
		{
			this.outputWriter = new BufferedWriter(new FileWriter(new File("result/query" + id + ".txt")));
		}
		catch (IOException e)
		{
			System.exit(-1);
		}
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				String tmp = writingEvents.take();
				if (tmp.equals("noWriting")) // poisonpill pour la writingEvents
				{
					break;
				}
				else
				{
					writeInFile(tmp);
				}
			}
			
			// ferme le bufferedwriter
			try 
			{
				outputWriter.flush();
				outputWriter.close();
			} 
			catch (IOException e) 
			{
				System.exit(-1);
			}
			
		}
		catch (InterruptedException ex)
		{
			System.exit(-1);
		}
	}
	
	/**
	 * Ecrit la string value dans le fichier
	 * @param value
	 */
	void writeInFile(String value)
	{
		try 
		{
			outputWriter.write(value);
			outputWriter.newLine();
		} 
		catch (IOException e)
		{
			System.exit(-1);
		}
		
	}
}
