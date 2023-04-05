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
   public void newMineMap(int numMines, int rows, int cols) {
      this.numMines = numMines;
      Random rand = new Random();
      for (int i = 0; i < numMines; i++) {
         int randomRow = rand.nextInt(rows);
         int randomCol = rand.nextInt(cols);
         isMined[randomRow][randomCol] = true;
         
      }
      
   }
}