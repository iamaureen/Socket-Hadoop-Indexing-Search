package tinyGoogle;

import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

//https://examples.javacodegeeks.com/desktop-java/swing/java-swing-boxlayout-example/

public class userInterface extends Frame implements ActionListener {
	
	   private Label lblInput;     // Declare input Label
	   private Label lblOutput;    // Declare output Label
	   private TextField tfInput;  // Declare input TextField
	   private TextField tfOutput; // Declare output TextField
	   private int sum = 0;        // Accumulated sum, init to 0
	   private javax.swing.JButton jButton_fileChoose;
	   private static javax.swing.JLabel jLabel_Image;
	   
	   static String pathname;
	   static String filename;
	   static String fullPathName;
	 
	   // Constructor to setup the GUI components and event handlers
	   public userInterface() {
		   
		   // Create and set up a frame window
			JFrame.setDefaultLookAndFeelDecorated(true);
			JFrame frame = new JFrame("BoxLayout Example X_AXIS");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			// Set the panel to add buttons
			JPanel panel = new JPanel();
			
			// Set the BoxLayout to be X_AXIS: from left to right
			BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
			
			// Set the Boxayout to be Y_AXIS from top to down
			//BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);

			panel.setLayout(boxlayout);
			
			// Set border for the panel
			panel.setBorder(new EmptyBorder(new Insets(150, 200, 150, 200)));	
			
			lblInput = new Label("Option (Index/Query): ");
			panel.add(lblInput);
			
			tfInput = new TextField(10); // Construct TextField
		    panel.add(tfInput);  
		    
		    lblOutput = new Label("Document Path ");
			panel.add(lblOutput);
			
			tfOutput = new TextField(40);
			panel.add(tfOutput);
			
			jButton_fileChoose = new javax.swing.JButton();
		    panel.add(jButton_fileChoose);
		      
		      //file chooser button -- choose text file
		        jButton_fileChoose.setText("Choose File");
		        jButton_fileChoose.addActionListener(new java.awt.event.ActionListener() {
		            public void actionPerformed(java.awt.event.ActionEvent evt) {
		            	jButtonFileChooseActionPerformed(evt);
		            }
	
					private void jButtonFileChooseActionPerformed(ActionEvent evt) {
						// TODO Auto-generated method stub
						
					JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 
				 
					int result = fileChooser.showOpenDialog(null);
				    	if (result == JFileChooser.APPROVE_OPTION) {
				    	    File selectedFile = fileChooser.getSelectedFile();
				    	    
				    	    fullPathName=selectedFile.getAbsolutePath();
				    	    pathname=selectedFile.getParent();
				    	    filename=selectedFile.getName();
				    	    
				    	    System.out.println("Selected file: " + selectedFile.getAbsolutePath()+ "\nfile name: " + selectedFile.getName());
				    	    tfOutput.setText(selectedFile.getAbsolutePath());
				    	}
						
					}
		        });
		        
				// Define new buttons
				JButton jb1 = new JButton("Send To Client");

				// Add buttons to the frame (and spaces between buttons)
				panel.add(jb1);
				
				jb1.addActionListener(this);
			
			// Set size for the frame
			//frame.setSize(300, 300);
			
			// Set the window to be visible as the default to be false
			frame.add(panel);
			frame.pack();
			frame.setVisible(true);

	   }
	   
	 
	   // The entry main() method
	   public static void main(String[] args) {
	      // Invoke the constructor to setup the GUI, by allocating an anonymous instance
	      new userInterface();
	   }
	 
	   // ActionEvent handler - Called back upon hitting "enter" key on TextField
	   @Override
	   public void actionPerformed(ActionEvent evt) {
	      System.out.println("send to client");
	   }

}
