package view;

import impl.UsersDAO;
import javafx.event.ActionEvent;
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

public class SignUpController {

	@FXML
	private Button btnSignUp;
	@FXML
	private Button btnCancel;

	@FXML
	private Label lbCheck;

	@FXML
	private TextField txtId;
	@FXML
	private PasswordField txtPw;
	@FXML
	private PasswordField txtCpw;
	@FXML
	private TextField txtNick;

	public void signUp(ActionEvent actionEvent) {

		try {

			Users usr = new Users();

			if (!txtPw.getText().equals(txtCpw.getText())) {

				lbCheck.setText("Password not matching");
				return;
			}

			usr = UsersDAO.getInstance().selectOne(txtId.getText());

			if (txtId.getText().equals(usr.getId())) {

				lbCheck.setText("ID already taken");
				return;
			}

			usr = UsersDAO.getInstance().selectOneNick(txtNick.getText());

			if (txtNick.getText().equals(usr.getNick())) {
				lbCheck.setText("Nickname already taken");
				return;
			}

			if (txtPw.getText() == null || txtPw.getText().isEmpty()) {
				lbCheck.setText("Password field empty");
				return;
			}

			if (txtId.getText() == null || txtId.getText().isEmpty()) {
				lbCheck.setText("ID field empty");
				return;
			}

			if (txtNick.getText() == null || txtNick.getText().isEmpty()) {
				lbCheck.setText("Nickname field empty");
				return;
			}

				usr.setId(txtId.getText());
				usr.setPw(txtPw.getText());
				usr.setNick(txtNick.getText());

				UsersDAO.getInstance().signUp(usr);

				AnchorPane logInPage;
				
				logInPage=FXMLLoader.load(getClass().getResource("/view/LogIn.fxml"));
				
				Scene scene=new Scene(logInPage);
				
				Stage primaryStage=(Stage) btnSignUp.getScene().getWindow();
				
				primaryStage.setScene(scene);
				
				
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
