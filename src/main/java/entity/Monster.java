package entity;

import java.awt.*;

import main.GamePanel;
import res.LoadResource;

import static MenuSetUp.DimensionSize.tileSize;

import java.util.Random;

public class Monster extends Entity {

    Random random;

    protected int changeDirection = 2;
    boolean moved;

    public Monster(GamePanel gp, int x, int y) {

        this.gp = gp;
        this.name = "monster";
        this.x = x;
        this.y = y;
        random = new Random();

        setDefaultValues();
        getMonsterImage();
    }

    public void getMonsterImage() {
        sprites = LoadResource.monster;
    }

    public void setDefaultValues() {

        speed = 1;
        direction = "down";
        spriteTime = 6;
        x = x * tileSize;
        y = y * tileSize;
        solidArea = new Rectangle(x + 12, y + 20, 24, 24);
    }

    public void update() {

        if (random.nextInt(100) < changeDirection) {
            randomMove();
        }

        gp.cChecker.checkTile(this);
        gp.cChecker.checkBombForEntity(this);
        gp.cChecker.checkPlayerForMonster(this);

        move();

        if (++spriteCounter > spriteTime) {

            if (moved) spriteNum++;
            if (spriteNum == sprites.length) {

                spriteNum = 0;
            }
            spriteCounter = 0;
        }

    }

    void randomMove() {
        int randomDirection = random.nextInt(4);
        switch (randomDirection) {
            case 0:
                if (canMoveUp) {
                    direction = "up";
                }
                break;
            case 1:
                if (canMoveDown) {
                    direction = "down";
                }
                break;
            case 2:
                if (canMoveLeft) {
                    direction = "left";
                }
                break;
            case 3:
                if (canMoveRight) {
                    direction = "right";
                }
                break;
        }
    }

    public void move() {
        moved = false;

        switch (direction) {
            case "up":
                if (canMoveUp) {
                    y -= speed;
                    moved = true;
                }
                break;
            case "down":
                if (canMoveDown) {
                    y += speed;
                    moved = true;
                }
                break;
            case "left":
                if (canMoveLeft) {
                    x -= speed;
                    moved = true;
                }
                break;
            case "right":
                if (canMoveRight) {
                    x += speed;
                    moved = true;
                }
                break;
        }
        solidArea.x = x + 12;
        solidArea.y = y + 20;

        if (!moved) {
            randomMove();
        }

        resetCollision();
    }

    public void draw(Graphics2D g2) {

        if (isAlive) {
            g2.drawImage(sprites[spriteNum], x, y, null);
        }
    }

    @Override
    public void beingHit() {
        super.beingHit();
        isAlive = false;
    }

}


