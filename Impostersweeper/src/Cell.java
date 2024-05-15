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
   // to be chosen based on cell's state
   public static final Color BG_NOT_REVEALED = Color.GRAY;
   public static final Color FG_NOT_REVEALED = Color.RED;    // flag, mines
   public static final Color BG_REVEALED = Color.BLACK;
   public static final Color FG_REVEALED = Color.YELLOW; // number of mines
   public static final Color BG_DEAD = Color.RED;
   public static final Color BG_IMPOSTER = Color.YELLOW;
   public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

   // Icons for flags and mines
   // Change to "./resources/X.png" in Windows
   // Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/X.png" in macOS
   private String absolutePath = System.getProperty("user.dir");
  
   private ImageIcon susIcon = new ImageIcon(absolutePath + "/Impostersweeper/resources/icons/sus.png");
   private ImageIcon imposterIcon = new ImageIcon(absolutePath + "/Impostersweeper/resources/icons/imposter.png");
   private ImageIcon deadIcon = new ImageIcon(absolutePath + "/Impostersweeper/resources/icons/dead.png");

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
   public void newGame() {
      // Reinitialize the cell to default values
      this.isRevealed = false; // default
      this.isFlagged = false;  // default
      this.isMined = false;  // default
      super.setEnabled(true);  // enable button
      super.setText("");       // display blank
      super.setIcon(null);
      paint();
   }

   /** Sets the Cell to be mined */
   public void startGame(boolean isMined) {
      this.isMined = isMined;  // given
   }

   /** Paint itself based on its status */
   public void paint() {
      super.setForeground(isRevealed? FG_REVEALED: FG_NOT_REVEALED);
      super.setBackground(isRevealed? BG_REVEALED: BG_NOT_REVEALED);
      
      // for macOS only
      super.setOpaque(true);
      super.setBorderPainted(false); 
      
   }

   // Flags the cell
   public void sus() {
      super.setIcon(susIcon);
      super.setBackground(BG_NOT_REVEALED);
   }

   // Unflag the cell
   public void unSus() {
      super.setIcon(null);
      super.setBackground(BG_NOT_REVEALED);
   }

   // Reveal the Imposter
   public void imposter() {
      super.setIcon(imposterIcon);
      super.setBackground(BG_IMPOSTER);
   }

   // Sets a dead body 
   public void dead() {
      super.setIcon(deadIcon);
      super.setBackground(BG_DEAD);
   }

}