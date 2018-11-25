package tinyGoogle;

import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

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
	      setLayout(new FlowLayout());
	         // "super" Frame (container) sets layout to FlowLayout, which arranges
	         // the components from left-to-right, and flow to next row from top-to-bottom.
	 
	      lblInput = new Label("Option (Index/Query)"); // Construct Label
	      add(lblInput);               // "super" Frame container adds Label component
	 
	      tfInput = new TextField(10); // Construct TextField
	      add(tfInput);                // "super" Frame adds TextField
	 
	      tfInput.addActionListener(this);
	         // "tfInput" is the source object that fires an ActionEvent upon entered.
	         // The source add "this" instance as an ActionEvent listener, which provides
	         //  an ActionEvent handler called actionPerformed().
	         // Hitting "enter" on tfInput invokes actionPerformed().
	 
	      lblOutput = new Label("Document Path ");  // allocate Label
	      add(lblOutput);               // "super" Frame adds Label
	      
	       
	      tfOutput = new TextField(10); // allocate TextField
	      tfOutput.setEditable(false);  // read-only
	      add(tfOutput);                // "super" Frame adds TextField
	      
	      jButton_fileChoose = new javax.swing.JButton();
	      add(jButton_fileChoose);
	      
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
	 
	      setTitle("Client");  // "super" Frame sets title
	      setSize(350, 120);  // "super" Frame sets initial window size
	      setVisible(true);   // "super" Frame shows
	   }
	   
	 
	   // The entry main() method
	   public static void main(String[] args) {
	      // Invoke the constructor to setup the GUI, by allocating an anonymous instance
	      new userInterface();
	   }
	 
	   // ActionEvent handler - Called back upon hitting "enter" key on TextField
	   @Override
	   public void actionPerformed(ActionEvent evt) {
	      // Get the String entered into the TextField tfInput, convert to int
	      int numberIn = Integer.parseInt(tfInput.getText());
	      sum += numberIn;      // Accumulate numbers entered into sum
	      tfInput.setText("");  // Clear input TextField
	      tfOutput.setText(sum + ""); // Display sum on the output TextField
	                                  // convert int to String
	   }

}
