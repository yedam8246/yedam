package view;

import javafx.scene.control.Button;

import impl.ScoreDAO;

public class ScoreController {

	ScoreDAO scoreDAO=new ScoreDAO().getInstance();
	
	@FXML
	private Button btnSearch;
	
	
	
}
