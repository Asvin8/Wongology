package codeFiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Main extends JPanel implements Runnable, KeyListener {

    Thread thread;
    int FPS = 60;
    int screenWidth = 840;
    int screenHeight = 840;

    // keeps track of which screen to use
    static int screenType = 1;
    static JFrame frame;

    // various screens
    Image About;
    Image Credits;
    Image GameOver;
    Image Instructions;
    Image Levels;
    Image Start;
    static Image mark;
    public static int monsterType=0;

    public static int points = 0;
    static JDialog level1Dialog;
    static JDialog level2Dialog;
    static JDialog level3Dialog;
    Clip backgroundMusic;

    public Main() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setVisible(true);

        try {
            // background music
            AudioInputStream sound = AudioSystem.getAudioInputStream(new File("src/BackgroundMusic.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(sound);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        thread = new Thread(this);
        thread.start();
    }

    // This code runs the entire game
    @Override
    public void run() {
        loadBackgroundAndPlatforms();
        backgroundMusic.start();
        while (true) {
            this.repaint();
            try {
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // This is the paint component
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        points = Level1.lvl1Points + Level2.lvl2Points + Level3.lvl3Points;

        // start screen
        if (screenType == 1) {
            g2.drawImage(Start, 0, 0, null);
        }

        // instructions screen
        else if (screenType == 2) {
            g2.drawImage(Instructions, 0, 0, null);
        }

        // about screen
        else if (screenType == 3) {
            g2.drawImage(About, 0, 0, null);
        }

        // credits screen
        else if (screenType == 4) {
            g2.drawImage(Credits, 0, 0, null);
        }

        // levels screen
        else if (screenType == 5) {
            g2.drawImage(Levels, 0, 0, null);
        }

        // game over screen
        else if (screenType == 6) {
            g2.drawImage(GameOver, 0, 0, null);
        }

        g2.drawImage(mark, 785, 840/2, null);
        g2.setFont(new Font("Arial", Font.PLAIN, 25));
        g2.setColor(Color.WHITE);
        g2.drawString(String.valueOf(points), 785, 840/2 + 100);

    }


    // render background and platforms
    public void loadBackgroundAndPlatforms() {
        MediaTracker tracker = new MediaTracker(this);

        // rendering game images
        try {
            About = Toolkit.getDefaultToolkit().getImage("src/DriverPics/About.png");
            tracker.addImage(About, 0);
            Credits = Toolkit.getDefaultToolkit().getImage("src/DriverPics/Credits.png");
            tracker.addImage(Credits, 1);
            GameOver = Toolkit.getDefaultToolkit().getImage("src/DriverPics/GameOver.png");
            tracker.addImage(GameOver, 2);
            Instructions = Toolkit.getDefaultToolkit().getImage("src/DriverPics/Instructions.png");
            tracker.addImage(Instructions, 3);
            Levels = Toolkit.getDefaultToolkit().getImage("src/DriverPics/Levels.png");
            tracker.addImage(Levels, 4);
            Start = Toolkit.getDefaultToolkit().getImage("src/DriverPics/Start.png");
            tracker.addImage(Start, 5);
            mark = Toolkit.getDefaultToolkit().getImage("src/Marks.png");
            tracker.addImage(mark, 5);
        } catch (Exception e) {
            System.out.println("Error loading images");
        }

        // make sure the images are loaded before continuing
        try { tracker.waitForAll(); }
        catch (InterruptedException e) { }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    // This function determines where the character moves based on user input
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // from start, go to levels
        if (key == KeyEvent.VK_SPACE && screenType == 1) {
            screenType = 5;
        }

        // go back to start
        if (key == KeyEvent.VK_H) {
            screenType = 1;
        }

        // head to instructions page
        else if (key == KeyEvent.VK_I) {
            screenType = 2;
        }

        // head to about page
        else if (key == KeyEvent.VK_A) {
            screenType = 3;
        }

        // head to credits page
        else if (key == KeyEvent.VK_C) {
            screenType = 4;
        }

        // level 1
        else if (key == KeyEvent.VK_1 && screenType == 5) {
            level1Dialog = new JDialog(frame, "Level 1", true);
            Level1 myPanel = new Level1();
            level1Dialog.add(myPanel);
            level1Dialog.addKeyListener(myPanel);
            level1Dialog.setSize(840, 840);
            level1Dialog.setVisible(true);
            level1Dialog.pack();
            level1Dialog.setResizable(false);
//            level1Dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        // level 2
        else if (key == KeyEvent.VK_2 && screenType == 5) {
            level2Dialog = new JDialog(frame, "Level 2", true);
            Level2 myPanel = new Level2();
            level2Dialog.add(myPanel);
            level2Dialog.addKeyListener(myPanel);
            level2Dialog.setSize(840, 840);
            level2Dialog.setVisible(true);
            level2Dialog.pack();
            level2Dialog.setResizable(true);
//            level2Dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        }

        // level 3
        else if (key == KeyEvent.VK_3 && screenType == 5) {
            level3Dialog = new JDialog(frame, "Level 3", true);
            Level3 myPanel = new Level3();
            level3Dialog.add(myPanel);
            level3Dialog.addKeyListener(myPanel);
            level3Dialog.setSize(840, 840);
            level3Dialog.setVisible(true);
            level3Dialog.pack();
//            level3Dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            level3Dialog.setResizable(false);
            level3Dialog.setLocationRelativeTo(null);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


    public static void main(String[] args) {
        frame = new JFrame("Wongology");
        Main myPanel = new Main();
        frame.add(myPanel);
        frame.addKeyListener(myPanel);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(true);
        new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
