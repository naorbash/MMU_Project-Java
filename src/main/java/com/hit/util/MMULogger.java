package com.hit.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MMULogger 
{
	public final static String DEFAULT_FILE_NAME = "log.txt";
	private FileHandler handler;
	private static MMULogger instance=null;
	
	private MMULogger() throws SecurityException, IOException
	{
		handler = new FileHandler("log/" + DEFAULT_FILE_NAME);
		OnlyMessageFormatter OnlyMessageFormatter = new OnlyMessageFormatter();
		this.handler.setFormatter(OnlyMessageFormatter);
	}
	
	public static MMULogger getInstance() throws SecurityException, IOException
	{
		if(instance==null)
			instance= new MMULogger();
		return instance;
	}
	
	public synchronized void write(String command,Level level)
	{
		LogRecord record = new LogRecord(level,command);
		this.handler.publish(record);
	}
	
	public class OnlyMessageFormatter extends Formatter
	{
		public OnlyMessageFormatter(){super();}

		@Override
		public String format(final LogRecord record) 
		{
			return record.getMessage()+ System.getProperty("line.separator");
		}
	}
	
	public void CloseHandler(){
		this.handler.close();
		this.instance=null;
	}
}
