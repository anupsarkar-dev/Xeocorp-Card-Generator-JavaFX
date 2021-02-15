package application;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLEncoder;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class CardGenerator implements Runnable {
	
	private static StringBuilder reportBuilder;
	public String directoryName , Acc_id;
	
	public int 	   incrementRate;
	public boolean isBarCode=true;
	public boolean isQrCode=true;
	
	CardController controller;
	Text error;
	Text total;
	
	Button generateBtn;
	ImageView cardView;
 
	Text suc;
	Text ID;
	Text Name;
	Text acc_id2;
	Text barStatus;
	Text qrStatus;
	
	
	  	
	//Constructor
	public CardGenerator(CardController cardController,boolean isBarCode, boolean isQrCode,  String directoryName, Text success,Text error, Text total, Text ID, Text Name, Text acc_id2, Text barStatus, Text qrStatus, ImageView cardView, Button generateBtn) 		
	{
		// Setting Default Values	
		this.controller		= cardController;
		this.isBarCode 		= isBarCode;
		this.isQrCode 		= isQrCode;
		this.directoryName 	= directoryName;
		this.suc			= success;		
		this.error			= error;
		this.total			= total;		
		this.ID				= ID;		
		this.Name			= Name;	
		this.acc_id2		= acc_id2;		
		this.cardView		= cardView;
		this.generateBtn	= generateBtn;
		
		suc.setText("000");
		error.setText("000");
		total.setText("002");
		ID.setText("000001");
		Name.setText("No Name");
		acc_id2.setText("00002");
	}


	public  void init() throws MalformedURLException, IOException {

		Generator card;	
		reportBuilder=new StringBuilder();
		
		File file = new File(directoryName);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
				
		//System.out.println(Arrays.toString(directories));

		directoryName = directoryName+"\\";
		
		int size	  = directories.length;
		
		incrementRate = 100 / size;
		
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
	
				total.setText(Integer.toString(size));							
         		generateBtn.setText("GENERATING...");
         		generateBtn.setDisable(true);
			}
	
			});
		
		
		String FileName			= "";
		int SuccsessCount		= 0;
		int FailtureCount		= 0;
		ArrayList<String> text	= new ArrayList<String>();
		
		reportBuilder.append("\n###########Report ###########\n\n");
		
		for(int i=0;i <size;i++)
		{
			//System.out.println(directories[i]);

			FileName=directories[i];

			//System.out.println(directoryName+directories[i] +"\\"+FileName + ".txt");
			
			String line = null;
			try {
					BufferedReader br = new BufferedReader(new FileReader( directoryName + directories[i] + "\\" + FileName + ".txt")) ;
					StringBuilder sb  = new StringBuilder();
					
					line = br.readLine();
									
					while (line != null) {
						
						text.add(line);						
						//System.out.println(line);						
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
					}
					
					String everything 	= sb.toString();
					String Folder		= FileName;				
					Acc_id				= text.get(5);
					int n				= Acc_id.length();			
					Acc_id				= Acc_id.substring(8, n);
										
					reportBuilder.append("\n*************** "+i+" *****************\n");
					reportBuilder.append("MEMBERSHIP NO : " +	Folder)	;
					reportBuilder.append("\nStatus:\n" );
					reportBuilder.append("--------\n" );
	
					String imageUrl;
					String destinationFile;
											
					if(isBarCode)
					{
						
						destinationFile  = directoryName + Folder + "\\bar-" + FileName + ".jpg";	
						
						//bar code API
						//imageUrl		 = "http://www.barcodes4.me/barcode/c128b/" + Acc_id + ".jpg?width=290&height=100";
											
						//Generate_BAR_Code(imageUrl, destinationFile,"BAR");
						
						Generate_BAR_Code_Local(290, 100, Acc_id, destinationFile,"BAR");
						
					}				
			    	
					if(isQrCode)
					{
						destinationFile = directoryName + Folder + "\\qr-" + FileName + ".png";	
						
						//QR code API
						//imageUrl 		= "https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=" + URLEncoder.encode(everything, "UTF-8");
										
						//Generate_Code(imageUrl, destinationFile,"QR");
						
						Generate_QR_Code_Local(180, 180, everything, destinationFile,"QR");
						
					}
 	
					
					String photo 	= Folder + "\\"+Folder+".jpg";				
					String bar 		= Folder + "\\bar-"+FileName+".jpg";
					String qr 		= Folder + "\\qr-"+FileName+ ".png";					
					//System.out.println("photo: "+ photo);
					
					card			= new Generator(photo, bar, qr, text, Folder, directoryName, cardView);
			
					if(card.generateCard())
					{
						SuccsessCount++;
						suc.setText(Integer.toString(SuccsessCount));					
						ID.setText(FileName);
						Name.setText(text.get(0));
						acc_id2.setText(Acc_id);	
						reportBuilder.append("Success : Card Generated &  saved !\n");						
					}
					else {
						FailtureCount++;
						error.setText(Integer.toString(FailtureCount));
						ID.setText(FileName);
						Name.setText(text.get(0));
						acc_id2.setText(Acc_id);	
						reportBuilder.append("Error : Card Generation Failed  !\n");
					}
 
					incrementRate+=incrementRate;
					text.clear();

				} catch (IOException e) {
				
					e.printStackTrace();
	
					reportBuilder.append("File Not Found : " + directories[i]+".txt");
			}

			reportBuilder.append("\n*********************************\n\n");

			
		}
		
		card=null;
		
		reportBuilder.append("\n###########Report ###########\n\n");
		reportBuilder.append("Success\t : \t " + SuccsessCount + "\n");
		reportBuilder.append("Failed\t : \t " + FailtureCount +"\n\n");
		
		reportBuilder.append("############################");
		
		JTextArea msg = new JTextArea(reportBuilder.toString());
		msg.setLineWrap(true);
		
		msg.setWrapStyleWord(true);
		msg.setMargin(new Insets(5,5,5,5));
				
		JScrollPane scrollPane 	= new JScrollPane(msg);
		scrollPane.setPreferredSize(new Dimension(350, 900));
		JOptionPane.showMessageDialog(null, scrollPane);
		
		System.out.println(reportBuilder.toString());
		
		Platform.runLater(new Runnable(){

			@Override
			public void run() {		
         		generateBtn.setText("GENERATE CARD");
         		generateBtn.setDisable(false);
			}
	
			});
	}

	
	public static void Generate_Code_Online(String imageUrl, String destinationFile,String mode)  {

		URL url;
		try {
			
			url 			= new URL(imageUrl);
			InputStream is 	= url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);
			byte[] b 		= new byte[2048];
			int length;
			
			//System.out.println("\n Destination :  " + destinationFile);

			while ((length = is.read(b)) != -1) 
			{
				os.write(b, 0, length);
			}

			reportBuilder.append(mode + "  : Downloaded !\n"  );

			is.close();
			os.close();

		} catch (MalformedURLException e) {
		
			e.printStackTrace();
			
		} catch (IOException e) {

			reportBuilder.append("QR Code Error: Download Failed. \n ");

			e.printStackTrace();
		}

	}
	
	public static void Generate_QR_Code_Local(int width,int height, String text, String destinationFile,String mode) throws IOException  
	{

			 
			OutputStream os = new FileOutputStream(destinationFile);
			
			try {
				
				CodeGenerator.writeQRCodeImage(text, width, height, os);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			os.close();

		

	}
	public static void Generate_BAR_Code_Local(int width,int height, String text,  String destinationFile,String mode)  {

		
		try {
			
		 
			OutputStream os = new FileOutputStream(destinationFile);
		 
			try {
				
				CodeGenerator.writeBarcode(text, width, height, os);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			reportBuilder.append( mode + " : Generated & Saved !\n"  );

		 
			os.close();


		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			reportBuilder.append(mode + " Error: Download Failed ! \n");
			e.printStackTrace();
		}


	}


	public static void Generate_BAR_Code_Online(String imageUrl, String destinationFile,String mode)  {

		
		try {
			
			URL    url 		= new URL(imageUrl);
			InputStream is  = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);
			byte[] b 		= new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			reportBuilder.append( mode + " : Downloaded !\n"  );

			is.close();
			os.close();


		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			reportBuilder.append(mode + " Error: Download Failed ! \n");
			e.printStackTrace();
		}


	}


	@Override
	public void run() {
	 
		try {
			
			init();
			
		} catch (MalformedURLException e) {	
			
			e.printStackTrace();
			
		} catch (IOException e) {
		 
			e.printStackTrace();
		}
		
	}
}







