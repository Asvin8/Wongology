package codeFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a Battle in the game.
 * It extends JPanel and implements Runnable and KeyListener interfaces.
 */
public class Battle extends JPanel implements Runnable, KeyListener {

    Thread thread; // Thread to run the battle
    HashMap<String, String> questions; // Questions for the battle
    int level=1; // current level
    int screenWidth = 840, screenHeight = 780; // Screen dimensions
    int playerHealth = 4, monsterHealth = 4; // Health of player and monster
    Image battleScreen, playerImage, monsterImage; // Images for battle screen, player and monster
    Image health1Monster, health2Monster, health3Monster, health4Monster; // Images for monster health
    Image health1Player, health2Player, health3Player, health4Player; // Images for player health
    String outputMessage = "Battle commences....."; // Initial output message
    String curQuestion="", curAns = ""; // Current question and answer

    /**
     * Constructor for Battle class.
     * @param level is the Level of the battle
     */
    public Battle(int level) {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setVisible(true);

        this.level = level;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * This method is the threading of the battle.
     * It initializes the battle and repaints the screen while the thread is alive.
     */
    @Override
    public void run() {
        initializeBattle();
        while (thread.isAlive()) {
            repaint();
        }
    }

    /**
     * This method sets the program for a new battle.
     * It loads the images and questions, and updates the question on the screen.
     */
    public void initializeBattle() {
        loadImages();
        loadQuestions();
        updateQuestionOnScreen();
    }

    /**
     * This method draws wrapped text on the screen.
     * @param g2 Graphics object
     * @param text Text to be drawn
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param width Width of the text
     */
    private void drawWrappedText(Graphics g2, String text, int x, int y, int width) {

        // process words in line
        g2.setFont(new Font("Verdana", Font.BOLD, 20));
        FontMetrics fontMetrics = g2.getFontMetrics();
        String[] words = text.split(" ");
        String curLine = "";

        // finds height of current line of words
        int curY = y + fontMetrics.getHeight();

        // prints wrapped text
        for (String word : words) {
            String testLine = curLine + word + " ";
            int lineWidth = fontMetrics.stringWidth(testLine);

            // word exceeds line limit
            if (lineWidth > width) {
                g2.drawString(curLine, x, curY);
                curLine = word + " ";
                curY += fontMetrics.getHeight();
            }

            // word is within line limit
            else {
                curLine = testLine;
            }
        }

        // Draw the last line
        g2.drawString(curLine, x, curY);
    }

    /**
     * This method draws stuff onto screen.
     * @param g Graphics object
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // draw image
        g2.drawImage(battleScreen, 0, 0, null);
        g2.drawImage(playerImage, 100, 500, null);
        g2.drawImage(monsterImage, 500, 100, null);

        // write text
        drawWrappedText(g2, outputMessage, 50, 660, 420);

        // Draw monster health
        if(monsterHealth == 1) {
            g2.drawImage(health1Monster, screenWidth/2 + 80, 380, null);
        } else if (monsterHealth == 2) {
            g2.drawImage(health2Monster, screenWidth/2 + 80, 380, null);
        } else if (monsterHealth == 3) {
            g2.drawImage(health3Monster, screenWidth/2 + 80, 380, null);
        } else if (monsterHealth == 4) {
            g2.drawImage(health4Monster, screenWidth/2 + 80, 380, null);
        }

        // Draw player health
        if(playerHealth == 1) {
            g2.drawImage(health1Player, 130, 60, null);
        } else if (playerHealth == 2) {
            g2.drawImage(health2Player, 130, 60, null);
        } else if (playerHealth == 3) {
            g2.drawImage(health3Player, 130, 60, null);
        } else if (playerHealth == 4) {
            g2.drawImage(health4Player, 130, 60, null);
        }

    }


    // loads questions for battle
    public void loadQuestions() {
        // Load questions based on level
        if(level == 1) {
            questions = new HashMap<>(Main.Level1Questions);
        } else if (level == 2) {
            questions = new HashMap<>(Main.Level1Questions);
        } else if (level == 3) {
            questions = new HashMap<>(Main.Level3Questions);
        }

    }

    // render images
    public void loadImages() {
        MediaTracker tracker = new MediaTracker(this);

        try {
            // Load battle screen image based on monster type
            if(Level2.monsterType == 2) {
                battleScreen = Toolkit.getDefaultToolkit().getImage("src/BattlePics/ArrayListMonsterBattle.png");
                tracker.addImage(battleScreen, 0);
            } else if(Level2.monsterType == 3) {
                battleScreen = Toolkit.getDefaultToolkit().getImage("src/BattlePics/BinarySearchMonsterBattle.png");
                tracker.addImage(battleScreen, 0);
            } else if(Level2.monsterType == 4) {
                battleScreen = Toolkit.getDefaultToolkit().getImage("src/BattlePics/ComparatorMonsterBattle.png");
                tracker.addImage(battleScreen, 0);
            } else {
                battleScreen = Toolkit.getDefaultToolkit().getImage("src/BattlePics/MonsterBattle.png");
                tracker.addImage(battleScreen, 0);
            }

            // Load player health images
            health1Player = Toolkit.getDefaultToolkit().getImage("src/BattlePics/1Health.png");
            tracker.addImage(health1Player, 1);

            health2Player = Toolkit.getDefaultToolkit().getImage("src/BattlePics/2Health.png");
            tracker.addImage(health2Player, 2);

            health3Player = Toolkit.getDefaultToolkit().getImage("src/BattlePics/3Health.png");
            tracker.addImage(health3Player, 3);

            health4Player = Toolkit.getDefaultToolkit().getImage("src/BattlePics/4Health.png");
            tracker.addImage(health3Player, 4);

            // Monster health images are same as player health images
            health1Monster = health1Player;
            health2Monster = health2Player;
            health3Monster = health3Player;
            health4Monster = health4Player;

        } catch (Exception e) { System.out.println("Error loading images"); }

        // make sure the images are loaded before continuing
        try { tracker.waitForAll(); }
        catch (InterruptedException e) { }

    }

    // updates questions on screen
    public void updateQuestionOnScreen() {
        curQuestion  = questions.keySet().iterator().next();
        outputMessage = curQuestion;
        curAns = questions.values().iterator().next();
        questions.remove(curQuestion);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * This method is called when a key is pressed.
     * It checks the key pressed and updates the health of player and monster accordingly.
     * @param e KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // true
        if (key == KeyEvent.VK_T) {
            if(curAns.equals("T")) {
                monsterHealth--;
            } else {
                playerHealth--;
            }
            updateQuestionOnScreen();
        }

        // false
        else if (key == KeyEvent.VK_F) {
            if(curAns.equals("F")) {
                monsterHealth--;
            } else {
                playerHealth--;
            }
            updateQuestionOnScreen();
        }

        // cheat code
        else if (key == KeyEvent.VK_W) {
            monsterHealth--;
            updateQuestionOnScreen();
        }

        // Close dialog box if player health is 0
        if(playerHealth == 0 || monsterHealth == 0) {
            if(level == 1) {
                Level1.closeDialogBox("battle");
            } else if (level == 2) {
                Level2.closeDialogBox("battle");
            } else if (level == 3) {
                System.out.println("test");
                Level3.closeDialogBox("battle");
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
