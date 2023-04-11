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
public class MineSweeperMain extends JFrame{
   private static final long serialVersionUID = 1L;  // to prevent serial warning

   // private variables


   // components
   GameBoardPanel board;
   // JButton btnNewGame = new JButton("New Game");
   JMenuBar menuBar = new JMenuBar();

   JMenu gameMenu = new JMenu("Game");
   JMenuItem newGame = new JMenuItem("New Game");
   JMenuItem resetGame = new JMenuItem("Reset Game");
   JMenuItem exit = new JMenuItem("Exit");

   JMenu options = new JMenu("Options");
   JMenu difficultySubMenu = new JMenu("Change Difficulty");
   JMenuItem easy = new JMenuItem("Easy");
   JMenuItem medium = new JMenuItem("Medium");
   JMenuItem hard = new JMenuItem("Hard");

   JMenu help = new JMenu("Help");
   JMenuItem hints = new JMenuItem("Hints");
   JMenu cheats = new JMenu("Cheats");
   JMenuItem revealMines = new JMenuItem("Reveal Mines");

   


   @SuppressWarnings("unused")
   private String playerName;
   private String difficulty;


   // Constructor to set up all the UI and game components
   public MineSweeperMain(String playerName, String difficulty) {
      this.playerName = playerName;
      this.difficulty = difficulty;

      board = new GameBoardPanel(playerName, difficulty);

      // btnNewGame.setForeground(Color.black);
      
      menuBar.add(gameMenu);
      menuBar.add(options);
      menuBar.add(help);

      gameMenu.add(newGame);
      gameMenu.add(resetGame);
      gameMenu.add(exit);
   
      options.add(difficultySubMenu);
      difficultySubMenu.add(easy);
      difficultySubMenu.add(medium);
      difficultySubMenu.add(hard);

      help.add(hints);
      help.add(cheats);
      cheats.add(revealMines);

      newGame.addActionListener(actionListener);
      resetGame.addActionListener(actionListener);
      exit.addActionListener(actionListener);
      easy.addActionListener(actionListener);
      medium.addActionListener(actionListener);
      hard.addActionListener(actionListener);
      revealMines.addActionListener(actionListener);

      setJMenuBar(menuBar);
      
      Container cp = this.getContentPane();           // JFrame's content-pane
      cp.setLayout(new BorderLayout()); // in 10x10 GridLayout
      cp.add(board, BorderLayout.CENTER);

      board.newGame();

      pack();  // Pack the UI components, instead of setSize()
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
      setTitle("Impostersweeper");
      setLocationRelativeTo(null); // set location to the center of the screen
      setVisible(true);   // show it
   }


   private ActionListener actionListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

         if (e.getSource() == newGame) {
            new WelcomeScreen();
            dispose();
         } 
         
         if (e.getSource() == resetGame) {
            board.newGame();
         }
         
         if (e.getSource() == exit) {
            System.exit(0);
         }

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

         if (e.getSource() == hard) {
            new MineSweeperMain(playerName, "HARD");
            dispose();
         }

         if (e.getSource() == revealMines) {
            if (!board.revealMinesCheat) {
               System.out.println("RevealMines Cheat Activated");
               board.activateCheats("REVEAL MINES");
            }
            else {
               System.out.println("RevealMines Cheat Deactivated");
               board.deactivateCheats("REVEAL MINES");
            }
         }

      }
   };

}