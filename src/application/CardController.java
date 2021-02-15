package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class CardController implements Initializable {
	public Generator card=null;
	
	private String totalCount,errorCount;

	public 	int SuccessCountN=0;
	
  	public boolean isRunning=false;

  	int ii=0;
  	
  	public Thread thread;

  	boolean isBarSelected=false;
  	boolean isQRSelected=false;


  	public boolean isBarCode=true;
  	public boolean isQrCode=true;
  	public String directoryName,Acc_id;

  
	@FXML
	public Button generateBtn;
	
	@FXML
	private Button reportBtn;
	
	@FXML
	public Button folderBtn;
	
	@FXML 
	public Text total;
	
	@FXML 
	public Text error;
	
	@FXML 
	public Text success;
	
	@FXML
	private ImageView cardImg;
	
	@FXML 
	private RadioButton BAR;
	
	@FXML 
	private RadioButton QR;
	
	@FXML 
	public Text ID,Name,acc_id,barStatus,qrStatus;

	
	
	private  Stage myStage;
	public void  setStage(Stage stage) {
	     myStage = stage;
	}
	

	@FXML
	protected void generate() throws MalformedURLException
	{
		
		
		isBarSelected=BAR.isSelected();
		isQRSelected=QR.isSelected();
		        	
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    
		    }
		});
			
			
		//For Better Performance Starting Card Generation Process in a separate Thread	   
		Thread thread = new Thread(new CardGenerator(this,isBarSelected, isQRSelected, directoryName,
				success,error,total,
				ID,Name,acc_id,barStatus,
				qrStatus,cardImg,generateBtn));
		
		thread.start();
	
		
	
	       
	}
	
	
	@FXML
	protected void setFolder()
	{
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Please Choose the data folder");
		File defaultDirectory = new File("C:\\");
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(myStage);
		
		if(selectedDirectory  != null)
		{
			folderBtn.setText(selectedDirectory.getAbsolutePath());
			
			directoryName=selectedDirectory.getAbsolutePath();

			generateBtn.setDisable(false);
			
		}else
			System.out.print("No Folder Selected !");
			
		
		
	}
	

	
	public String getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}


	public String getErrorCount() {
		return errorCount;
	}


	public void setErrorCount(String errorCount) {
		this.errorCount = errorCount;
	}

	
	public void updateTotal()
	{
		total.setText(totalCount);
	}
	
	public void updateSuccesCount(String c)
	{
		
	 StringProperty value = new SimpleStringProperty("0");
	 value.setValue(String.valueOf(c));
	 success.textProperty().bind(value);;

	}
	
	public void updateErrorCount()
	{
		error.setText(errorCount);
	}
	
	public void setID(String iD) {
		ID.setText(iD);
		
	
	}

	public void setName(String name) {
		Name.setText(name);
	}


	public void setBarStatus(String barStatus) {
		this.barStatus.setText(barStatus);
	}


	public void setQrStatus(String qrStatus) {
		this.qrStatus.setText(qrStatus);
	}

	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		 
		 
	
	}
	
	
	  







		
	
	
}
