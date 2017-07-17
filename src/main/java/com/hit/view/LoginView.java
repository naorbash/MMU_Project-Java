package com.hit.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observable;
import javax.swing.JFrame;

public class LoginView extends Observable implements View{

	private String username,password,filename;
	private LoginPanel panel;
	private  void createAndShowLoginPanel() 
	{
		//Create and set up the window.
		JFrame frame = new JFrame("Authentication panel");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(300,200));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim =tk.getScreenSize();
		int xPos = (dim.width/4)-(frame.getWidth()/4);
		int yPos = (dim.height/4)-(frame.getHeight()/4);
		frame.setLocation(xPos, yPos);
		//Create and set up the panel and add it to our frame.
		panel = new LoginPanel(this);
		frame.getContentPane().add(panel);
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	
	@Override
	public void start() {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShowLoginPanel();
			}
		});
		
	}
	public void gettingLoginInfo(String[] loginInfo){
		this.setChanged();
		this.notifyObservers(loginInfo);
		this.clearChanged();
	}

}
