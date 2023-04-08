package minesweeper;
import java.util.*;
/**
 * Locations of Mines
 */
public class MineMap {
   // package access
   int numMines;
   boolean[][] isMined = new boolean[GameBoardPanel.ROWS][GameBoardPanel.COLS];
   // default is false

   // Constructor
   public MineMap() {
      super();
   }

   // randomly determine which cells have mines
   public void newMineMap(int numMines, int rows, int cols, int currentRow, int currentCol) {

      System.out.println("First cell: ("+ currentRow + "," + currentCol + ")\n"); // For Debugging
      this.numMines = numMines;
      int totalCells = rows * cols;
      int mines = 0;

      // Finds the probability of getting each cell
      double probability = (double) numMines / totalCells;
   
      // ensures that the number of mines is set correctly
      while (mines != numMines) {
         for (int row = 0; row < rows; row++) {
            if (mines == numMines) break; // break from loop if the number of mines is set correctly
            for (int col = 0; col < cols; col++) {
                  // Makes sure that the first cell the user clicks is not a mine
                  if (mines == numMines) break; // break from loop if the number of mines is set correctly
                  if (row == currentRow && col == currentCol) {
                     isMined[row][col] = false;
                  } else {
                     // If the random number is lower than the probability, set the cell as a mine
                     if (Math.random() < probability) {
                        isMined[row][col] = true;
                        mines++;
                        System.out.println("Mined cell: ("+ row + "," + col + ")"); // For Debugging
                     } else {
                        isMined[row][col] = false;
                     }
                  }
            }
         } 
      }  
   }
}