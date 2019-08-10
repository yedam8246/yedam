package game;

import java.util.Random;

public class Test2048 {

	enum State {
		start, won, running, over
	}

	final static int target = 2048;

	static int highest;
	static int score;

	private Random rand = new Random();

	private Tile[][] tiles;
	private int side = 4;
	private State gamestate = State.start;
	private boolean checkingAvailableMoves;
	
	

	void startGame() {
		if (gamestate != State.running) {
			score = 0;
			highest = 0;
			gamestate = State.running;
			tiles = new Tile[side][side];

		}

	}

	private void addRandomTile() {
		int pos = rand.nextInt(side * side);
		int row, col;

		do {
			pos = (pos + 1) % (side * side);
			row = pos / side;
			col = pos & side;
		} while (tiles[row][col] != null);

		int val = rand.nextInt(10) == 0 ? 4 : 2;
		tiles[row][col] = new Tile(val);
	}

	private boolean move(int countDownFrom, int yIncr, int xIncr) {
		boolean moved = false;

		for (int i = 0; i < side * side; i++) {
			int j = Math.abs(countDownFrom - i);

			int r = j / side;
			int c = j % side;

			if (tiles[r][c] == null)
				continue;

			int nextR = r + yIncr;
			int nextC = c + xIncr;

			while (nextR >= 0 && nextR < side && nextC >= 0 && nextC < side) {

				Tile next = tiles[nextR][nextC];
				Tile curr = tiles[r][c];

				if (next == null) {
					if (checkingAvailableMoves)
						return true;
					
					tiles[nextR][nextC]=curr;
					tiles[r][c]=null;
					
					r=nextR;
					c=nextC;
					
					nextR+=yIncr;
					nextC+=xIncr;
					moved=true;

				}else if(next.canMergeWith(curr)) {
					
					if(checkingAvailableMoves)
						return true;
					
					int value=next.mergeWith(curr);
					if(value > highest)
						highest=value;
					score+=value;
					
					tiles[r][c]=null;
					
					moved=true;
					
					break;
				}else
					break;
			}
		}
		
		if(moved) {
			if(highest < target) {
				clearMerged();
				addRandomTile();
				if(!movesAvailable()) {
					gamestate=State.over;
				}
			}else if(highest==target)
				gamestate=State.won;
		}
		return moved;
	}
	
	boolean moveUp() {
		return move(0, -1, 0);
	}
	
	boolean moveDown() {
		return move(side*side - 1, 1, 0);
	}
	
	boolean moveLeft() {
		return move(0, 0, -1);
	}
	
	boolean moveRight() {
		return move(side * side - 1, 0, 1);
	}
	
	void clearMerged() {
		for(Tile[] row : tiles) {
			for(Tile tile : row)
				if(tile != null)
					tile.setMerged(false);
		}
	}
	
	boolean movesAvailable() {
		checkingAvailableMoves=true;
		boolean hasMoves= moveUp() || moveLeft() || moveRight() || moveDown();
		checkingAvailableMoves=false;
		return hasMoves;
	}

}
