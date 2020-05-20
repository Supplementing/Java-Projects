
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

abstract public class MainWindow extends MouseInputAdapter implements ActionListener {
	protected static JPanel panel;
	private JFrame window;
	static double sizeB = 100;
	static int[] sizes = { 10, 20, 50 };

	public MainWindow() {

		MainWindow me = this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window = new JFrame("A-Star Pathinfinding Visualizer v1.0");
				panel = new JPanel() {

					protected void paintComponent(Graphics g) {

						Dimension d = getCanvas().getSize();
						g.drawRect(0, 0, (int) d.getWidth() - 1, (int) d.getHeight() - 1);

						for (int i = 0; i < sizeB; i += 10) {

							for (int j = 0; j < sizeB; j += 10) {
								g.drawRect(i, j, 10, 10);
							}
						}
						paintCanvas((Graphics2D) g);

					}
				};

				JToolBar tb = new JToolBar();

				JButton help = new JButton("Help");
				JButton f = new JButton("Find Path");
				JButton r = new JButton("Reset Board");

				JComboBox<String> sizeChooser = new JComboBox<String>();
				sizeChooser.addItem("Board size");
				sizeChooser.addItem("10x10");
				sizeChooser.addItem("20x20");
				sizeChooser.addItem("50x50");

				JComboBox<String> densityChooser = new JComboBox<String>();
				densityChooser.addItem("% of blocked nodes");
				densityChooser.addItem("10% Blocked Nodes");
				densityChooser.addItem("25% Blocked Nodes");
				densityChooser.addItem("50% Blocked Nodes");

				r.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						Board.closedList.clear();
						Board.openList.clear();

						Board newBoard = new Board();
						Driver.board = newBoard;
						Driver.sizeBoard = (int) sizeB / 10;
						Driver.boardArray = newBoard.generateBoard(10, (int) (sizeB / 10));
						Driver.reset = true;
						getCanvas().repaint();

					}

				});
				tb.add(densityChooser);
				tb.add(sizeChooser);
				tb.add(f);
				tb.add(r);
				tb.add(help);

				panel.setLayout(new BorderLayout());
				panel.add(tb, BorderLayout.NORTH);

				panel.addMouseListener(me);
				panel.addMouseMotionListener(me);
				window.getContentPane().add(panel);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setPreferredSize(new Dimension(520, 540));
				window.pack();

				// Let a subclass do further configurations
				// like adding listeners
				init();
				window.setVisible(true);

				// all action listeners added below
				// ---------------------------------------------------------------------------------------------------------------
				help.addActionListener(me);

				f.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						Driver.calculationStarted = true;
						Driver.board.calculatepath();

					}

				});

				densityChooser.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
						String selectedSize = (String) comboBox.getSelectedItem();

						switch (selectedSize) {
						case "10% Blocked Nodes":
							Driver.density = 10;
							Driver.boardArray = Driver.board.generateBoard(Driver.density, Driver.sizeBoard);
							break;

						case "25% Blocked Nodes":
							Driver.density = 25;
							Driver.boardArray = Driver.board.generateBoard(Driver.density, Driver.sizeBoard);
							break;
						case "50% Blocked Nodes":
							Driver.density = 50;
							Driver.boardArray = Driver.board.generateBoard(Driver.density, Driver.sizeBoard);
							break;

						default:
							Driver.density = 10;
							Driver.boardArray = Driver.board.generateBoard(Driver.density, Driver.sizeBoard);
							break;
						}

						panel.repaint();
					}
				});

				sizeChooser.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
						String selectedSize = (String) comboBox.getSelectedItem();

						switch (selectedSize) {
						case "10x10":
							sizeB = 10 * 10;
							Driver.sizeBoard = 10;
							Driver.boardArray = Driver.board.generateBoard(Driver.density, 10);
							break;
						case "20x20":
							sizeB = 20 * 10;
							Driver.sizeBoard = 20;
							Driver.boardArray = Driver.board.generateBoard(Driver.density, 20);
							break;
						case "50x50":
							sizeB = 50 * 10;
							Driver.sizeBoard = 50;
							Driver.boardArray = Driver.board.generateBoard(Driver.density, 50);
							break;
						default:
							sizeB = 10 * 10;
							Driver.sizeBoard = 10;
							Driver.boardArray = Driver.board.generateBoard(Driver.density, 10);
							break;
						}

						panel.repaint();

					}

				});
			}

			public void actionPerformed(ActionEvent e) {
			}

		});
	}

	/**
	 * Init the window, once all parts have been created but before it is made
	 * visible. The default method does nothing. The intention is to use this method
	 * to add listeners and do further configuration of the window before it is
	 * displayed.
	 * 
	 * @author mapq
	 */
	public void init() {
	}

	/**
	 * Returns the JPanel where the drawing takes place.
	 * 
	 * @author mapq
	 * @return JPanel that is used for drawing
	 */
	public JPanel getCanvas() {
		return panel;
	}

	/**
	 * Returns the JFrame used in the example.
	 * 
	 * @author mapq
	 * @return JFrame in the app
	 */
	public JFrame getWindow() {
		return window;
	}

	/**
	 * Abstract method that gets called to draw the window content.
	 * 
	 * @author mapq
	 */
	abstract void paintCanvas(Graphics2D g);
}
