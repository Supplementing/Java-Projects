
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class Driver {

	static double squareX = 0;
	static double squareY = 0;
	static Color sC = Color.white;
	static double density = 10;
	static int sizeBoard = 10;
	static Board board = new Board();
	static Node[][] boardArray = board.generateBoard(density, sizeBoard);
	static int startRow = 0;
	static int startCol = 0;
	static int endRow = 1;
	static int endCol = 1;
	static boolean delay = false;
	static boolean calculationStarted = false;
	static boolean reset = false;

//	static Timer timer = new Timer(100, new ActionListener() {
//		@Override
//		public void actionPerformed(ActionEvent e) {
//
//			MainWindow.panel.repaint();
//
//		};

//	});

	public static void main(String args[]) {

		MainWindow w = new MainWindow() {
			private Point startPt;
			private Point endPt;

			public void mousePressed(MouseEvent e) {
				startPt = e.getPoint();

				if (startPt != null && e.isShiftDown() == false) {

					squareX = (Math.floor(startPt.getX() / 10) * 10);
					squareY = (Math.floor(startPt.getY() / 10) * 10);
					startRow = (int) (squareX / 10);
					startCol = (int) (squareY / 10);
					board.setStartingPoint(startRow, startCol);
					sC = Color.green;
					getCanvas().repaint();
				}

				if (startPt != null && e.isShiftDown()) {
					squareX = Math.floor(startPt.getX() / 10) * 10;
					squareY = Math.floor(startPt.getY() / 10) * 10;
					endRow = (int) (squareX / 10);
					endCol = (int) (squareY / 10);
					board.setEndingPoint(endRow, endCol);
					sC = Color.red;
					getCanvas().repaint();
				}

			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseDragged(MouseEvent e) {
			}

			public void paintCanvas(Graphics2D g) {

				if (reset == true) {

					for (int i = 0; i < MainWindow.sizeB; i += 10) {

						for (int j = 0; j < MainWindow.sizeB; j += 10) {
							g.setColor(Color.white);
							g.fillRect(i, j, 10, 10);
							g.setColor(Color.black);
							g.drawRect(i, j, 10, 10);

						}
						reset = false;
					}

				}

				else {
					if (startPt != null) {
						g.setColor(sC);
						g.fillRect((int) squareX, (int) squareY, 10, 10);
					}

					for (int i = 0; i < boardArray.length; i++) {
						for (int j = 0; j < boardArray.length; j++) {
							if (boardArray[i][j].getType() == 1) {
								g.setColor(Color.black);
								int row = i * 10;
								int column = j * 10;
								g.fillRect(row, column, 10, 10);
							}

							if (!Board.openList.isEmpty()) {
								for (int k = 0; k < Board.openList.size(); k++) {
									g.setColor(Color.cyan);
									int row = Board.openList.get(k).getRow() * 10;
									int column = Board.openList.get(k).getCol() * 10;

									g.fillRect(row, column, 10, 10);
								}
							}

							if (!Board.closedList.isEmpty()) {

								for (int h = 1; h < Board.closedList.size() - 1; h++) {
									g.setColor(Color.yellow);
									int row = Board.closedList.get(h).getRow() * 10;
									int column = Board.closedList.get(h).getCol() * 10;

									g.fillRect(row, column, 10, 10);

								}
							}

						}
					}
				}

			}

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel,
						"Welcome to Pathfinding Visualizer version 1.0. Click on any square to choose a start point, "
								+ "and shift click to choose an endpoint!\nCyan: explored nodes\nYellow: Path");

			}

		};

	}

}
