import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.DefaultCaret;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Main{
	
	//for jar file
	JTextArea output=new JTextArea(400,5);
	
	//JTextArea output=new JTextArea(300,5);
	
	//used for indicating if answer1 and answer2 have been uploaded, so that submit button can be enabled
	boolean f0=false,  f1=false,f2=false;
	
	int maxLabels=100;
	String answer1="",answer2="";
	
	JMenuBar menuBar=new JMenuBar();
	JMenu optionMenu=new JMenu("Options");
	JMenuItem qUp=new JMenuItem("Upload Question");
	JMenuItem aUp1=new JMenuItem("Upload Answer 1");
	JMenuItem aUp2=new JMenuItem("Upload Answer 2");
	JMenuItem submit=new JMenuItem("Submit");
	
	JFrame frame=new JFrame("Code Magnet");
	
	//panel contains of all question text areas
	JPanel panel=new JPanel(new GridLayout(maxLabels,1,0,1));
	
	//rightPanel contains the two RSyntax text areas i.e entire right part
	JPanel rightPanel=new JPanel();
	
	//leftPanel contains output and question text areas i.e entire left part
	JPanel leftPanel=new JPanel();
	
	JTextArea area[]=new JTextArea[maxLabels];
	
	RSyntaxTextArea rUp=new RSyntaxTextArea(3,60);
	RSyntaxTextArea rDown=new RSyntaxTextArea(5,60);
	
	JScrollPane lUpScroller = new JScrollPane(output);
	JScrollPane lDownScroller = new JScrollPane(panel);  
	
	RTextScrollPane rUpscroller = new RTextScrollPane(rUp);
	RTextScrollPane rDownscroller = new RTextScrollPane(rDown);
	
	int time=0;
	Thread timer=new Thread(new timerJob());
		
	public static void main(String[] args){
		new Main().go();
	}
	void go(){
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, fall back to cross-platform
		    try {
		        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		    } catch (Exception ex) {
		        // not worth my time
		    }
		}
		
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(lUpScroller);
		leftPanel.add(lDownScroller);
		
		lUpScroller.getVerticalScrollBar().setUnitIncrement(3);
		lDownScroller.getVerticalScrollBar().setUnitIncrement(36);
		lDownScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		rUp.setFont(new Font("Courier New",Font.PLAIN,14));
		rUp.setAnimateBracketMatching(true);
		rUp.setAutoIndentEnabled(true);
		
		rDown.setAnimateBracketMatching(true);
		rDown.setAutoIndentEnabled(true);
		rDown.setFont(new Font("Courier New",Font.PLAIN,14));

		rUpscroller.getVerticalScrollBar().setUnitIncrement(16);
		rDownscroller.getVerticalScrollBar().setUnitIncrement(16);		
		
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(rUpscroller);
		rightPanel.add(rDownscroller);
		
		qUp.addActionListener(new qUpListener());
		aUp1.addActionListener(new aUp1Listener());
		aUp2.addActionListener(new aUp2Listener());
		submit.addActionListener(new submitListener());
		
		output.setEditable(false);
    	output.setDragEnabled(false);
    	output.setFont(new Font("Courier New",Font.BOLD,16));
    	
		for(int i=0;i<maxLabels;i++){
			
			area[i]=new JTextArea(3,60);
			DefaultCaret caret = (DefaultCaret)area[i].getCaret();
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
			area[i].setFont(new Font("Courier New",Font.PLAIN,14));
			area[i].setLineWrap(true);
			area[i].setWrapStyleWord(true);
	    	area[i].setEditable(false);
			area[i].setTransferHandler(new TransferHandler("text"));
			area[i].setDropTarget(null);
	    	area[i].addMouseListener(new MouseAdapter(){
					public void mousePressed(MouseEvent e){
								JTextArea a = (JTextArea)e.getSource();
								TransferHandler handle = a.getTransferHandler();
								handle.exportAsDrag(a, e, TransferHandler.COPY);
				           }
				 });

			area[i].setDragEnabled(true);
			panel.add(area[i]);
		}
		
		submit.setEnabled(false);
		
		optionMenu.add(qUp);
		optionMenu.add(aUp1);
		optionMenu.add(aUp2);
		optionMenu.add(submit);
		menuBar.add(optionMenu);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(BorderLayout.EAST,leftPanel);
		frame.getContentPane().add(BorderLayout.WEST,rightPanel);
		frame.setVisible(true);
	}
	
	class qUpListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser fileOpen=new JFileChooser();
			fileOpen.setDialogTitle("Upload Question");
			fileOpen.showOpenDialog(frame);
			File file=fileOpen.getSelectedFile();
            String s=file.getPath();
            try{
            	BufferedReader br=new BufferedReader(new FileReader(s));
            	String l;
            	int count=0;
            	
            	
            	//set syntax for rUp text area
            	l=br.readLine().trim();
            	if(l.equals("0"))
            		rUp.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            	else
            		rUp.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
            	
            	
            	//set syntax for rDown text area
            	l=br.readLine().trim();
            	if(l.equals("0"))
            		rDown.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
            	else
            		rDown.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
            	
               	
            	while(!((l=(br.readLine())).trim().equals("end")))
               		output.append(l+"\n");
               	output.setText(output.getText().trim());
            	
               	while((l=br.readLine())!=null){
            			
               		l=l.trim();   
               		if(l.equals("end"))
               		   count++;
               		else
               		   area[count].append(l+"\n");
                }
               	
               //indicates question uploaded successfully	
              	f0=true;
              	qUp.setEnabled(false);
              	
            	//everything uploaded, time to pause the application
              	if(f0 && f1 && f2)	
            	{
               		panel.setVisible(false);
               		output.setVisible(false);    
               		JOptionPane.showConfirmDialog(frame, "Go Techno", "Code Magnet", -1);         		
               		timer.start();
               		output.setVisible(true);
               		panel.setVisible(true);
            	}
              	
              	for(int i=0;i<maxLabels;i++)area[i].setText(area[i].getText().trim());
                for(int i=0;i<maxLabels;i++)area[i].append("\n");
                
            }
            catch(Exception ee){ee.printStackTrace();}
		}
	}
	
	class aUp1Listener implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileOpen=new JFileChooser();
			fileOpen.setDialogTitle("Upload Answer 1");
			fileOpen.showOpenDialog(frame);
			File file=fileOpen.getSelectedFile();
            String s=file.getPath();
            try{
            	BufferedReader br=new BufferedReader(new FileReader(s));
            	String l;
            	while((l=br.readLine())!=null)
        	 		answer1+=l;					//indexes start with 0
            	
            	answer1=answer1.replaceAll("\\s","");
        	
            	f1=true;
            	aUp1.setEnabled(false);
            	
            	if(f1&&f2)submit.setEnabled(true);
        	
            	//everything uploaded, time to pause the application
            	if(f0 && f1 && f2)	
            	{
            		panel.setVisible(false);
            		output.setVisible(false);    
            		JOptionPane.showConfirmDialog(frame, "Go Techno", "Code Magnet", -1);         		
            		timer.start();
            		output.setVisible(true);
            		panel.setVisible(true);
            	}
            }catch(Exception ee){ee.printStackTrace();}
		}
	}
	
	class aUp2Listener implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileOpen=new JFileChooser();
			fileOpen.setDialogTitle("Upload Answer 2");
			fileOpen.showOpenDialog(frame);
			File file=fileOpen.getSelectedFile();
            String s=file.getPath();
            try{
            	BufferedReader br=new BufferedReader(new FileReader(s));
            	String l;
            	while((l=br.readLine())!=null)
        	 		answer2+=l;					//indexes start with 0
            	
            	answer2=answer2.replaceAll("\\s","");
        	
            	f2=true;
            	aUp2.setEnabled(false);
            	
            	if(f1&&f2)submit.setEnabled(true);
        	
            	//everything uploaded, time to pause the application
            	if(f0 && f1 && f2)	
            	{
            		panel.setVisible(false);
            		output.setVisible(false);    
            		JOptionPane.showConfirmDialog(frame, "Go Techno", "Code Magnet", -1);         		
            		timer.start();
            		output.setVisible(true);
            		panel.setVisible(true);
            	}
            }catch(Exception ee){ee.printStackTrace();}
		}
	}


	class submitListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			boolean flag1=answer1.equals(rUp.getText().replaceAll("\\s",""));
			boolean flag2=answer2.equals(rDown.getText().replaceAll("\\s",""));
			
			if(flag1&&flag2)
			{
				JOptionPane.showMessageDialog(frame,"2 Correct!\nPlease call the organisers","Code Magnet",JOptionPane.INFORMATION_MESSAGE);
				timer.stop();
			}
				else if(flag1 || flag2)
				JOptionPane.showMessageDialog(frame,"Only 1 Correct!\n","Code Magnet",JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(frame,"inCorrect!","Code Magnet",JOptionPane.INFORMATION_MESSAGE);
	}
	}
	
	class timerJob implements Runnable{
		public void run(){
			while(true){
				time++;
				frame.setTitle("Code Magnet "+time);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}