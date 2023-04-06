package minesweeper;
import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.Set;
import javax.sound.sampled.*;
import java.io.*;

public class WelcomeScreen extends JFrame {

    private Clip clip;
    private Clip uiHoverClip;
    private JRadioButton easyButton;
    private JRadioButton mediumButton;
    private JRadioButton hardButton;
    private ButtonGroup buttonGroup;

    private String difficulty;
    private LineListener lineListener;

    

    public WelcomeScreen() {
        super("Minesweeper"); // Set Title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit Program on Close
        setResizable(false);    // Make it unresizable
        setLayout(new BorderLayout(0, 0)); 

        // Changes the UI Elements' color values
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.BLACK);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("RadioButton.foreground", Color.WHITE);
        UIManager.put("RadioButton.background", Color.BLACK);
        UIManager.put("RadioButton.select", Color.WHITE);

        setVisible(true);

        
        try {
            // Main Menu Theme
            File file = new File("/Users/irfansyakir/Documents/OOP-Minesweeper/assets/main_menu.wav"); // change this to the "main_menu.wav" on windows
            AudioInputStream main_menu = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(main_menu);
            clip.start();

            // Makes sure that the clip loops continuously
            // Set up line listener
            lineListener = new LineListener() {
                public void update(LineEvent evt) {
                    if (evt.getType() == LineEvent.Type.STOP) {
                        clip.setFramePosition(0); // rewind to the beginning
                        clip.start(); // restart
                    }
                }
            };
            clip.addLineListener(lineListener); 


            // UI Hover Sound
            File uiHoverFile = new File("/Users/irfansyakir/Documents/OOP-Minesweeper/assets/ui_hover.wav"); // change this to the "ui_hover.wav" on windows
            AudioInputStream ui_hover = AudioSystem.getAudioInputStream(uiHoverFile);
            uiHoverClip = AudioSystem.getClip();
            uiHoverClip.open(ui_hover);

        } catch (Exception e) { 
            e.printStackTrace();
        }
        

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
        JLabel additionalLabel = new JLabel("There are mines among us.");
        
        additionalLabelPanel.add(additionalLabel);
        subPanel.add(additionalLabelPanel, BorderLayout.NORTH);



        try {
            String path = "/Users/irfansyakir/Documents/OOP-Minesweeper/assets/amongus.ttf"; // change this to the "amongus.tff" on windows
            // load a custom font in your project folder
           amongus = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(200f);	
           GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
           ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(path)));
           welcomeLabel.setFont(amongus);
           additionalLabel.setFont(amongus.deriveFont(75f));
           
       } catch (Exception e) {
           welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
           additionalLabel.setFont(amongus.deriveFont(100f));
       }

        JLabel playerNameLabel = new JLabel("Crewmate Name: ");
        playerNameLabel.setFont(normalFont);
        inputPanel.add(playerNameLabel);

        Border border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)); // top, left, bottom, right padding

        JTextField playerNameTextField = new JTextField();
        playerNameTextField.setMargin(new Insets(0, 10, 0, 0)); // add padding of 5 pixels on top, 10 pixels on left, bottom, and right
        playerNameTextField.setFont(normalFont);
        playerNameTextField.setBorder(border);
        inputPanel.add(playerNameTextField);

        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        radioButtonPanel.setBackground(Color.BLACK);

        JPanel inputRadioPanel = new JPanel(new GridLayout(0, 1, 0, 5)); // Use GridLayout to stack inputPanel and radioButtonPanel
        inputRadioPanel.setBackground(Color.BLACK);
        inputRadioPanel.add(inputPanel);
        inputRadioPanel.add(radioButtonPanel);

        easyButton = new JRadioButton("Easy");
        mediumButton = new JRadioButton("Medium");
        hardButton = new JRadioButton("Hard");

        easyButton.setFont(normalFont);
        mediumButton.setFont(normalFont);
        hardButton.setFont(normalFont);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(easyButton);
        buttonGroup.add(mediumButton);
        buttonGroup.add(hardButton);

        radioButtonPanel.add(easyButton);
        radioButtonPanel.add(mediumButton);
        radioButtonPanel.add(hardButton);
        

        
        
        panel.add(subPanel, BorderLayout.CENTER);

        // Create a start button panel with FlowLayout and add the start button to it
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);
    
        JButton startButton = new JButton("Start");
        startButton.setFont(normalFont);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorder(border);

        
        buttonPanel.add(startButton);
       
    
        subPanel.add(inputRadioPanel,BorderLayout.CENTER);
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

                if (playerNameTextField.getText() != null 
                && (easyButton.isSelected() || mediumButton.isSelected() || hardButton.isSelected()) ){

                    String playerName = playerNameTextField.getText(); // get player name from text field
                    System.out.println("Start button pressed by " + playerName);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            String playerName = playerNameTextField.getText(); 
                            if (easyButton.isSelected()) {
                                difficulty = "EASY";
                            } else if (mediumButton.isSelected()){
                                difficulty = "MEDIUM";
                            } else if (hardButton.isSelected()) {
                                difficulty = "HARD";
                            }
                            System.out.println("Difficulty set to " + difficulty);
                            new MineSweeperMain(playerName, difficulty);  // run the main program
                        }
                    });
                    clip.removeLineListener(lineListener);
                    clip.stop();
                    dispose();

            }
        }
        });
        

        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GREEN, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)); // top, left, bottom, right padding
                startButton.setBorder(border);
                uiHoverClip.setFramePosition(0); // rewind to beginning of clip
                uiHoverClip.start();
                uiHoverClip.addLineListener(new LineListener() {
                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP) {
                            uiHoverClip.stop();
                        }
                    }
                });
            }
            public void mouseExited(MouseEvent e) {
                Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.white, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)); // top, left, bottom, right padding
                startButton.setBorder(border);
                
                
            
            }
        });

        playerNameTextField.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GREEN, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)); // top, left, bottom, right padding
                    playerNameTextField.setBorder(border);
                    uiHoverClip.setFramePosition(0); // rewind to beginning of clip
                    uiHoverClip.start();
                    uiHoverClip.addLineListener(new LineListener() {
                        public void update(LineEvent event) {
                            if (event.getType() == LineEvent.Type.STOP) {
                                uiHoverClip.stop();
                            }
                        }
                    });
            }
            public void mouseExited(MouseEvent e) {
                Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.white, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)); // top, left, bottom, right padding
                    playerNameTextField.setBorder(border);
                
                
            
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
