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
import java.util.HashMap;
import javax.swing.*;

@SuppressWarnings("serial")
public class Level2 extends JPanel implements Runnable, KeyListener {

    Thread thread;
    int FPS = 60;
    int screenWidth = 840;
    int screenHeight = 780;

    // stats
    int lives = 3;

    Rectangle player = new Rectangle(0, 0, 30, 50);

    // size of each platform block
    final int platformSize = 60;

    // player variables
    boolean left, right, jump;
    boolean directionFacing = false; // left is true, right is false
    double yVel = 0;
    double scale = 2.4;
    double airSpeed = 0;
    double gravity = 0.4 * scale;
    double jumpSpeed = -5.3 * scale;
    double playerSpeed = 4.0;
    boolean inAir = false;
    int startingXPlayer = platformSize, startingYPlayer = (int)(5*platformSize-player.getHeight()-1);

    // images in program
    Image background;
    Image platformImage;
    Image arrayListEnemyImage, searchEnemyImage, unknownMonsterImage, comparatorMonsterImage;
    Image playerImageLeft, playerImageRight;
    Image heartImage;

    // keeps track of which screen to use
    static int screenType = 1;
    static JFrame frame;

    /*
     * Here is the key we will use to determine which screen is used at the moment:
     * 1 --> start screen
     * 2 --> instructions screen
     * 3 --> about screen
     * 4 --> game over screen
     * 5 --> bacgkround 1 screen
     * 6 --> background 2 screen
     * 7 --> background 3 screen
     */

    // various screens
    Image gameOverScreen;
    Image instructionsScreen;
    Image aboutScreen;
    Image startScreen;
    Image backgroundImage1, backgroundImage2, backgroundImage3, backgroundImage4;

    /**
     * This is the array that models the objects on the screen
     * Notation:
     * 0 --> empty
     * 1 --> platform
     * 2 --> arraylist monster
     * 3 --> search monster
     * 4 --> comparator monster
     * 5 --> unknown monster
     */
    int[][] screen = new int[screenHeight/platformSize][screenWidth/platformSize];

    // platforms
    Platform[] platforms = new Platform[screen.length];
    public static JDialog battleDialog;

    public Level2() {

        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setVisible(true);

        // variables that help keep track of keystrokes
        jump = false;
        left = false;
        right = false;

        thread = new Thread(this);
        thread.start();
    }

    // This code runs the entire game
    @Override
    public void run() {

        // start game
        initializeGame();

        while (true) {
            move();
            keepInBound();

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

        if (screenType == 1) { // background 1
            g2.drawImage(backgroundImage1, 0, 0, null);
        } else if (screenType == 2) { // background 2
            g2.drawImage(backgroundImage2, 0, 0, null);
        } else if (screenType == 3) { // background 3
            g2.drawImage(backgroundImage3, 0, 0, null);
        } else if (screenType == 4) { // background 3
            g2.drawImage(backgroundImage4, 0, 0, null);
        }

        // draw platforms
        AlphaComposite transparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        g2.setComposite(transparency);
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                if (screen[i][j] == 1) {
                    transparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f);
                    g2.setComposite(transparency);
                    g2.drawRect(j * platformSize, i * platformSize, platformSize, platformSize);
                    transparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
                    g2.setComposite(transparency);
                    g2.drawImage(platformImage, j * platformSize, i * platformSize, null);
                }
            }
        }

        // draw stuff on screen
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {

                // draw enemies
                if (screen[i][j] == 2) {
                    int row = j * platformSize;
                    int col = i * platformSize;
                    g2.drawImage(arrayListEnemyImage, row, col, null);
                } else if (screen[i][j] == 3) {
                    int row = j * platformSize;
                    int col = i * platformSize;
                    g2.drawImage(searchEnemyImage, row, col, null);
                } else if (screen[i][j] == 4) {
                    int row = j * platformSize;
                    int col = i * platformSize;
                    g2.drawImage(comparatorMonsterImage, row, col, null);
                } else if (screen[i][j] == 5) {
                    int row = j * platformSize;
                    int col = i * platformSize;
                    g2.drawImage(unknownMonsterImage, row, col, null);
                }
            }
        }

        g2.drawImage(Main.mark, 785, 840/2, null);
        g2.setFont(new Font("Arial", Font.PLAIN, 25));
        g2.setColor(Color.WHITE);
        g2.drawString(String.valueOf(Main.points), 785, 840/2 + 100);

        // draw hearts
        if (lives >= 1)
            g2.drawImage(heartImage, screenWidth-platformSize, 440, null);

        if (lives >= 2)
            g2.drawImage(heartImage, screenWidth-platformSize, 410, null);

        if (lives >= 3) {
            g2.drawImage(heartImage, screenWidth-platformSize, 380, null);
            lives = 3;
        }

        // draw transparent rectangle around player
        transparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f);
        g2.setComposite(transparency);
        g2.draw(player);

        // draw player
        transparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        g2.setComposite(transparency);

        // determines which direction the player is facing
        if (directionFacing) {
            g2.drawImage(playerImageLeft, player.x, player.y, null);
        } else {
            g2.drawImage(playerImageRight, player.x, player.y, null);
        }

    }

    // This method initializes the game when it starts
    public void initializeGame() {
        player.y = startingYPlayer;
        player.x = startingXPlayer;
        resetInAir();
        resetScreenArray();
        loadBackgroundAndPlatforms();
        generatePlatforms();
        generateEnemy();
        right = false;
        directionFacing = false;
        jump = false;
    }

    // This function resets the screen array
    void resetScreenArray() {
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                screen[i][j] = 0;
            }
        }
    }

    // render background and platforms
    public void loadBackgroundAndPlatforms() {
        MediaTracker tracker = new MediaTracker(this);

        try {

            // rendering game images
            backgroundImage1 = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/back1.png");
            tracker.addImage(background, 0);
            platformImage = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/platformImage.png");
            tracker.addImage(platformImage, 1);
            searchEnemyImage = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/ArrayListMonster.png");
            tracker.addImage(arrayListEnemyImage, 2);
            arrayListEnemyImage = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/SearchMonster.png");
            tracker.addImage(searchEnemyImage, 3);
            playerImageLeft = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/wongleft.png");
            tracker.addImage(playerImageLeft, 4);
            playerImageRight = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/wongright.png");
            tracker.addImage(playerImageRight, 5);
            backgroundImage2 = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/back2.png");
            tracker.addImage(background, 6);
            backgroundImage3 = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/back3.png");
            tracker.addImage(background, 7);
            backgroundImage4 = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/back4.png");
            tracker.addImage(background, 8);
            unknownMonsterImage = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/unknownMonster.png");
            tracker.addImage(background, 9);
            comparatorMonsterImage = Toolkit.getDefaultToolkit().getImage("src/Level2Pics/ComparatorMonster.png");
            tracker.addImage(background, 10);

        } catch (Exception e) {
            System.out.println("Error loading images");
        }

        // make sure the images are loaded before continuing
        try { tracker.waitForAll(); }
        catch (InterruptedException e) { }
    }

    // This function generates random platforms of varying lengths at every row in an array
    public void generatePlatforms() {

        // platform that player stands on
        platforms[1] = new Platform(platformSize, platformSize * 5);
        screen[5][1] = 1;
        int x=0, curBlockPlace=0, upAmt=0;

        // add 1 platform per column either above or below player
        for(int i = 2; i < screen[0].length-1; i++) {
            x = (int) (Math.random() * 8) + 1;
            upAmt = -1;
            if(x>=3) upAmt = x-2;
            curBlockPlace = (int)(Math.ceil(10*platforms[i-1].y/screenHeight) + upAmt);
            if(curBlockPlace < screen.length - 1 && curBlockPlace > 1) {
                screen[curBlockPlace][i] = 1;
                platforms[i] = new Platform(i*platformSize, curBlockPlace*platformSize);
            }
            else i--;
        }
    }

    // enemy generation
    void generateEnemy() {
        for (int i = 2; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                if(i==5 && j==1) continue;
                else if (screen[i][j] == 1 && screen[i - 1][j] == 0) {
                    int randNum = (int) (Math.random() * 2) + 1; // 1/2 chance to spawn
                    if (randNum == 1) {
                        int enemyToDraw = (int)(Math.random()*4) + 1;
                        if(enemyToDraw == 1) screen[i-1][j] = 2;
                        else if(enemyToDraw == 2) screen[i-1][j] = 3;
                        else if(enemyToDraw == 3) screen[i-1][j] = 4;
                        else if(enemyToDraw == 4) screen[i-1][j] = 5;
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    // This function determines where the character moves based on user input
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) { // left
            left = true;
            directionFacing = true;
        } else if (key == KeyEvent.VK_D) { // right
            right = true;
            directionFacing = false;
        } else if (key == KeyEvent.VK_W) { // jump
            jump = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            left = false;
        } else if (key == KeyEvent.VK_D) {
            right = false;
        } else if (key == KeyEvent.VK_W) {
            jump = false;
        }
    }

    public static void closeDialogBox(String action) {
        if(action.equals("battle")) battleDialog.dispose();
    }

    // This function handles the movement of the player
    void move() {

        // if player is currently jumping
        if (jump)
            jump();

        // player reaches bottom of screen
        if (player.y + player.height >= screenHeight) {
            int randNum = (int) (Math.random() * 4-1+1) + 1; // 1/2 chance to spawn enemy
            screenType = randNum;
            initializeGame();
        }

        // detecting if player is hitting monster
        int curRow = player.y / platformSize, curCol = player.x / platformSize;
        if (screen[curRow][curCol] > 1) {
            Main.monsterType = screen[curRow][curCol];
            System.out.println("battle should run");
            battleDialog = new JDialog(frame,"battle", true);
            Battle panel = new Battle(2);
            battleDialog.add(panel);
            battleDialog.addKeyListener(panel);
            battleDialog.setSize(840, 780);
            battleDialog.setVisible(true);
            battleDialog.pack();
            battleDialog.setResizable(true);
            battleDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            startedABattle = true;
            collision(screen[curRow][curCol]);
            screen[curRow][curCol] = 0;
        }

        // if user is not pressing any keys, player does not move
        if (!left && !right && !inAir)
            return;

        double xSpeed = 0;

        // user presses left key
        if (left && !right)
            xSpeed = -playerSpeed;

        // user presses right key
        if (right && !left)
            xSpeed = playerSpeed;

        // if player is on ground
        if (!inAir) {
            if (!isEntityOnFloor()) {
                inAir = true;
            }
        }
        if (CanMoveHere(player.x, player.y + airSpeed)) {
            player.y += airSpeed;
            airSpeed += gravity;
        } else {
            player.y = (int) GetEntityYPosUnderRoofOrAboveFloor(airSpeed);
            if (airSpeed > 0)
                resetInAir();
            else
                airSpeed = 0;
        }
        updateXPos(xSpeed);
    }

    boolean startedABattle = false;

    void collision(int item) {
        lives--;
        initializeGame();
    }

    // This function detects of player is on floor
    boolean isEntityOnFloor() {
        if (player.y <= screenHeight)
            return false;
        return true;
    }

    // This function models the jump of the player
    void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    // this resets the player's motion in the air
    void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    /**
     * This function models the fall or jump of a player
     * @param airSpeed is the speed that the player travels in air
     * @return location of y coordinate of nearest platform
     */
    double GetEntityYPosUnderRoofOrAboveFloor(double airSpeed) {
        if (airSpeed > 0) { // falling
            int platformYPos = (player.y/platformSize) * platformSize;
            int yOffset = platformSize - player.height;
            return platformYPos + yOffset - 1;
        } else { // jumping
            return player.y + gravity;
        }
    }


    /**
     * This function moves the player right beside the platform
     * @param xSpeed current speed of the player in x direction
     * @return x position of platform closest to it
     */
    double GetEntityXPosNextToWall(double xSpeed) {
        int currentPlatform = (int) (player.x / platformSize);
        if (xSpeed > 0) { // right
            int platformXPos = currentPlatform * platformSize;
            int xOffset = platformSize - player.width;
            return platformXPos + xOffset - 1;
        } else { // left
            int platformXPos = (currentPlatform) * platformSize;
            return platformXPos;
        }
    }

    /**
     * This function updates the x position of the player
     * @param xSpeed current speed in x direction
     */
    void updateXPos(double xSpeed) {
        if (CanMoveHere(player.x + xSpeed, player.y))
            player.x += xSpeed;
        else
            player.x = (int) GetEntityXPosNextToWall(xSpeed);
    }

    /**
     * This function determines if it is possible for a player to move to a location
     * @param xPos current x coordinate of player
     * @param yPos current y coordinate of player
     * @return whether the player can move to specified location
     */
    boolean CanMoveHere(double xPos, double yPos) {
        if (!isPlatform(xPos, yPos)) { // check top left corner
            if (!isPlatform(xPos + player.width, yPos + player.height)) { // check bottom right corner
                if (!isPlatform(xPos + player.width, yPos)) { // check top right corner
                    if (!isPlatform(xPos, yPos + player.height)) { // check bottom left corner
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This function checks if current square is platform or not
     * @param x coordinate
     * @param y coordinate
     * @return whether the current pos is platform
     */
    boolean isPlatform(double x, double y) {
        int xPos = (int) (x / platformSize), yPos = (int) (y / platformSize);
        if (screen[yPos][xPos] == 1)
            return true;
        return false;
    }

    // This function keeps the player within the boundaries
    void keepInBound() {
        if (player.x < platformSize) {
            player.x = platformSize;
        } else if (player.x > screenWidth - player.width - platformSize) {
            initializeGame();
        }

        if (player.y < 0) {
            player.y = 0;
            yVel = 0;
        } else if (player.y > screenHeight - player.height - 100) {
            player.y = screenHeight - player.height;
            inAir = false;
            yVel = 0;
        }
    }
}

class Platform {
    int x, y, len;

    public Platform(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

