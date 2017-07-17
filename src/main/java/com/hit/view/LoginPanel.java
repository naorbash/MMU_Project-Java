package com.hit.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanel extends JPanel implements ActionListener
{

	protected JButton loginButton = new JButton("Login");
	protected JLabel username = new JLabel("User Name: ");
	protected JLabel password = new JLabel("Password: ");
	protected JLabel fileName = new JLabel("File Name: ");
	protected JTextField userNameAnswer = new JTextField("",15);
	protected JTextField passwordAnswer = new JTextField("",15);
	protected JTextField fileNameAnswer = new JTextField("",15);
	protected LoginView myLoginView;
	public LoginPanel(LoginView LV) {
		this.myLoginView = LV;
		
		loginButton.setVerticalTextPosition(AbstractButton.CENTER);
		loginButton.setHorizontalTextPosition(AbstractButton.CENTER); //aka LEFT, for left-to-right locales
		loginButton.setActionCommand("pressed");
		
		loginButton.addActionListener(this);
		this.add(username);this.add(userNameAnswer);
		this.add(password);this.add(passwordAnswer);
		this.add(fileName);this.add(fileNameAnswer);
		this.add(loginButton);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if ("pressed".equals(e.getActionCommand()))
		{
			myLoginView.gettingLoginInfo(new String[]{userNameAnswer.getText(),passwordAnswer.getText(),fileNameAnswer.getText()});
		}

	}




}
