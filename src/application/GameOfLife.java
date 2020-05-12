package application;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * The Game of Life by John Conway.
 *
 * @author Kongtapp Veerawattananun
 */
public class GameOfLife {

	public Board board;
	private int historySize = 30;
	private List<Board> boardHistory = new ArrayList<Board>(historySize);
	
    /**
     * Initialize the rows and columns of the board and fill
     *
     * @param board Board of the game.
     */
    public GameOfLife(Board board) {
        this.board = board;
    }

    /**
     * Print the board in the console for observation and testing
     */
    public void printBoard() {
        for (int y = 0; y < board.getRows(); y++) {
            StringBuilder line = new StringBuilder("|");
            for (int x = 0; x < board.getColumns(); x++) {
                if (board.getValue(x, y) == board.DEAD) {
                    line.append(" 0 |");
                } else {
                    line.append(" 1 |");
                }
            }
            System.out.println(line);
        }
        System.out.println();
    }
    
    public int countNeighbors(int x, int y) {
        // position of the surrounding neighbors
        int[][] position = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        int count = 0;
        for (int[] set : position) {
            count += board.getValue(x + set[0], y + set[1]);
        }
        return count;
    }
    
    /**
     * Proceeds the population in the board to the next generation from the following rules
     * The Rules:
     * For a space that is populated:
     *  • Each cell with one or no neighbors dies, as if by solitude.
     *  • Each cell with four or more neighbors dies, as if by overpopulation.
     *  • Each cell with two or three neighbors survives.
     * For a space that is empty or unpopulated:
     * 	• Each cell with three neighbors becomes populated.
     */
    public void nextGeneration() {
    	
        Board tempBoard = new Board(board.getRows(), board.getColumns());
        int rows = board.getRows();
        int columns = board.getColumns();
        
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
            	
                if (board.getValue(x, y) == board.ALIVE) {        // Board at position x,y is Populated
                    if (countNeighbors(x, y) <= 1) {
                        tempBoard.setDead(x, y);
                    } else if (countNeighbors(x, y) >= 4) {
                        tempBoard.setDead(x, y);
                    } else if (countNeighbors(x, y) == 2 || countNeighbors(x, y) == 3) {
                        tempBoard.setAlive(x, y);
                    }
                } else {    // Board at position x,y is unpopulated
                    if (countNeighbors(x, y) == 3) {
                        tempBoard.setAlive(x, y);
                    }
                }
            }
        }
        this.board = tempBoard;
        
    }

    /**
     * randomly add the Alive Neighbors into the board.
     * Alive Chance is 1/8
     */
    public void random() {
    	Random rn = new Random();
    	for (int y = 0; y < board.getRows(); y++) {
    		for (int x = 0; x < board.getColumns(); x++) {
    			if (rn.nextInt(8) == 4) {
    				board.setAlive(x, y);
    			} else {
    				board.setDead(x, y);
    			}
    		}
    	}
    }
    
    /**
     * Saved Board for later used. If the board is empty the board will not saved and if board history is already full
     * deleted the oldest one and replaced with current one.
     */
    public void saveBoard() {
    	Board emptyBoard = new Board(board.getRows(), board.getColumns());
    	if ( !(this.board.equals(emptyBoard)) ) {    		
    		if (boardHistory.size() < historySize) {
    			boardHistory.add(0, this.board);    		    		
    		} else if (boardHistory.size() == historySize) {
    			boardHistory.remove(historySize-1);
    			boardHistory.add(0, this.board);
    		}
    	}
    }

    /**
     * Change the board of the Game of life to the previous save board.
     * if there's no saved board then do nothing.
     */
    public void loadBoard() {
    	if (! this.boardHistory.isEmpty() ) {
    		this.board = boardHistory.remove(0);    		
    	}
    }
}
