package game;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GenerateSquares extends StackPane							//This class extends StackPane.
{
	private Rectangle squaresOnBoard = new Rectangle(132 , 132);		//Creates rectangles with specified lengths and widths.
	private int valueOfSquare = 0;										//Sets the rectangle's value to 0 to start.
	private Label numberOnTile = new Label("");							//Creates a label with an empty string.
	
	// Creates a new square for the game board.  It rounds the edges, and sets the color to dark gray.
	public GenerateSquares()
		{
			super();													//Runs the parent's constructor.
			squaresOnBoard.setArcWidth(10);								//Rounds the edges of all of the squares.
			squaresOnBoard.setArcHeight(10);							//Rounds the edges of all of the squares.
			squaresOnBoard.setFill(Color.web("#CCC0B3"));				//Sets the color of the squares to a light gray.
			this.getChildren().addAll(squaresOnBoard, numberOnTile);	//Takes the squares and add's a blank string to them.
		}
	
	//Sets the color of the board depending on what value is in the board.  All of the colors correspond to the actual game colors.
	//They were obtained by using a picture of a 2048 board on google images with a 'ColorPick Eyedropper.'
	public void setColorsOfSquares()
	{
		if 		(valueOfSquare == 0)	squaresOnBoard.setFill(Color.TRANSPARENT);
		else if (valueOfSquare == 2)	squaresOnBoard.setFill(Color.web("#F0E8DF"));  
		else if (valueOfSquare == 4)	squaresOnBoard.setFill(Color.web("#EFE4CE")); 
		else if (valueOfSquare == 8)	squaresOnBoard.setFill(Color.web("#F4BA84")); 
		else if (valueOfSquare == 16)	squaresOnBoard.setFill(Color.web("#F6A06E")); 
		else if (valueOfSquare == 32)	squaresOnBoard.setFill(Color.web("#F57C5E")); 
		else if (valueOfSquare == 64)	squaresOnBoard.setFill(Color.web("#F65D3B")); 
		else if (valueOfSquare == 128)	squaresOnBoard.setFill(Color.web("#EFCE71")); 
		else if (valueOfSquare == 256)	squaresOnBoard.setFill(Color.web("#EDCC63")); 
		else if (valueOfSquare == 512)	squaresOnBoard.setFill(Color.web("#EBC850")); 
		else if (valueOfSquare == 1024)	squaresOnBoard.setFill(Color.web("#EDC53F")); 
		else if (valueOfSquare == 2048)	squaresOnBoard.setFill(Color.web("#EEC22E")); 
		else 							squaresOnBoard.setFill(Color.BLACK); 		          

	}
	
	// Places the number on the board with font Calibri, bold, 50. If the number is a zero then displays nothing and if the number is not zero display the number.
	public void displayNumber()
	{
			
		numberOnTile.setFont(Font.font("Calibri", FontWeight.BOLD, 50));
		if			(valueOfSquare == 0) numberOnTile.setText("");
		else		numberOnTile.setText(valueOfSquare + "");
	}
	
	
	//Returns the value of the square because the value of the square is a private data field.
	public int getValueOfSquare()
	{
		return valueOfSquare;
	}
	
	//Sets the value of the square because the value of the square is a private data field.
	public void setValueOfSquare(int value)
	{
		this.valueOfSquare = value;
	}
}//End of GenerateSquares()