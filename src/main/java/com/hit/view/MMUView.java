package com.hit.view;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.table.TableColumn;

public class MMUView extends Observable implements View
{
	private List<String> logListRecived;
	private HashMap<String,Integer>processesSelected;
	private int numOfProcesses,i;
	private JFrame Mframe;
	private Container container;
	private TablePanel tablePanel;
	private FaultReplacmentCountPanel PFRFPanel;
	private ProcessesPanel processesPanel ;
	private PlayPanel playpanel;
	
	private  void createAndShowGUI()  //Activated for each command. local and remote
	{
		//Create and set up the window.
		Mframe = new JFrame("MainFrame");
		Mframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Mframe.setPreferredSize(new Dimension(900,500));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim =tk.getScreenSize();
		int xPos = (dim.width/4)-(Mframe.getWidth()/4);
		int yPos = (dim.height/4)-(Mframe.getHeight()/4);
		Mframe.setLocation(xPos, yPos);
		
		
		numOfProcesses = Integer.parseInt(logListRecived.get(1).substring(logListRecived.get(2).indexOf(":")+1));//Extracting the number of processes from the file.
		i=0;    //setting the number of line counter to 0 in each activation.
		
		//Create and set up the panels and add it to our frame.
		
		playpanel = new PlayPanel(this);   			//Play/Play all button
		playpanel.setBounds(30, 300, 200, 200);
		playpanel.setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT);
		
		processesPanel = new ProcessesPanel(this,numOfProcesses);    	//Processes list panel 
		processesPanel.setBounds(700, 200, 150, 200);
		
		tablePanel = new TablePanel();   	//Table panel 
		tablePanel.setBounds(10, 10, 670, 175);
		
		PFRFPanel = new FaultReplacmentCountPanel();   			// PF/PR counters panel 
		PFRFPanel.setBounds(690,50,200,150);
		
		container = Mframe.getContentPane();
		container.setLayout(null);
		container.add(playpanel);
		container.add(processesPanel);
		container.add(tablePanel);
		container.add(PFRFPanel);
		
		//Display the window.
		Mframe.pack();
		Mframe.setVisible(true);

	}
	
	
	
	@Override
	public void start() 
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShowGUI();
			}
		});
	}
	
	public void playLog()
	{
		if(i==logListRecived.size())
			System.out.println("No more commands");//need to be change to a panel!!!!!!!
		else if(processesSelected==null)
			System.out.println("No Process was selected");
		else
		{
			if(logListRecived.get(i).contains("GP")) //if there was a Get Page line
			{
				String processnum = logListRecived.get(i).substring(logListRecived.get(i).indexOf("P")+3,logListRecived.get(i).indexOf(" ")); //extracting the process number from the string
				String pagenum = logListRecived.get(i).substring(logListRecived.get(i).indexOf(" ")+1,logListRecived.get(i).indexOf("["));  //extracting the page number from the string
				if(processesSelected.containsKey(processnum))//if it is a process the user wish to see
				{
					tablePanel.editColumn(Integer.parseInt(pagenum),pagenum,logListRecived.get(i).substring(logListRecived.get(i).indexOf("[")+1, logListRecived.get(i).indexOf("]")));
				}
			}
			else if(logListRecived.get(i).contains("PR"))
			{
				PFRFPanel.setPageReplacementCount(PFRFPanel.getPageReplacementCount()+1);  //incrementing the Page-Replacement count
				String pagetoremove = logListRecived.get(i).substring(logListRecived.get(i).indexOf(" ")+1,logListRecived.get(i).indexOf("MTR")-1);//Extracting the MTH page
				String pageinsidetable = (String) tablePanel.table.getTableHeader().getColumnModel().getColumn(Integer.parseInt(pagetoremove)).getHeaderValue();  //getting the value at that place on the table
				if(pagetoremove.equals(pageinsidetable)) //if the page is inside the table , we will clear it's column.
				{
					tablePanel.editColumn(Integer.parseInt(pagetoremove)," ","0,0,0,0,0");
				}

			}
			else if(logListRecived.get(i).contains("PF"))
			{PFRFPanel.setPageFaultCount(PFRFPanel.getPageFaultCount()+1);}  //changing the Page-Fault count

			i++;
		}
	}

	public void playAllLog()
	{
		if(i==logListRecived.size())
			System.out.println("No more commands");//need to be change to a panel!!!!!!!
		else if(processesSelected==null)
			System.out.println("No Process was selected");
		else
		{
			for(;i<logListRecived.size();i++)
			{
				if(logListRecived.get(i).contains("GP")) //if there was a Get Page line
				{
					String processnum = logListRecived.get(i).substring(logListRecived.get(i).indexOf("P")+3,logListRecived.get(i).indexOf(" ")); //extracting the process number from the string
					String pagenum = logListRecived.get(i).substring(logListRecived.get(i).indexOf(" ")+1,logListRecived.get(i).indexOf("["));  //extracting the page number from the string
					if(processesSelected.containsKey(processnum))//if it is a process the user wish to see
					{
						tablePanel.editColumn(Integer.parseInt(pagenum),pagenum,logListRecived.get(i).substring(logListRecived.get(i).indexOf("[")+1, logListRecived.get(i).indexOf("]")));
					}
				}
				if(logListRecived.get(i).contains("PR"))
				{
					PFRFPanel.setPageReplacementCount(PFRFPanel.getPageReplacementCount()+1);  //changing the Page-Replacement count
					String pagetoremove = logListRecived.get(i).substring(logListRecived.get(i).indexOf(" ")+1,logListRecived.get(i).indexOf("MTR")-1);//Extracting the MTH page
					String pageinsidetable = (String) tablePanel.table.getTableHeader().getColumnModel().getColumn(Integer.parseInt(pagetoremove)).getHeaderValue();  //getting the value at that place on the table
					if(pagetoremove.equals(pageinsidetable)) //if the page is inside the table , we will clear it's column.
					{
						tablePanel.editColumn(Integer.parseInt(pagetoremove)," ","0,0,0,0,0");
					}

				}
				else if(logListRecived.get(i).contains("PF"))
				{PFRFPanel.setPageFaultCount(PFRFPanel.getPageFaultCount()+1);}

			}
		}

	}

	public void gettingLogList(List<String> logList)//getting the log list and starting GUI
	{
		this.logListRecived=logList;
		this.start();
	}
	
	public void gettingProcesses( HashMap<String,Integer> processesmap)//getting a map of processes the user wish to see
	{this.processesSelected= processesmap;}

	
}
