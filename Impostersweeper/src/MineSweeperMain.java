import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import javax.swing.*;     // Use Swing's Containers and Components
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
/**
 * The Mine Sweeper Game.
 * Left-click to reveal a cell.
 * Right-click to plant/remove a flag for marking a suspected mine.
 * You win if all the cells not containing mines are revealed.
 * You lose if you reveal a cell containing a mine.
 */
public class MineSweeperMain extends JFrame{
   private static final long serialVersionUID = 1L;  // to prevent serial warning

   // private variables


   // components
   GameBoardPanel board;
   JButton btnNewGame = new JButton("New Game");
   JMenuBar menuBar = new JMenuBar();
   JMenu newGameMenu = new JMenu("New Game");
   JMenu resetGameMenu = new JMenu("Reset Game");
   JMenu optionsMenu = new JMenu("Options");
   JMenu helpMenu = new JMenu("Help");
   JMenu difficultySubMenu = new JMenu("Change Difficulty");
   JMenuItem easy = new JMenuItem("Easy");
   JMenuItem medium = new JMenuItem("Medium");
   JMenuItem hard = new JMenuItem("Hard");


   @SuppressWarnings("unused")
   private String playerName;
   private String difficulty;


   // Constructor to set up all the UI and game components
   public MineSweeperMain(String playerName, String difficulty) {
      this.playerName = playerName;
      this.difficulty = difficulty;

      board = new GameBoardPanel(playerName, difficulty);

      btnNewGame.setForeground(Color.black);
      
      menuBar.add(newGameMenu);
      menuBar.add(resetGameMenu);
      menuBar.add(optionsMenu);
      menuBar.add(helpMenu);
      optionsMenu.add(difficultySubMenu);
      difficultySubMenu.add(easy);
      difficultySubMenu.add(medium);
      difficultySubMenu.add(hard);

     

      add(menuBar);
      setJMenuBar(menuBar);
      
      Container cp = this.getContentPane();           // JFrame's content-pane
      cp.setLayout(new BorderLayout()); // in 10x10 GridLayout

      cp.add(board, BorderLayout.CENTER);

      // Add btnNewGame to the south to re-start the game
      // ......
      //cp.add(btnNewGame, BorderLayout.SOUTH);

      resetGameMenu.addMenuListener(menuListener);
      newGameMenu.addMenuListener(menuListener);
      optionsMenu.addMenuListener(menuListener);
      easy.addActionListener(actionListener);
      medium.addActionListener(actionListener);
      hard.addActionListener(actionListener);

      board.newGame();

      pack();  // Pack the UI components, instead of setSize()
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
      setTitle("Impostersweeper");
      setLocationRelativeTo(null); // set location to the center of the screen
      setVisible(true);   // show it
   }


   private MenuListener menuListener = new MenuListener() {
      @Override
      public void menuSelected(MenuEvent e) {

         if (e.getSource() == newGameMenu) {
            new WelcomeScreen();
            dispose();
         } 
         
         if (e.getSource() == resetGameMenu) {
            board.newGame();
         } 
         
      }

      @Override
      public void menuDeselected(MenuEvent e) {
        
      }

      @Override
      public void menuCanceled(MenuEvent e) {
      }

   };

   private ActionListener actionListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == easy) {
            new MineSweeperMain(playerName, "EASY");
            dispose();
         } 
         
         if (e.getSource() == medium) {
            new MineSweeperMain(playerName, "MEDIUM");
            dispose();
         } 
         
         if (e.getSource() == hard) {
            new MineSweeperMain(playerName, "HARD");
            dispose();
         }
         
      }
   };

}