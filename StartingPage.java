package minesweeper;
import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;
import javax.swing.*;

public class StartingPage {
    public static void main(String[] args) {
        // Create a JFrame object and set its size
        JFrame frame = new JFrame("My Starting Page");
        frame.setSize(400, 300);

        // Create a JPanel object to hold the content
        JPanel panel = new JPanel();

        // Create a welcome message label and add it to the panel
        JLabel welcomeLabel = new JLabel("Welcome to my Java Application!");
        panel.add(welcomeLabel);

        // Create a start button and add it to the panel
        JButton startButton = new JButton("Start");
        panel.add(startButton);

        // Set the panel as the content pane of the frame
        frame.setContentPane(panel);

        // Make the frame visible
        frame.setVisible(true);
    }
}