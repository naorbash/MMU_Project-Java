package com.hit.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LFUAlgoCacheImpl;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.SecondChanceAlgoCacheImpl;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;

public class MMUModel extends Observable implements Model
{
	private MemoryManagementUnit mmu;
	private int ramCapacity;
	private int numProcesses;
	private List<String> logFileStrings;
	private RunConfiguration runConfig;
	private MMULogger mmuLogger = null;
	public void setConfiguration(String[] command,boolean local) throws JsonIOException, JsonSyntaxException, InterruptedException, IOException
	{
		IAlgoCache<Long, Long> algo = null;
		ramCapacity = Integer.parseInt(command[1]);   //Getting the capacity from the string
		if(command[0].equals("LFU"))                   //init the selected algo unit
			algo = new  LFUAlgoCacheImpl<Long, Long>(ramCapacity);
		else if(command[0].equals("LRU"))
			algo = new  LRUAlgoCacheImpl<Long, Long>(ramCapacity);
		else
			algo = new  SecondChanceAlgoCacheImpl<Long, Long>(ramCapacity);

		mmu = new MemoryManagementUnit(ramCapacity, algo);
		LoggerWrite("RC:" + ramCapacity);
		
		if(local)//if the user choose LOCAL mode.
			runConfig = readConfigurationFile();   //reading the JSON file into a RunConfiguration member if it is in LOCAL state.
		
		List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
		numProcesses = processCycles.size();
		LoggerWrite("PN:" + numProcesses);

		List<Process> processes = createProcesses(processCycles, mmu);   //creating new processes. combining each process with his applications scenarios and MMU.
		runProcesses(processes);  //sending all of the created processes to execute, using an executor for managing them.
	}

	public  RunConfiguration readConfigurationFile() throws JsonIOException, JsonSyntaxException, FileNotFoundException
	{
		Gson gson = new Gson();
		RunConfiguration runConfig = gson.fromJson(new JsonReader(new FileReader("src/main/resources/com/hit/config/Configuration.json")), RunConfiguration.class);
		return runConfig;
	}

	public  List<Process> createProcesses(List<ProcessCycles> appliocationsScenarios, MemoryManagementUnit mmu)
	{
		List<Process> processes = new ArrayList<Process>();
		for(int i=0;i<appliocationsScenarios.size();++i)
		{
			Process p  = new Process(i,mmu,appliocationsScenarios.get(i));
			processes.add(p);
		}
		return processes;
	}

	public  void runProcesses(List<Process> applications) throws InterruptedException, IOException
	{
		Executor executor = Executors.newCachedThreadPool();
		for(int i=0;i<applications.size();++i)
			executor.execute(applications.get(i));
		((ExecutorService)executor).shutdown();
		((ExecutorService)executor).awaitTermination(10, TimeUnit.SECONDS);
		mmu.shutdown();
		
		mmuLogger = MMULogger.getInstance();
		mmuLogger.CloseHandler();  //closing file after all changes has been written.

		Scanner sc = new Scanner(new FileReader("log/log.txt"));
		logFileStrings = new ArrayList<>();
		while (sc.hasNextLine()) 
		{
			logFileStrings.add(sc.nextLine());
		}
		sc.close();
		
		this.setChanged();
		this.notifyObservers(logFileStrings);//sending the LOG file strings in a list to the controller.
		this.clearChanged();

	}

	public  void LoggerWrite(String command) throws SecurityException, IOException //Writing to the logger
	{
		mmuLogger = MMULogger.getInstance();
		mmuLogger.write(command, Level.INFO);
	}

	@Override
	public void start(String[] command,boolean local) 
	{
		try 
		{
			this.setConfiguration(command,local);
		} 
		catch (JsonIOException | JsonSyntaxException | InterruptedException | IOException e) 
		{
			e.printStackTrace();
		}
	
	}
	public void setRunConfig(RunConfiguration runConfig) {
		this.runConfig = runConfig; //reading the JSON file into a RunConfiguration member if it is in REMOTE state.
	}


}
