package com.hit.processes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import com.hit.memoryunits.Page;
import com.hit.util.MMULogger;

public class Process implements Runnable
{
	private int id;
	private com.hit.memoryunits.MemoryManagementUnit MMU;
	private ProcessCycles processCycles;
	
	public Process(int id,com.hit.memoryunits.MemoryManagementUnit mmu,ProcessCycles processCycles) 
	{
		this.id=id;
		this.MMU=mmu;
		this.processCycles=processCycles;
	}

	public int getId()
	{return id;}

	public void setId(int id) 
	{this.id = id;}

	@Override
	public void run() 
	{
		List<ProcessCycle> processCycle = this.processCycles.getProcessCycles();
		for(int i=0;i<processCycle.size();i++)
		{
			Long[] pageIds= new Long[processCycle.get(i).getPages().size()];//creating a Long array
			Page<byte[]>[] pagesToAppend = null;
			for(int j=0;j<processCycle.get(i).getPages().size();++j) //converting the List to an Long array of id's to send to MMU.
				pageIds[j]=processCycle.get(i).getPages().get(j);

			try {
				pagesToAppend = this.MMU.getPages(pageIds);//getting the pages needed for the process.
			}
			catch (IOException e1) 
			{e1.printStackTrace();}

			List<byte[]> dataToInsertToPages = processCycle.get(i).getData();//getting the DATA to write to the pages.
			for(int j=0;j<pagesToAppend.length;j++)  //for every page that needs appending
			{
				pagesToAppend[j].setContent(dataToInsertToPages.get(j));  //writing the new DATA to the page.
				try 
				{LoggerWrite("GP:P"+(this.getId()+1)+" "+pagesToAppend[j].getPageId()+ Arrays.toString(dataToInsertToPages.get(j)));} //writing to logger.
				catch (SecurityException | IOException e) 
				{e.printStackTrace();}
			}
			try
			{
				Thread.sleep(processCycle.get(i).getSleepMs());
			}
			catch(InterruptedException e)
			{System.out.println("Interrupted while sleeping");}
		}
	}
	public  void LoggerWrite(String command) throws SecurityException, IOException //Writing to the logger
	{
		MMULogger mmuLogger = MMULogger.getInstance();
		mmuLogger.write(command, Level.INFO);
	}

}
