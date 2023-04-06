
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
   public static String DIFICULTY;
   public static int ROWS = 10;      // number of cells
   public static int COLS = 10;

   // Define named constants for UI sizes
   public static int CELL_SIZE = 60;  // Cell width and height, in pixels
   public static int CANVAS_WIDTH;
   public static int CANVAS_HEIGHT;

   // Define properties (package-visible)
   /** The game board composes of ROWSxCOLS cells */
   private Cell cells[][];
   /** Number of mines */
   private int numMines = 0;
   private int flags = 0;

   private CellMouseListener listener;
   private boolean gameStarted = false;
   private boolean gameOver = false;

   private Clip deathClip;


   /** Constructor */
   public GameBoardPanel(String difficulty) {
      GameBoardPanel.DIFICULTY = difficulty;

      if (DIFICULTY.matches("EASY")) {
         ROWS = 9;
         COLS = 9;
         numMines = 10;
      } else if (DIFICULTY.matches("MEDIUM")) {
         ROWS = 16;
         COLS = 16;
         numMines = 40;
      } else if (DIFICULTY.matches("HARD")) {
         ROWS = 16;
         COLS = 20;
         numMines = 64;
      } else {
         
         System.out.println("Invalid difficulty");
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
         // Main Menu Theme
         File file = new File("/Users/irfansyakir/Documents/OOP-Minesweeper/assets/death.wav"); // change this to the "main_menu.wav" on windows
         AudioInputStream death = AudioSystem.getAudioInputStream(file);
         deathClip = AudioSystem.getClip();
         deathClip.open(death);
  

         

     } catch (Exception e) { 
         e.printStackTrace();
     }

     

      // Set the size of the content-pane and pack all the components
      //  under this container.
      super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
   }

   // Initialize and re-initialize a new game
   public void newGame() {
      gameStarted = false;
      gameOver = false;
      flags = 0;
      deathClip.setFramePosition(0);
      // Reset cells, mines, and flags
      for (int row = 0; row < ROWS; row++) {
         for (int col = 0; col < COLS; col++) {
            // Initialize each cell without mine
            cells[row][col].newGame(false);

            // removes the mouse listeners after new game is started only after first game is started
            for (MouseListener ml : cells[row][col].getMouseListeners()) {
               cells[row][col].removeMouseListener(ml);
           }

            cells[row][col].addMouseListener(listener);     
         }
      }
   }

   private void initialize(int currentRow, int currentCol) {
       // Get a new mine map
       MineMap mineMap = new MineMap();
       mineMap.newMineMap(numMines, ROWS, COLS, currentRow, currentCol);

       // Reset cells, mines, and flags
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
      if (flag) {

         if (flags != numMines) {
            // flag the selected cell
            System.out.println("You flagged(" + srcRow + "," + srcCol + ")");
            cells[srcRow][srcCol].isFlagged = true;
            cells[srcRow][srcCol].sus();
            flags++;
         }
        
      } else {
         // unflag the selected cell
         System.out.println("You unflagged(" + srcRow + "," + srcCol + ")");
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
      System.out.println("Cells that are not mined: " + unMinedCells);
      int cellRevealedCount = 0;
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            if(cells[row][col].isRevealed) {
               ++cellRevealedCount;
            }
         }
      } 
      System.out.println(cellRevealedCount);
      return cellRevealedCount == unMinedCells;
   
   }

   public void loss() {
      for (int row = 0; row < ROWS; ++row) {
          for (int col = 0; col < COLS; ++col) {
              cells[row][col].removeMouseListener(listener);
              if (cells[row][col].isMined)
               cells[row][col].imposter();
          }
      } 
  
      if (!gameOver) {
         //JOptionPane.showMessageDialog(null, "Game Over!");
         JDialog dialog = new JDialog();
         dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

         try {
         deathClip.start();
         // Load the animated GIF from file
         ImageIcon icon = new ImageIcon("/Users/irfansyakir/Documents/OOP-Minesweeper/assets/death.gif");
         JLabel label = new JLabel(icon);
         dialog.getContentPane().add(label, BorderLayout.CENTER);
         dialog.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
         dialog.pack();
         dialog.setResizable(false);
         dialog.setLocationRelativeTo(null);
         dialog.setVisible(true);
         
         } catch (Exception e) {
            e.printStackTrace();
         }

         gameOver = true;
      }
  }
  

   // [TODO 2] Define a Listener Inner Class
   private class CellMouseListener extends MouseAdapter {

      @Override
      public void mousePressed(MouseEvent e) {         // Get the source object that fired the Event
         Cell sourceCell = (Cell)e.getSource();
         // For debugging
         
         // Left-click to reveal a cell; Right-click to plant/remove the flag.
         if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked

            if (!sourceCell.isFlagged) {

               if (gameStarted) {
                  System.out.println("You clicked on (" + sourceCell.row + "," + sourceCell.col + ")");
                  // [TODO 5] (later, after TODO 3 and 4
                  // if you hit a mine, game over
                  // else reveal this cell
                  if (sourceCell.isMined) {
                     System.out.println("Loss");
                     loss();
                  } else {
                  revealCell(sourceCell.row, sourceCell.col);
                  // System.out.println(sourceCell[sourceCell.row][sourceCell.col].isRevealed)
                  if (hasWon())
                     JOptionPane.showMessageDialog(null, "You won!");
                  }
               } else {
                  initialize(sourceCell.row, sourceCell.col);
                  revealCell(sourceCell.row, sourceCell.col);
                  gameStarted = true;
               }

            }
         } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
            
            // If this cell is flagged, remove the flag
            // else plant a flag.
            // ......
            // [TODO 6]
            if (!sourceCell.isRevealed) {
               if (sourceCell.isFlagged) {
                  
                  flagCell(sourceCell.row, sourceCell.col, false);
               } else {
                  
                  flagCell(sourceCell.row, sourceCell.col, true);
               }
            }    
         }
      }
   }

   
  
}