import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JOptionPane;

class submitListener implements ActionListener{
	
	Main obj;
	
	public submitListener(Main o) {
		obj=o;
	}
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {
			
			String msg="You Can Submit only once, no changes are allowed after submitting and \nthe timer shall stop. " +
					"Do you wish to continue ?";
			int n=JOptionPane.showConfirmDialog(obj.frame, msg, "Code Magnet", JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
			if(n==JOptionPane.NO_OPTION)return;
			
			try{
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(obj.userFile1));
				bw.write(obj.rUp.getText());
				bw.close();
			}
			catch (Exception e1) {
				System.out.println("Error Saving the file 1");
				e1.printStackTrace();
			}
			try{
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(obj.userFile2));
				bw.write(obj.rDown.getText());
				bw.close();
			}
			catch (Exception e2) {
				System.out.println("Error Saving the file 2");
				e2.printStackTrace();
			}
			
			obj.timer.stop();
			obj.lUpScroller.setVisible(false);
           	obj.lDownScroller.setVisible(false);
           	obj.submit.setEnabled(false);
			obj.rUpscroller.setVisible(false);
			obj.rDownscroller.setVisible(false);
			
			String msg2="You have successfully submitted the solution!\n" +
					"You took "+obj.time+" s.\n" +
					"Do not close this window and inform the organiser.";
			JOptionPane.showMessageDialog(obj.frame, msg2, "Code Magnet", JOptionPane.INFORMATION_MESSAGE, null);
			
		}
	}