package minesweeper;
import java.awt.*;       
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.Set;
import javax.sound.sampled.*;
import java.io.*;

public class WelcomeScreen extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Declare variables
    private Clip clip;
    private Clip uiHoverClip;
    private Clip uiClickClip;
    private LineListener lineListener;

    private JRadioButton easyButton;
    private JRadioButton mediumButton;
    private JRadioButton hardButton;
    private ButtonGroup buttonGroup;

    private String difficulty;
    private String absolutePath;

    public WelcomeScreen() {
        super("Impostersweeper"); // Set Title
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

       
        absolutePath = System.getProperty("user.dir");
       
        
        System.out.println(absolutePath);

        try {

            /* Main Menu Theme
               Change to "./resources/sounds/main_menu.wav" in Windows
               Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/sounds/main_menu.wav" in macOS
             */ 

            File file = new File(absolutePath + "/resources/sounds/main_menu.wav"); 
            
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

            /* UI Hover Sound
               Change to "/.resources/sounds/ui_hover.wav" in Windows
               Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/sounds/ui_hover.wav" in macOS
             */


            File uiHoverFile = new File(absolutePath + "/resources/sounds/ui_hover.wav"); 
            AudioInputStream ui_hover = AudioSystem.getAudioInputStream(uiHoverFile);
            uiHoverClip = AudioSystem.getClip();
            uiHoverClip.open(ui_hover);

            /* UI Hover Sound
               Change to "/.resources/sounds/ui_hover.wav" in Windows
               Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/sounds/ui_hover.wav" in macOS
             */


             File uiClickFile = new File(absolutePath + "/resources/sounds/ui_click.wav"); 
             AudioInputStream ui_click = AudioSystem.getAudioInputStream(uiClickFile);
             uiClickClip = AudioSystem.getClip();
             uiClickClip.open(ui_click);

 

        } catch (Exception e) { 
            e.printStackTrace();
        }
        
        // fonts
        Font normalFont = new Font("Arial", Font.PLAIN, 25);
	    Font amongus = normalFont; // initialise to normal font at first
      
        // Create a JPanel object with BorderLayout to hold the content
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.BLACK);

        // Create a welcome message panel with FlowLayout and add it to the main panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        welcomePanel.setBackground(Color.BLACK);
        JLabel welcomeLabel = new JLabel("Impostersweeper");
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
        JLabel additionalLabel = new JLabel("Find the imposters among us.");
        additionalLabelPanel.add(additionalLabel);

        // tries to find the Among Us font and set the welcome and additional label's font accordingly
        try {
            // Change to "./resources/amongus.ttf" in Windows
            // Change to "/Users/irfansyakir/Documents/OOP-Minesweeper/resources/amongus.ttf" in macOS
            String path = absolutePath + "/resources/amongus.ttf"; 
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

        // Create a Label for the crewmate name and add it to the input panel
        JLabel playerNameLabel = new JLabel("Crewmate Name: ");
        playerNameLabel.setFont(normalFont);
        inputPanel.add(playerNameLabel);

        // Default Border for the TextField and Button
        Border border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)); // top, left, bottom, right padding
            
        // Create a TextField and add it to the input panel and set properties
        JTextField playerNameTextField = new JTextField();
        playerNameTextField.setMargin(new Insets(0, 10, 0, 0)); // add padding of 5 pixels on top, 10 pixels on left, bottom, and right
        playerNameTextField.setFont(normalFont);
        playerNameTextField.setBorder(border);
        inputPanel.add(playerNameTextField);


       // Create a panel for the radio buttons
        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        radioButtonPanel.setBackground(Color.BLACK);

        // Create a panel that includes the radio button panel and the input panel
        JPanel inputRadioPanel = new JPanel(new GridLayout(0, 1, 0, 5)); // Use GridLayout to stack inputPanel and radioButtonPanel
        inputRadioPanel.setBackground(Color.BLACK);
        inputRadioPanel.add(inputPanel);
        inputRadioPanel.add(radioButtonPanel);

        // Create the buttons 
        easyButton = new JRadioButton("Easy");
        mediumButton = new JRadioButton("Medium");
        hardButton = new JRadioButton("Hard");

        // Set the font of the buttons
        easyButton.setFont(normalFont);
        mediumButton.setFont(normalFont);
        hardButton.setFont(normalFont);

        // Create the radio group and the buttons to it
        buttonGroup = new ButtonGroup();
        buttonGroup.add(easyButton);
        buttonGroup.add(mediumButton);
        buttonGroup.add(hardButton);

        // Add the Buttons to the radio button panel
        radioButtonPanel.add(easyButton);
        radioButtonPanel.add(mediumButton);
        radioButtonPanel.add(hardButton);


        // Create a start button panel with FlowLayout 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);


        // Create the start button and set its properties and add it to the start button panel
        JButton startButton = new JButton("Start");
        startButton.setFont(normalFont);
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorder(border);
        buttonPanel.add(startButton);

        // Add all of the inner panels to the sub panel
        subPanel.add(additionalLabelPanel, BorderLayout.NORTH);
        subPanel.add(inputRadioPanel,BorderLayout.CENTER);
        subPanel.add(buttonPanel, BorderLayout.SOUTH);

        // add the sub panel to the main panel
        panel.add(subPanel, BorderLayout.CENTER);

        // Set the panel as the content pane of the frame
        setContentPane(panel);
        // Pack the UI components, instead of setSize()
        pack();  
        // Make the frame visible and centered on the screen
        setLocationRelativeTo(null);
       
        // Add a KeyListener to the text field (Enter Button)
        playerNameTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.doClick(); // Simulate a click on the Start button
            }
        });

        
        // Adds a mouse listener to the start button
        // Changes the border of the start button to green and plays a sound effect when hovering over the start button
        startButton.addMouseListener(new MouseAdapter() {
            // Called when the mouse is over the start button
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
            // Called when the mouse is no longer over the start button (after initially being over)
            public void mouseExited(MouseEvent e) {
                Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.white, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)); // top, left, bottom, right padding
                startButton.setBorder(border);
            }
        });


        // Adds a mouse listener to the name text field
        // Changes the border of the text field to green and plays a sound effect when hovering over the text field
        playerNameTextField.addMouseListener(new MouseAdapter() {
            // Called when the mouse is over the text field
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
            // Called when the mouse is no longer over the text field (after initially being over)
            public void mouseExited(MouseEvent e) {
                Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.white, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)); // top, left, bottom, right padding
                    playerNameTextField.setBorder(border);
            }
        });

        // Adds a Action Listener to the start button that will bring the user to the main activity
        startButton.addActionListener(new ActionListener() {
            // Called when the user clicks on the start button
            @Override
            public void actionPerformed(ActionEvent e) {
                uiClickClip.start();

                // Makes sure that the user has entered their name and selected a difficulty option
                if (playerNameTextField.getText() != null 
                     && (easyButton.isSelected() || mediumButton.isSelected() || hardButton.isSelected()) ) {

                    String playerName = playerNameTextField.getText(); // get player name from text field
                    System.out.println("Crewmate Name: " + playerName); // For Debugging
                    if (easyButton.isSelected()) {
                        difficulty = "EASY";
                    } else if (mediumButton.isSelected()){
                        difficulty = "MEDIUM";
                    } else if (hardButton.isSelected()) {
                        difficulty = "HARD";
                    }

                    System.out.println("Difficulty set to " + difficulty); // For Debugging

                    // run the main program
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new MineSweeperMain(playerName, difficulty);  // run the main program
                        }
                    });
                    
                    // Stops the main menu theme and closes the Welcome Screen Window
                    clip.removeLineListener(lineListener);
                    clip.stop();
                    dispose();

                }
            }
        });

    }

    // main function
    public static void main(String[] args) {
       new WelcomeScreen();
    }
}
