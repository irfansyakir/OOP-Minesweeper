import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import javax.swing.*;     // Use Swing's Containers and Components
/**
 * The Mine Sweeper Game.
 * Left-click to reveal a cell.
 * Right-click to plant/remove a flag for marking a suspected mine.
 * You win if all the cells not containing mines are revealed.
 * You lose if you reveal a cell containing a mine.
 */
public class MineSweeperMain extends JFrame {
   private static final long serialVersionUID = 1L;  // to prevent serial warning

   // private variables
   @SuppressWarnings("unused")
   private String playerName;
   private String DIFFICULTY;

   // components
   GameBoardPanel board;
   JButton btnNewGame = new JButton("New Game");

   // Constructor to set up all the UI and game components
   public MineSweeperMain(String playerName, String difficulty) {
      this.playerName = playerName;
      this.DIFFICULTY = difficulty;
      board = new GameBoardPanel(playerName, this.DIFFICULTY);

      btnNewGame.setForeground(Color.black);
      
      Container cp = this.getContentPane();           // JFrame's content-pane
      cp.setLayout(new BorderLayout()); // in 10x10 GridLayout

      cp.add(board, BorderLayout.CENTER);

      // Add btnNewGame to the south to re-start the game
      // ......
      cp.add(btnNewGame, BorderLayout.SOUTH);
      btnNewGame.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            board.newGame();
         }
      });

      board.newGame();

      pack();  // Pack the UI components, instead of setSize()
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
      setTitle("Impostersweeper");
      setLocationRelativeTo(null); // set location to the center of the screen
      setVisible(true);   // show it
   }

   
}