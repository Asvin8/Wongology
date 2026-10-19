/**
 * Asvin and Alex
 * Due date: Jan 22
 * This is level 1
 */

package codeFiles;

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
import java.util.*;

import javax.swing.*;

public class Level1 extends JPanel implements Runnable, KeyListener {
    boolean isKeyPressed = false;
    static int speed = 60;
    static Image startScreen;
    static Image playerImageLeft;
    static Image playerImageRight;
    static Image playerImageUp;
    static Image playerImageDown;
    static Image stringMon,fileMon,binaryMon,exceptionMon;
    static Image dark;
    static Image computer;
    public static int lvl1Points = 0;
    int FPS = 60;
    static JDialog battleDialog = new JDialog(new JFrame(),"battle", true);
    int wongArrY=0;
    int wongArrX=0;

    char[][] gameArr = {
            {'X','X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A','X'},
            {'X','X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X','X'}
    };

    Queue <String> monQueue = new LinkedList<>();
    Thread thread;
    Rectangle wong = new Rectangle(60, 60, 30, 50);
    ArrayList<Level1Mon> monsterList = new ArrayList<>();
    boolean go;
    String direction = "down";

    public Level1() {
        setPreferredSize(new Dimension(840, 840));
        setVisible(true);

        thread = new Thread(this);
        thread.start();
    }
//load images
    //P,V/none
    public void loadImages() {
        MediaTracker tracker = new MediaTracker(this);

        try {
            // rendering game images
            startScreen = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/level1back.png");
            tracker.addImage(startScreen, 0);
            playerImageRight = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/wongright.png");
            tracker.addImage(playerImageLeft, 1);
            playerImageLeft = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/wongleft.png");
            tracker.addImage(playerImageRight, 2);
            playerImageUp = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/wongback.png");
            tracker.addImage(playerImageLeft, 3);
            playerImageDown = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/wongforward.png");
            tracker.addImage(playerImageRight, 4);
            computer = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/computer.png");
            tracker.addImage(computer, 4);
            exceptionMon = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/exceptionmonster.png");
            tracker.addImage(computer, 4);
            fileMon = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/filemonster.png");
            tracker.addImage(computer, 4);
            stringMon = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/stringmonster.png");
            tracker.addImage(computer, 4);
            binaryMon = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/binaryMonster.png");
            tracker.addImage(computer, 4);
            dark = Toolkit.getDefaultToolkit().getImage("src/Level1Pics/dark.png");
            tracker.addImage(dark, 4);

            System.out.println("Images loaded!");

        } catch (Exception e) {
            System.out.println("Error loading images");
        }

        // make sure the images are loaded before continuing
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            System.out.println("Error");
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(startScreen, 0, 0, null);

        g2.drawString(String.valueOf(speed), 200, 200);

        g2.drawImage(Main.mark, 785, 840/2, null);
        g2.setFont(new Font("Arial", Font.PLAIN, 25));
        g2.setColor(Color.WHITE);
        g2.drawString(String.valueOf(lvl1Points), 785, 840/2 + 100);
//prints dark
        for(int i = 1; i < gameArr.length-1; i++){
            for(int j = 1; j < gameArr[i].length-1;j++){
                if(((direction=="right"&&gameArr[i][j-1]!='W')||(direction=="left"&&gameArr[i][j+1]!='W')||(direction=="up"&&gameArr[i+1][j]!='W')||(direction=="down"&&gameArr[i-1][j]!='W'))){
                    g2.drawImage(dark, j*60, i*60, null);
                }
            }
        }
//checks if next is a monster
        if(direction == "left") {
            g2.drawImage(playerImageLeft, wong.x, wong.y, null);
            if(gameArr[wongArrY][wongArrX-1]=='S'){
                g2.drawImage(stringMon,wong.x-speed,wong.y,null);
            }
            else if(gameArr[wongArrY][wongArrX-1]=='B'){
                g2.drawImage(binaryMon,wong.x-speed,wong.y,null);
            }
            else if(gameArr[wongArrY][wongArrX-1]=='F'){
                g2.drawImage(fileMon,wong.x-speed,wong.y,null);
            }
            else if(gameArr[wongArrY][wongArrX-1]=='E'){
                g2.drawImage(exceptionMon,wong.x-speed,wong.y,null);
            }
            else if(wong.x-speed>=60){
                g2.drawImage(computer,wong.x-speed,wong.y,null);
            }
        }
        if(direction =="right"){
            g2.drawImage(playerImageRight, wong.x, wong.y, null);
            if(gameArr[wongArrY][wongArrX+1]=='S'){
                g2.drawImage(stringMon,wong.x+speed,wong.y,null);
            }
            else if(gameArr[wongArrY][wongArrX+1]=='B'){
                g2.drawImage(binaryMon,wong.x+speed,wong.y,null);
            }
            else if(gameArr[wongArrY][wongArrX+1]=='E'){
                g2.drawImage(exceptionMon,wong.x+speed,wong.y,null);
            }
            else if(gameArr[wongArrY][wongArrX+1]=='F'){
                g2.drawImage(fileMon,wong.x+speed,wong.y,null);
            }
            else if(wong.x+speed<=720){
                g2.drawImage(computer,wong.x+speed,wong.y,null);
            }


        }
        if(direction =="up"){

            g2.drawImage(playerImageUp, wong.x, wong.y, null);
            if(gameArr[wongArrY-1][wongArrX]=='S'){
                g2.drawImage(stringMon,wong.x,wong.y-speed,null);
            }
            else if(gameArr[wongArrY-1][wongArrX]=='B'){
                g2.drawImage(binaryMon,wong.x,wong.y-speed,null);
            }
            else if(gameArr[wongArrY-1][wongArrX]=='E'){
                g2.drawImage(exceptionMon,wong.x,wong.y-speed,null);
            }
            else if(gameArr[wongArrY-1][wongArrX]=='F'){
                g2.drawImage(fileMon,wong.x,wong.y-speed,null);
            }
            else if(wong.y - speed >= 60){
                g2.drawImage(computer,wong.x,wong.y-speed,null);
            }
        }
        if(direction =="down"){
            g2.drawImage(playerImageDown, wong.x, wong.y, null);
            if(gameArr[wongArrY+1][wongArrX]=='S'){
                g2.drawImage(stringMon,wong.x,wong.y+speed,null);
            }
            else if(gameArr[wongArrY+1][wongArrX]=='B'){
                g2.drawImage(binaryMon,wong.x,wong.y+speed,null);
            }
            else if(gameArr[wongArrY+1][wongArrX]=='E'){
                g2.drawImage(exceptionMon,wong.x,wong.y+speed,null);
            }
            else if(gameArr[wongArrY+1][wongArrX]=='F'){
                g2.drawImage(fileMon,wong.x,wong.y+speed,null);
            }
            else if(wong.y + speed <= 720){
                g2.drawImage(computer,wong.x,wong.y+speed,null);
            }
        }
        AlphaComposite transparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        g2.setComposite(transparency);
    }


    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        if (key == 'a') { // left
            direction = "left";
        } else if (key == 'd') { // right
            direction = "right";
        } else if (key == 'w') { // up
            direction = "up";
        } else if (key == 's') { // down
            direction = "down";
        }
        else if(key =='q'){
            go = true;
        }
        System.out.println(key);
        isKeyPressed = true;
    }
//initiazlie the game
    //P: none, V: none
    public void initializeGame() {
        loadImages();
        wongArrX = wong.x/60;
        wongArrY = wong.y/60;
        gameArr[wongArrY][wongArrX]='W';
        monQueue.add("string");
        monQueue.add("file");
        monQueue.add("exception");
        monQueue.add("binary");
        generateMon();
        printboardArr();
    }
//prints board array
    //P,V.none
    public void printboardArr(){
        for(int i = 0; i < gameArr.length; i++){
            for(int j = 0; j < gameArr[i].length;j++){
                System.out.print(gameArr[i][j] + " ");
            }
            System.out.println();
        }
    }
//generates the monster on array
    //P,V = none
    public void generateMon(){
        for(int i = 0; i <5;i++){
            int randX = (int)(Math.random()*11)+2;
            int randY = (int)(Math.random()*11)+2;
            String type = monQueue.remove();

            if(type.equals("string")){
                gameArr[randX][randY]= 'S';
            }
            if(type.equals("binary")){
                gameArr[randX][randY]= 'B';
            }
            if(type.equals("exception")){
                gameArr[randX][randY]= 'E';
            }
            if(type.equals("file")){
                gameArr[randX][randY]= 'F';
            }

            Level1Mon newMonster = new Level1Mon(randX,randY,type);
            monsterList.add(newMonster);
            monQueue.add(type);
            Collections.sort(monsterList);

        }

    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    //runs code
    //P,R, none
    @Override
    public void run() {
        // start game
        initializeGame();
        if(lvl1Points >= 5) closeDialogBox("lvl1");
        while (true) {
            move();
            this.repaint();
            try {
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//player movement method
    //P,R, none
    private void move() {
        if (isKeyPressed) {

            if(go) {
                wongArrX = wong.x/60;
                wongArrY = wong.y/60;

                boolean hasMoved = false;
                //detect if can move to empty
                if (direction == "left" && (wong.x - speed >= 60)&&gameArr[wongArrY][wongArrX-1]=='A') {
                    wong.x -= speed;
                    hasMoved = true;
                } else if (direction == "right" && (wong.x + speed <= 720)&&gameArr[wongArrY][wongArrX+1]=='A') {
                    wong.x += speed;
                    hasMoved = true;
                }
                else if (direction == "up" && (wong.y - speed >= 60)&&gameArr[wongArrY-1][wongArrX]=='A') {
                    wong.y += -speed;
                    hasMoved = true;
                } else if (direction == "down" && (wong.y + speed <= 720)&&gameArr[wongArrY+1][wongArrX]=='A') {
                    wong.y += speed;
                    hasMoved = true;
                }
                //detect battle conditions

                if (direction == "left" && (wong.x - speed >= 60)&&gameArr[wongArrY][wongArrX-1]!='A'&&gameArr[wongArrY][wongArrX-1]!='X') {
                    System.out.println("Battle" + gameArr[wongArrY][wongArrX-1]);
                    checkMonsterType(gameArr[wongArrY][wongArrX-1]);
                    gameArr[wongArrY][wongArrX-1]='A';
                    startBattle();
                }
                else if (direction == "right" && (wong.x + speed <= 720)&&gameArr[wongArrY][wongArrX+1]!='A'&&gameArr[wongArrY][wongArrX+1]!='X') {
                    System.out.println("Battle" + gameArr[wongArrY][wongArrX+1]);
                    checkMonsterType(gameArr[wongArrY][wongArrX+1]);
                    gameArr[wongArrY][wongArrX+1]='A';
                    startBattle();
                }

                else if (direction == "up" && (wong.y - speed >= 60)&&gameArr[wongArrY-1][wongArrX]!='A'&&gameArr[wongArrY-1][wongArrX]!='X') {
                    System.out.println("Battle" + gameArr[wongArrY-1][wongArrX]);
                    checkMonsterType(gameArr[wongArrY-1][wongArrX]);
                    gameArr[wongArrY-1][wongArrX]='A';
                    startBattle();
                }

                else if (direction == "down" && (wong.y + speed <= 720)&&gameArr[wongArrY+1][wongArrX]!='A'&&gameArr[wongArrY+1][wongArrX]!='X') {
                    System.out.println("Battle" + gameArr[wongArrY+1][wongArrX]);
                    checkMonsterType(gameArr[wongArrY+1][wongArrX]);
                    gameArr[wongArrY+1][wongArrX]='A';
                    startBattle();
                }

                //if direction and valid move
                isKeyPressed = false;

                if (hasMoved) {
                    gameArr[wongArrY][wongArrX]='A';
                    wongArrX = wong.x/60;
                    wongArrY = wong.y/60;
                    gameArr[wongArrY][wongArrX]='W';
                    printboardArr();
                }
            }
            go=false;
        }
    }
//checks moneter type
    //P: char X, R, none
    void checkMonsterType(char x) {
        if(x == 'S') Main.monsterType = 9;
        else if(x == 'B') Main.monsterType = 6;
        else if(x == 'F') Main.monsterType = 8;
        else if(x == 'E') Main.monsterType = 7;
        System.out.println("x = " + x);
    }
//closes dialogue box
    //P, R, void
    public static void closeDialogBox(String action) {
        if(action.equals("battle")) battleDialog.dispose();
        if(action.equals("lvl1")) Main.level1Dialog.dispose();
    }
//starts battle
    //P, V, void
    void startBattle() {
        Battle panel = new Battle(1);
        battleDialog.add(panel);
        battleDialog.addKeyListener(panel);
        battleDialog.setSize(840, 840);
        battleDialog.setVisible(true);
        battleDialog.pack();
        battleDialog.setResizable(true);
        battleDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
