package view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import impl.ScoreDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Score;

public class ScoreController implements Initializable {

	ScoreDAO sDAO = new ScoreDAO().getInstance();

	@FXML
	private Button btnSearch;
	@FXML
	private Button btnSearchAll;
	@FXML
	private Button btnHighScores;
	@FXML
	private Button btnReturn;

	@FXML
	private TextField txtSearch;

	@FXML
	private TableView<Score> scoreTable;
	@FXML
	private TableColumn<Score, Integer> columnSId;
	@FXML
	private TableColumn<Score, String> columnNick;
	@FXML
	private TableColumn<Score, Integer> columnScore;
	@FXML
	private TableColumn<Score, String> columnGDate;

	private Executor exec;

	public void search() {

		Task<ObservableList<Score>> task = new Task<ObservableList<Score>>() {

			@Override
			protected ObservableList<Score> call() throws Exception {
				List<Score> list = ScoreDAO.getInstance().selectOne(txtSearch.getText());
				ObservableList<Score> obsList = FXCollections.observableArrayList(list);
				return obsList;
			}

		};

		task.setOnFailed(e -> task.getException().printStackTrace());
		task.setOnSucceeded(e -> scoreTable.setItems((ObservableList<Score>) task.getValue()));

		Thread t1 = new Thread(task);
		t1.start();

//		exec.execute(task);

	}

	public void searchAll() {
		Task<ObservableList<Score>> task = new Task<ObservableList<Score>>() {

			@Override
			protected ObservableList<Score> call() throws Exception {
				List<Score> list = ScoreDAO.getInstance().selectAll();
				ObservableList<Score> obsList = FXCollections.observableArrayList(list);
				return obsList;
			}

		};

		task.setOnFailed(e -> task.getException().printStackTrace());
		task.setOnSucceeded(e -> scoreTable.setItems((ObservableList<Score>) task.getValue()));

		Thread t1 = new Thread(task);
		t1.start();
	}
	
	public void returnLogIn() {
		AnchorPane logInPage;
		try {
			logInPage=FXMLLoader.load(getClass().getResource("/view/LogIn.fxml"));
			Scene scene=new Scene(logInPage);
			
			Stage primaryStage=(Stage) btnSearch.getScene().getWindow();
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Log In");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		columnSId.setCellValueFactory(new  PropertyValueFactory("s_id"));
		columnNick.setCellValueFactory(new PropertyValueFactory("nick"));
		columnScore.setCellValueFactory(new PropertyValueFactory("score"));
		columnGDate.setCellValueFactory(new PropertyValueFactory("g_date"));
		
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
	}

}
