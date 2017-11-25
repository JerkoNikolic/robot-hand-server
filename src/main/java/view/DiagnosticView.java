package view;

import java.awt.FlowLayout;

import javax.swing.JFrame;

public class DiagnosticView extends JFrame{
	
	public DiagnosticView() {
		this.setSize(800, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		this.add(new FingerInfoPanel("Index"));
		this.add(new FingerInfoPanel("Thumb"));
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new DiagnosticView();
	}
	
	
}
