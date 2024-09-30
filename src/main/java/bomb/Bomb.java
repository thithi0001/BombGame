package bomb;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Bomb {

    public String name;

    public GamePanel gp;
    public int x, y;

    // ANIMATION
    public BufferedImage[] sprites = null;
    public BufferedImage[] idle = null;
    public BufferedImage[] explosion = null;
    public int spriteTime;// time between 2 sprites
    public int spriteNum = 0;// index of the using sprite
    public int spriteCounter = 0;// should be frame counter

    // COUNTDOWN
    public double countdownInSecond;
    public double countdownInFrame;
    public int countDown;

    // STATE
    public boolean exploding = false;
    public boolean exploded = false;

    // COLLISION
    public Rectangle solidArea;
    public boolean collision = true;
    public boolean letPlayerPassThrough = true;

    // FLAME
    public Flame flame;

    public Bomb(){

    }

    public int col() {

        return (x + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
    }

    public int row() {

        return (y + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
    }
}