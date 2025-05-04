package entity;

import main.GamePanel;
import main.UtilityTool;

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
    protected int maxSpeedLevel;
    protected int speed;

    public Direction direction = Direction.DOWN;// direction of sprites
    protected BufferedImage[] sprites;
    protected int spriteTime;// time between 2 sprites
    protected int spriteNum = 0;// index of the using sprite
    protected int spriteCounter = 0;// should be frame counter

    public Rectangle solidArea;
    public boolean collisionOn = true;
    public boolean canMoveUp = true, canMoveDown = true, canMoveLeft = true, canMoveRight = true;

    int timer = 0;
    int invincibleTime = 0;

    protected int maxHp;
    protected int hp;
    public boolean isAlive = true;
    protected boolean isVulnerable = true;
    protected boolean isGettingHit = false;
    protected boolean hasShield = false;

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

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        setHp(maxHp);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(hp, maxHp);
    }

    public void increaseHp() {
        hp++;
        hp = Math.max(hp, maxHp);
    }

    public void decreaseHp() {
        hp--;
        if (hp == 0) isAlive = false;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCurrentSpeedLevel() {
        return currentSpeedLevel;
    }

    public void setSpeedLevel(int level) {
        level = Math.max(0, level);
        currentSpeedLevel = Math.min(level, maxSpeedLevel);
        speed = speedLevel[currentSpeedLevel];
    }

    public int getMaxSpeedLevel() {
        return maxSpeedLevel;
    }

    public void setMaxSpeedLevel(int level) {
        level = Math.max(0, level);
        maxSpeedLevel = Math.min(level, speedLevel.length - 1);
        setSpeedLevel(maxSpeedLevel);
    }

    public void increaseSpeed() {
        setSpeedLevel(++currentSpeedLevel);
    }

    public void decreaseSpeed() {
        setSpeedLevel(--currentSpeedLevel);
    }

    public void moveUp() {
        if (!canMoveUp) return;
        y -= speed;
    }

    public void moveDown() {
        if (!canMoveDown) return;
        y += speed;
    }

    public void moveLeft() {
        if (!canMoveLeft) return;
        x -= speed;
    }

    public void moveRight() {
        if (!canMoveRight) return;
        x += speed;
    }

    public void getHit() {
        System.out.println("hit " + name);
        isGettingHit = true;
        if (hasShield) {
            System.out.println("Hit shield!");
            return;
        }
        if (isVulnerable) decreaseHp();
    }

    public boolean isVulnerable() {
        return isVulnerable;
    }

    public void activateShield() {
        hasShield = true;
        setInvincibleTime(1.0);
    }

    public int getInvincibleTime() {
        return invincibleTime;
    }

    public void setInvincibleTime(int frames) {
        invincibleTime = frames;
    }

    public void setInvincibleTime(double seconds) {
        invincibleTime = UtilityTool.convertTime(seconds);
    }

    public void enterInvincibleTime() {
        invincibleTime--;
        isVulnerable = false;
        if (invincibleTime == 0) {
            hasShield = false;
            isVulnerable = true;
            isGettingHit = false;
            setInvincibleTime(1.0);
        }
    }
}