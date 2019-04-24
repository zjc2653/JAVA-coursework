import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;



public class GameBoard extends JFrame {
	
	/**
	 * GameBoard creates the GUI which will display the Sudoku game.
	 * Stores each number tile <NumTile> in two 2-dimensional arrays, one holding the rows and columns,
	 * 	the other holding the actual square objects. 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private static NumTile[][] rowsColumns = new NumTile[9][9];
	private static NumTile[][] squares = new NumTile[9][9];
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private EventHandler eh;
	private JPanel p;

	public static void main(String[] args) {
		GameBoard gb = new GameBoard();
		gb.setVisible(true);
	}
	
	/**
	 * Initiates the Game window, setting the size, close operation, title.
	 * Creates a container, which will hold the components of the game board. Calls a method to create game board.
	 * Creates an Event Handler to handle the actions of user clicks, in this case on menus.
	 * Initializes the game board by calling methods initializeBoard which sets up a game with a given amount of 
	 * tiles filled in, and buildMenu which creates the user menu.
	 */
	
	
	public GameBoard() {		
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Sudoku");
		
		Container c = getContentPane();
		c.add(createGamePanel());
		
		eh = new EventHandler();
		
		initializeBoard();
		buildMenu();
		pack();
	}
	
	
	/**
	 * Creates the game panel by setting its background color, layout, title, header, and number tile grid.
	 * 
	 * Creates tile grid by creating 81 tiles in a 9x9 layout of 9x9 squares. Every other 9x9 square is shaded blue.
	 * 
	 * Each tile is assigned a mouseListener and keyListener.
	 * 
	 * The mouseListener detects when the user hovers the mouse over a tile, and displays a tool tip which contains
	 * 	the string result of a dynamically updated Helper object, which calculates which numbers are allowed in the cell.
	 * 
	 * The keyListener restricts the value typed into the tiles to be only numbers or a backspace,
	 *  and interacts with the string returned by Helper to further restrict values to those allowed in the cell.
	 *   
	 * @return JPanel p which is the interactive game interface.
	 */
	
	
	private JPanel createGamePanel() {
		
		p = new JPanel();
		p.setBackground(Color.WHITE);
		p.setLayout(new BorderLayout());
		
		JLabel header = new JLabel("Sudoku", SwingConstants.CENTER);	
		header.setFont(new Font("Arial", Font.PLAIN, 50));
		header.setForeground(Color.BLACK);
		
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(3,3));
		
		int rowCount = 0;
		int columnCount = 0;
		int level = 0;
		int tick = 0;
		int tock = 0;

		for (int i = 0; i<9; i++) { 

			if (i < 3)
				level = 0;
			if (i > 2 & i < 6)
				level = 3;
			if (i > 5)
				level = 6;
			
			if (tick > 6)
				tick = 0;
			
			JPanel s = new JPanel();
			s.setLayout(new GridLayout(3,3));
			s.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			
			for (int j = 0; j < 9; j++) {
				if (tock > 2)
					tock = 0;
				rowCount = (j / 3) + level;
				columnCount = tick+tock;
				
				NumTile n = new NumTile();		

				n.addMouseListener(new MouseAdapter() {
					int row = 0;
					int col = 0;
					public void mouseEntered(MouseEvent e) {
						row = 0;
						col = 0;
						boolean found = false;
						while(!found) {
							for (int i = 0; i < 9; i++) {
								for (int j = 0; j < 9; j++) {
									NumTile temp = (NumTile) e.getSource();
									if (temp.equals(rowsColumns[i][j])) {
										row = i;
										col = j;
										found = true;
									}
								}
							}
						}
						if (rowsColumns[row][col].getNum().isEmpty()) {
							Helper help = new Helper(rowsColumns, squares, row, col);
							String stringHelp = help.useHelper();
							rowsColumns[row][col].setToolTipText(stringHelp);
							n.addKeyListener(new KeyAdapter() {
								   public void keyTyped(KeyEvent e) {
									   
									  Helper help = new Helper(rowsColumns, squares, row, col);
									  String stringHelp = help.useHelper();
									  
									  boolean isValidKey = true;
									  boolean ok = false;
								      char c = e.getKeyChar();
								      if ( (!Character.isDigit(c)) && (c != KeyEvent.VK_BACK_SPACE)) 
								    	 isValidKey = false;
								      
								      if (isValidKey) {
								    	  for (int x = 0; x < stringHelp.length(); x++) {
								    		  if(stringHelp.charAt(x) == c)
								    			  ok = true;
								    	  }
								      }
								      if (!isValidKey || !ok)
								    	  e.consume();
								   }
								});
						}
						else
							rowsColumns[row][col].setToolTipText("");
					}
				});
				
				if (i%2 == 1)
					n.setBackground(Color.CYAN);
				if (i%2 == 0)
					n.setBackground(Color.WHITE);
				s.add(n);
				squares[i][j] = n;
				rowsColumns[rowCount][columnCount] = n;
				
				tock ++;
			}
			
			tick = tick + 3;
			grid.add(s);
		}
		
		p.add(grid, BorderLayout.CENTER);
		p.add(header, BorderLayout.NORTH);
		return p;
	}

	/**
	 * Initializes the board by setting up a new game that already has numbers filled in, which the user cannot
	 * 	edit at any point in the game.
	 * These given numbers are shaded gray in order to make it obvious that they are uneditable.
	 */
	
	public void initializeBoard() {

		rowsColumns[0][1].setNum("4") ;
		rowsColumns[0][6].setNum("2") ;
		rowsColumns[0][7].setNum("9");
		rowsColumns[1][2].setNum("8");
		rowsColumns[1][4].setNum("3");
		rowsColumns[1][5].setNum("7");
		rowsColumns[2][0].setNum("9");
		rowsColumns[2][2].setNum("6");
		rowsColumns[2][8].setNum("3");
		rowsColumns[3][0].setNum("4");
		rowsColumns[3][1].setNum("5");
		rowsColumns[3][5].setNum("2");
		rowsColumns[3][8].setNum("7");
		rowsColumns[4][3].setNum("7");
		rowsColumns[4][5].setNum("3");
		rowsColumns[5][0].setNum("8");
		rowsColumns[5][3].setNum("6");
		rowsColumns[5][7].setNum("2");
		rowsColumns[5][8].setNum("5");
		rowsColumns[6][0].setNum("6");
		rowsColumns[6][6].setNum("3");
		rowsColumns[6][8].setNum("9");
		rowsColumns[7][3].setNum("3");
		rowsColumns[7][4].setNum("4");
		rowsColumns[7][6].setNum("7");
		rowsColumns[8][1].setNum("7");
		rowsColumns[8][2].setNum("2");
		rowsColumns[8][7].setNum("4");
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (! rowsColumns[i][j].getNum().isEmpty()) {
					rowsColumns[i][j].setForeground(Color.GRAY);
					rowsColumns[i][j].setEditable(false);
				}
			}
		}
	}

	
	/**
	 * Builds a menu with sub-menu "Game", which allows the user to select from options "New" to clear the tiles,
	 * 	"Read" to load a previously saved game and read in its progress, "Save" to save current game progress to
	 * 	be continued later, and "Exit" to exit the game.
	 * 
	 * The menu is placed in the JFrame.
	 */
	public void buildMenu() {
		
		menuBar = new JMenuBar();

		fileMenu = new JMenu("Game");
		
		JMenuItem menuItem = new JMenuItem("New");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);
		
		menuItem = new JMenuItem("Read");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);
		
		menuItem = new JMenuItem("Save");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);
		
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(eh);
		fileMenu.add(menuItem);
		
		menuBar.add(fileMenu);
			
		setJMenuBar(menuBar);

	}
	
	/**
	 * Creates a new game by accessing each number tile, and if it is an editable tile (not filled in by default),
	 * 	it sets the text inside the tile to an empty string.
	 */
	
	public void newGame(){
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (! rowsColumns[i][j].getNum().isEmpty()) {
					if(rowsColumns[i][j].isEditable())
						rowsColumns[i][j].setNum("");
				}
			}
		}
	}
	
	private class EventHandler implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			/**
			 * "New" clears all the cells and re-initializes the board, allowing the user
			 *  to start over and play a new game
			 */
			if (arg0.getActionCommand().equals("New")){
				newGame();
			}
			
			/**
			 * "Read" allows the user to use a JFileChooser to open and read a text file that describes
			 *  a Sudoku puzzle
			 */
			if (arg0.getActionCommand().equals("Read")){
				JFileChooser chooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Text file", new String[] {"txt"});
				chooser.addChoosableFileFilter(filter);
				chooser.setFileFilter(filter);
                chooser.showOpenDialog(p);
                File file = chooser.getSelectedFile();
                newGame();
                int count = 0;
                try {
					Scanner s = new Scanner(file);
					while (s.hasNextLine()) {
	    				String line = s.nextLine();
	    				line = line.trim();
	    				int row = count/9;
	    				int col = count%9;
	    				rowsColumns[row][col].setNum(line);
	    				count ++;
					}
					s.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			/**
			 * "Save" allows the user to use a JFileChooser to save the current state of the puzzle to a text file.
			 *  The user can later use the "Read" function to resume the puzzle from the saved state.
			 */
			
			if (arg0.getActionCommand().equals("Save")){
				JFileChooser fileChooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Text file", new String[] {"txt"});
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setFileFilter(filter);
				int returnVal = fileChooser.showSaveDialog(p);
				if (returnVal == JFileChooser.APPROVE_OPTION) { 
				    File file = fileChooser.getSelectedFile();
					try {
						FileOutputStream fos = new FileOutputStream(file+".txt");
						OutputStreamWriter osw = new OutputStreamWriter(fos);
						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 9; j++) {
								String data = rowsColumns[i][j].getNum();
								if(i == 8 && j == 8)
									osw.write(data);
								else
									osw.write(data + "\n");
							}
						}
						osw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				fileChooser.setSelectedFile(file);
				}
			}
			
			/**
			 * Exits the program.
			 */
			
			if (arg0.getActionCommand().equals("Exit"))
				System.exit(0);		
		}
	}
}