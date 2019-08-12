package game;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{
	
	private Stage primaryStage;
	private AnchorPane startLayout;
	
	public static void main(String[] args) {
		launch(args);
		
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		primaryStage=stage;
		
		initStart();
		
	}
	
	public void initStart() {
		
		try {
			startLayout=FXMLLoader.load(getClass().getResource("../view/Start.fxml"));
			
			Scene scene=new Scene(startLayout);
			
			scene.getRoot().requestFocus();
			
			primaryStage.setTitle("2048");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
