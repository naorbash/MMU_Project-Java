package com.hit.view;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PlayPanel extends JPanel implements ActionListener
{
	protected JButton b1,b2;
	protected MMUView mainFrame;
	
	public PlayPanel(MMUView mf)
	{
		this.mainFrame=mf;
		//this.setLayout(new FlowLayout());
		
		b1 = new JButton("PLAY");
		b1.setVerticalTextPosition(AbstractButton.CENTER);
		b1.setHorizontalTextPosition(AbstractButton.CENTER); //aka LEFT, for left-to-right locales
		b1.setActionCommand("pressedPLAY");
		b1.setFont(new Font("Arial",Font.BOLD,16));
		
		b2 = new JButton("PLAY ALL");
		b2.setVerticalTextPosition(AbstractButton.CENTER);
		b2.setHorizontalTextPosition(AbstractButton.CENTER); //aka LEFT, for left-to-right locales
		b2.setActionCommand("pressedPLAYALL");
		b2.setFont(new Font("Arial",Font.BOLD,16));
		
		b1.addActionListener(this);
		b2.addActionListener(this);
		this.add(b1);this.add(b2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(((JButton)e.getSource()).getText().equals("PLAY"))
		{
			this.mainFrame.playLog();
		}
		else if ("pressedPLAYALL".equals(e.getActionCommand()))
		{
			this.mainFrame.playAllLog();
		}
		
	}
	
}
