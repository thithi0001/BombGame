package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bomb.NormalBomb;
import main.GamePanel;
import main.KeyHandler;
import main.Main;
import main.UtilityTool;

import static MenuSetUp.DimensionSize.tileSize;

public class Player extends Entity {

    KeyHandler keyH;

    BufferedImage[] upSprites, downSprites, leftSprites, rightSprites;
    // idle, death will be up direction
    // bombing, riding animal, kicking bomb, picking up bomb will depend on the
    // direction


    public ArrayList<NormalBomb> bombs = new ArrayList<>();
    int maxBombs = 1;
    double cooldownInSecond;
    double cooldownInFrame;
    int cooldown;
    int timer = 0;

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;
        this.name = "player";

        cooldownInSecond = 1;
        cooldownInFrame = cooldownInSecond * gp.FPS;
        cooldown = (int) cooldownInFrame;

        solidArea = new Rectangle(12, 20, 24, 24);

        setDefaultValues();
        getPlayerImage();
    }

    void setDefaultValues() {

        x = tileSize * 2;
        y = tileSize * 10;
        speed = 4;
        direction = "down";
        spriteTime = 12;
    }

    void getPlayerImage() {

        sprites = UtilityTool.loadSpriteSheet(gp, Main.res + "/bomb/normal/bomb_64_x_16_.png");
    }

    public void update() {

        if (keyH.enterPressed && timer == 0) {

            bombs.add(new NormalBomb(gp, col() * tileSize, row() * tileSize, this));
            timer = cooldown;
        }

        bombs.forEach(NormalBomb::update);
        bombs.removeIf(bomb -> bomb.exploded);

        if (timer > 0) {

            timer--;
        }

        if (keyH.movePressed) {

            if (keyH.upPressed) {
                direction = "up";
            }
            if (keyH.downPressed) {
                direction = "down";
            }
            if (keyH.leftPressed) {
                direction = "left";
            }
            if (keyH.rightPressed) {
                direction = "right";
            }

            // CHECK COLLISION
            gp.cChecker.checkTile(this);
            gp.bombs.addAll(bombs);
            gp.cChecker.checkBomb(this);

            move();
        }

        if (++spriteCounter > spriteTime) {

            if (++spriteNum == sprites.length) {

                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        // BufferedImage image = null;

        // switch (direction) {
        // case "up":
        // image = upSprites[spriteNum];
        // break;
        // case "down":
        // image = downSprites[spriteNum];
        // break;
        // case "left":
        // image = leftSprites[spriteNum];
        // break;
        // case "right":
        // image = rightSprites[spriteNum];
        // break;

        // default:
        // break;
        // }

        // image = sprites[0];
        // g2.drawImage(image, x, y, null);

        // g2.drawImage(sprites[spriteNum], x, y, null);

        g2.setColor(Color.RED);
        g2.fillRect(x + solidArea.x, y + solidArea.y, solidArea.width, solidArea.height);

        for (NormalBomb bomb : bombs) {
            bomb.draw(g2);
        }
    }

    public void move() {

        switch (direction) {
            case "up":
                if (canMoveUp) y -= speed;
                break;
            case "down":
                if (canMoveDown) y += speed;
                break;
            case "left":
                if (canMoveLeft) x -= speed;
                break;
            case "right":
                if (canMoveRight) x += speed;
                break;
        }
        resetCollision(true);
    }
}