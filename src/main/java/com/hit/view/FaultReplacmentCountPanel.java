package com.hit.view;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FaultReplacmentCountPanel extends JPanel
{
	protected JTextField PageFault,PageReplacement;
	
	public FaultReplacmentCountPanel()
	{
		PageFault = new JTextField("",3);
		PageReplacement = new JTextField("",3);
		PageFault.setFont(new Font("Arial",Font.ITALIC,12));
		PageReplacement.setFont(new Font("Arial",Font.ITALIC,12));
		PageFault.setText("0");
		PageReplacement.setText("0");

		JLabel PageFaultLabel = new JLabel("Page Faults:");
		PageFaultLabel.setFont(new Font("Arial",Font.BOLD,14));
		JLabel PageReplacementLabel = new JLabel("Page Replacements:");
		PageReplacementLabel.setFont(new Font("Arial",Font.BOLD,14));
		
		this.add(PageFaultLabel);
		this.add(PageFault);
		this.add(PageReplacementLabel);
		this.add(PageReplacement);
	}
	
	public void setPageFaultCount(Integer c)
	{PageFault.setText(c.toString());}
	
	public void setPageReplacementCount(Integer c)
	{PageReplacement.setText(c.toString());}
	
	public int getPageFaultCount()
	{return Integer.parseInt(PageFault.getText());}
	
	public int getPageReplacementCount()
	{return Integer.parseInt(PageReplacement.getText());}

}
