package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import game.GenerateSquares;
import impl.ScoreDAO;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController implements Initializable{
	@FXML
	AnchorPane anchorPane;
	
	static GenerateSquares[][] board = new GenerateSquares[4][4]; // Creates a 4x4 array, which is the game board.
	static GenerateSquares[][] boardRotated = new GenerateSquares[4][4];// Creates a 4x4 array, which is the game board
																		// that rotates.

	static int[] scoreCounter = new int[11]; // Creates a 1x1 array to hold score.
	static int[] moveCounter = new int[11]; // Creates a 1x1 array to hold the move number.
	static int[][][] backUpBoard = new int[10][4][4]; // Creates the back up board to undo a move (Up to ten moves).
	static boolean keepPlayingTheGame = false; // This variable prevents from duplication code that was needed in my
												// original board.
												// It is used to prevent the game from popping up the congratulations
												// window everytime
												// a 2048 tile is on the board.

	static File saveGame = new File("2048.dat"); // Creates a file to save the game.
	static Label score = new Label(); // Made static so that once you load the old game, loadTheLastGame() can see
										// this label
										// and can place the old score at the top of the game.

	static Label count = new Label(); // Made static so that once you load the old game, loadTheLastGame() can see
										// this label
										// and can place the old count at the top of the game.


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		// Creates title of the game which is 2048.
				Label titleOfGame = new Label("2048 ");
				titleOfGame.setFont(Font.font("Calibri", FontWeight.BOLD, 50));

				// Creates quick description on how to play the game. This is what the game has.
				Label description = new Label("Join the numbers and get to the 2048 tile!");
				description.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

				// Sets the font and alignment for the score Label.
				score.setFont(Font.font("Calibri", FontWeight.BOLD, 25));
				score.setText("Score\n" + scoreCounter[0]);
				score.setTextAlignment(TextAlignment.CENTER);

				// Creates score box in the shape of a rectangle.
				Rectangle scoreBox = new Rectangle(140, 90);
				scoreBox.setFill(Color.GRAY);
				scoreBox.setArcWidth(10);
				scoreBox.setArcHeight(10);

				// Adds the score label on top of the scoreBox rectangle.
				StackPane scoreBoxAndTitle = new StackPane();
				scoreBoxAndTitle.getChildren().addAll(scoreBox, score);
				score.setAlignment(Pos.CENTER);

				// Sets the font and alignment for the count Label.
				count.setFont(Font.font("Calibri", FontWeight.BOLD, 25));
				count.setText("Moves\n" + moveCounter[0]);
				count.setTextAlignment(TextAlignment.CENTER);

				// Creates count box.
				Rectangle countBox = new Rectangle(140, 90);
				countBox.setFill(Color.GRAY);
				countBox.setArcWidth(10);
				countBox.setArcHeight(10);

				// Adds the count box on top of the countBox rectangle.
				StackPane countBoxAndTitle = new StackPane();
				countBoxAndTitle.getChildren().addAll(countBox, count);
				count.setAlignment(Pos.CENTER);

				// Creates a BorderPane to place the title of the game '2048' on the left
				// margin, the number of moves StackPane in the center,
				// the running score StackPane in the right margin, and the small how to play
				// description label in the bottom.
				BorderPane titles = new BorderPane();
				titles.setLeft(titleOfGame);
				titles.setCenter(countBoxAndTitle);
				titles.setRight(scoreBoxAndTitle);
				titles.setBottom(description);
				titles.setPadding(new Insets(0, 0, 0, 0));
				// T,R,B,L

				// Creates 4 buttons that displays how the game works, exits the game, saves the
				// game or loads the game. setFocusTraversable to false prevents the
				// focus from highlighing the buttons. This, in turn, keeps the focus just on
				// the game board.
				Button helpButton = new Button("Help");
				helpButton.setFocusTraversable(false);
				Button exitButton = new Button("Exit");
				exitButton.setFocusTraversable(false);
				Button loadButton = new Button("Load");
				loadButton.setFocusTraversable(false);
				Button saveButton = new Button("Save");
				saveButton.setFocusTraversable(false);

				// Puts all of the ALT buttons in an HBox on the bottom of the stage.
				HBox buttons = new HBox(115);
				buttons.getChildren().addAll(helpButton, exitButton, loadButton, saveButton);

				// Puts the 'titles' BorderPane in the top of this BorderPane and the HBox with
				// the the ALT buttons do at the bottom. The buttons are center aligned.
				BorderPane background = new BorderPane();
				background.setTop(titles);
				background.setBottom(buttons);
				buttons.setAlignment(Pos.CENTER);
				background.setPadding(new Insets(20, 20, 20, 20));
				// T,R,B,L

				// Creates a GridPane of the board. This is the dark gray background and light
				// gray squares that do not move.
				GridPane gridOfSquares = new GridPane();
				gridOfSquares.setStyle("-fx-background-color: #BBADA0; -fx-background-radius: 15,15,15,15;");
				gridOfSquares.setAlignment(Pos.CENTER);
				gridOfSquares.setPadding(new Insets(18, 18, 18, 18));
				// T,R,B,L
				gridOfSquares.setHgap(18);
				gridOfSquares.setVgap(18);
				for (int c = 0; c < 4; c++)
					for (int r = 0; r < 4; r++) {
						GenerateSquares board = new GenerateSquares();
						gridOfSquares.add(board, c, r); // Gridpanes are [column] by [row]
					}

				// Creates another GridPane that takes the 16 squares and places their reference
				// into a 4x4 array so that the numbers can be manipulated later on.
				GridPane topGridOfSquares = new GridPane();
				topGridOfSquares.setAlignment(Pos.CENTER);
				topGridOfSquares.setPadding(new Insets(18, 18, 18, 18));
				// T,R,B,L
				topGridOfSquares.setHgap(18);
				topGridOfSquares.setVgap(18);
				for (int c = 0; c < 4; c++)
					for (int r = 0; r < 4; r++) {
						GenerateSquares frontBoard = new GenerateSquares();
						topGridOfSquares.add(frontBoard, c, r); // Gridpanes are [column] by [row]
						frontBoard.setColorsOfSquares();
						board[r][c] = frontBoard;
					}

				// Generates 2 random numbers and puts them on the board.
				fillAnEmptySquare();
				fillAnEmptySquare();

				// Adding the background grid with the dark gray color and the light gray
				// squares and grid that will have the future tiles that will be moving.
				StackPane stackOfSquares = new StackPane();
				stackOfSquares.getChildren().addAll(gridOfSquares, topGridOfSquares);
				background.setCenter(stackOfSquares);
				
				anchorPane.getChildren().add(background);

				// When the user clicks on the help button, the helpingHand window is displayed.
				helpButton.setOnAction(e -> {
					helpingHandScreen();
				});

				// When the user clicks on the exit button, the final score and move counts, and
				// game over window is displayed.
				exitButton.setOnAction(e -> {
					finalScoresScreen();
				});

				// When the user clicks on the save button, the current score, move count, and
				// game board will be saved.
				saveButton.setOnAction(e -> {
					saveTheCurrentGame();
				});

				// When the user clicks on the load button, the previous game's score, move
				// count and status of the board is loaded.
				loadButton.setOnAction(e -> {
					loadTheLastGame();
				});

				anchorPane.requestFocus(); // The GridPane requests focus.

				// Checks the keyboard for a button click from the user. Moves UP, DOWN, LEFT,
				// RIGHT, displays the help windows, exits the game,
				// loads an old game, saves the current game and undos a move (up to 10 moves).
				anchorPane.setOnKeyPressed(e -> {
					switch (e.getCode()) {
					case DOWN:
						noRotation();
						if (isMovementPossible()) // Checks to see if the player can move down.
						{
							backUpBoard();
							makeMovement(); // If possible, then we will move down.
							noRotationBack();
							fillAnEmptySquare(); // Generates a random 2 or 4 in a square.
							score.setText("Score\n" + scoreCounter[0]); // Updates the running score of the game.
							count.setText("Moves\n" + moveCounter[0]); // Updates the running move count of the game.
							if (isWinner()) // Checks to see if the user has a 2048 tile.
							{
								winnerScreen(); // If the user has a 2048 tile, then they will be asked if they would like to
												// continue or quit.
							}
							checkToSeeIfPlayIsAvailable(); // Checks to see if there are any moves available on the board, and
															// if there aren't
															// the final score and move count will be displayed, along with the
															// game board 5 seconds
															// later, and after 2 seconds, the game closes.
						}
						break;

					case UP:
						rotateTheBoard180Degrees();
						if (isMovementPossible()) // Checks to see if the player can move up.
						{
							backUpBoard(); // Backs up the board, score, and moves.
							makeMovement();
							rotateTheBoardBack180Degrees(); // If possible, then we will move up.
							fillAnEmptySquare(); // Generates a random 2 or 4 in a square.
							score.setText("Score\n" + scoreCounter[0]); // Updates the running score of the game.
							count.setText("Moves\n" + moveCounter[0]); // Updates the running move count of the game.
							if (isWinner()) // Checks to see if the user has a 2048 tile.
							{
								winnerScreen(); // If the user has a 2048 tile, then they will be asked if they would like to
												// continue or quit.
							}
							checkToSeeIfPlayIsAvailable(); // Checks to see if there are any moves available on the board, and
															// if there aren't
															// the final score and move count will be displayed, along with the
															// game board 5 seconds
															// later, and after 2 seconds, the game closes.
						}
						break;

					case LEFT:
						rotateTheBoard270Degrees();
						if (isMovementPossible()) // Checks to see if the player can move left.
						{
							backUpBoard(); // Backs up the board, score, and moves.
							makeMovement();
							rotateTheBoardBack270Degrees(); // If possible, then we will move left.
							fillAnEmptySquare(); // Generates a random 2 or 4 in a square.
							score.setText("Score\n" + scoreCounter[0]); // Updates the running score of the game.
							count.setText("Moves\n" + moveCounter[0]); // Updates the running move count of the game.
							if (isWinner()) // Checks to see if the user has a 2048 tile.
							{
								winnerScreen(); // If the user has a 2048 tile, then they will be asked if they would like to
												// continue or quit.
							}
							checkToSeeIfPlayIsAvailable(); // Checks to see if there are any moves available on the board, and
															// if there aren't
															// the final score and move count will be displayed, along with the
															// game board 5 seconds
															// later, and after 2 seconds, the game closes.
						}
						break;

					case RIGHT:
						rotateTheBoard90Degrees();
						if (isMovementPossible()) // Checks to see if the player can move right.
						{
							backUpBoard(); // Backs up the board, score, and moves.
							makeMovement();
							rotateTheBoardBack90Degrees(); // If possible, then we will move right.
							fillAnEmptySquare(); // Generates a random 2 or 4 in a square.
							score.setText("Score\n" + scoreCounter[0]); // Updates the running score of the game.
							count.setText("Moves\n" + moveCounter[0]); // Updates the running move count of the game.
							if (isWinner()) // Checks to see if the user has a 2048 tile.
							{
								winnerScreen(); // If the user has a 2048 tile, then they will be asked if they would like to
												// continue or quit.
							}
							checkToSeeIfPlayIsAvailable(); // Checks to see if there are any moves available on the board, and
															// if there aren't
															// the final score and move count will be displayed, along with the
															// game board 5 seconds
															// later, and after 2 seconds, the game closes.
						}
						break;

					case Z:
						if (e.isControlDown()) {
							undoBoard(); // Undos the board (Up to 10 times.)
							score.setText("Score\n" + scoreCounter[0]); // Displays the current score.
							count.setText("Moves\n" + moveCounter[0]); // Displays the current number of moves executed.
						}
						break;

					case H:
						if (e.isAltDown())
							helpingHandScreen(); // If ALT + H is pressed then a window appears telling the user more
													// information
													// on how to play the game and what all the buttons do.
						break;

					case L:
						if (e.isAltDown()) // Loads the last game that was saved.
							loadTheLastGame();
						break;

					case S:
						if (e.isAltDown()) // Saves the current game.
							saveTheCurrentGame();
						break;

					case X:
						if (e.isAltDown()) {
							finalScoresScreen(); // Displays the final score and move count before the game is closed.
						}
						break;

					}// End of switch statement.

				});// End of topGridOfSquares.setOnKeyPressed

		
	}
	
	static void fillAnEmptySquare() {
		final double PROBABILITY_2 = .9;
		int count = 0;
		for (int r = 0; r <= 3; r++)
			for (int c = 0; c <= 3; c++)
				if (board[r][c].getValueOfSquare() == 0)
					count++; // 1.) Counts the number of empty squares on the board.

		int locationNumber = 0;
		int[][] emptyLocations = new int[count][2]; // Creates an array of count by 2.
		for (int r = 0; r <= 3; r++)
			for (int c = 0; c <= 3; c++)
				if (board[r][c].getValueOfSquare() == 0) {
					emptyLocations[locationNumber][0] = r;
					emptyLocations[locationNumber][1] = c;
					locationNumber++;
				} // 2.) Generates a list of the empty squares.
		int cell = (int) (Math.random() * count); // 3.) Picks one of the locations on the new array that references the
													// game board.

		int r = emptyLocations[cell][0];
		int c = emptyLocations[cell][1];

		if (Math.random() > PROBABILITY_2) // 4.) In the chosen location, place a 4 and bloom it.
		{
			board[r][c].setValueOfSquare(4);

			ScaleTransition scaleTransitionBloom4 = new ScaleTransition(Duration.millis(250), board[r][c]);
			scaleTransitionBloom4.setFromX(.01);
			scaleTransitionBloom4.setToX(1.0);
			scaleTransitionBloom4.setFromY(.01);
			scaleTransitionBloom4.setToY(1.0);
			scaleTransitionBloom4.play();
		} else // 4.) In the chosen location, place a 2 and bloom it.
		{
			board[r][c].setValueOfSquare(2);

			ScaleTransition scaleTransitionBloom2 = new ScaleTransition(Duration.millis(250), board[r][c]);
			scaleTransitionBloom2.setFromX(.01);
			scaleTransitionBloom2.setToX(1.0);
			scaleTransitionBloom2.setFromY(.01);
			scaleTransitionBloom2.setToY(1.0);
			scaleTransitionBloom2.play();
		}

		for (r = 0; r <= 3; r++) // Based on whether the number is a 2 or a 4, that specific number and its
									// corresponding
			for (c = 0; c <= 3; c++) // color will be placed on the GUI.
			{
				board[r][c].displayNumber();
				board[r][c].setColorsOfSquares();

			}

	} // End of fillAnEmptySquare.

	// Checks to see if moving down is a valid move. It checks all of the columns in
	// every row.
	static boolean isMovementPossible() {
		for (int c = 0; c <= 3; c++) // Checks all of the columns.
		{
			int row0 = boardRotated[0][c].getValueOfSquare(); // Value one is in row 0 column c.
			int row1 = boardRotated[1][c].getValueOfSquare(); // Value two is in row 1 column c.
			int row2 = boardRotated[2][c].getValueOfSquare(); // Value three is in row 2 column c.
			int row3 = boardRotated[3][c].getValueOfSquare(); // Value four is in row 3 column c.

			if (!((row0 == 0 && row1 == 0 && row2 == 0 && row3 == 0) || // If all squares are zeros.
					(row0 == 0 && row1 == 0 && row2 == 0 && row3 != 0) || // If squares 1-3 are zeros and 4 isn't.
					(row0 == 0 && row1 == 0 && row2 != 0 && row3 != 0 && row2 != row3) || // If squares 1 & 2 are zeros
																							// and 3 & 4 are not and 3
																							// != 4.
					(row0 == 0 && row1 != 0 && row2 != 0 && row3 != 0 && row1 != row2 && row2 != row3) || // If square 1
																											// is a zero
																											// and 2-4
																											// are not
																											// and 2 !=
																											// 3 & 3 !=
																											// 4.
					(row0 != 0 && row1 != 0 && row2 != 0 && row3 != 0 && row0 != row1 && row1 != row2 && row2 != row3))) // If
																															// all
																															// squares
																															// are
																															// not
																															// zero
																															// and
																															// 1
																															// !=
																															// 2
																															// &
																															// 2
																															// !=
																															// 3
																															// &
																															// 3
																															// !=
																															// 4.

				return true;
		}
		return false;
	} // End of isDownPossible.

	// The following method shifts all of the numbers down so that there are no
	// zeros in between all of the non zeros after which it combine numbers
	// that are the same value and puts them into the lower cell. Ex: 0 or 0
	// 0 2
	// 2 2
	// 2 4
	// Becomes : Becomes :
	// 0 0
	// 0 0
	// 0 4
	// 4 4
	//
	// After they have been shifted down, the color and number is displayed and the
	// move counter is increased by one.

	static void makeMovement() {
		for (int c = 0; c <= 3; c++) {
			if ((boardRotated[0][c].getValueOfSquare() + boardRotated[1][c].getValueOfSquare()
					+ boardRotated[2][c].getValueOfSquare() + boardRotated[3][c].getValueOfSquare()) != 0) // Checks to
																											// see if
																											// the
																											// columns
																											// are != 0.
			{
				if (boardRotated[3][c].getValueOfSquare() == 0) // If boardRotated[3][c] == 0 shift all the numbers
																// down.
				{
					boardRotated[3][c].setValueOfSquare(boardRotated[2][c].getValueOfSquare());
					boardRotated[2][c].setValueOfSquare(boardRotated[1][c].getValueOfSquare());
					boardRotated[1][c].setValueOfSquare(boardRotated[0][c].getValueOfSquare());
					boardRotated[0][c].setValueOfSquare(0);
				}
				if (boardRotated[3][c].getValueOfSquare() == 0) // If boardRotated[3][c] == 0 shift all the numbers
																// down.
				{
					boardRotated[3][c].setValueOfSquare(boardRotated[2][c].getValueOfSquare());
					boardRotated[2][c].setValueOfSquare(boardRotated[1][c].getValueOfSquare());
					boardRotated[1][c].setValueOfSquare(boardRotated[0][c].getValueOfSquare());
					boardRotated[0][c].setValueOfSquare(0);
				}
				if (boardRotated[3][c].getValueOfSquare() == 0) // If boardRotated[3][c] == 0 shift all the numbers
																// down.
				{
					boardRotated[3][c].setValueOfSquare(boardRotated[2][c].getValueOfSquare());
					boardRotated[2][c].setValueOfSquare(boardRotated[1][c].getValueOfSquare());
					boardRotated[1][c].setValueOfSquare(boardRotated[0][c].getValueOfSquare());
					boardRotated[0][c].setValueOfSquare(0);
				}

			}

			if ((boardRotated[0][c].getValueOfSquare() + boardRotated[1][c].getValueOfSquare()
					+ boardRotated[2][c].getValueOfSquare() + boardRotated[3][c].getValueOfSquare()) != 0) // Checks to
																											// see if
																											// the
																											// columns
																											// are != 0.
			{
				if (boardRotated[2][c].getValueOfSquare() == 0) // If boardRotated[2][c] == 0 shift all the numbers
																// down.
				{
					boardRotated[2][c].setValueOfSquare(boardRotated[1][c].getValueOfSquare());
					boardRotated[1][c].setValueOfSquare(boardRotated[0][c].getValueOfSquare());
					boardRotated[0][c].setValueOfSquare(0);
				}
				if (boardRotated[2][c].getValueOfSquare() == 0) // If boardRotated[2][c] == 0 shift all the numbers
																// down.
				{
					boardRotated[2][c].setValueOfSquare(boardRotated[1][c].getValueOfSquare());
					boardRotated[1][c].setValueOfSquare(boardRotated[0][c].getValueOfSquare());
					boardRotated[0][c].setValueOfSquare(0);
				}

			}

			if ((boardRotated[0][c].getValueOfSquare() + boardRotated[1][c].getValueOfSquare()
					+ boardRotated[2][c].getValueOfSquare() + boardRotated[3][c].getValueOfSquare()) != 0) // Checks to
																											// see if
																											// the
																											// columns
																											// are != 0.
			{
				if (boardRotated[1][c].getValueOfSquare() == 0) // If boardRotated[1][c] == 0 shift all the numbers
																// down.
				{
					boardRotated[1][c].setValueOfSquare(boardRotated[0][c].getValueOfSquare());
					boardRotated[0][c].setValueOfSquare(0);
				}

			}

			if (boardRotated[3][c].getValueOfSquare() == boardRotated[2][c].getValueOfSquare()) // If boardRotated[3][c]
																								// == boardRotated[2][c]
																								// then take what's in
																								// boardRotated[2][c]
																								// and add it in
																								// boardRotated[3][c]
			{
				boardRotated[3][c].setValueOfSquare(boardRotated[2][c].getValueOfSquare() * 2);
				boardRotated[2][c].setValueOfSquare(boardRotated[1][c].getValueOfSquare());
				boardRotated[1][c].setValueOfSquare(boardRotated[0][c].getValueOfSquare());
				boardRotated[0][c].setValueOfSquare(0);

				scoreCounter[0] += (boardRotated[3][c].getValueOfSquare()); // Adds the newly combined numbers into a
																			// running sum.
			}

			if (boardRotated[2][c].getValueOfSquare() == boardRotated[1][c].getValueOfSquare()) // If boardRotated[2][c]
																								// == boardRotated[1][c]
																								// then take what's in
																								// boardRotated[1][c]
																								// and add it in
																								// boardRotated[2][c].
			{
				boardRotated[2][c].setValueOfSquare(boardRotated[1][c].getValueOfSquare() * 2);
				boardRotated[1][c].setValueOfSquare(boardRotated[0][c].getValueOfSquare());
				boardRotated[0][c].setValueOfSquare(0);

				scoreCounter[0] += (boardRotated[2][c].getValueOfSquare()); // Adds the newly combined numbers into a
																			// running sum.
			}

			if (boardRotated[1][c].getValueOfSquare() == boardRotated[0][c].getValueOfSquare()) // If boardRotated[1][c]
																								// == boardRotated[0][c]
																								// then take what's in
																								// boardRotated[0][c]
																								// and add it in
																								// boardRotated[1][c].
			{
				boardRotated[1][c].setValueOfSquare(boardRotated[0][c].getValueOfSquare() * 2);
				boardRotated[0][c].setValueOfSquare(0);
				scoreCounter[0] += (boardRotated[1][c].getValueOfSquare()); // Adds the newly combined numbers into a
																			// running sum.
			}

		} // End of for loop for moveDown
		moveCounter[0] += 1; // Increase the moveCounter by one.
	} // End of moveDown.

	static void noRotation() {
		boardRotated[0][0] = board[0][0];
		boardRotated[0][1] = board[0][1];
		boardRotated[0][2] = board[0][2];
		boardRotated[0][3] = board[0][3];
		boardRotated[1][0] = board[1][0];
		boardRotated[1][1] = board[1][1];
		boardRotated[1][2] = board[1][2];
		boardRotated[1][3] = board[1][3];
		boardRotated[2][0] = board[2][0];
		boardRotated[2][1] = board[2][1];
		boardRotated[2][2] = board[2][2];
		boardRotated[2][3] = board[2][3];
		boardRotated[3][0] = board[3][0];
		boardRotated[3][1] = board[3][1];
		boardRotated[3][2] = board[3][2];
		boardRotated[3][3] = board[3][3];
	}

	static void noRotationBack() {
		board[0][0] = boardRotated[0][0];
		board[0][1] = boardRotated[0][1];
		board[0][2] = boardRotated[0][2];
		board[0][3] = boardRotated[0][3];
		board[1][0] = boardRotated[1][0];
		board[1][1] = boardRotated[1][1];
		board[1][2] = boardRotated[1][2];
		board[1][3] = boardRotated[1][3];
		board[2][0] = boardRotated[2][0];
		board[2][1] = boardRotated[2][1];
		board[2][2] = boardRotated[2][2];
		board[2][3] = boardRotated[2][3];
		board[3][0] = boardRotated[3][0];
		board[3][1] = boardRotated[3][1];
		board[3][2] = boardRotated[3][2];
		board[3][3] = boardRotated[3][3];
	}

	static void rotateTheBoard180Degrees() // For the up movement
	{
		boardRotated[3][3] = board[0][0];
		boardRotated[3][2] = board[0][1];
		boardRotated[3][1] = board[0][2];
		boardRotated[3][0] = board[0][3];
		boardRotated[2][3] = board[1][0];
		boardRotated[2][2] = board[1][1];
		boardRotated[2][1] = board[1][2];
		boardRotated[2][0] = board[1][3];
		boardRotated[1][3] = board[2][0];
		boardRotated[1][2] = board[2][1];
		boardRotated[1][1] = board[2][2];
		boardRotated[1][0] = board[2][3];
		boardRotated[0][3] = board[3][0];
		boardRotated[0][2] = board[3][1];
		boardRotated[0][1] = board[3][2];
		boardRotated[0][0] = board[3][3];

	}

	static void rotateTheBoardBack180Degrees() {
		board[0][0] = boardRotated[3][3];
		board[0][1] = boardRotated[3][2];
		board[0][2] = boardRotated[3][1];
		board[0][3] = boardRotated[3][0];
		board[1][0] = boardRotated[2][3];
		board[1][1] = boardRotated[2][2];
		board[1][2] = boardRotated[2][1];
		board[1][3] = boardRotated[2][0];
		board[2][0] = boardRotated[1][3];
		board[2][1] = boardRotated[1][2];
		board[2][2] = boardRotated[1][1];
		board[2][3] = boardRotated[1][0];
		board[3][0] = boardRotated[0][3];
		board[3][1] = boardRotated[0][2];
		board[3][2] = boardRotated[0][1];
		board[3][3] = boardRotated[0][0];
	}

	static void rotateTheBoard270Degrees() // For the left movement
	{
		boardRotated[3][0] = board[0][0];
		boardRotated[2][0] = board[0][1];
		boardRotated[1][0] = board[0][2];
		boardRotated[0][0] = board[0][3];
		boardRotated[3][1] = board[1][0];
		boardRotated[2][1] = board[1][1];
		boardRotated[1][1] = board[1][2];
		boardRotated[0][1] = board[1][3];
		boardRotated[3][2] = board[2][0];
		boardRotated[2][2] = board[2][1];
		boardRotated[1][2] = board[2][2];
		boardRotated[0][2] = board[2][3];
		boardRotated[3][3] = board[3][0];
		boardRotated[2][3] = board[3][1];
		boardRotated[1][3] = board[3][2];
		boardRotated[0][3] = board[3][3];
	}

	static void rotateTheBoardBack270Degrees() {
		board[0][0] = boardRotated[3][0];
		board[0][1] = boardRotated[2][0];
		board[0][2] = boardRotated[1][0];
		board[0][3] = boardRotated[0][0];
		board[1][0] = boardRotated[3][1];
		board[1][1] = boardRotated[2][1];
		board[1][2] = boardRotated[1][1];
		board[1][3] = boardRotated[0][1];
		board[2][0] = boardRotated[3][2];
		board[2][1] = boardRotated[2][2];
		board[2][2] = boardRotated[1][2];
		board[2][3] = boardRotated[0][2];
		board[3][0] = boardRotated[3][3];
		board[3][1] = boardRotated[2][3];
		board[3][2] = boardRotated[1][3];
		board[3][3] = boardRotated[0][3];
	}

	static void rotateTheBoard90Degrees() // For the right movement
	{
		boardRotated[0][3] = board[0][0];
		boardRotated[1][3] = board[0][1];
		boardRotated[2][3] = board[0][2];
		boardRotated[3][3] = board[0][3];
		boardRotated[0][2] = board[1][0];
		boardRotated[1][2] = board[1][1];
		boardRotated[2][2] = board[1][2];
		boardRotated[3][2] = board[1][3];
		boardRotated[0][1] = board[2][0];
		boardRotated[1][1] = board[2][1];
		boardRotated[2][1] = board[2][2];
		boardRotated[3][1] = board[2][3];
		boardRotated[0][0] = board[3][0];
		boardRotated[1][0] = board[3][1];
		boardRotated[2][0] = board[3][2];
		boardRotated[3][0] = board[3][3];
	}

	static void rotateTheBoardBack90Degrees() {
		board[0][0] = boardRotated[0][3];
		board[0][1] = boardRotated[1][3];
		board[0][2] = boardRotated[2][3];
		board[0][3] = boardRotated[3][3];
		board[1][0] = boardRotated[0][2];
		board[1][1] = boardRotated[1][2];
		board[1][2] = boardRotated[2][2];
		board[1][3] = boardRotated[3][2];
		board[2][0] = boardRotated[0][1];
		board[2][1] = boardRotated[1][1];
		board[2][2] = boardRotated[2][1];
		board[2][3] = boardRotated[3][1];
		board[3][0] = boardRotated[0][0];
		board[3][1] = boardRotated[1][0];
		board[3][2] = boardRotated[2][0];
		board[3][3] = boardRotated[3][0];

	}

	// The first for loop creates backup arrays with all of the values from the
	// moves on the board. The second for loop gets the values of the current board
	// and puts it in the top layer of the 10 undos. The last two for loops back up
	// all of the scores an moves.

	static void backUpBoard() {
		for (int layers = 9; layers > 0; layers--) // Depth (Layers)
			for (int row = 0; row <= 3; row++) // Rows
				for (int column = 0; column <= 3; column++) // Columns
					backUpBoard[layers][row][column] = backUpBoard[layers - 1][row][column];

		for (int row = 0; row <= 3; row++)
			for (int column = 0; column <= 3; column++)
				backUpBoard[0][row][column] = board[row][column].getValueOfSquare();

		for (int numberCounter = 10; numberCounter > 0; numberCounter--)
			scoreCounter[numberCounter] = scoreCounter[numberCounter - 1];

		for (int movingCounter = 10; movingCounter > 0; movingCounter--)
			moveCounter[movingCounter] = moveCounter[movingCounter - 1];

	} // End of backUpBoard

	// The first if statement checks to make sure that the user has made at least
	// one move before they can undo the board. The first two for loops take the
	// values that are in
	// the highest layer of the backUpBoard and place them on the board for the user
	// to see. The next three for loops move all of the backUpBoards from a higher
	// slot
	// to a lower one. Ex: backUpBoard 9 is now in 8, 8 is in 7, 7 is in 6 etc. The
	// following two for loops undo the score and number of moves.
	// The last two for loops display the colors and values of all of the squares.
	static void undoBoard() {
		if (moveCounter[0] > 0) // Checks to make sure that the user has moved before they can undo the board.
		{
			for (int row = 0; row <= 3; row++)
				for (int column = 0; column <= 3; column++)
					board[row][column].setValueOfSquare(backUpBoard[0][row][column]);

			for (int layers = 0; layers < 9; layers++) // Depth (Layers)
				for (int row = 0; row <= 3; row++) // Rows
					for (int column = 0; column <= 3; column++) // Columns
					{
						backUpBoard[layers][row][column] = backUpBoard[layers + 1][row][column];
					}

			for (int numberCounter = 0; numberCounter < 10; numberCounter++)
				scoreCounter[numberCounter] = scoreCounter[numberCounter + 1];

			for (int movingCounter = 0; movingCounter < 10; movingCounter++)
				moveCounter[movingCounter] = moveCounter[movingCounter + 1];

			for (int rowToBoard = 0; rowToBoard <= 3; rowToBoard++)
				for (int columnToBoard = 0; columnToBoard <= 3; columnToBoard++) {
					board[rowToBoard][columnToBoard].displayNumber();
					board[rowToBoard][columnToBoard].setColorsOfSquares();
				}
		}
	} // End of undoBoard()

	// The only thing that defines the game is the integers at their location and
	// the keepPlayingTheGame boolean, therefore, the only things we need to
	// save are the integers from the board, score, move count, and the backUp board
	// and the boolean that holds once a 2048 tile is created.

	static void saveTheCurrentGame() {
		try (ObjectOutputStream outputTheGame = new ObjectOutputStream(new FileOutputStream("2048.dat"))) {
			for (int scoreCounterVariable = 0; scoreCounterVariable <= 10; scoreCounterVariable++) // Saves all of the
																									// scores to the
																									// file.
				outputTheGame.writeInt(scoreCounter[scoreCounterVariable]);

			for (int moveCounterVariable = 0; moveCounterVariable <= 10; moveCounterVariable++) // Saves all of the
																								// moves to the file.
				outputTheGame.writeInt(moveCounter[moveCounterVariable]);

			for (int r = 0; r <= 3; r++)
				for (int c = 0; c <= 3; c++)
					outputTheGame.writeInt(board[r][c].getValueOfSquare()); // Saves the current values on the board.

			for (int d = 0; d <= 9; d++) // Depth (Layers)
				for (int r = 0; r <= 3; r++) // Rows
					for (int c = 0; c <= 3; c++) // Columns
						outputTheGame.writeInt(backUpBoard[d][r][c]); // Saves all of the back up boards.

			outputTheGame.writeBoolean(keepPlayingTheGame); // Saves the boolean that keeps track of the first
															// occurrence of a 2048 file.
		} catch (IOException ex) {

		}
	}// End of the saveTheCurrentGame()

	// Loads the last game that was saved by the user. The data is read in order, so
	// since the current board was read in first, the current board must
	// be loaded first.

	static void loadTheLastGame() {
		try (ObjectInputStream inputTheGame = new ObjectInputStream(new FileInputStream("2048.dat"))) {
			for (int scoreCounterVariable = 0; scoreCounterVariable <= 10; scoreCounterVariable++) // Loads the scores
																									// back into the
																									// game.
				scoreCounter[scoreCounterVariable] = inputTheGame.readInt();

			for (int moveCounterVariable = 0; moveCounterVariable <= 10; moveCounterVariable++) // Loads the moves back
																								// into the game.
				moveCounter[moveCounterVariable] = inputTheGame.readInt();

			for (int row = 0; row <= 3; row++)
				for (int column = 0; column <= 3; column++)
					board[row][column].setValueOfSquare(inputTheGame.readInt()); // Sets the current board to the last
																					// saved board.

			for (int layers = 0; layers <= 9; layers++) // Depth (Layers)
				for (int row = 0; row <= 3; row++) // Rows
					for (int column = 0; column <= 3; column++) // Columns
						backUpBoard[layers][row][column] = inputTheGame.readInt(); // Loads all of the backup boards.

			keepPlayingTheGame = inputTheGame.readBoolean(); // Assigns the previous value of keepPlayingTheGame to the
																// current game's value

			for (int row = 0; row <= 3; row++) // This for loop displays the color and number of the tile.
				for (int column = 0; column <= 3; column++) {
					board[row][column].displayNumber();
					board[row][column].setColorsOfSquares();
				}

			score.setText("Score\n" + scoreCounter[0]); // Displays the score from the previously saved game.
			count.setText("Moves\n" + moveCounter[0]); // Displays the move count from the previously saved game.

		}

		catch (IOException ex) {

		}

	}// End of the loadTheLastGame()

	// Checks to see if the board has a 2048. If it does, it returns true for having
	// one.
	static boolean isWinner() {
		for (int row = 0; row <= 3; row++)
			for (int column = 0; column <= 3; column++)
				if (board[row][column].getValueOfSquare() == 2048)
					return true;
		return false;
	} // End of isWinner.

	// Checks to see if there are any possible moves on the board. If there are
	// possible moves, it skips over finalScoresScreen(). If not,
	// then the finalScoresScreen() is called.

	static void checkToSeeIfPlayIsAvailable() {
		int movementsPossible = 0;

		noRotation();
		if (isMovementPossible())
			movementsPossible++;

		noRotationBack();

		rotateTheBoard180Degrees();
		if (isMovementPossible())
			movementsPossible++;

		rotateTheBoardBack180Degrees();

		rotateTheBoard270Degrees();
		if (isMovementPossible())
			movementsPossible++;

		rotateTheBoardBack270Degrees();

		rotateTheBoard90Degrees();
		if (isMovementPossible())
			movementsPossible++;

		rotateTheBoardBack90Degrees();

		if (movementsPossible == 0)
			finalScoresScreen();
	}

	// Creates a window when the user presses ALT + H or clicks on the help button.
	// This method provides information on how to play the game 2048.
	// Displays the helpingHand window if the user needs help on how to play the
	// game, they can press ALT + H and it will open this new window.

	public void helpingHandScreen() {
		Stage helpAndInstructions = new Stage();

		Label help = new Label(
				"Welcome to the game 2048.  The objective of this game is to merge the squares of the same\nvalue to get a tile that reads "
						+ "2048.  In order to do this, you  must use the UP, DOWN, LEFT\nand RIGHT keys. If you would like to quit the game "
						+ "press ALT + X.  If you would like to\nsave your current game press ALT + S or the save button."
						+ "If you would like to load an old game \npress ALT + L or the load button. If you would like to"
						+ " undo a move that you made press \nCNTRL + Z (you can do up to 10 undos).");
		help.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.ITALIC, 20));

		BorderPane helpSizing = new BorderPane();
		helpSizing.setCenter(help);

		Scene helpScene = new Scene(helpSizing); // Create a scene.
		helpAndInstructions.setResizable(false); // Sets the output to not resizeable.
		helpAndInstructions.setTitle("Help and Instructions"); // Set the stage title.
		helpAndInstructions.setScene(helpScene); // Put the scene in the stage.
		helpAndInstructions.show(); // Display the stage.
	} // End of helpingHand method.

	// The finalScoresScreen() displays the final score the total number of moves
	// that the player has made.
	// If the player does not want to continue playing once they get a 2048 tile,
	// then this window will pop up and tell them there final score and
	// score and move count.
	static void finalScoresScreen() {
		Stage thanksForPlaying = new Stage();

		Label endingScore = new Label("Your final score is: " + scoreCounter[0]);
		endingScore.setFont(Font.font("Calibri", 15));
		ScoreDAO.getInstance().insertScore(scoreCounter[0]);

		Label endingMoves = new Label("  The number of moves you made is: " + moveCounter[0]);
		endingMoves.setFont(Font.font("Calibri", 15));

		HBox thanksHBox = new HBox();
		thanksHBox.getChildren().addAll(endingScore, endingMoves);
		thanksHBox.setAlignment(Pos.CENTER);

		BorderPane thanksPane = new BorderPane();
		thanksPane.setCenter(thanksHBox);
		thanksPane.setPadding(new Insets(20, 20, 20, 20));
		// T,R,B,L

		Scene thanksScene = new Scene(thanksPane); // Create a scene.
		thanksForPlaying.setResizable(false); // Sets the output to not resizeable.
		thanksForPlaying.setTitle("Final score and move count!"); // Set the stage title.
		thanksForPlaying.setScene(thanksScene); // Put the scene in the stage.
		thanksForPlaying.show(); // Display the stage.

		EventHandler closeFinalScoresWindow = e -> {
			thanksForPlaying.close();
			gameOverScreen();
		};

		Timeline finalScoresScreen = new Timeline(new KeyFrame(Duration.millis(5000))); // The finalScoresScreen stays
																						// open for 5 seconds.
		finalScoresScreen.setOnFinished(closeFinalScoresWindow);
		finalScoresScreen.play();

	}// End of gameOverScreen.

	// The gameOverScreen() displays when the user quits or when there are no other
	// possible moves. A window pops up telling the user
	// that the game is over and thanks for playing. When the user doesn't want to
	// continue the game after getting a 2048 tile, then
	// this window will pop up and say Game Over.

	static void gameOverScreen() {
		Stage gameOver = new Stage();

		Label gameOverLabel = new Label("Game Over \nThanks for playing 2048!");
		gameOverLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

		Scene thanksScene = new Scene(gameOverLabel); // Create a scene.
		gameOver.setResizable(false); // Sets the output to not resizeable.
		gameOver.setTitle("Game Over!"); // Set the stage title.
		gameOver.setScene(thanksScene); // Put the scene in the stage.
		gameOver.show(); // Display the stage.

		EventHandler closeGameOver = e -> {
			gameOver.close();
			System.exit(0);
		};

		Timeline gameOverTimeline = new Timeline(new KeyFrame(Duration.millis(2000), closeGameOver)); // The
																										// gameOverTimeline
																										// is displayed
																										// for 2
																										// seconds.
		gameOverTimeline.play();

	}

	// If the user has created a 2048 tile, then this window will pop up asking them
	// if they would like to continue.

	static void winnerScreen() {
		if (!keepPlayingTheGame) {
			keepPlayingTheGame = true; // Sets this variable to true so that this window will not pop up again. It is
										// only used for the first 2048 that pops up.
			Stage winnerStage = new Stage();

			Label winnerLabel = new Label("CONGRATULATIONS, YOU MADE A 2048 TILE! Would you like to keep playing?");
			winnerLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 20));

			Button winnerButtonKeepPlaying = new Button("Sí");

			Button winnerButtonNoPlay = new Button("No");

			HBox winnerButtons = new HBox();
			winnerButtons.getChildren().addAll(winnerButtonKeepPlaying, winnerButtonNoPlay);
			winnerButtons.setAlignment(Pos.CENTER);

			BorderPane winnerPane = new BorderPane();
			winnerPane.setCenter(winnerLabel);
			winnerPane.setBottom(winnerButtons);
			winnerPane.setPadding(new Insets(20, 20, 20, 20));
			// T,R,B,L

			Scene winnerScene = new Scene(winnerPane); // Create a scene.
			winnerStage.setResizable(false); // Sets the output to not resizeable.
			winnerStage.setTitle("YOU GOT A 2048 TILE"); // Set the stage title.
			winnerStage.setScene(winnerScene); // Put the scene in the stage.
			winnerStage.show(); // Display the stage.

			winnerButtonKeepPlaying.setOnAction(e -> {
				winnerStage.close();
			});

			winnerButtonNoPlay.setOnAction(e -> {
				finalScoresScreen();
			});
		} // End of the if statement
	}


}// End of GUI2048 class

//All of the imports allow us to create shapes, animations, and stages.
//This class creates the squares that are placed on the game board.  The class creates the squares, 
//sets the colors, displays the numbers on the tiles, and gets and sets the numbers that are on the 
//board.


	


