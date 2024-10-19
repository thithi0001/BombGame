package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import bomb.Bomb;
import entity.Item;
import entity.Player;
import tile.Map;
import tile.TileManager;

import static MenuSetUp.DimensionSize.screenHeight;
import static MenuSetUp.DimensionSize.screenWidth;

public class GamePanel extends JPanel implements Runnable {

    // FPS
    public int FPS = 60;

    public TileManager tileManager = new TileManager(this);
    public Map map;
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this, keyH);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public ArrayList<Bomb> bombs = new ArrayList<>();// all bombs in the map

    public GamePanel(String mapFileName) {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);// this can be focused to receive key input
        this.map = new Map(this, mapFileName);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void endGame(String result) {
        // result: win, lose, uncompleted
        switch (result) {
            case "win":
                // score
                // completion time
                // OPTIONS: replay, next level, quit (->level panel)
                // save user data
                break;

            case "lose":
                // score
                // playing time
                // OPTIONS: replay, quit (-> level panel)
                break;

            case "uncompleted":
                // OPTIONS: resume, quit (-> level panel)
                break;
        }
//        gameThread = null;
    }

    @Override
    public void run() {

        double drawInterval = 1e9 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();// call paintComponent
                delta--;
                drawCount++;
            }

            if (timer >= 1e9) {
                // System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {

        bombs.clear();
        player.update();
        map.items.forEach(Item::update);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // DRAW ORDER
        // background outside the tile map
        // tileMap
        // player and monster
        // bomb and item
        // ui

        map.draw(g2);

        player.draw(g2);

        g2.dispose();// save memories
    }

}