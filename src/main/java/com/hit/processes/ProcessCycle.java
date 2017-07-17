package com.hit.processes;

import java.util.List;

public class ProcessCycle 
{
	private List<java.lang.Long> pages;
	private int sleepMs;
	private List<byte[]> data;
	
	public ProcessCycle(java.util.List<java.lang.Long> pages,int sleepMs,java.util.List<byte[]> data)
	{
		this.pages=pages;
		this.sleepMs = sleepMs;
		this.data = data;
	}
	public java.util.List<java.lang.Long> getPages()
	{return pages;}
	
	public void setPages(java.util.List<java.lang.Long> pages)
	{this.pages = pages;}
	
	public int getSleepMs() 
	{return sleepMs;}
	
	public void setSleepMs(int sleepMs) 
	{this.sleepMs = sleepMs;}
	
	public java.util.List<byte[]> getData() 
	{return data;}
	
	public void setData(java.util.List<byte[]> data) 
	{this.data = data;}
	
	@Override
	public String toString() {
		return "ProcessCycle [pages=" + pages + ", sleepMs=" + sleepMs + ", data=" + data + "]";
	}

	

}
