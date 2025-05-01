package entity;

import main.GamePanel;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static MenuSetUp.DimensionSize.tileSize;

public class Entity {

    GamePanel gp;
    public String name;
    public int x, y;// x = column, y = row
    final int[] speedLevel = {1, 2, 3, 4, 6, 8, 12, 16, 24, 48};
    private int currentSpeedLevel = 0;
    private int maxSpeedLevel = 3;
    protected int speed = speedLevel[currentSpeedLevel];

    protected BufferedImage[] sprites;
    public Direction direction;// direction of sprites
    protected int spriteTime;// time between 2 sprites
    protected int spriteNum = 0;// index of the using sprite
    protected int spriteCounter = 0;// should be frame counter

    public Rectangle solidArea;
    public boolean collisionOn = true;
    public boolean canMoveUp = true, canMoveDown = true, canMoveLeft = true, canMoveRight = true;
    public boolean isAlive = true;

    protected void resetCollision() {
        canMoveUp = true;
        canMoveDown = true;
        canMoveLeft = true;
        canMoveRight = true;
    }

    public int col() {
        return (x + tileSize / 2) / tileSize;
    }

    public int row() {
        return (y + tileSize / 2) / tileSize;
    }

    public Point getPosition() {
        // tra ve vi tri o hien tai dang dung
        return new Point(col(), row());
    }

    public void beingHit() {
        System.out.println("hit " + name);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeedLevel(int level) {
        level = Math.max(0, level);
        currentSpeedLevel = Math.min(level, speedLevel.length);
        speed = speedLevel[level];
    }

    private void setMaxSpeedLevel(int level) {
        level = Math.max(0, level);
        maxSpeedLevel = Math.min(level, speedLevel.length);
    }

    public void increaseSpeed() {
        setSpeedLevel(++currentSpeedLevel);
    }

    public void decreaseSpeed() {
        setMaxSpeedLevel(--currentSpeedLevel);
    }

    public void moveUp() {
        y -= speed;
    }
    public void moveDown() {
        y += speed;
    }
    public void moveLeft() {
        x -= speed;
    }
    public void moveRight() {
        x += speed;
    }
}