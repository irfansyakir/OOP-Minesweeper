package minesweeper;
import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import javax.swing.*;
import java.util.Set;
import javax.sound.sampled.*;
import java.io.*;

public class WelcomeScreen extends JFrame {
    

    public WelcomeScreen() {
        super("Minesweeper"); // Set Title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit Program on Close
        setResizable(false);    // Make it unresizable
        setLayout(new BorderLayout(0, 0)); 
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.BLACK);
        UIManager.put("Button.foreground", Color.WHITE);
        setVisible(true);

        /* 
        try {
            File file = new File("a1_ch1_goto4.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            //DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
        } catch (Exception e) { 
            e.printStackTrace();
        }
        */
        

        Font normalFont = new Font("Arial", Font.PLAIN, 25);
	    Font amongus = normalFont;
      
        // Create a JPanel object with BorderLayout to hold the content
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.BLACK);

        // Create a welcome message panel with FlowLayout and add it to the main panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        welcomePanel.setBackground(Color.BLACK);
        JLabel welcomeLabel = new JLabel("Minesweeper");

        welcomePanel.add(welcomeLabel);
        panel.add(welcomePanel, BorderLayout.NORTH);

        // Create an input panel with GridLayout and add it to a sub-panel of the main panel
        JPanel subPanel = new JPanel(new BorderLayout(0, 10));
        subPanel.setBackground(Color.BLACK);
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 0, 5));
        inputPanel.setBackground(Color.BLACK);
        
        // Create a panel for the additional label with FlowLayout
        JPanel additionalLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        additionalLabelPanel.setBackground(Color.BLACK);
        JLabel additionalLabel = new JLabel("There mines among us.");
        
        additionalLabelPanel.add(additionalLabel);
        subPanel.add(additionalLabelPanel, BorderLayout.NORTH);

        try {
            // load a custom font in your project folder
           amongus = Font.createFont(Font.TRUETYPE_FONT, new File("amongus.ttf")).deriveFont(200f);	
           GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
           ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("amongus.ttf")));
           welcomeLabel.setFont(amongus);
           additionalLabel.setFont(amongus.deriveFont(100f));
           
       } catch (Exception e) {
           welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
           additionalLabel.setFont(amongus.deriveFont(100f));
       }

        JLabel playerNameLabel = new JLabel("Crewmate Name: ");
        playerNameLabel.setFont(normalFont);
        inputPanel.add(playerNameLabel);

        JTextField playerNameTextField = new JTextField();
        playerNameTextField.setMargin(new Insets(0, 10, 0, 0)); // add padding of 5 pixels on top, 10 pixels on left, bottom, and right
        playerNameTextField.setFont(normalFont);
        inputPanel.add(playerNameTextField);
        subPanel.add(inputPanel, BorderLayout.CENTER);
        panel.add(subPanel, BorderLayout.CENTER);

        // Create a start button panel with FlowLayout and add the start button to it
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);
    
        JButton startButton = new JButton("Start");
        startButton.setFont(normalFont);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15) // top, left, bottom, right padding
        ));
        
       

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
                        new MineSweeperMain(playerName);  // run the main program
                    }
                });
                //clip.stop();
                dispose();

            }
        });

        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                startButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GREEN, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15) // top, left, bottom, right padding
        ));
            }
            public void mouseExited(MouseEvent e) {
                startButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15) // top, left, bottom, right padding
            ));
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
