package com.hit.memoryunits;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.hit.util.MMULogger;
public class HardDisk 
{
	private static HardDisk instance=null;
	private final int SIZE = 100;
	private final String fileName = "HardDiskFile.dat";
	private Map<Long,Page<byte[]>> HDpages;
	private  ObjectOutputStream out  = null;
	private  ObjectInputStream in  = null;
	
	private HardDisk() throws SecurityException, IOException   //private constructor  - Singleton
	{
		HDpages = new HashMap<Long, Page<byte[]>>(SIZE);
		
		for(long i=0;i<SIZE;++i)
		{
			byte[] a = new byte[10];
			Page<byte[]> p = new Page<byte[]>((Long)i,a);  //Creating a new Page with the Long number and an empty byte array defult size 10 
			HDpages.put((Long)i,p);    //Storing it inside the Hard Disk page map.
		}
		
		try 
		{
			File f = new File(fileName);
			if(!f.isFile()){    //if there isn't a file already.open a new one an insert the MAP.
			out = new ObjectOutputStream(new FileOutputStream(fileName));  //writing the HDpages map as an object to a file
			out.writeObject(HDpages);
			out.flush();
			out.close();}
		} 
		catch (IOException e) 
		{
			LoggerWrite("Hard Disk:Constractur:IOException");
			e.printStackTrace();
		}
		
		
	}
	public static HardDisk getInstance() throws SecurityException, IOException
	{
		if(instance==null)
			instance= new HardDisk();
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public Page<byte[]> pageFault(Long pageId)throws FileNotFoundException,IOException 
	{
		try
		{
		in = new ObjectInputStream(new FileInputStream(this.fileName));
			this.HDpages = (Map<Long,Page<byte[]>>)in.readObject();
			in.close();
		}
		catch (Exception e1) 
		{
			LoggerWrite("Hard Disk:pageFault:Exception:Line 70");
			e1.printStackTrace();
		}
		
			if (this.HDpages.containsKey(pageId))  //if my HD contains the requested page
			{
				Page<byte[]> pagetoreturn = this.HDpages.get(pageId);  //getting that page from HD
				return pagetoreturn;
			}
			else
				throw new IOException("page id not found/not vaild"); //if the giving id is invalid
}
	
	@SuppressWarnings("unchecked")
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage ,Long moveToRamId)throws java.io.FileNotFoundException,
	java.io.IOException
	{
			try
			{
				in = new ObjectInputStream(new FileInputStream(this.fileName));
				this.HDpages = (Map<Long,Page<byte[]>>)in.readObject();
				in.close();
			}
			catch (Exception e1) 
			{
				LoggerWrite("Hard Disk:pageReplacement:Exception:Line 95");
				e1.printStackTrace();
			}
			if(this.HDpages.containsKey(moveToRamId))
			{
				Page<byte[]> pagetoreturn = this.HDpages.get(moveToRamId);  //getting the requested page from HD

				this.HDpages.put(moveToHdPage.getPageId(), moveToHdPage);  //inserting the giving HDpage to HD
				try
				{
					out = new ObjectOutputStream(new FileOutputStream(this.fileName));  //writing the UPDATED HDpages map as an object the file
					out.writeObject(this.HDpages);
					out.flush();
					out.close();
				}
				catch(FileNotFoundException e)  //if File is not found 
				{
					LoggerWrite("Hard Disk:pageReplacement:FileNotFoundException:Line 112");
					e.printStackTrace();
				}
				return pagetoreturn;    //returning the requested page
			}
			else 
				throw new IOException("page id not found/not vaild");  //if the giving id is invalid
		
	}
	@SuppressWarnings("unchecked")
	boolean finalDownToDisk(Map<Long,Page<byte[]>> ramMap) throws IOException
	{
		try  //getting the map from the file
		{
			in = new ObjectInputStream(new FileInputStream(this.fileName));
			this.HDpages = (Map<Long,Page<byte[]>>)in.readObject();
			in.close();
		}
		catch (Exception e1) 
		{
			LoggerWrite("Hard Disk:finalDownToDisk:IOException:Line 132");
			e1.printStackTrace();
		}
		for(Entry<Long,Page<byte[]>> entry : ramMap.entrySet())  //Updating the Hard disk pages map with the received data from RAM
		{
			Long key = entry.getKey();
			this.HDpages.put(key, ramMap.get(key));
		}
		try 	 //writing the updated map back to the file.
		{
			out = new ObjectOutputStream(new FileOutputStream(this.fileName));  //writing the map to the Hard Disk in it's most updated form.
			out.writeObject(this.HDpages);
			out.flush();
			out.close();
		}
		catch(FileNotFoundException e)  //if File is not found 
		{
			LoggerWrite("Hard Disk:finalDownToDisk:FileNotFoundException:Line 149");
			e.printStackTrace();
		}
		
		
		/*for(long i=0;i<HDpages.size();i++)  //Printing after change was made
		{
			System.out.println(HDpages.get(i).getContent()+ " NEW HD");
		}*/
		
		return true;
	}
	public Map<Long,Page<byte[]>> getMAP()
	{return this.HDpages;}
	
	public  void LoggerWrite(String command) throws SecurityException, IOException //Writing to the logger
	{
		MMULogger mmuLogger = MMULogger.getInstance();
		mmuLogger.write(command, Level.SEVERE);
	}
}
