package entity;

import main.GamePanel;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static MenuSetUp.DimensionSize.tileSize;

public class Entity {

    GamePanel gp;
    public String name;
    public int x, y;// x = column, y = row
    protected int speed;
//    final int[] speedLevel = {1, 2, 3, 4, 6, 8, 12, 16, 24, 48};
    final int maxSpeed = 4;

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

    public void beingHit() {
        System.out.println("hit " + name);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int n) {
        speed = n;
    }

    public void addSpeed(int n) {
        if (speed < maxSpeed)
            speed += n;
    }
}