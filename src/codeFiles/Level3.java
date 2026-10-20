package codeFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("serial")
public class Level3 extends JPanel implements Runnable, KeyListener {
    public static int lvl3Points = 0;

    Rectangle wong = new Rectangle(0, 0, 45, 60);

    Rectangle monster = new Rectangle(400,400,60,60);

    Rectangle computerRect = new Rectangle(420,420,30,30);
    static JDialog battleDialog = new JDialog(new JFrame(),"battle", true);

    Rectangle[] walls = new Rectangle[40];
    boolean up, down, left, right;
    int speed = 3;
    int screenWidth = 840;
    int screenHeight = 840;
    Thread thread;
    int FPS = 60;

    double monSpeed = 1.5;

    int screenImage=1;

    Queue <Integer> screenQueue = new LinkedList<>();

    char[][] gameArr = new char[14][14];

    public static Image playerImageLeft, playerImageRight, playerImageUp, playerImageDown;
    public static Image screen1,screen2,screen3,screen4,tree,treelight,treedark,marks,computer,treeMon;

    public Level3() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setVisible(true);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        initialize();
        while (true) {
            //main game loop
            update();
            this.repaint();
            try {
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        gameArr = new char[14][14];

        for(int i = 0; i < walls.length; i++){
            int randX = (int)(Math.random()*12)+1;
            int randY = (int)(Math.random()*12)+1;

            if(randX==7&&randY==7){
                while(randX==7&&randY==7){
                    randX = (int)(Math.random()*12)+1;
                    randY = (int)(Math.random()*12)+1;
                }
            }

            walls[i] = new Rectangle(randX*60, randY*60, 60, 60);
            gameArr[randY][randX]='T';
        }
        monster.x=420;
        monster.y=420;
    }

    public void initialize() {
        screenQueue.add(1);
        screenQueue.add(2);
        screenQueue.add(3);
        screenQueue.add(4);

        loadImages();
        reset();
    }

    public void update() {
        move();
        keepInBound();
        moveMonster();

        for (int i = 0; i < walls.length; i++)
            checkCollision(walls[i]);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(screenImage==1){
            g2.drawImage(screen1, 0, 0, null);
        }
        else if(screenImage==2){
            g2.drawImage(screen2, 0, 0, null);
        }
        else if(screenImage==3){
            g2.drawImage(screen3, 0, 0, null);
        }
        else if(screenImage==4){
            g2.drawImage(screen4, 0, 0, null);
        }

        g2.drawImage(marks,800,800,null);
        g2.drawImage(computer,420,420,null);
        g2.drawImage(treeMon,monster.x,monster.y,null);

        for (int i = 0; i < walls.length; i++) {
            if(i%3==0)
                g2.drawImage(tree, walls[i].x, walls[i].y, null);
            else if(i%3==1)
                g2.drawImage(treelight, walls[i].x, walls[i].y, null);
            else if(i%3==2)
                g2.drawImage(treedark, walls[i].x, walls[i].y, null);
        }

        g2.drawImage(Main.mark, 785, 840/2, null);
        g2.setFont(new Font("Arial", Font.PLAIN, 25));
        g2.setColor(Color.WHITE);
        g2.drawString(String.valueOf(Main.points), 785, 840/2 + 100);

        if(left){
            g2.drawImage(playerImageLeft, wong.x, wong.y, null);
        }
        else if(right){
            g2.drawImage(playerImageRight, wong.x, wong.y, null);
        }
        else if(up){
            g2.drawImage(playerImageUp, wong.x, wong.y, null);
        }
        else if(down){
            g2.drawImage(playerImageDown, wong.x, wong.y, null);
        }
        else {
            g2.drawImage(playerImageDown, wong.x, wong.y, null);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            left = true;
            right = false;
        } else if (key == KeyEvent.VK_D) {
            right = true;
            left = false;
        } else if (key == KeyEvent.VK_W) {
            up = true;
            down = false;
        } else if (key == KeyEvent.VK_S) {
            down = true;
            up = false;
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
            up = false;
        } else if (key == KeyEvent.VK_S) {
            down = false;
        }
    }

    void move() {
        if (left)
            wong.x -= speed;
        else if (right)
            wong.x += speed;

        if (up)
            wong.y += -speed;
        else if (down)
            wong.y += speed;
    }

    void moveMonster(){
        if(wong.x>monster.x){
            monster.x+=monSpeed;
        }
        else if(wong.x<monster.x){
            monster.x-=monSpeed;
        }
        if(wong.y>monster.y) {
            monster.y+=monSpeed;
        }
        else if(wong.y<monster.y){
            monster.y-=monSpeed;
        }
    }

    void keepInBound() {
        if (wong.x < 0){
            wong.x = screenWidth - wong.width;
        }

        else if (wong.x > screenWidth - wong.width){
            wong.x = 0;
        }

        if (wong.y < 0){
            wong.y = screenHeight - wong.height;
        }

        else if (wong.y > screenHeight - wong.height){
            wong.y = 0;
        }
    }

    void checkCollision(Rectangle wall) {
        //check if rect touches wall
        if (wong.intersects(wall)) {
            //stop the rect from moving
            double left1 = wong.getX();
            double right1 = wong.getX() + wong.getWidth();
            double top1 = wong.getY();
            double bottom1 = wong.getY() + wong.getHeight();
            double left2 = wall.getX();
            double right2 = wall.getX() + wall.getWidth();
            double top2 = wall.getY();
            double bottom2 = wall.getY() + wall.getHeight();
            if (right1 > left2 &&
                    left1 < left2 &&
                    right1 - left2 < bottom1 - top2 &&
                    right1 - left2 < bottom2 - top1) {
                //rect collides from left side of the wall
                wong.x = wall.x - wong.width;
            } else if (left1 < right2 &&
                    right1 > right2 &&
                    right2 - left1 < bottom1 - top2 &&
                    right2 - left1 < bottom2 - top1) {
                //rect collides from right side of the wall
                wong.x = wall.x + wall.width;
            } else if (bottom1 > top2 && top1 < top2) {
                //rect collides from top side of the wall
                wong.y = wall.y - wong.height;
            } else if (top1 < bottom2 && bottom1 > bottom2) {
                //rect collides from bottom side of the wall
                wong.y = wall.y + wall.height;
            }
        }

        if(wong.intersects(monster)) {
            wong.x=0;
            wong.y=0;
            monster.x=400;
            monster.y=400;
            battleDialog = new JDialog(new JFrame(),"battle", true);
            Battle panel = new Battle(3);
            battleDialog.add(panel);
            battleDialog.addKeyListener(panel);
            battleDialog.setSize(840, 780);
            battleDialog.setVisible(true);
            battleDialog.pack();
            battleDialog.setResizable(true);
            battleDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            reset();
        }

        if(wong.intersects(computerRect)){
            Main.points += 1;
            wong.x=0;
            wong.y=0;
            monster.x=400;
            monster.y=400;
            Integer chooseScreen = screenQueue.remove();
            screenImage = chooseScreen.intValue();
            screenQueue.add(chooseScreen);
        }
    }

    public static void closeDialogBox(String action) {
        if(action.equals("battle")) battleDialog.dispose();
    }

    public void loadImages() {
        MediaTracker tracker = new MediaTracker(this);

        try {
            // rendering game images
            screen1 = Toolkit.getDefaultToolkit().getImage("assets/level3/green1.png");
            tracker.addImage(screen1, 5);
            screen2 = Toolkit.getDefaultToolkit().getImage("assets/level3/green2.png");
            tracker.addImage(screen2, 6);
            screen3 = Toolkit.getDefaultToolkit().getImage("assets/level3/green3.png");
            tracker.addImage(screen3, 7);
            screen4 = Toolkit.getDefaultToolkit().getImage("assets/level3/green4.png");
            tracker.addImage(screen4, 8);
            playerImageRight = Toolkit.getDefaultToolkit().getImage("assets/level3/wongright.png");
            tracker.addImage(playerImageLeft, 1);
            playerImageLeft = Toolkit.getDefaultToolkit().getImage("assets/level3/wongleft.png");
            tracker.addImage(playerImageRight, 2);
            playerImageUp = Toolkit.getDefaultToolkit().getImage("assets/level3/wongback.png");
            tracker.addImage(playerImageLeft, 3);
            playerImageDown = Toolkit.getDefaultToolkit().getImage("assets/level3/wongforward.png");
            tracker.addImage(playerImageRight, 4);
            tree = Toolkit.getDefaultToolkit().getImage("assets/level3/tree.png");
            tracker.addImage(tree, 9);
            treelight = Toolkit.getDefaultToolkit().getImage("assets/level3/treelight.png");
            tracker.addImage(tree, 10);
            treedark = Toolkit.getDefaultToolkit().getImage("assets/level3/treedark.png");
            tracker.addImage(tree, 11);
            marks = Toolkit.getDefaultToolkit().getImage("assets/ui/marks.png");
            tracker.addImage(tree, 12);
            computer = Toolkit.getDefaultToolkit().getImage("assets/level3/computer.png");
            tracker.addImage(tree, 13);
            treeMon = Toolkit.getDefaultToolkit().getImage("assets/level3/treeMon.png");
            tracker.addImage(treeMon, 14);

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
}
