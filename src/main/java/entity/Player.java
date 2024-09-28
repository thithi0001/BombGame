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

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    BufferedImage[] upSprites, downSprites, leftSprites, rightSprites;
    // idle, death will be up direction
    // bombing, riding animal, kicking bomb, picking up bomb will depend on the
    // direction


    public ArrayList<NormalBomb> bombs = new ArrayList<NormalBomb>();
    int maxBombs = 1;
    double cooldownInSecond;
    double cooldownInFrame;
    int cooldown;
    int timer = 0;

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        cooldownInSecond = 1;
        cooldownInFrame = cooldownInSecond * gp.FPS;
        cooldown = (int) cooldownInFrame;

        solidArea = new Rectangle(12, 20, 24, 24);

        setDefaultValues();
        getPlayerImage();
    }

    void setDefaultValues() {

        x = gp.tileSize * 1;
        y = gp.tileSize * 1;
        speed = 2;
        direction = "down";
        spriteTime = 12;
    }

    void getPlayerImage() {

        sprites = UtilityTool.loadSpriteSheet(gp, Main.res + "/bomb/normal/bomb_64_x_16_.png");
    }

    public int col() {

        return (x + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
    }

    public int row() {

        return (y + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
    }

    public void update() {

        if (keyH.enterPressed && timer == 0) {

            bombs.add(new NormalBomb(gp, col() * gp.tileSize, row() * gp.tileSize, this));
            timer = cooldown;
        }

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

            // CHECK TILE COLLiSION
            gp.cChecker.checkTile(this);
            gp.bombs.addAll(bombs);
            gp.cChecker.checkBomb(this);

            move();
        }

        NormalBomb bomb;
        for (int i = 0; i < bombs.size(); ) {

            bomb = bombs.get(i);
            bomb.update();
            // bomb.checkPlayer(this);

            if (bomb.exploded) {
                bombs.remove(i);
            } else {
                i++;
            }
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