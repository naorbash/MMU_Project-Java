package com.hit.memoryunits;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.hit.algorithm.IAlgoCache;
import com.hit.util.MMULogger;

public class MemoryManagementUnit 
{
	private IAlgoCache<Long,Long> algo;
	private RAM ramUnit;
	public MemoryManagementUnit(int ramCapacity,IAlgoCache<Long,Long> algo)
	{
		this.algo = algo;
		this.ramUnit = new RAM(ramCapacity);
	}
	
	public synchronized Page<byte[]>[] getPages(Long[] pageIds) throws java.io.IOException
	{
		@SuppressWarnings("unchecked")
		Page<byte[]>[] pagesToReturn = (Page<byte[]>[]) new Page[pageIds.length]; // //creating a new empty array of Page<byte[]> 
		List<Long> pages = new ArrayList<>();   //creating a List of the pagesIds received to send to algoCache 
		List<Long> elementsToGetFromHD = new ArrayList<>();    //creating a List of elements that ARE NOT inside RAM and needed to be imported from HD

		
		int j,k=0;
		for(int i=0;i<pageIds.length;++i)   //putting the requested pagesID'S inside a list in order for it to be send to algoCache
			pages.add(pageIds[i]);
		
		List<Long> elementsReturnd = this.algo.getElement(pages);  //Getting the pages id's back from the algo
		for(int i=0;i<pageIds.length;i++)
			if(!elementsReturnd.contains(pageIds[i]))
					elementsToGetFromHD.add(pageIds[i]);  //Storing all the pages Id's that didn't came back from algoCache
		  
		for( j=0;j<elementsReturnd.size();j++)  //Inserting the requested pages to the return list
			pagesToReturn[j]=this.ramUnit.getPage(elementsReturnd.get(j));
	
		if(elementsToGetFromHD.size()>0)       // If algoCache didn't had all the pages
		{
			int numOfPagesToReplace = this.ramUnit.getPages().size() + elementsToGetFromHD.size() - this.ramUnit.getInitialCapacity(); //Getting the amount of pages to replace
			int availablePlaceInRam=this.ramUnit.getInitialCapacity()-this.ramUnit.getPages().size();      //And if the RAM is not full
			if(availablePlaceInRam>0) //if there is enough space in RAM for all/some requested pages.
			{
				
				List<Long> elementsToGetFromHDUsingFault = new ArrayList<>();  //a sub list for all elements that need to get from HD using FAULT method
				if(numOfPagesToReplace>0)
				for(int i=0;i<availablePlaceInRam;i++)
					elementsToGetFromHDUsingFault.add(elementsToGetFromHD.get(i));		//getting the pages that need to be in a Fault
				else
					for(int i=0;i<elementsToGetFromHD.size();i++)
						elementsToGetFromHDUsingFault.add(elementsToGetFromHD.get(i));	//getting the pages that need to be in a Fault
				
				for(int i=0;i<elementsToGetFromHDUsingFault.size();++i)  //for all those paging needed getting by Fault
				{
					HardDisk HD = HardDisk.getInstance();  //Getting the instance of the HD
					Page<byte[]> pagetoaddtoRAM = HD.pageFault(elementsToGetFromHDUsingFault.get(i));  //perform Fault
					ramUnit.addPage(pagetoaddtoRAM);  //Inserting the requested pages to RAM
					pagesToReturn[j]= pagetoaddtoRAM;j++;  //Inserting the requested pages to the return list
					LoggerWrite("PF: " + pagetoaddtoRAM.getPageId());
				}
				List<Long> pagesToInsertToHD=this.algo.putElement(elementsToGetFromHD, elementsToGetFromHD);//Inserting the requested pages to ALGO//NEED TO BE FIXED for scenario when elements are bigger than algo 
				for(int i=elementsToGetFromHDUsingFault.size();i<elementsToGetFromHD.size();++i)  //for all remaining elements that need to be replaced between RAM and HD 
				{
					HardDisk HD = HardDisk.getInstance();  //getting the instance of the HD
					Page<byte[]> pagetoaddtoRAM = HD.pageReplacement(this.ramUnit.getPage(pagesToInsertToHD.get(k)), elementsToGetFromHD.get(i)); //Perform page replacement between HD and Ram
					this.ramUnit.removePage(this.ramUnit.getPage(pagesToInsertToHD.get(k)));
					this.ramUnit.addPage(pagetoaddtoRAM);  //Inserting the requested pages to RAM
					pagesToReturn[j]= pagetoaddtoRAM;j++;  //Inserting the requested pages to the return list
					LoggerWrite("PR:MTH "+pagesToInsertToHD.get(k)+" MTR "+pagetoaddtoRAM.getPageId());
					k++;
				}
			}
			
			else     	 //If the RAM is FULL.we will use Page replacement from the start.
			{
				//need to write a solution for a case when the algo and RAM is empty and the received pages size is larger than the RAM capacity/there isnt enough pages inside RAM to remove

				List<Long> pagesToInsertToHD = this.algo.putElement(elementsToGetFromHD, elementsToGetFromHD); //getting the pages that needs to be removed from RAM using algo logic.
				for(int i=0;i<elementsToGetFromHD.size();++i)  //for all elements that need to be replaced between RAM and HD 
				{
					HardDisk HD = HardDisk.getInstance();  //getting the instance of the HD
					Page<byte[]> pagetoaddtoRAM = HD.pageReplacement(this.ramUnit.getPage(pagesToInsertToHD.get(i)), elementsToGetFromHD.get(i)); //Perform page replacement between HD and Ram
					this.ramUnit.removePage(this.ramUnit.getPage(pagesToInsertToHD.get(i)));    //Removing the actual page from RAM.
					this.ramUnit.addPage(pagetoaddtoRAM);  //Inserting the requested pages to RAM
					pagesToReturn[j]= pagetoaddtoRAM;j++;  //Inserting the requested pages to the return list
					LoggerWrite("PR:MTH "+pagesToInsertToHD.get(i)+" MTR "+pagetoaddtoRAM.getPageId());
				}
			}
		}
		return pagesToReturn;
	}
	
	public boolean shutdown() throws IOException  //Shutdown method for a program Shutdown ,perform a final write to Disk of all pages in RAM.
	{
		HardDisk HD = HardDisk.getInstance();
		if(HD.finalDownToDisk(this.ramUnit.getPages()))
			return true;
		return false;
	}
	
	public  void LoggerWrite(String command) throws SecurityException, IOException //Writing to the logger
	{
		MMULogger mmuLogger = MMULogger.getInstance();
		mmuLogger.write(command, Level.INFO);
	}
}
