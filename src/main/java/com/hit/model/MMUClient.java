package com.hit.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hit.processes.RunConfiguration;

public class MMUClient 
{
	private String[] LoginInfo;
	private String[] Commands;
	private MMUModel myModel;
	
	public MMUClient(Model MMUModel) {
		this.myModel = (MMUModel) MMUModel;
	}
	
	public String[] getCommands() {
		return Commands;
	}
	public void setCommands(String[] Comm) {
		Commands = Comm;
	}
	public String[] getLoginInfo() {
		return LoginInfo;
	}
	public void setLoginInfo(String[] loginInfo) {
		LoginInfo = loginInfo;
	}
	
	public void makeARequest(){
		Socket myServer;
		try {
			myServer = new Socket(InetAddress.getLocalHost(), 12345);
			ObjectOutputStream output=new ObjectOutputStream(myServer.getOutputStream());
			ObjectInputStream input=new ObjectInputStream(myServer.getInputStream());

			output.writeObject(this.LoginInfo[0]+ " " + this.LoginInfo[1]+ " " + this.LoginInfo[2]);
			output.flush();
			String Login =(String)input.readObject();
			System.out.println(Login);
			
			if(Login.contains("successfully")){
				File configurationFile =(File)input.readObject();
				if(configurationFile!=null)
				{
					Gson gson = new Gson();
					RunConfiguration runConfig = gson.fromJson(new JsonReader(new FileReader(configurationFile)), RunConfiguration.class);
					myModel.setRunConfig(runConfig);
					myModel.start(Commands, false);
				}
				else
				System.out.println("No such file,Please try again.");
			}
			output.close();
			input.close();

		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Client error");
		}

	}



}
