import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.sound.sampled.*;

import java.io.*;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.text.SimpleDateFormat;  




public class GameBoardPanel extends JPanel {
   private static final long serialVersionUID = 1L;  // to prevent serial warning
   
   //Define named constants for the timer and the shortest timing
    private Timer timer;
    private JLabel timerLabel;
    private int elapsedTime = 0;
    private int highScore = 0;
    private JLabel highScoreLabel;

    JPanel boardPanel;

   // Define named constants for the game properties
   public static String DIFFICULTY;
   public static String PLAYERNAME;
   private String absolutePath; 

   public static int ROWS = 10;      
   public static int COLS = 10;

   // Define named constants for UI sizes
   public static int CELL_SIZE = 60;  // Cell width and height, in pixels
   public static int CANVAS_WIDTH;
   public static int CANVAS_HEIGHT;

   // Define properties (package-visible)
   /** The game board composes of ROWSxCOLS cells */
   private Cell cells[][];
   /** Number of mines and flags */
   private int numMines = 0;
   private int flags = 0;
   private int tilesClicked = 0;

   private CellMouseListener listener;

   private boolean gameStarted = false;
   private boolean gameOver = false;
   public boolean revealMinesCheat = false;
   private boolean minesRevealed = false;

   private Clip deathClip;
   private Clip winClip;

   

   /** Constructor */
   public GameBoardPanel(String name, String difficulty) {
      GameBoardPanel.PLAYERNAME = name;
      GameBoardPanel.DIFFICULTY = difficulty;
      
      createGameBoard(difficulty);
   
      timer = new Timer(1000, new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            elapsedTime++;
            timerLabel.setText("Score: " + Integer.toString(elapsedTime));
         }
      }); 
      timer.start();

      //Allocate a common listener as the MouseEvent listener for all the Cells (JButtons)
      listener = new CellMouseListener();
      
      // Gets the .wav files from the sounds folder
      getSoundFiles();
     
      // Set the size of the content-pane and pack all the components
      //  under this container.
      
   }

   public void createGameBoard(String difficulty) {
      // Changes the number of cells and mines according to the difficulty

      if (difficulty.matches("EASY")) {
         ROWS = 9;
         COLS = 9;
         numMines = 15;
      } else if (difficulty.matches("MEDIUM")) {
         ROWS = 16;
         COLS = 16;
         numMines = 40;
      } else if (difficulty.matches("HARD")) {
         // On Bigger Screens, COLS = 30 and numMines = 99, else COLS = 20 and numMines = 64/80
         ROWS = 16;
         COLS = 40;
         numMines = 99;
      } else {
         
         System.out.println("Invalid difficulty"); // For Debugging
         System.exit(0);
      }
      
      // Creates the board size

      cells = new Cell[ROWS][COLS];
      CANVAS_WIDTH = CELL_SIZE * COLS;
      CANVAS_HEIGHT = CELL_SIZE * ROWS;

      //super.setLayout(new GridLayout(ROWS, COLS, 2, 2));  // JPanel
      super.setLayout(new BorderLayout(2, 2));
      boardPanel = new JPanel(new GridLayout(ROWS, COLS, 2, 2));
      // Allocate the 2D array of Cells, and add it into content-pane.
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            cells[row][col] = new Cell(row, col);
            boardPanel.add(cells[row][col]);
         }
      }

      Font normalFont = new Font("Arial", Font.PLAIN, 25);
      
      super.add(boardPanel, BorderLayout.CENTER);
      super.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
      
      timerLabel = new JLabel("Score: 0", SwingConstants.CENTER);
      timerLabel.setFont(normalFont);

      //"Best Time: "+ highScore 
      highScoreLabel = new JLabel("", SwingConstants.CENTER);
      highScoreLabel.setFont(normalFont);

      JPanel scorePanel = new JPanel(new BorderLayout());
      scorePanel.setBackground(Color.BLACK);
      scorePanel.add(timerLabel,BorderLayout.WEST);
      scorePanel.add(highScoreLabel,BorderLayout.EAST);
      super.add(scorePanel, BorderLayout.NORTH);

      super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

   }

   private void getSoundFiles() {
      absolutePath = System.getProperty("user.dir");
      
      try {
         /* Death Sound Effect
            Change to "./resources/sounds/death.wav" in Windows
            Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/sounds/death.wav" in macOS
         */ 
         File file = new File(absolutePath + "/resources/sounds/death.wav"); // change this to the "main_menu.wav" on windows
         AudioInputStream death = AudioSystem.getAudioInputStream(file);
         deathClip = AudioSystem.getClip();
         deathClip.open(death);

         /* Win Sound Effect
            Change to "./resources/sounds/victory.wav" in Windows
            Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/sounds/victory.wav" in macOS
         */ 

         file = new File(absolutePath + "/resources/sounds/victory.wav");
         AudioInputStream win = AudioSystem.getAudioInputStream(file);
         winClip = AudioSystem.getClip();
         winClip.open(win);
  
     } catch (Exception e) { 
         e.printStackTrace();
     }
   }

   public void restartTimer() {
      timer.stop();
      elapsedTime = 0;
      timerLabel.setText("Score: 0");
      timer.start();
   } 

   // Initialize and re-initialize a new game
   public void newGame() {
      // Reset the variables to the initial state
      restartTimer();
      gameStarted = false;
      gameOver = false;
      revealMinesCheat = false;
      minesRevealed = false;
      flags = 0;
      tilesClicked = 0;
      deathClip.setFramePosition(0);
      winClip.setFramePosition(0);
      // Reset cells, mines, and flags
      for (int row = 0; row < ROWS; row++) {
         for (int col = 0; col < COLS; col++) {
            // Initialize each cell without mine
            cells[row][col].newGame();

            // Removes the existing mouse listeners of each cell
            for (MouseListener ml : cells[row][col].getMouseListeners()) {
               cells[row][col].removeMouseListener(ml);
           }

           // Re-adds the mouse listener to the cell
            cells[row][col].addMouseListener(listener);     
         }
      }
   }

   public void activateCheats(String cheat) {
      if (cheat.equals("REVEAL MINES")) 
         revealMinesCheat = true;
      triggerCheats();
   }

   public void deactivateCheats(String cheat) {
      triggerCheats();
      if (cheat.equals("REVEAL MINES"))
         revealMinesCheat = false;
         
   }

   // Triggers when the user clicks on a cell for the first time
   private void initialize(int currentRow, int currentCol) {
       // Get a new mine map
       MineMap mineMap = new MineMap();
       mineMap.newMineMap(numMines, ROWS, COLS, currentRow, currentCol);

       // Set the Cells with mines
       for (int row = 0; row < ROWS; row++) {
         for (int col = 0; col < COLS; col++) {
            cells[row][col].startGame(mineMap.isMined[row][col]);
               
         }
      }
   }

   // Return the number of mines [0, 8] in the 8 neighboring cells
   //  of the given cell at (srcRow, srcCol).
   private int getSurroundingMines(int srcRow, int srcCol) {
      int numMines = 0;
      for (int row = srcRow - 1; row <= srcRow + 1; row++) {
         for (int col = srcCol - 1; col <= srcCol + 1; col++) {
            // Need to ensure valid row and column numbers too
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
               if (cells[row][col].isMined) numMines++;
            }
         }
      }
      return numMines;
   }

   // Reveal the cell at (srcRow, srcCol)
   // If this cell has 0 mines, reveal the 8 neighboring cells recursively
   private void revealCell(int srcRow, int srcCol) {
      int numMines = getSurroundingMines(srcRow, srcCol);
      if (numMines != 0) 
         cells[srcRow][srcCol].setText(numMines + "");
      cells[srcRow][srcCol].isRevealed = true;
      cells[srcRow][srcCol].paint();  // based on isRevealed
      if (numMines == 0) {
         // Recursively reveal the 8 neighboring cells
         for (int row = srcRow - 1; row <= srcRow + 1; row++) {
            for (int col = srcCol - 1; col <= srcCol + 1; col++) {
            // Need to ensure valid row and column numbers too
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
               if (!cells[row][col].isRevealed && !cells[row][col].isFlagged) 
                  revealCell(row, col);
               }
            }
         }
      }
   }

   // flag or unflag cells if they are not revealed
   private void flagCell (int srcRow, int srcCol, boolean flag) {

      // do only if flagging is true
      if (flag) {
         // Flags the selected cell only if there are flags still available
         if (flags != numMines) {
            // flag the selected cell and set the SUS icon ;)
            System.out.println("You flagged(" + srcRow + "," + srcCol + ")"); // For Debugging
            cells[srcRow][srcCol].isFlagged = true;
            cells[srcRow][srcCol].sus();
            flags++;
         }
      
         // do only if flagging is false
      } else {
         // unflag the selected cell and remove the SUS icon ;)
         System.out.println("You unflagged(" + srcRow + "," + srcCol + ")"); // For Debugging
         cells[srcRow][srcCol].isFlagged = false;
         cells[srcRow][srcCol].unSus();
         flags--;
      }
   }

   private void triggerCheats() {
      if (revealMinesCheat) {
         if (!minesRevealed) {
            // Shows which cells are the imposter
            for (int row = 0; row < ROWS; ++row) {
               for (int col = 0; col < COLS; ++col) {
                  if (cells[row][col].isMined && !cells[row][col].isFlagged) {
                     cells[row][col].imposter();
                  } 
               }
            } 
            minesRevealed = true;
         } 
         else {
            for (int row = 0; row < ROWS; ++row) {
               for (int col = 0; col < COLS; ++col) {
                  if (cells[row][col].isMined && !cells[row][col].isFlagged) {
                     cells[row][col].unSus();
                  } 
               }
            } 
            minesRevealed = false;
         }
      }
   }

   // Return true if the player has won (all cells have been revealed or were mined)
   public boolean hasWon() {
      if (gameOver) {
         return false;
      }
      int unMinedCells = (ROWS * COLS) - numMines;
      System.out.println("Cells that are not mined: " + unMinedCells); // For debugging
      int cellRevealedCount = 0;
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            if(cells[row][col].isRevealed) {
               ++cellRevealedCount;
            }
         }
      } 
      System.out.println("Cells Revealed: " + cellRevealedCount); // For Debugging
      return cellRevealedCount >= unMinedCells;

   }

   // Triggers on Win
   public void win() {
      // Reveal remaining cells and removes the listener from each cell
      for (int row = 0; row < ROWS; ++row){
         for (int col = 0; col < COLS; ++col) {
            cells[row][col].removeMouseListener(listener);
            if (!cells[row][col].isFlagged && !cells[row][col].isMined && !cells[row][col].isRevealed)
               revealCell(row, col);
         }
      }
      
      timer.stop();
      if (elapsedTime < highScore || highScore == 0) {
         highScore = elapsedTime;
         highScoreLabel.setText("Best Time: " + highScore);
         
      }
      writeScore("Won");
      winAnimationDialog();   
   }


   // Creates a new Frame containing the Win Animation
   private void winAnimationDialog() {
      JDialog dialog = new JDialog();
      dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      // Tries to play the death sound and open the death animation gif in a new window
      try {
         winClip.start();
         ImageIcon icon = new ImageIcon(absolutePath + "/resources/images/victory.png");
         JLabel label = new JLabel("Score: ", icon, JLabel.CENTER);
         label.setFont(Cell.FONT_NUMBERS);
         label.setForeground(Color.white);
         label.setVerticalTextPosition(JLabel.BOTTOM);
         label.setHorizontalTextPosition(JLabel.CENTER);
         dialog.setTitle("Time spend to complete: " +elapsedTime + " seconds");
         dialog.getContentPane().add(label, BorderLayout.CENTER);
         dialog.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
         dialog.pack();
         dialog.setResizable(false);
         dialog.setLocationRelativeTo(null);
         dialog.setVisible(true);

         // Closes the dialog when player clicks outside the dialog window
         dialog.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {}
         
            @Override
            public void windowLostFocus(WindowEvent e) {
               dialog.dispose();
               winClip.stop();
            }
         });

      } catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null, "You won!");
      }
   }

  
   private void writeScore(String result) {
      try {
         String timestamp = new SimpleDateFormat("dd.MMMM.yyyy - HH.mm.ss").format(new java.util.Date());
         
         String outputFilePath = absolutePath + "/scores/Impostersweeper Score " + timestamp  +".txt";
         BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
         writer.write("Name: " + PLAYERNAME);
         writer.write("\nResult: " + result);
         writer.write("\nScore: " + elapsedTime);
         writer.write("\nDifficulty: " + DIFFICULTY);
         writer.write("\nTiles Clicked: " + tilesClicked);
         writer.write("\nFlags Placed: " + flags);
         
         writer.close();
       } catch (IOException e) {
         e.printStackTrace();
       }
   
   }

   // Triggers on Loss
   private void loss(int currentRow, int currentCol) {

      // Shows which cells are the imposter
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            cells[row][col].removeMouseListener(listener);
            if (cells[row][col].isMined && (row != currentRow && col != currentCol)) {
               cells[row][col].imposter();
            }
            cells[currentRow][currentCol].dead(); // show the selected cell that caused the loss    
         }
      } 

      // makes sure that this is triggered only once
      if (!gameOver) {
         timer.stop();
         deathAnimationDialog();
         gameOver = true;
         writeScore("Loss");

      }
   }

   private void deathAnimationDialog() {
       // Create a new dialog for the death animation
       JDialog dialog = new JDialog();
       dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       dialog.setTitle("Game Over!");

       // Tries to play the death sound and open the death animation gif in a new window
       try {
         deathClip.start();
         // Load the animated GIF from file
        
         // Select a random death animation
         int randomNumber = (int)(Math.random() * 5) + 1;
         // Change to "./resources/death_animations/" in Windows
         // Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/death_animations/" in macOS
         String filePath = absolutePath + "/resources/death_animations/";

         switch(randomNumber) {
            case 1:
               filePath += "death_gun.gif";
               break;
            case 2:
               filePath += "death_knife_stab.gif";
               break;
            case 3:
               filePath += "death_laser.gif";
               break;
            case 4:
               filePath += "death_snap_neck.gif";
               break;
            case 5:
               filePath += "death_tongue.gif";
               break;
            default:
               break;
            }

         ImageIcon icon = new ImageIcon(filePath);
         JLabel label = new JLabel(icon);
         dialog.getContentPane().add(label, BorderLayout.CENTER);
         dialog.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
         dialog.pack();
         dialog.setResizable(false);
         dialog.setLocationRelativeTo(null);
         dialog.setVisible(true);

         // Closes the dialog when player clicks outside the dialog window
         dialog.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {}
        
            @Override
            public void windowLostFocus(WindowEvent e) {
                dialog.dispose();
                deathClip.stop();
            }
        });

       } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(null, "Game Over!");
       }
   }
  
   private class CellMouseListener extends MouseAdapter {
      
      @Override
      public void mousePressed(MouseEvent e) {       // Get the source object that fired the Event
         // Left-click to reveal a cell; Right-click to plant/remove the flag.
         Cell sourceCell = (Cell)e.getSource();
         
         // Triggers on Left-click 
         if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
            tilesClicked++;
            // If this cell is not flagged, reveal it
            if (!sourceCell.isFlagged) {
               // Checks if the game has started
               if (gameStarted) {
                  System.out.println("You clicked on (" + sourceCell.row + "," + sourceCell.col + ")"); // For Debugging
                 
                  // If the source cell is mined, invoke loss()
                  if (sourceCell.isMined) {
                     System.out.println("Loss"); // For Debugging
                     loss(sourceCell.row, sourceCell.col);

                  // else reveal the cell and check if the player has won
                  } else {
                     revealCell(sourceCell.row, sourceCell.col);
                     // invoke win() if player has won
                     if (hasWon())
                        win();
                  }
                  // If game has not started, initialise the mine map and start the game
               } else {
                  initialize(sourceCell.row, sourceCell.col); // Initializes the mine map
                  revealCell(sourceCell.row, sourceCell.col); // Reveal the cell after clicking
                  gameStarted = true;
               }

            }

         // Triggers on Right Click
         } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
            
            // If this cell is flagged, remove the flag
            // else plant a flag
            if (!sourceCell.isRevealed) { // Makes sure that the player can't flag revealed cells
               if (sourceCell.isFlagged)          
                  flagCell(sourceCell.row, sourceCell.col, false); // Unflag cell if the selected flag is already flag
               else  
                  flagCell(sourceCell.row, sourceCell.col, true); // Flag cell if the selected flag is not flagged
         }    

         // Triggers on Middle Click (FOR DEBUGGING ONLY, to be removed/ activated on by cheats)
         } else if (e.getButton() == MouseEvent.BUTTON2) {
               
         }
      }
   }
}
