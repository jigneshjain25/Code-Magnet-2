import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.DefaultCaret;

import org.fife.ui.rtextarea.RTextScrollPane;

public class Main{
	
	String userName=null;
	File userFile1,userFile2;
	
	//for java 7
	JTextArea output=new JTextArea(65,5);
	
	//for java 6
	//	JTextArea output=new JTextArea(450,5);
	
	int maxLabels=100;

	JMenuBar menuBar=new JMenuBar();
	JMenu optionMenu=new JMenu("Options");
	JMenuItem qUp=new JMenuItem("Upload Question");
	JMenuItem submit=new JMenuItem("Submit");
	
	JFrame frame=new JFrame("Code Magnet");
	
	//panel contains of all question text areas
	JPanel panel=new JPanel(new GridLayout(maxLabels,1,0,1));
	
	//rightPanel contains the two RSyntax text areas i.e entire right part
	JPanel rightPanel=new JPanel();
	
	//leftPanel contains output and question text areas i.e entire left part
	JPanel leftPanel=new JPanel();
	
	JTextArea area[]=new JTextArea[maxLabels];
	
	RSTA rUp=new RSTA(3,53);
	RSTA rDown=new RSTA(5,53);
	
	JScrollPane lUpScroller = new JScrollPane(output);
	JScrollPane lDownScroller = new JScrollPane(panel);  
	
	RTextScrollPane rUpscroller = new RTextScrollPane(rUp);
	RTextScrollPane rDownscroller = new RTextScrollPane(rDown);
	
	int time=0;
	Thread timer=new Thread(new timerJob(this));
		
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
		    		System.out.println("Error setting the look");
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
		rUp.setText("//First question goes here\n");
		
		rDown.setAnimateBracketMatching(true);
		rDown.setAutoIndentEnabled(true);
		rDown.setFont(new Font("Courier New",Font.PLAIN,14));
		rDown.setText("//Second question goes here\n");

		rUpscroller.getVerticalScrollBar().setUnitIncrement(16);
		rDownscroller.getVerticalScrollBar().setUnitIncrement(16);		
		
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(rUpscroller);
		rightPanel.add(rDownscroller);
		
		qUp.addActionListener(new qUpListener(this));
		submit.addActionListener(new submitListener(this));
		
		output.setEditable(false);
    	output.setDragEnabled(false);
    	output.setFont(new Font("Courier New",Font.BOLD,16));
    	
    	DefaultCaret caret1 = (DefaultCaret)output.getCaret();
		caret1.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
    	
		for(int i=0;i<maxLabels;i++){
			
			area[i]=new JTextArea(3,55);
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
		
		//diable typing - allow only enter and backspace
		rUp.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e1){
					char c=e1.getKeyChar();
					if ( (c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_ENTER) ) 
				         e1.consume();  // ignore event
				}			
			});
		rDown.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e1){
				char c=e1.getKeyChar();
				if ( (c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_ENTER) ) 
			         e1.consume();  // ignore event
			}			
		});
		
		submit.setEnabled(false);
		
		optionMenu.add(qUp);
		optionMenu.add(submit);
		menuBar.add(optionMenu);
		frame.setLocation(30,5);
	//	frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(950, 720);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setJMenuBar(menuBar);
	
		frame.getContentPane().add(BorderLayout.EAST,leftPanel);
		frame.getContentPane().add(BorderLayout.WEST,rightPanel);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
}