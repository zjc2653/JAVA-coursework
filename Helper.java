import java.util.ArrayList;


public class Helper {
	NumTile [][] rowsAndCols;
	NumTile [][] boxes;
	ArrayList<Integer> allowedNums = new ArrayList<Integer>();
	int boxIndex;
	int row;
	int column;
	
	/**
	 * Initiates the helper method to alert the user to the possible
	 *  numbers that can be entered into any given cell
	 * @param rC 2D Array containing the NumTiles describing the rows and columns
	 * @param b 2D Array containing the NumTiles describing the boxes(squares) that comprise the Sudoku board
	 * @param rIndex The index of the row that the given tile is a part of (0-8)
	 * @param cIndex The index of the column that the given tile is a part of (0-8)
	 */
	 
	public Helper(NumTile[][] rC, NumTile[][] b, int rIndex, int cIndex) {
		rowsAndCols = rC;
		boxes = b;
		row = rIndex;
		column = cIndex;
		boxIndex = getBoxNum(row,column);
	}
	 
	/**
	 * This method checks all of the rows and columns and boxes(squares) to determine the
	 *  possible numbers for the given cell
	 * @return a String that lists all possible numbers to go into the given cell
	 */
	
	public String useHelper() {
		ArrayList<Integer> okRow = checkNine(getRow());
		ArrayList<Integer> okCol = checkNine(getColumn());
		ArrayList<Integer> okBox = checkNine(getBox());
		
		for(int i=1; i<10; i++) {
			if(okRow.contains(i) && okCol.contains(i) && okBox.contains(i)) 
				allowedNums.add(i);
		}
		
		StringBuilder helper = new StringBuilder();
		
		for (int i = 0; i<allowedNums.size(); i++) {
			if(i==0)
				helper.append("[");
			if(i == allowedNums.size()-1)
				helper.append(Integer.toString(allowedNums.get(i))+"]");
			else
				helper.append(Integer.toString(allowedNums.get(i))+", ");
		}

		return helper.toString();
	}
	
	/**
	 * This method uses the Numtile found in the array rowsAndCols to find the tile's location
	 *  within the boxes array.
	 * @param rIndex The index of the row that the given tile is a part of (0-8)
	 * @param cIndex The index of the column that the given tile is a part of (0-8)
	 * @return The index of the box (the outer loop) where the
	 *   Numtile was found
	 */
	
	public int getBoxNum(int row, int col) {
		NumTile temp = rowsAndCols[row][col];
		for(int i = 0; i<9; i++) {
			for(int j = 0; j<9; j++) {
				if(boxes[i][j].equals(temp))
					return i;
			}
		}
		return 100;
	}
	
	/**
	 * This method transforms the Numtiles from a given row into integers
	 * @return The array of integers that describe the text from the given row
	 */
	
	public int[] getRow() {
		String val;
		int[] nums = new int[9];
		for(int i=0; i<9; i++) {
			val = rowsAndCols[row][i].getNum();
			if(! val.equals(""))
				nums[i] = Integer.valueOf(val);
		}
		return nums;
	}
	 
	/**
	 * This method transforms the Numtiles from a given column into integers
	 * @return The array of integers that describe the text from the given column
	 */
	
	public int[] getColumn() {
		String val;
		int[] nums = new int[9];
		for(int i=0; i<9; i++) {
			val = rowsAndCols[i][column].getNum();
			if(! val.equals(""))
				nums[i] = Integer.valueOf(val);
		}
		return nums;
	}
	 
	/**
	 * This method transforms the Numtiles from a given box(square) into integers
	 * @return The array of integers that describe the text from the given box(square)
	 */
	
	public int[] getBox() {
		String val;
		int[] nums = new int[9];
		for(int i=0; i<9; i++) {
			val = boxes[boxIndex][i].getNum();
			if (! val.equals(""))
				nums[i] = Integer.valueOf(val);
		}
		return nums;
	}
	 
	
	/**
	 * 
	 * @param nineToCheck An array that contains 9 values (numbers or null)
	 * These 9 numbers should describe a row, a column, or a box(square)
	 * @return The numbers between 1-9 which are not present in the given array of 9 values
	 */
	
	public ArrayList<Integer> checkNine(int[] nineToCheck) {
		int[] numsToCheck = nineToCheck;
		boolean found;
		ArrayList<Integer> allowedNums = new ArrayList<Integer>();
		for(int i=1; i<10; i++) {
			found = false;
			for(int x=0; x<9; x++) {
				if(numsToCheck[x] == i) 
					found = true;	
			}
			if(!found)
				allowedNums.add(i);
		}	
		return allowedNums;
	}

}