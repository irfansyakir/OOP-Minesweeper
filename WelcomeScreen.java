package minesweeper;
import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import javax.swing.*;
import java.util.Set;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        super("Minesweeper"); // Set Title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit Program on Close
        setResizable(false);    // Make it unresizable
        setLayout(new BorderLayout(0, 0)); 
        setVisible(true);
      
        // Create a JPanel object with BorderLayout to hold the content
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a welcome message panel with FlowLayout and add it to the main panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel welcomeLabel = new JLabel("Welcome to Minesweeper!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomePanel.add(welcomeLabel);
        panel.add(welcomePanel, BorderLayout.NORTH);

        // Create an input panel with GridLayout and add it to a sub-panel of the main panel
        JPanel subPanel = new JPanel(new BorderLayout(0, 10));
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JLabel playerNameLabel = new JLabel("Enter your name:");
        playerNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(playerNameLabel);
        JTextField playerNameTextField = new JTextField();
        inputPanel.add(playerNameTextField);
        subPanel.add(inputPanel, BorderLayout.CENTER);
        panel.add(subPanel, BorderLayout.CENTER);

        // Create a start button panel with FlowLayout and add the start button to it
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(startButton);
        subPanel.add(buttonPanel, BorderLayout.SOUTH);

       
        // Add a KeyListener to the text field
        playerNameTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.doClick(); // Simulate a click on the Start button
            }
        });

        // Create the start button Action Listener

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = playerNameTextField.getText(); // get player name from text field
                System.out.println("Start button pressed by " + playerName);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        String playerName = playerNameTextField.getText(); 
                        new MineSweeperMain(playerName);  // Let the constructor do the job
                    }
                });
                dispose();

            }
        });

        // Set the panel as the content pane of the frame
        setContentPane(panel);
        pack();  // Pack the UI components, instead of setSize()
        // Make the frame visible and centered on the screen
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
       new WelcomeScreen();
    }
}
