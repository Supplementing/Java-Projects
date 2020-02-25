import java.util.*;



/**
 * @author Mason Herron
 * A-Star pathfinding
 * 9-27-19
 * Driver is the entry point to the program
 *
 */
public class driver {
	public static void main(String[] args) {

		// creates the board
		Board board = new Board();
		Node[][] boardArray = board.generateBoard();
		int spRow, spColumn, endRow, endColumn;
		

		Scanner input = new Scanner(System.in);

		board.printBoard();
		System.out.println();
		
			//do-while loop checks to make sure that the user is not choosing unpathable nodes for the starting and ending nodes
			do {
				System.out.println("\n---------------------------------------------\nROWS AND COLUMNS START AT 0 and go to 14!!\n---------------------------------------------\n");
				System.out.println(
						"Please enter the row for the starting point(must be a pathable location, unpathable nodes are marked 'X'):");
				spRow = input.nextInt();
				System.out.println(
						"Please enter the column for the starting point: (must be a pathable location, unpathable nodes are marked 'X')");
				spColumn = input.nextInt();
	
				System.out.println(
						"Please enter the row for the ending point: (must be a pathable location, unpathable nodes are marked 'X')");
				endRow = input.nextInt();
				System.out.println(
						"Please enter the column for the ending point: (must be a pathable location, unpathable nodes are marked 'X')");
				endColumn = input.nextInt();
			} while (boardArray[spRow][spColumn].getType() == 1 || boardArray[endRow][endColumn].getType() == 1);
			
			
			
			// sets the starting and ending points
			board.setStartingPoint(spRow, spColumn);
			board.setEndingPoint(endRow, endColumn);
	
			
				
			//calculates the path (or lack thereof) and stores it in path
			String path = board.calculatepath(); 
			
			//prints the updated board so the used can see the starting and ending points
			System.out.print("Updated board:\n");
			board.printBoard();
			
			//empty line for formatting
			System.out.println();
			
			System.out.println(path);
			

	}

}
