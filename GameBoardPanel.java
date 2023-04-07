
package minesweeper;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.sound.sampled.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class GameBoardPanel extends JPanel {
   private static final long serialVersionUID = 1L;  // to prevent serial warning

   

   // Define named constants for the game properties
   public static String DIFFICULTY;
   public static int ROWS = 10;      // number of cells
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

   private CellMouseListener listener;

   private boolean gameStarted = false;
   private boolean gameOver = false;

   private Clip deathClip;
   private Clip winClip;

   /** Constructor */
   public GameBoardPanel(String difficulty) {
      GameBoardPanel.DIFFICULTY = difficulty;

      if (DIFFICULTY.matches("EASY")) {
         ROWS = 9;
         COLS = 9;
         numMines = 10;
      } else if (DIFFICULTY.matches("MEDIUM")) {
         ROWS = 16;
         COLS = 16;
         numMines = 40;
      } else if (DIFFICULTY.matches("HARD")) {
         // On Bigger Screens, COLS = 30 and numMines = 99, else COLS = 20 and numMines = 64/80
         ROWS = 16;
         COLS = 30;
         numMines = 99;
      } else {
         
         System.out.println("Invalid difficulty"); // For Debugging
         System.exit(0);
      }
      cells = new Cell[ROWS][COLS];
      CANVAS_WIDTH = CELL_SIZE * COLS;
      CANVAS_HEIGHT = CELL_SIZE * ROWS;

      super.setLayout(new GridLayout(ROWS, COLS, 2, 2));  // JPanel

    
      // Allocate the 2D array of Cell, and added into content-pane.
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            cells[row][col] = new Cell(row, col);
            super.add(cells[row][col]);
         }
      }

      // [TODO 3] Allocate a common listener as the MouseEvent listener for all the
      //  Cells (JButtons)
      listener = new CellMouseListener();
      

      try {
         /* Death Sound Effect
            Change to "./resources/sounds/death.wav" in Windows
            Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/sounds/death.wav" in macOS
         */ 
         File file = new File("./resources/sounds/death.wav"); // change this to the "main_menu.wav" on windows
         AudioInputStream death = AudioSystem.getAudioInputStream(file);
         deathClip = AudioSystem.getClip();
         deathClip.open(death);

         /* Win Sound Effect
            Change to "./resources/sounds/victory.wav" in Windows
            Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/sounds/victory.wav" in macOS
         */ 

         file = new File("./resources/sounds/victory.wav");
         AudioInputStream win = AudioSystem.getAudioInputStream(file);
         winClip = AudioSystem.getClip();
         winClip.open(win);
  
     } catch (Exception e) { 
         e.printStackTrace();
     }

     

      // Set the size of the content-pane and pack all the components
      //  under this container.
      super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
   }

   // Initialize and re-initialize a new game
   public void newGame() {
      // Reset the variables to the initial state
      gameStarted = false;
      gameOver = false;
      flags = 0;
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
               if (!cells[row][col].isRevealed && !cells[row][col].isFlagged) revealCell(row, col);
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
            // flag the selected cell
            System.out.println("You flagged(" + srcRow + "," + srcCol + ")"); // For Debugging
            cells[srcRow][srcCol].isFlagged = true;
            cells[srcRow][srcCol].sus();
            flags++;
         }
      
         // do only if flagging is false
      } else {
         // unflag the selected cell
         System.out.println("You unflagged(" + srcRow + "," + srcCol + ")"); // For Debugging
         cells[srcRow][srcCol].isFlagged = false;
         cells[srcRow][srcCol].unSus();
         flags--;
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

   public void win() {
      // Reveal remaining cells and removes the listener from each cell
      for (int row = 0; row < ROWS; ++row){
         for (int col = 0; col < COLS; ++col) {
            cells[row][col].removeMouseListener(listener);
            if (!cells[row][col].isFlagged && !cells[row][col].isMined && !cells[row][col].isRevealed)
               revealCell(row, col);
         }
      }

      JDialog dialog = new JDialog();
      dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      // Tries to play the death sound and open the death animation gif in a new window
      try {
         winClip.start();
         ImageIcon icon = new ImageIcon("./resources/victory.png");
         JLabel label = new JLabel("Score: ", icon, JLabel.CENTER);
         label.setFont(Cell.FONT_NUMBERS);
         label.setForeground(Color.white);
         label.setVerticalTextPosition(JLabel.BOTTOM);
         label.setHorizontalTextPosition(JLabel.CENTER);
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

   // Triggers on Loss
   public void loss(int currentRow, int currentCol) {

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
         deathAnimationDialog();
         gameOver = true;
      }
   }

   private void deathAnimationDialog() {
       // Create a new dialog for the death animation
       JDialog dialog = new JDialog();
       dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

       // Tries to play the death sound and open the death animation gif in a new window
       try {
         deathClip.start();
         // Load the animated GIF from file
        
         

         // Select a random death animation
         int randomNumber = (int)(Math.random() * 5) + 1;
         // Change to "./resources/death_animations/" in Windows
         // Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/death_animations/" in macOS
         String filePath = "./resources/death_animations/";

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
         
         // Left-click 
         if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
            // If this cell is not flagged, reveal it
            if (!sourceCell.isFlagged) {
               // Checks if the game has started
               if (gameStarted) {
                  System.out.println("You clicked on (" + sourceCell.row + "," + sourceCell.col + ")"); // For Debugging
                 
                  // If the source cell is mined, trigger loss()
                  if (sourceCell.isMined) {
                     System.out.println("Loss"); // For Debugging
                     loss(sourceCell.row, sourceCell.col);

                  // else reveal the cell and check if the player has won
                  } else {
                     revealCell(sourceCell.row, sourceCell.col);
                     // trigger win() if player has won
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
         // Right Click
         } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
            
            // If this cell is flagged, remove the flag
            // else plant a flag
            if (!sourceCell.isRevealed) { // Makes sure that the player can't flag revealed cells
               if (sourceCell.isFlagged)          
                  flagCell(sourceCell.row, sourceCell.col, false); // Unflag cell if the selected flag is already flag
               else  
                  flagCell(sourceCell.row, sourceCell.col, true); // Flag cell if the selected flag is not flagged
            }    
         } else if (e.getButton() == MouseEvent.BUTTON2) {
            // Shows which cells are the imposter
            for (int row = 0; row < ROWS; ++row) {
               for (int col = 0; col < COLS; ++col) {
                  if (cells[row][col].isMined) {
                     cells[row][col].imposter();
                  }
               }
            } 

         }

      }
   }
}