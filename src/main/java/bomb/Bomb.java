package bomb;

import java.awt.*;
import java.awt.image.BufferedImage;

import MenuSetUp.Sound;
import entity.Entity;
import main.GamePanel;
import res.LoadResource;

import static MenuSetUp.DimensionSize.tileSize;

public class Bomb {

    public String name;
    public Entity owner = null;

    public GamePanel gp;
    public int x, y;

    // ANIMATION
    protected BufferedImage[] sprites = null;
    protected BufferedImage[] idle = null;
    protected BufferedImage[] explosion = null;
    protected int spriteTime;// time between 2 sprites
    protected int spriteNum = 0;// index of the using sprite
    protected int spriteCounter = 0;// should be frame counter

    // COUNTDOWN
    protected int countDown;

    // STATE
    public boolean exploding = false;
    public boolean exploded = false;

    // COLLISION
    public Rectangle solidArea;
    public boolean collision = true;
    public boolean letPlayerPassThrough = true;

    // FLAME
    public Flame flame;
    public int flameLength = 1;

    // SOUND
    Sound explosionSound;

    public Bomb() {
    }

    public Bomb(GamePanel gp, int x, int y, Entity owner, int flameLength) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.flameLength = flameLength;
        explosionSound = LoadResource.explosionSound;
    }

    public int col() {
        return x / tileSize;
    }

    public int row() {
        return y / tileSize;
    }

    public void update() {
    }

    public void draw(Graphics2D g2) {
    }

    void explode() {
        exploding = true;
        sprites = explosion;
        spriteCounter = 0;
        spriteNum = 0;
        explosionSound.play();
    }

    public void setCountDown(int secondLeft) {
        countDown = secondLeft;
    }

    void checkPlayer() {

        if (letPlayerPassThrough) {

            Rectangle playerSolidArea = new Rectangle(owner.solidArea);
            playerSolidArea.x += owner.x;
            playerSolidArea.y += owner.y;

            if (!playerSolidArea.intersects(solidArea)) {
                letPlayerPassThrough = false;
            }
        }
    }
}