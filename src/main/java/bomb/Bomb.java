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
    public final int speed = 4;
    public String direction = "down";

    // ANIMATION
    protected BufferedImage[] sprites = null;
    protected BufferedImage[] idle = null;
    protected BufferedImage[] explosion = null;
    protected int spriteTime;// time between 2 sprites
    protected int spriteNum = 0;// index of the using sprite
    protected int spriteCounter = 0;// should be frame counter
    protected int drawX, drawY;// use for pickup bomb

    // COUNTDOWN
    protected int countDown;

    // STATE
    public boolean exploding = false;
    public boolean exploded = false;
    public boolean beingPickedUp = false;
    public boolean moving = false;

    // COLLISION
    public Rectangle solidArea;
    public boolean collision = true;
    public boolean letPlayerPassThrough = true;

    // FLAME
    public Flame flame = null;
    public int flameLength = 1;

    // SOUND
    Sound explosionSound;

    public Bomb() {
    }

    public Bomb(GamePanel gp, int x, int y, Entity owner, int flameLength) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.drawX = x;
        this.drawY = y;
        this.owner = owner;
        this.flameLength = flameLength;
        this.solidArea = new Rectangle(x, y, tileSize, tileSize);
        explosionSound = LoadResource.explosionSound;
    }

    public int col() {
        return x / tileSize;
    }

    public int row() {
        return y / tileSize;
    }

    public void update() {
        if (moving) move();
        checkPlayer();
    }

    public void draw(Graphics2D g2) {

        g2.drawImage(sprites[spriteNum], x, y, null);
        if (exploding) {
            flame.draw(g2);
        }
    }

    void explode() {
        exploding = true;
        sprites = explosion;
        spriteCounter = 0;
        spriteNum = 0;
        explosionSound.play();
    }

    public void beingPickedUp() {
        drawX -= 36;
        beingPickedUp = true;
    }

    public void resetDraw() {
        drawX = x;
    }

    public void setCountDown(int frameLeft) {
        countDown = frameLeft;
    }

    void checkPlayer() {

        if (letPlayerPassThrough)
            if (!owner.solidArea.intersects(solidArea))
                letPlayerPassThrough = false;
    }

    void changeOwner(Entity newOwner) {
        this.owner = newOwner;
    }

    public void move() {
        boolean stop = gp.cChecker.checkTileForBomb(this)
                || gp.cChecker.checkBombForBomb(this);
        if (stop) {
            moving = false;
            return;
        }
        switch (direction) {
            case "up":
                y -= speed;
                break;

            case "down":
                y += speed;
                break;

            case "left":
                x -= speed;
                break;

            case "right":
                x += speed;
                break;
        }
        solidArea.x = x;
        solidArea.y = y;
    }

}