package application;

public class Board {

	private int rows;
	private int columns;
	private int[][] board;
	final int DEAD = 0;
	final int ALIVE = 1;
	
	public Board(int rows, int columns) {
		this.columns = columns;
		this.rows = rows;
		this.board = new int[rows][columns];
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public int getColumns() {
		return this.columns;
	}
	
	/**
     * check if the coordinate is inside the boundary return true if it is, false otherwise
     * @param x coordinate
     * @param y coordinate
     * @return true if inbound, false otherwise.
     */
    private boolean checkInBound(int x, int y) {
    	if (x < 0 || x >= this.columns) {
            return false;
        } else if (y < 0 || y >= this.rows) {
            return false;
        }
    	return true;
    }
    
    /**
     * check if the board with position x, y is inside the board and status of the population reside in that position
     * return 0 if the index is more than rows or columns or less than 0
     * otherwise return value of that position.
     *
     * @param x rows of the board
     * @param y columns of the board
     * @return the value of that board position. return 0 if the index is out of the board.
     */
    public int getValue(int x, int y) {
        if (checkInBound(x, y)) {
        	return this.board[x][y];
        }
        return 0;
    }
    
    /**
     * Set the board value at rows(x) and columns(y) to 1.
     *
     * @param x rows of the board
     * @param y columns of the board
     */
    public void setAlive(int x, int y) {
    	if (checkInBound(x, y)) {
    		this.board[x][y] = ALIVE;    		
    	}
    }

    /**
     * Set the board value at rows(x) and columns(y) to 0.
     *
     * @param x rows of the board
     * @param y columns of the board
     */
    public void setDead(int x, int y) {
    	if (checkInBound(x, y)) {
    		this.board[x][y] = DEAD;    		
    	}
    }
	
    /**
     * Board is the same when the Value inside the Board is the same
     * Return true if all value is the same, false otherwise.
     * 
     * @return true if all value is the same, false otherwise.
     */
    public boolean equals(Object object) {
    	if (! (object.getClass() == this.getClass()) ) {
    		return false;
    	}
    	Board board = (Board)object;
    	for (int y = 0; y < this.getRows(); y++) {
    		for (int x = 0; x < this.getColumns(); x++) {
    			if (! (board.getValue(x, y) == this.getValue(x, y))) {
    				return false;
    			}
    		}
    	}
		return true;
    }
    
}
