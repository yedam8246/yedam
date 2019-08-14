package view;

import java.io.IOException;

import game.Game;
import impl.UsersDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Users;

public class LogInController {

	public Button getLogIn() {
		return logIn;
	}

	public void setLogIn(Button logIn) {
		this.logIn = logIn;
	}

	@FXML
	private TextField txtId;
	@FXML
	private PasswordField txtPw;

	@FXML
	private Button logIn;
	@FXML
	private Button signUp;
	
	@FXML
	private Label alert;

	public void logIn() {
//		Users usr = new Users();
//
//		usr = UsersDAO.getInstance().selectOne(txtId.getText());
//
//		if (!usr.getPw().equals(txtPw.getText())) {
//			return;
//		}
		
		
		AnchorPane gamePage;
		try {
			gamePage = FXMLLoader.load(getClass().getResource("/view/Game.fxml"));
			Scene scene = new Scene(gamePage);
			
			Stage primaryStage = (Stage) logIn.getScene().getWindow();
			
			primaryStage.setScene(scene);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	public void signUpPage() {
		try {
			AnchorPane signUpPage = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
			Scene scene=new Scene(signUpPage);
			
			Stage primaryStage = (Stage) signUp.getScene().getWindow();
			
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
