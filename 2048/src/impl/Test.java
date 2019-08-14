package impl;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Test extends Application {
	
	private Stage primaryStage;
	private Scene scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage=primaryStage;
		logInPage();
		
		primaryStage.show();
		
//		AnchorPane signUpPage = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
//		
//		Scene signUpScene=new Scene(signUpPage);
//		
//		primaryStage.setTitle("SignUp");
//		primaryStage.setScene(signUpScene);
//		primaryStage.setResizable(false);
//		primaryStage.show();
		
	}

	public void signUpPage() {
		try {
			
			AnchorPane signUpPage = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
			
			scene.setRoot(FXMLLoader.load(getClass().getResource("/view/LogIn.fxml")));

			
//			primaryStage.setResizable(false);
//			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void logInPage() {
		
		try {
			AnchorPane logInPage=FXMLLoader.load(getClass().getResource("/view/LogIn.fxml"));
			
			scene=new Scene(logInPage);
			
			primaryStage.setTitle("SignUp");
			primaryStage.setScene(scene);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
