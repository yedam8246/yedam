package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class StartController implements Initializable {

	@FXML
	private ImageView start; // 시작 버튼 선언
	
	@FXML
	public void start(MouseEvent event) {
		
			System.out.println("clicked");
		
		
	}
	
	
	
	@FXML
	public void key(KeyEvent evt) {
		
		if(evt.getCode()==KeyCode.RIGHT) {
			start.setX(start.getX()+10);
		}else if(evt.getCode()==KeyCode.LEFT) {
			start.setX(start.getX()-10);
		}
	}
	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
