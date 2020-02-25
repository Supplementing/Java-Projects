
/*
 * Project Name: 8-Queens Problem with Hill-CLimbing/Random Restarts
 * Author: Mason Herron
 * Date: 9-1-19
 */
import java.util.*;
import java.util.Random;
import javax.tools.DocumentationTool.Location;

public class Board {
	private int restarts = 0;
	private int stateChanges = 0;

	// used to generate the board
	public Queen[] generateBoard() {

		Queen[] queenArray = new Queen[8];

		// variables to hold the queens locations
		for (int i = 0; i < 8; i++)

		{
			Random rng = new Random();
			queenArray[i] = new Queen(rng.nextInt(8), i);

		}

		return queenArray;

	}

//prints the board using a 2d array
	public void printBoard(Queen[] queenArray) {
		// 2-d array to hold all the values of the board
		int[][] boardArray = new int[8][8];

		// assigns the queens to their corresponding places on the board array and puts
		// them as a '1'
		for (int i = 0; i < queenArray.length; i++) {
			boardArray[queenArray[i].getRow()][queenArray[i].getColumn()] = 1;

		}

		// prints the entire board out
		// System.out.println();
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				System.out.print("[" + boardArray[i][k] + "] ");
			}
			System.out.println();
		}

	}

	// used to find the overall board hueristic value (conflicts)
	public int findBoardHeuristic(Queen[] queenArray) {
		int h = 0;
		for (int i = 0; i < queenArray.length; i++) {
			for (int k = i + 1; k < queenArray.length; k++) {
				if (queenArray[i].conflicts(queenArray[k])) {
					h++;
				}
			}
		}
		return h;

	}

//gets each elements heuristic value and saves it to that elements 'hueristic' field
	public void findElementHueristics(Queen[] queenArray) {

		for (int i = 0; i < queenArray.length; i++) {
			int neighborHueristic = 0;
			for (int k = i; k < queenArray.length; k++) { // I think the issue with the wrong moves being made is due to
															// this loops implementation
				if (k != i) {
					if (queenArray[i].conflicts(queenArray[k])) {
						neighborHueristic++;
					}
				}
			}

			queenArray[i].setHueristic(neighborHueristic); // sets the hueristic field of each element in the array of
															// queens
		}
	}

//betterBoard is used to iterate through all possible moves and return the best possible board given the current board. 
	// This means either making a move and returning the updated board, or returning
	// a newly generated board
	public Queen[] betterBoard(Queen[] queenArray) {

		Queen[] startingArray = queenArray;
		Queen[] testArray = new Queen[8];
		int[] startingHueristic = new int[8];
		int[] newHueristic = new int[8];
		int foundIndices[] = new int[8];
		int bh = 999;

		// initializes the temp array with all the same values stored in the
		// startingArray
		for (int l = 0; l < queenArray.length; l++) {
			testArray[l] = new Queen(startingArray[l].getRow(), startingArray[l].getColumn());
		}

		// iterates through each possible move and stores the best move based on
		// hueristic value
		for (int i = 0; i < startingArray.length; i++) {
			findElementHueristics(startingArray); // calculates/updates each elements hueristic value
			startingHueristic[i] = startingArray[i].getHueristic();
			int bestElementHueristic = startingHueristic[i];
			bh = bestElementHueristic;

			for (int k = 0; k < 8; k++) // this inner loop is used to go through each possible row for a queen, and then
										// checks the new heuristic value
			{

				testArray[i].setRow(k); // sets the row for each iteration
				findElementHueristics(testArray); // recalculates heuristic and sets the value

				if (testArray[i].getHueristic() < bh) {
					bestElementHueristic = testArray[i].getHueristic();
					bh = bestElementHueristic;

					foundIndices[i] = k; // holds the row number that yielded the best hueristic value if it were moved
											// to there
				}

			}
			newHueristic[i] = bh;
		}

		// iterates the arrays holding old and new hueristics and finds the min so it
		// can be used to make a decision

		int found = 20;
		int min = 999;
		int betterNeighbors = 0;

		for (int j = 0; j < 8; j++) {
			// System.out.println("Start: " + startingHueristic[j] + " New: " +
			// newHueristic[j]); //Used for debugging!!

			if ((newHueristic[j] < startingHueristic[j])) // checks to see if the newly calculated lowest heuristic is
															// lower than each elements existing heuristic
			{
				// checks if this heuristic is the lowest of the collection (IE: best move)
				if (newHueristic[j] < min) {
					min = newHueristic[j];
					int f = j;
					found = f;
					betterNeighbors++;
				}

				else {
					betterNeighbors++;
				}

			}
		}

		// used if there are no moves left, does a random restart and generates a new
		// board and saves it to be returned on line 180
		if (betterNeighbors == 0) {
			System.out.println("Number of neighbors found with lower Hueristic: " + betterNeighbors);
			System.out.println("Local minima reached, restarting!");
			startingArray = generateBoard();
			restarts++;
		}

		// used to print the number of better neighbors as well as making the best move
		// that was calculated
		else {
			System.out.println("Number of neighbors found with lower Hueristic: " + betterNeighbors);
			// System.out.println("New best found at place: " + (found+1)); //used for
			// debugging!!
			System.out.println("Changing State! ");
			startingArray[found].setRow(foundIndices[found]);
			stateChanges++;

		}
		return startingArray;

	}

	// used to return the state changes and random restarts fields of the object
	public void stats() {
		System.out.println("State Changes: " + stateChanges + "\nRandom Restarts: " + restarts);
	}

}