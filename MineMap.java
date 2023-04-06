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

   // Allow user to change the rows and cols
   public void newMineMap(int numMines, int rows, int cols, int currentRow, int currentCol) {
      System.out.println("First cell: ("+ currentRow + "," + currentCol + ")");
      this.numMines = numMines;
      int totalCells = rows * cols;
      double probability = (double) numMines / totalCells;

      for (int row = 0; row < rows; row++) {
         for (int col = 0; col < cols; col++) {
               if (row == currentRow && col == currentCol) {
                  isMined[row][col] = false;
               } else {
                  if (Math.random() < probability) {
                     isMined[row][col] = true;
                     System.out.println("Mined cell: ("+ row + "," + col + ")");
                  } else {
                     isMined[row][col] = false;
                  }
               }
         }
      }
      
   }
}