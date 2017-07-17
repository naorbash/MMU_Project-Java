package com.hit.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Observable;
import java.util.Scanner;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.hit.view.View;

public class CLI extends Observable implements Runnable,View
{
	private InputStream in;
	//private OutputStream out;
	private OutputStreamWriter wr;
	public static final java.lang.String LFU="LFU";
	public static final java.lang.String LRU="LRU";
	public static final java.lang.String SECOND_CHANCE="SS";
	public static final java.lang.String START="START";
	public static final java.lang.String STOP="STOP";
	
	CLI(InputStream in,OutputStream out)
	{
		this.in=in;
		this.wr = new OutputStreamWriter(out); 
	}
	
	public void write(String string) throws IOException
	{
		wr.write(string + "\n");
		wr.flush();
	}

	@Override
	public void run() 
	{
		while(true)
		{
			try
			{
				String command=null,secondCommand=null,algoType=null,CacheSizeInString=null,location=null;
				BufferedReader br = new BufferedReader(new InputStreamReader(this.in));
				write("Enter Command(START/STOP)");
				command = br.readLine();

				if(command.equals(START))
				{

					write("Please enter required algorithm, RAM capacity and locality mode");
					Scanner sc = new Scanner(br.readLine());  //Getting the second command from the user.
					sc.useDelimiter(" ");
					algoType = sc.next();
					CacheSizeInString = sc.next();
					location = sc.next();	

					if(!(algoType.equals(LFU)|| algoType.equals(LRU) || algoType.equals(SECOND_CHANCE)))
					{
						write("Not a valid algo type");
						continue;
					} 
					else
					{
						try
						{Integer.parseInt(CacheSizeInString);} //Checking for size correctness.
						catch(NumberFormatException e)
						{
							write("Not a valid size");
							continue;
						}

						if(location.equals("LOCAL")||location.equals("REMOTE") )
						{
							this.setChanged();			//Notify the controller when getting a valid command.
							this.notifyObservers(new String[]{algoType,CacheSizeInString,location});
							this.clearChanged(); 
						}
						else
						{
							write("Not a valid location type - make sure to use CAPITAL letters");
							continue;
						}

					}
				}
				else if(command.equals(STOP))
				{ 
					write("Thank you");
					wr.close(); //closing the output
					break;
				}
				else
				{
					write("Not a valid command");
					continue;
				} 
			}
			catch (JsonIOException | JsonSyntaxException | IOException e) 
			{e.printStackTrace();}
		}

	}

	@Override
	public void start() 
	{run();}
}
