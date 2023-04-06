package minesweeper;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.Dimension;

/**
 * The Cell class model the cells of the MineSweeper, by customizing (subclass)
 *   the javax.swing.JButton to include row/column and states.
 */
public class Cell extends JButton {
   private static final long serialVersionUID = 1L;  // to prevent serial warning

   // Define named constants for JButton's colors and fonts
   //  to be chosen based on cell's state
   public static final Color BG_NOT_REVEALED = Color.GRAY;
   public static final Color FG_NOT_REVEALED = Color.RED;    // flag, mines
   public static final Color BG_REVEALED = Color.BLACK;
   public static final Color FG_REVEALED = Color.YELLOW; // number of mines
   public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

  
   private ImageIcon susIcon = new ImageIcon("/Users/irfansyakir/Documents/OOP-Minesweeper/assets/sus.png");
   private ImageIcon imposterIcon = new ImageIcon("/Users/irfansyakir/Documents/OOP-Minesweeper/assets/among-us-kill.png");

   // Define properties (package-visible)
   /** The row and column number of the cell */
   int row, col;
   /** Already revealed? */
   boolean isRevealed;
   /** Is a mine? */
   boolean isMined;
   /** Is Flagged by player? */
   boolean isFlagged;

   /** Constructor */
   public Cell(int row, int col) {
      super();   
      this.row = row;
      this.col = col;
      // Set JButton's default display properties
      super.setFont(FONT_NUMBERS);
      super.setPreferredSize(new Dimension(100,100));
      
      super.updateUI();
      super.setFocusable(false);
      
   }

   /** Reset this cell, ready for a new game */
   public void newGame(boolean isMined) {
      this.isRevealed = false; // default
      this.isFlagged = false;  // default
      this.isMined = isMined;  // given
      super.setEnabled(true);  // enable button
      super.setText("");       // display blank
      super.setIcon(null);
      paint();
   }

   /** Reset this cell, ready for a new game */
   public void startGame(boolean isMined) {
      this.isRevealed = false; // default
      if (!this.isFlagged)
      this.isFlagged = false;  // default
      this.isMined = isMined;  // given
      super.setEnabled(true);  // enable button
      super.setText("");       // display blank
      
      paint();
   }

   /** Paint itself based on its status */
   public void paint() {
      super.setForeground(isRevealed? FG_REVEALED: FG_NOT_REVEALED);
      super.setBackground(isRevealed? BG_REVEALED: BG_NOT_REVEALED);
      
      // for macOS only
      super.setOpaque(true);
      super.setBorderPainted(false); 
      
   }

   public void sus(){
      super.setIcon(susIcon);
   }

   public void unSus(){
      super.setIcon(null);
   }

   public void imposter(){
      super.setIcon(imposterIcon);
   }

}