
import java.util.ArrayList;

/**
 * @author Mason Herron Board.java creates a board and also has methods that
 *         print the board, calculate the path, set the f, g, and h values for
 *         each node, etc.
 *
 */

public class Board {
	private int size = 10;
	private Node[][] board = new Node[size][size];
	private Node startingNode;
	private Node endingNode;
	static ArrayList<Node> openList = new ArrayList();
	static ArrayList<Node> closedList = new ArrayList();

	// generates the entire board including the blocked nodes, returns a 2d array of
	// node objects
	public Node[][] generateBoard(double percentageBlocked, int size) {
		this.size = size;
		board = new Node[size][size];
//blocks nodes based on what percentage is sent in
		Node[] blockedNodes = new Node[((int) ((percentageBlocked * .01) * (size * size)))];
		boolean duplicates = false;

		// creates new blocked (unpathable) nodes using a random number generation for
		// the row and column
		for (int i = 0; i < blockedNodes.length; i++) {
			blockedNodes[i] = new Node((int) getRandom(0, (size - 1)), (int) getRandom(0, (size - 1)), 1);
		}

		// checks to make sure there arent duplicate blocked nodes to ensure there are
		// 23 (10%) blocked nodes and no less
		do {

			for (int i = 0; i < blockedNodes.length; i++) {
				for (int k = i + 1; k < 10; k++) {
					// if the nodes are in the same place
					if ((blockedNodes[i].getRow() == blockedNodes[k].getRow())
							&& (blockedNodes[i].getCol() == blockedNodes[k].getCol())) {
						duplicates = true;
						blockedNodes[i] = new Node((int) getRandom(0, (size - 1)), (int) getRandom(0, (size - 1)), 1);
					}
				}

			}

		} while (duplicates == true);

		// fills the board with all pathable nodes
		for (int i = 0; i < size; i++) {

			for (int k = 0; k < size; k++) {
				board[i][k] = new Node(i, k, 0);
			}
		}

		// adds the nonpathable nodes in
		for (Node n : blockedNodes) {
			board[n.getRow()][n.getCol()] = n;
		}

		return board;
	}

//sets the starting point
	public void setStartingPoint(int row, int column) {

		this.board[row][column].setStart();
		this.startingNode = this.board[row][column];
	}

	// this method resets clears the old start/end points and clears the open and
	// closed lists
	public void reset() {
		for (int i = 0; i < this.board.length; i++) {
			for (int k = 0; k < this.board.length; k++) {
				if (this.board[i][k].startingPoint == true) {
					this.board[i][k].startingPoint = false;
				} else if (this.board[i][k].endPoint == true) {
					this.board[i][k].endPoint = false;
				}
			}
		}
		openList.clear();
		closedList.clear();

	}

	// sets the user chosen end point
	public void setEndingPoint(int row, int column) {
		this.board[row][column].setEnd();
		this.endingNode = this.board[row][column];
	}

//iterates and prints the boards contents based on each node objects toString method
	public void printBoard() {
		for (int i = 0; i < 15; i++) {
			System.out.println();
			for (int k = 0; k < 15; k++) {

				System.out.print(this.board[i][k].toString());
			}
		}
	}

	// calculate path returns the path in string form, calls the setValues method
	// which does the actual calculations
	public String calculatepath() {

		Node s = this.startingNode;
		Node e = this.endingNode;

		// sets the current node to the starting node to begin
		Node current = s;
		openList.add(s);
//		Driver.delay = true;
		MainWindow.panel.repaint();

		StringBuilder path = new StringBuilder();

		setValues(this.board, current, endingNode);
//do while loop that runs until a path is found(ending node is in closed list) or until it gets to a point that a path cannot be found(the openlist is empty)
		do {
			int lowestF = 9999;
			Node lowestFNode = openList.get(0);
			for (Node n : openList) {
				if (n.getF() < lowestF) {
					lowestF = n.getF();
					lowestFNode = n;
				}
			}

			// removes lowest f node from open list and adds to closed list, then sets the
			// current node to that lowest f value node to be passed to the setvalues method
			// for calculations
			openList.remove(lowestFNode);
			closedList.add(lowestFNode);
			current = lowestFNode;
			setValues(this.board, current, endingNode);
			path.append("(" + lowestFNode.getRow() + ", " + lowestFNode.getCol() + ")");
		} while (closedList.contains(endingNode) == false && openList.isEmpty() == false);

		// returns the path if there is one, and returns no path found if a path cannot
		// be found
		if (closedList.contains(endingNode)) {
			return "Path found: " + path.toString();
		} else {
			return "No path found";
		}

	}

	// setValues does all the calculations and sets the g, h, and F values for each
	// node. There are nested statements that check each possible block around the
	// current
	// and makes sure they're pathable, not off the board, etc, then evaluates.
	public void setValues(Node[][] board, Node currentNode, Node endingNode) {

//code below calculates the g,h,and f values for all surrounding squares

		// --------------------------------------------------------------------------------------------------------------//

		// node to the right

		// checks to make sure its not the right most column before trying to evaluate
		// column to the right
		if (currentNode.getCol() != (this.size - 1)) {

			// checks to make sure that the next node to the right is pathable and not
			// already on closed list
			if (((board[currentNode.getRow()][currentNode.getCol() + 1]).getType() != 1)
					&& (closedList.contains(board[currentNode.getRow()][currentNode.getCol() + 1]) == false)) {

				// checks if the node is already on the open list and if so, compares the values
				// to see if its better to go through the current node or leave it
				if (openList.contains(board[currentNode.getRow()][currentNode.getCol() + 1])) {
					int tempGValue = (board[currentNode.getRow()][currentNode.getCol() + 1].getG());

					// checking to see if the current g value could be made better
					if ((currentNode.getG() + 10) < tempGValue) {
						Node n = board[currentNode.getRow()][currentNode.getCol() + 1];
						// sets the g value equal to + 10 of the current g value
						n.setG(10 + currentNode.getG());
						// manhattan method of calculating heuristic
						n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
								+ Math.abs(endingNode.getRow() - n.getRow())));
						n.setF();
						this.board[n.getRow()][n.getCol()] = n;
					}

				} else {
					// hold the location of the node to the right
					Node n = board[currentNode.getRow()][currentNode.getCol() + 1];
					// sets the g value equal to + 10 of the current g value
					n.setG(10 + n.getG());
					n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
							+ Math.abs(endingNode.getRow() - n.getRow())));
					n.setF();
					openList.add(n);
//					Driver.delay = true;
					MainWindow.panel.repaint();

					this.board[n.getRow()][n.getCol()] = n;
				}

			}
		}

		// --------------------------------------------------------------------------------------------------------------//

		// node to the left
		// checks to make sure it is not the left most column first
		if (currentNode.getCol() != 0) {
			if (((board[currentNode.getRow()][currentNode.getCol() - 1]).getType() != 1)
					&& (closedList.contains(board[currentNode.getRow()][currentNode.getCol() - 1]) == false)) {
				// checks if the node is already on the open list and if so, compares the values
				// to see if its better to go through the current node or leave it
				if (openList.contains(board[currentNode.getRow()][currentNode.getCol() - 1])) {
					int tempGValue = (board[currentNode.getRow()][currentNode.getCol() - 1].getG());

					// checking to see if the current g value could be made better
					if ((currentNode.getG() + 10) < tempGValue) {
						Node n = board[currentNode.getRow()][currentNode.getCol() - 1];
						// sets the g value equal to + 10 of the current g value
						n.setG(10 + currentNode.getG());
						n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
								+ Math.abs(endingNode.getRow() - n.getRow())));
						n.setF();
						this.board[n.getRow()][n.getCol()] = n;
					}

				} else {
					// hold the location of the node to be checked
					Node n = board[currentNode.getRow()][currentNode.getCol() - 1];
					// sets the g value equal to + 10 of the current g value
					n.setG(10 + n.getG());
					n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
							+ Math.abs(endingNode.getRow() - n.getRow())));
					n.setF();
					openList.add(n);
//					Driver.delay = true;
					MainWindow.panel.repaint();

					this.board[n.getRow()][n.getCol()] = n;
				}

			}
		}

		// --------------------------------------------------------------------------------------------------------------//

		// node above
		// checks to make sure its not the top row
		if (currentNode.getRow() != 0) {
			if (((board[currentNode.getRow() - 1][currentNode.getCol()]).getType() != 1)
					&& (closedList.contains(board[currentNode.getRow() - 1][currentNode.getCol()]) == false)) {

				// checks if the node is already on the open list and if so, compares the values
				// to see if its better to go through the current node or leave it
				if (openList.contains(board[currentNode.getRow() - 1][currentNode.getCol()])) {
					int tempGValue = (board[currentNode.getRow() - 1][currentNode.getCol()].getG());

					// checking to see if the current g value could be made better
					if ((currentNode.getG() + 10) < tempGValue) {
						Node n = board[currentNode.getRow() - 1][currentNode.getCol()];
						// sets the g value equal to + 10 of the current g value
						n.setG(10 + currentNode.getG());
						n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
								+ Math.abs(endingNode.getRow() - n.getRow())));
						n.setF();
						this.board[n.getRow()][n.getCol()] = n;
					}

				} else {
					// hold the location of the node to be checked
					Node n = board[currentNode.getRow() - 1][currentNode.getCol()];
					// sets the g value equal to + 10 of the current g value
					n.setG(10 + n.getG());
					n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
							+ Math.abs(endingNode.getRow() - n.getRow())));
					n.setF();

					openList.add(n);

					MainWindow.panel.repaint();

					this.board[n.getRow()][n.getCol()] = n;
				}

			}

		}

		// --------------------------------------------------------------------------------------------------------------//

		// node below
		if (currentNode.getRow() != (size - 1)) {
			if (((board[currentNode.getRow() + 1][currentNode.getCol()]).getType() != 1)
					&& (closedList.contains(board[currentNode.getRow() + 1][currentNode.getCol()]) == false)) {

				// checks if the node is already on the open list and if so, compares the values
				// to see if its better to go through the current node or leave it
				if (openList.contains(board[currentNode.getRow() + 1][currentNode.getCol()])) {
					int tempGValue = (board[currentNode.getRow() + 1][currentNode.getCol()].getG());

					// checking to see if the current g value could be made better
					if ((currentNode.getG() + 10) < tempGValue) {
						Node n = board[currentNode.getRow() + 1][currentNode.getCol()];
						// sets the g value equal to + 10 of the current g value
						n.setG(10 + currentNode.getG());
						n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
								+ Math.abs(endingNode.getRow() - n.getRow())));
						n.setF();
						this.board[n.getRow()][n.getCol()] = n;
					}

				} else {
					// hold the location of the node to be checked
					Node n = board[currentNode.getRow() + 1][currentNode.getCol()];
					// sets the g value equal to + 10 of the current g value, H value is calculated
					// using manhattan method
					n.setG(10 + n.getG());
					n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
							+ Math.abs(endingNode.getRow() - n.getRow())));
					n.setF();
					openList.add(n);
//					Driver.delay = true;
					MainWindow.panel.repaint();

					this.board[n.getRow()][n.getCol()] = n;
				}

			}

		}

		// --------------------------------------------------------------------------------------------------------------//

		// up-right diagonal
		if (currentNode.getRow() != 0 && currentNode.getCol() != (size - 1)) {

			if (((board[currentNode.getRow() - 1][currentNode.getCol() + 1]).getType() != 1)
					&& (closedList.contains(board[currentNode.getRow() - 1][currentNode.getCol() + 1]) == false)) {

				// checks if the node is already on the open list and if so, compares the values
				// to see if its better to go through the current node or leave it
				if (openList.contains(board[currentNode.getRow() - 1][currentNode.getCol() + 1])) {
					int tempGValue = (board[currentNode.getRow() - 1][currentNode.getCol() + 1].getG());

					// checking to see if the current g value could be made better
					if ((currentNode.getG() + 14) < tempGValue) {
						Node n = board[currentNode.getRow() - 1][currentNode.getCol() + 1];
						// sets the g value equal to + 10 of the current g value
						n.setG(14 + currentNode.getG());
						n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
								+ Math.abs(endingNode.getRow() - n.getRow())));
						n.setF();
						// sets the node with its updated fields since it was saved as a local var (n)
						this.board[n.getRow()][n.getCol()] = n;
					}

				} else {
					// hold the location of the node to be checked
					Node n = board[currentNode.getRow() - 1][currentNode.getCol() + 1];
					// sets the g value equal to + 10 of the current g value
					n.setG(14 + n.getG());
					n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
							+ Math.abs(endingNode.getRow() - n.getRow())));
					n.setF();
					openList.add(n);
//					Driver.delay = true;
					MainWindow.panel.repaint();

					this.board[n.getRow()][n.getCol()] = n;
				}

			}
		}

		// --------------------------------------------------------------------------------------------------------------//

		// up-left diagonal
		if (currentNode.getRow() != 0 && currentNode.getCol() != 0) {

			if (((board[currentNode.getRow() - 1][currentNode.getCol() - 1]).getType() != 1)
					&& (closedList.contains(board[currentNode.getRow() - 1][currentNode.getCol() - 1]) == false)) {

				// checks if the node is already on the open list and if so, compares the values
				// to see if its better to go through the current node or leave it
				if (openList.contains(board[currentNode.getRow() - 1][currentNode.getCol() - 1])) {
					int tempGValue = (board[currentNode.getRow() - 1][currentNode.getCol() - 1].getG());

					// checking to see if the current g value could be made better
					if ((currentNode.getG() + 14) < tempGValue) {
						Node n = board[currentNode.getRow() - 1][currentNode.getCol() - 1];
						// sets the g value equal to + 10 of the current g value
						n.setG(14 + currentNode.getG());
						n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
								+ Math.abs(endingNode.getRow() - n.getRow())));
						n.setF();
						this.board[n.getRow()][n.getCol()] = n;
					}

				} else {
					// hold the location of the node to be checked
					Node n = board[currentNode.getRow() - 1][currentNode.getCol() - 1];
					// sets the g value equal to + 10 of the current g value
					n.setG(14 + n.getG());
					n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
							+ Math.abs(endingNode.getRow() - n.getRow())));
					n.setF();
					openList.add(n);
//					Driver.delay = true;
					MainWindow.panel.repaint();

					this.board[n.getRow()][n.getCol()] = n;
				}

			}

		}

		// --------------------------------------------------------------------------------------------------------------//

		// down-right diagonal
		if (currentNode.getRow() != (size - 1) && currentNode.getCol() != (size - 1)) {

			if (((board[currentNode.getRow() + 1][currentNode.getCol() + 1]).getType() != 1)
					&& (closedList.contains(board[currentNode.getRow() + 1][currentNode.getCol() + 1]) == false)) {

				// checks if the node is already on the open list and if so, compares the values
				// to see if its better to go through the current node or leave it
				if (openList.contains(board[currentNode.getRow() + 1][currentNode.getCol() + 1])) {
					int tempGValue = (board[currentNode.getRow() + 1][currentNode.getCol() + 1].getG());

					// checking to see if the current g value could be made better by going through
					// the current node
					if ((currentNode.getG() + 14) < tempGValue) {
						Node n = board[currentNode.getRow() + 1][currentNode.getCol() + 1];
						// sets the g value equal to + 10 of the current g value
						n.setG(14 + currentNode.getG());
						n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
								+ Math.abs(endingNode.getRow() - n.getRow())));
						n.setF();
						this.board[n.getRow()][n.getCol()] = n;
					}

				} else {
					// hold the location of the node to be checked
					Node n = board[currentNode.getRow() + 1][currentNode.getCol() + 1];
					// sets the g value equal to + 10 of the current g value
					n.setG(14 + n.getG());
					n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
							+ Math.abs(endingNode.getRow() - n.getRow())));
					n.setF();
					openList.add(n);
//					Driver.delay = true;
					MainWindow.panel.repaint();

					this.board[n.getRow()][n.getCol()] = n;
				}

			}
		}

		// --------------------------------------------------------------------------------------------------------------//

		// down-left diagonal
		if (currentNode.getRow() != (size - 1) && currentNode.getCol() != 0) {
			if (((board[currentNode.getRow() + 1][currentNode.getCol() - 1]).getType() != 1)
					&& (closedList.contains(board[currentNode.getRow() + 1][currentNode.getCol() - 1]) == false)) {

				// checks if the node is already on the open list and if so, compares the values
				// to see if its better to go through the current node or leave it
				if (openList.contains(board[currentNode.getRow() + 1][currentNode.getCol() - 1])) {
					int tempGValue = (board[currentNode.getRow() + 1][currentNode.getCol() - 1].getG());

					// checking to see if the current g value could be made better
					if ((currentNode.getG() + 14) < tempGValue) {
						Node n = board[currentNode.getRow() + 1][currentNode.getCol() - 1];
						// sets the g value equal to + 10 of the current g value
						n.setG(14 + currentNode.getG());
						n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
								+ Math.abs(endingNode.getRow() - n.getRow())));
						n.setF();
						this.board[n.getRow()][n.getCol()] = n;
					}

				} else {
					// hold the location of the node to be checked
					Node n = board[currentNode.getRow() + 1][currentNode.getCol() - 1];
					// sets the g value equal to + 10 of the current g value
					n.setG(14 + n.getG());
					n.setH(10 * (Math.abs(endingNode.getCol() - n.getCol())
							+ Math.abs(endingNode.getRow() - n.getRow())));
					n.setF();
					openList.add(n);
//					Driver.delay = true;
					MainWindow.panel.repaint();

					this.board[n.getRow()][n.getCol()] = n;
				}

			}

		}
	}

//used to generate random numbers in a range
	public static double getRandom(double min, double max) {
		return (Math.random() * (max + 1 - min)) + min;
	}

}
