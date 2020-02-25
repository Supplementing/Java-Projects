/*
 * Project Name: 8-Queens Problem with Hill-CLimbing/Random Restarts
 * Author: Mason Herron
 * Date: 9-1-19
 */

import java.util.*;

public class driver {
	public static void main(String[] args) {

		Board board = new Board();

		Queen[] queenArray = board.generateBoard();
		
		if(board.findBoardHeuristic(queenArray) ==0) //checks to see if starting state is goal state
		{
			System.out.println("\nSolution Found!");
			board.printBoard(queenArray);
			board.stats();
		}
	
		else {
			//runs until the board is solved, prints the board, updates each elements heuristics as needed, and searches for a better board
			while (board.findBoardHeuristic(queenArray) > 0) {

				System.out.println("\nCurrent State: " + board.findBoardHeuristic(queenArray));
				board.printBoard(queenArray);
				board.findElementHueristics(queenArray); //updates elements heuristic fields
				queenArray = board.betterBoard(queenArray); //searches for a better board and assigns the better board (if one exists) or assigns a newly generated board (random restart)
				
			}
			System.out.println("\nSolution Found!");
			board.printBoard(queenArray);
			board.stats(); //prints the stats such as state changes and restarts
		}
		

	}
}
