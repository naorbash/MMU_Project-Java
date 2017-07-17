package com.hit.controller;

import java.util.List;
import java.util.Observable;
import com.hit.driver.CLI;
import com.hit.model.MMUClient;
import com.hit.model.Model;
import com.hit.util.MMULogger;
import com.hit.view.View;
import com.hit.view.LoginView;
import com.hit.view.MMUView;

public class MMUController implements Controller
{
	private Model myModel;
	private View myView;
	private LoginView myLoginView;
	private MMUClient myClient;
	public MMUController(Model m, View[] viws)
	{
		this.myModel=m;
		this.myView=viws[0];
		this.myLoginView = (LoginView) viws[1];
		myClient = new MMUClient(myModel);
	}

	@Override
	public void update(Observable o, Object arg)
	{
		try{
			if(o instanceof CLI)
			{
				String[] commands = (String[]) arg;
				if(commands[2].equals("REMOTE")){  //if the user choose the REMOTE state.
					this.myLoginView.start();
					myClient.setCommands(commands);
				}
				else
					this.myModel.start(commands,true);  //if the user choose the Local state.
			}

			else if(o instanceof Model)
			{
				@SuppressWarnings("unchecked")
				List<String> logFileStrings = (List<String>) arg;
				((MMUView)this.myView).gettingLogList(logFileStrings);
			}
			else if(o instanceof LoginView)
			{
				String[] LoginInfo = (String[]) arg;
				myClient.setLoginInfo(LoginInfo);
				myClient.makeARequest();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

}
