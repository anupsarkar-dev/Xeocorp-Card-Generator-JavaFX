package application;
	
import java.awt.Button;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	
	@FXML
	private Button button;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		
			BorderPane root = null;	
			root = (BorderPane)FXMLLoader.load(getClass().getResource("UI.fxml"));

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
			try
			{
				String logo =getClass().getResource("/card_templates/xeocorp.png").toString();
				primaryStage.setTitle("Xeocorp Bulk Business Card  Generator ");
			    primaryStage.getIcons().add(new Image(logo));
				primaryStage.setScene(scene);
				
			}catch(Exception e)
			{
				System.out.println("File Not Found or Incorrect Path!");
			}
		 						
			CardController controller=new CardController();			
			controller.setStage(primaryStage);			
			primaryStage.show();
			Platform.setImplicitExit(false);
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent t) {
			        Platform.exit();
			        System.exit(0);
			    }
			});
			
      
		
	}
	
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
