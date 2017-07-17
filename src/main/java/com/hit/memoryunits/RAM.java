package com.hit.memoryunits;


import java.util.HashMap;
import java.util.Map;

public class RAM 
{
	private int capacity;
	private Map<Long,Page<byte[]>> pages;
	
	RAM(int initialCapacity)
	{
		this.capacity = initialCapacity;  
		this.pages = new HashMap<>();  //Container for the pages inside the RAM
	}
	
	public void addPage(Page<byte[]> addPage)
	{
		this.pages.put(addPage.getPageId(),addPage);  //adding a page to the RAM
	}
	
	public void addPages(Page<byte[]>[] addPages)   //adding pages to the RAM
	{
		for(int i=0;i<addPages.length;++i)
			pages.put(addPages[i].getPageId(),addPages[i]);
	}
	
	
	public Page<byte[]> getPage(Long pageId)  //returning the requested page.
	{
		return pages.get(pageId);
	}
	
	public Page<byte[]>[] getPages(Long[] pageIds)  //returning the requested pages.
	{
		@SuppressWarnings("unchecked")
		Page<byte[]>[] pagesToReturn = (Page<byte[]>[]) new Page[pageIds.length];  //creating a new empty array of Page<byte[]> 
		for(int i=0;i<pageIds.length;++i)
			if(this.pages.containsKey(pageIds[i]))    //if my RAM contains the requested page
				pagesToReturn[i]= pages.get(pageIds[i]);   //adding the page to the created array
		return pagesToReturn;   //returning the array with the requested pages.
	}
	
	public Map<Long,Page<byte[]>> getPages()  //returning all pages inside the RAM
	{
		return this.pages;
	}
	
	
	
	public void removePage(Page<byte[]> page) //removing the received page from the RAM
	{
		if(this.pages.containsKey(page.getPageId()))  //if this page is inside the RAM,remove it
			this.pages.remove(page.getPageId());
	}
	
	public void removePages(Page<byte[]>[] pages) //removing all pages received
	{
		for(int i=0;i<pages.length;++i)    //for all pages received
			if(this.pages.containsKey(pages[i].getPageId()))   //if the selected page is inside the RAM,remove it.
				this.pages.remove(pages[i].getPageId());
	}
	
	public int getInitialCapacity()
	{return this.capacity;}
	
	public void setInitialCapacity(int initialCapacity)
	{this.capacity = initialCapacity;}
	
	public void setPages(java.util.Map<java.lang.Long,Page<byte[]>> pages)   //setting the private variable-pages to the received pages container.
	{this.pages=pages;}
}
