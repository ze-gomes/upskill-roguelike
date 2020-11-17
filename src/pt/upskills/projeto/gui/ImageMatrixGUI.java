package pt.upskills.projeto.gui;

import pt.upskills.projeto.game.LevelManager;
import pt.upskills.projeto.objects.Hero;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.List;
import java.util.*;

/*
 * ATT: Non Thread-safe version!!!
 * Implementations of ImageTile should synch getPosition() and setPosition()
 *
 * */

/**
 * @author Iscte 2016
 * <p>
 * ImageMatrixGUI, manages and displays in its main window a grid of
 * square images (implementations of ImageTile) of the same size. The
 * default image-size is 48x48 and the default grid-size is 10x10.
 * <p>
 * This class also provides a status-bar (also composed of square images
 * of the same size) that will be displayed above the grid.
 * <p>
 * This class is observable and will send an update message everytime a
 * key is pressed.
 * <p>
 * The images that ImageTiles refer to MUST be in a folder called
 * "images" directly under the project folder.
 * <p>
 * ImageTile is required to provide the name of the image (e.g.
 * "XxxX") and its position (in tile coordinates, with 0,0 in the top
 * left corner and increasing to the right in the horizontal axis and
 * downwards in the vertical axis). ImageMatrixGUI will look for an
 * image with that name in the "images" folder (e.g. "XxxX.png") and draw
 * that image in the window.
 * <p>
 * ImageMatrixGUI implements the Singleton pattern.
 */

/**
 * @author lmmn
 */
public class ImageMatrixGUI extends Observable implements ActionListener {

    private static final ImageMatrixGUI INSTANCE = new ImageMatrixGUI();

    private final String IMAGE_DIR = "images";
    private final int SQUARE_SIZE;
    private final int N_SQUARES_WIDTH;
    private final int N_SQUARES_HEIGHT;
    // To access some features to update info
    LevelManager levelManager = LevelManager.getInstance();
    private JFrame frame;
    private JPanel panel;
    private JPanel info;
    // Created for extra costumizations
    private JLabel label;
    private JButton buttonRestartGame;
    private JButton buttonRestoreSave;
    private JLabel labelHighscores;
    private JLabel labelNewHighscore;
    private JLabel labelRetry;
    private JButton buttonSubmitHighscore;
    private Map<String, ImageIcon> imageDB = new HashMap<String, ImageIcon>();

    private List<ImageTile> images = new ArrayList<ImageTile>();
    private List<ImageTile> statusImages = new ArrayList<ImageTile>();

    private int lastKeyPressed;
    private boolean keyPressed;

    private ImageMatrixGUI() {
        SQUARE_SIZE = 48;
        N_SQUARES_WIDTH = 10;
        N_SQUARES_HEIGHT = 10;
        init();
    }

    /**
     * @return Access to the Singleton instance of ImageMatrixGUI
     */
    public static ImageMatrixGUI getInstance() {
        return INSTANCE;
    }

    /**
     * Setter for the name of the frame
     *
     * @param name Name of application (will be displayed as a frame title in the
     *             top left corner)
     */

    public void setName(final String name) {
        frame.setTitle(name); // Corrected 2-Mar-2016
    }

    private void init() {
        frame = new JFrame();
        panel = new RogueWindow();
        info = new InfoWindow();


        panel.setPreferredSize(new Dimension(N_SQUARES_WIDTH * SQUARE_SIZE, N_SQUARES_HEIGHT * SQUARE_SIZE));
        info.setPreferredSize(new Dimension(N_SQUARES_WIDTH * SQUARE_SIZE, SQUARE_SIZE));
        info.setBackground(Color.BLACK);
        frame.add(panel);
        frame.add(info, BorderLayout.NORTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initImages();
        new KeyWatcher().start();


        // Create a label to display the current score on the status bar
        label = new JLabel("Current Score: 0");
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setSize(193, 20);
        label.setLocation(144, 28);
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(0, 0, 0, 130));
        label.setBorder(new EmptyBorder(0, 25, 0, 0));
        label.setOpaque(true);

        // Restart game button
        buttonRestartGame = new JButton("New Game");
        buttonRestartGame.setSize(160, 30);
        buttonRestartGame.setFont(new Font("SansSerif", Font.BOLD, 18));
        buttonRestartGame.setLocation(160, 430);
        buttonRestartGame.setForeground(Color.BLACK);
        buttonRestartGame.setBackground(Color.WHITE);
        buttonRestartGame.setBorderPainted(false);
        buttonRestartGame.setFocusPainted(false);
        buttonRestartGame.setVisible(false);
        // Label to present High Scores at gameover
        labelHighscores = new JLabel("");
        labelHighscores.setBorder(new EmptyBorder(0, 10, 0, 0));
        labelHighscores.setSize(210, 150);
        labelHighscores.setLocation(135, 10);
        labelHighscores.setForeground(Color.WHITE);
        labelHighscores.setBackground(new Color(0, 0, 0, 130));
        labelHighscores.setOpaque(true);
        labelHighscores.setVisible(false);
        // Label New High Score Warning
        labelNewHighscore = new JLabel("");
        labelNewHighscore.setBorder(new EmptyBorder(5, 5, 5, 5));
        labelNewHighscore.setSize(390, 50);
        labelNewHighscore.setLocation(45, 260);
        labelNewHighscore.setForeground(Color.WHITE);
        labelNewHighscore.setBackground(new Color(255, 0, 0, 130));
        labelNewHighscore.setOpaque(true);
        labelNewHighscore.setVisible(false);
        // Submit highscore button
        buttonSubmitHighscore = new JButton("Submit Score");
        buttonSubmitHighscore.setSize(160, 30);
        buttonSubmitHighscore.setFont(new Font("SansSerif", Font.BOLD, 18));
        buttonSubmitHighscore.setLocation(160, 390);
        buttonSubmitHighscore.setForeground(Color.BLACK);
        buttonSubmitHighscore.setBackground(Color.WHITE);
        buttonSubmitHighscore.setBorderPainted(false);
        buttonSubmitHighscore.setFocusPainted(false);
        buttonSubmitHighscore.setVisible(false);
        buttonSubmitHighscore.setEnabled(false);
        // Go to save button
        buttonRestoreSave = new JButton("Restart Save");
        buttonRestoreSave.setSize(160, 30);
        buttonRestoreSave.setFont(new Font("SansSerif", Font.BOLD, 18));
        buttonRestoreSave.setLocation(160, 350);
        buttonRestoreSave.setForeground(Color.BLACK);
        buttonRestoreSave.setBackground(Color.WHITE);
        buttonRestoreSave.setBorderPainted(false);
        buttonRestoreSave.setFocusPainted(false);
        buttonRestoreSave.setVisible(false);
        buttonRestoreSave.setEnabled(false);
        // Label Lives left
        labelRetry = new JLabel("");
        labelRetry.setVerticalAlignment(JLabel.BOTTOM);
        labelRetry.setBorder(new EmptyBorder(0, 10, 10, 0));
        labelRetry.setSize(210, 80);
        labelRetry.setLocation(135, 260);
        labelRetry.setForeground(Color.WHITE);
        labelRetry.setBackground(new Color(0, 0, 0, 130));
        labelRetry.setOpaque(true);
        labelRetry.setVisible(false);
        // Action Listenners for clicks on buttons
        buttonRestoreSave.addActionListener(this);
        buttonRestartGame.addActionListener(this);
        buttonSubmitHighscore.addActionListener(this);
        //Add elements to window
        info.add(label);
        panel.add(buttonRestartGame);
        panel.add(buttonRestoreSave);
        panel.add(buttonSubmitHighscore);
        panel.add(labelNewHighscore);
        panel.add(labelHighscores);
        panel.add(labelRetry);
        info.setLayout(null);
        panel.setLayout(null);


        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                lastKeyPressed = e.getKeyCode();
                keyPressed = true;
                releaseObserver();
            }
        });
    }


    synchronized void releaseObserver() {
        notify();
    }

    synchronized void waitForKey() throws InterruptedException {
        while (!keyPressed) {
            wait();
        }
        setChanged();
        notifyObservers(lastKeyPressed);
        keyPressed = false;
    }

    private void initImages() {
        File dir = new File(IMAGE_DIR);
        for (File f : dir.listFiles()) {
            assert (f.getName().lastIndexOf('.') != -1);
            imageDB.put(f.getName().substring(0, f.getName().lastIndexOf('.')),
                    new ImageIcon(IMAGE_DIR + "/" + f.getName()));
        }
    }

    /**
     * Make the window visible.
     */
    public void go() {
        frame.setVisible(true);
    }

    /**
     * Add a new set of images to the main window.
     *
     * @param newImages images to be added to main window
     * @throws IllegalArgumentException if no image with that name (and a suitable extension) is
     *                                  found the images folder
     */

    public void newImages(final List<ImageTile> newImages) {
        synchronized (images) { // Added 16-Mar-2016
            if (newImages == null)
                return;
            if (newImages.size() == 0)
                return;
            for (ImageTile i : newImages) {
                if (!imageDB.containsKey(i.getName())) {
                    throw new IllegalArgumentException("No such image in DB " + i.getName());
                }
            }
            images.addAll(newImages);
        }
    }

    // Added 2-Mar-2016

    /**
     * Removes the image given as a parameter.
     * <p>
     * Does nothing if there is no match.
     *
     * @param image to be removed (must be the exact same Object and not a copy)
     */

    public void removeImage(final ImageTile image) {
        synchronized (images) { // Added 16-Mar-2016
            images.remove(image);
        }
    }

    // Added 2-Mar-2016

    /**
     * Adds image to main window
     *
     * @param image to be added
     */
    public void addImage(final ImageTile image) {
        synchronized (images) { // Added 16-Mar-2016
            images.add(image);
        }
    }

    /**
     * Clear all images displayed in main window.
     */
    public void clearImages() {
        synchronized (images) { // Added 16-Mar-2016
            images.clear();
        }
    }

    /**
     * Add a new set of images to the status window.
     *
     * @param newImages images to be added to status bar
     * @throws IllegalArgumentException if no image with that name (and a suitable extension) is
     *                                  found the images folder
     */

    public void newStatusImages(final List<ImageTile> newImages) {
        synchronized (statusImages) { // Added 16-Mar-2016
            if (newImages == null)
                return;
            if (newImages.size() == 0)
                return;
            for (ImageTile i : newImages) {
                if (!imageDB.containsKey(i.getName())) {
                    throw new IllegalArgumentException("No such image in DB " + i.getName());
                }
            }
            statusImages.addAll(newImages);
        }
    }

    // Added 2-Mar-2016

    /**
     * Removes the image given as a parameter from the status bar.
     * <p>
     * Does nothing if there is no match.
     *
     * @param image to be removed (must be the exact same Object and not a copy)
     */

    public void removeStatusImage(final ImageTile image) {
        synchronized (statusImages) { // Added 16-Mar-2016
            statusImages.remove(image);
        }
    }

    // Added 2-Mar-2016

    /**
     * Adds image to status window
     *
     * @param image to be added
     */
    public void addStatusImage(final ImageTile image) {
        synchronized (statusImages) { // Added 16-Mar-2016
            statusImages.add(image);
        }
    }

    /**
     * Clear all images displayed in status window.
     */

    public void clearStatus() {
        synchronized (statusImages) { // Added 16-Mar-2016
            statusImages.clear();
        }
    }


    public void gameOver(Hero hero) {
        // GUI Modifications - End Game Screen, Current Score info and Start Again Button
        // update label on screen with the current score
        int currentScore = hero.getScore();
        label.setText("Current Score: " + currentScore);
        // If game is over, add Start again buttonRestartGame
        buttonRestartGame.setVisible(true);
        labelHighscores.setVisible(true);
        buttonRestoreSave.setVisible(true);
        buttonSubmitHighscore.setVisible(true);
        labelHighscores.setText("<html><p style=\"font-size:14px\">Current HighScores:</p> <br>" +
                "1. " + levelManager.getHighscoresList().get(0) + "<br>" +
                "2. " + levelManager.getHighscoresList().get(1) + "<br>" +
                "3. " + levelManager.getHighscoresList().get(2) + "<br>" +
                "4. " + levelManager.getHighscoresList().get(3) + "<br>" +
                "5. " + levelManager.getHighscoresList().get(4) + "<br>" +
                "</html>");
        int vidas = hero.getLives();
        if (vidas > 0) {
            buttonRestoreSave.setEnabled(true);
            labelRetry.setVisible(true);
            labelRetry.setText("<html>You still have " + vidas + " lives left, click Restart Save " +
                    "to go back to the last saved room! But you lose 200 points!</html>");
        } else if (levelManager.checkifHighScore(currentScore)) {
            labelNewHighscore.setText("<html>Congratulations! You made it to top5 with " + currentScore + " points!<br>" +
                    "Click Submit Scores and enter your name in the console to save it!</html>");
            labelNewHighscore.setVisible(true);
            buttonSubmitHighscore.setEnabled(true);
        }

    }

    // Disables buttons and labels created on the end game screen
    public void disableButtonsandLabels(){
        buttonRestoreSave.setEnabled(false);
        buttonRestartGame.setVisible(false);
        buttonRestoreSave.setVisible(false);
        buttonSubmitHighscore.setVisible(false);
        buttonSubmitHighscore.setEnabled(false);
        labelHighscores.setVisible(false);
        labelNewHighscore.setVisible(false);
        labelRetry.setVisible(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonRestartGame) {
            levelManager.restartGame();
            disableButtonsandLabels();
        } else if (e.getSource() == buttonRestoreSave) {
            levelManager.restartSavedLevel();
            disableButtonsandLabels();
        } else if (e.getSource() == buttonSubmitHighscore) {
            levelManager.submitHighscore();
            buttonSubmitHighscore.setEnabled(false);
        }
        panel.repaint();
        frame.requestFocus();
        frame.repaint();
    }


    /**
     * Force scheduling of a new window paint (this may take a while, it does
     * not necessarily happen immediately after this instruction is issued)
     */
    public void update() {
        // update label on screen with the current score
        int currentScore = levelManager.getHeroInstance().getScore();
        label.setText("Current Score: " + currentScore);
        frame.repaint();
    }


    /**
     * Terminate window GUI
     */
    public void dispose() {
        images.clear();
        statusImages.clear();
        imageDB.clear();
        frame.dispose();
    }

    /**
     * Grid dimensions
     *
     * @return the width and height of the image grid
     */
    public Dimension getGridDimension() {
        return new Dimension(N_SQUARES_WIDTH, N_SQUARES_HEIGHT);
    }

    @SuppressWarnings("serial") // Added 2-Mar-2016
    private class RogueWindow extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            // System.out.println("Thread " + Thread.currentThread() + "
            // repainting");
            synchronized (images) { // Added 16-Mar-2016
                for (ImageTile i : images) {

                    g.drawImage(imageDB.get(i.getName()).getImage(), i.getPosition().getX() * SQUARE_SIZE,
                            i.getPosition().getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, frame);
                }
            }
        }
    }

    @SuppressWarnings("serial") // Added 2-Mar-2016
    private class InfoWindow extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            synchronized (statusImages) { // Added 16-Mar-2016
                for (ImageTile i : statusImages)
                    g.drawImage(imageDB.get(i.getName()).getImage(), i.getPosition().getX() * SQUARE_SIZE, 0,
                            SQUARE_SIZE, SQUARE_SIZE, frame);

            }
        }
    }

    private class KeyWatcher extends Thread {
        public void run() {
            try {
                while (true)
                    waitForKey();
            } catch (InterruptedException e) {
            }
        }
    }

}
