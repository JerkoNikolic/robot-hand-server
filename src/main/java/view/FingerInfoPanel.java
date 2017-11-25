package view;

import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FingerInfoPanel extends JPanel {
	private JLabel x, y, z, baseMotor, tipMotor;
	
	public FingerInfoPanel(String name) {
		this.setSize(800, 75);
		this.x = new JLabel("0.0");
		this.y = new JLabel("0.0");
		this.z = new JLabel("0.0");
		this.baseMotor = new JLabel("0.0");
		this.tipMotor = new JLabel("0.0");

		this.setLayout(new FlowLayout());
		this.add(new JLabel(name));
		this.add(x);
		this.add(y);
		this.add(z);
		this.add(baseMotor);
		this.add(tipMotor);
	}
	
	public void refreshInputData(int x, int y, int z) {
		
	}
	public void refreshOutputData() {
		
	}
}
