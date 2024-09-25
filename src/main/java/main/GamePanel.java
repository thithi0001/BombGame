package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import bomb.NormalBomb;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    public final int originalTileSize = 16;
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale;

    public final int maxScreenCol = 12;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize;// 192
    public final int screenHeight = maxScreenRow * tileSize;// 192

    // FPS
    public int FPS = 60;

    public TileManager tileManager = new TileManager(this);
    KeyHandler keyh = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this, keyh);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public NormalBomb nBomb = new NormalBomb(this, tileSize, tileSize);

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        // this.setBackground(Color.GREEN);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyh);
        this.setFocusable(true);// this can be focused to receive key input
    }

    void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
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

        player.update();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // DRAW ORDER
        // background outside the tilemap
        // tilemap
        // player and monster
        // bomb and item
        // ui

        tileManager.draw(g2);

        player.draw(g2);

        // nBomb.draw(g2);

        g2.dispose();// save memories
    }

}