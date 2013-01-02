import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;


public class myWindowListener implements WindowListener {

	Main obj;
	
	public myWindowListener(Main o) {
		obj=o;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		int n=JOptionPane.showConfirmDialog(obj.frame, "Are you sure you want to exit ?", "Code Magnet", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
		if(n==JOptionPane.YES_OPTION)
		{
			obj.frame.dispose();
			System.exit(0);
		}

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
