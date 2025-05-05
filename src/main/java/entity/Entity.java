package entity;

import main.GamePanel;
import main.UtilityTool;
import skill.Skill;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;

import static MenuSetUp.DimensionSize.tileSize;

public class Entity {

    GamePanel gp;
    public String name;
    public int x, y;// x = column, y = row
    final int[] speedLevel = {1, 2, 3, 4, 6, 8, 12, 16, 24, 48};
    private int currentSpeedLevel = 0;
    protected int maxSpeedLevel;
    protected int speed;

    protected int maxHp;
    protected int hp;
    public boolean isAlive = true;
    protected boolean isVulnerable = true;
    protected boolean isGettingHit = false;
    protected boolean hasShield = false;

    protected EntityStatus status = EntityStatus.NORMAL;

    public ArrayList<Skill> skills;

    public Direction direction = Direction.DOWN;// direction of sprites
    protected BufferedImage[] sprites;
    protected int spriteTime;// time between 2 sprites
    protected int spriteNum = 0;// index of the using sprite
    protected int spriteCounter = 0;// should be frame counter

    public Rectangle solidArea;
    public boolean collisionOn = true;
    public boolean canMoveUp = true, canMoveDown = true, canMoveLeft = true, canMoveRight = true;

    protected int timer = 0;
    protected int invincibleTime = 0;

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public void draw(Graphics2D g2) {}
    public void drawEffect(Graphics2D g2) {
        if (status == EntityStatus.NORMAL) return;
        float[] scales;
        switch (status) {
            case GET_HIT:
                scales = new float[]{1f, 0.3f, 0.3f, 1f};// RED
                g2.drawImage(sprites[spriteNum], getRop(scales), x, y);
                break;

            case HARDEN:
                scales = new float[]{0.7f, 0.65f, 0.65f, 1f};// GRAY
                g2.drawImage(sprites[spriteNum], getRop(scales), x, y);
                break;

            case ACCELERATE:
                scales = new float[]{0.075f, 0.96f, 0.96f, 1f};// CYAN
                g2.drawImage(sprites[spriteNum], getRop(scales), x, y);
                break;

            case HEAL:
                scales = new float[]{0.2f, 0.9f, 0.25f, 1f};// GREEN
                g2.drawImage(sprites[spriteNum], getRop(scales), x, y);
                break;

        }
    }

    RescaleOp getRop(float[] scales) {// R,G,B,A
        return new RescaleOp(scales, new float[4], null);
    }

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
        hp = Math.min(hp, maxHp);
    }

    public void decreaseHp() {
        hp--;
        status = EntityStatus.GET_HIT;
        isVulnerable = false;
        if (hp <= 0) isAlive = false;
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
        if (isGettingHit && !isVulnerable) return;
        isGettingHit = true;
        if (isVulnerable) decreaseHp();
    }

    public boolean isVulnerable() {
        return isVulnerable;
    }

    public void setVulnerable(boolean bool) {
        isVulnerable = bool;
    }

    public void activateShield() {
        hasShield = true;
        isVulnerable = false;
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
        if (invincibleTime == 0) {
            System.out.println("End invincible time!");
            isVulnerable = true;
            isGettingHit = false;
            setInvincibleTime(1.0);
            status = EntityStatus.NORMAL;

            if (hasShield)
                hasShield = false;
        }
    }
}