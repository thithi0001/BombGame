package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bomb.Bomb;
import bomb.NormalBomb;
import bomb.TimeBomb;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import res.LoadResource;

import static MenuSetUp.DimensionSize.tileSize;

public class Player extends Entity {

    private final KeyHandler keyH;

    BufferedImage[] playerUp, playerDown, playerLeft, playerRight;

    ArrayList<Bomb> bombs = new ArrayList<>();
    private String bombType = "normal";
    private int maxBombs;
    private int flameLength;
    int cooldown;
    int timer = 0;
    int invincibleTime = 0;

    private boolean beingHit = false;
    private boolean hasShield = false;
    public int score = 0;

    public Player() {
        keyH = new KeyHandler();
    }

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;
        this.name = "player";

        solidArea = new Rectangle(12, 20, 24, 24);

        setDefaultValues();
        getPlayerImage();
    }

    void setDefaultValues() {

        maxBombs = 1;
        flameLength = 1;
        speed = 4;
        direction = "down";
        spriteTime = 6;
        cooldown = UtilityTool.convertTime(0.1);
        x = gp.map.checkPos.x * tileSize;
        y = gp.map.checkPos.y * tileSize;
    }

    void getPlayerImage() {

        playerUp = LoadResource.playerUp;
        playerDown = LoadResource.playerDown;
        playerLeft = LoadResource.playerLeft;
        playerRight = LoadResource.playerRight;
        sprites = playerUp;
    }

    public void update() {

        // placing bomb
        gp.bombs.addAll(bombs);
        if (keyH.enterPressed && bombs.size() < maxBombs
                && timer == 0 && gp.cChecker.canPlaceBomb(this)) {
            placingBomb();
            timer = cooldown;
        }

        bombs.forEach(Bomb::update);
        bombs.removeIf(bomb -> bomb.exploded);

        if (hasShield && beingHit) {
            enterInvincibleTime();
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

            // CHECK COLLISION
            gp.cChecker.checkTile(this);
            gp.cChecker.checkBombForMoving(this);

            move();
        }

        if (++spriteCounter > spriteTime) {

            if (keyH.movePressed) spriteNum++;
            if (spriteNum == sprites.length) {

                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        switch (direction) {
            case "up":
                sprites = playerUp;
                break;
            case "down":
                sprites = playerDown;
                break;
            case "left":
                sprites = playerLeft;
                break;
            case "right":
                sprites = playerRight;
                break;
        }

        g2.drawImage(sprites[spriteNum], x, y, null);
        if (hasShield) {
            g2.drawImage(LoadResource.itemImgMap.get("protection_effect"), x, y, null);
        }

        bombs.forEach(bomb -> bomb.draw(g2));
    }

    void move() {

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

    @Override
    public void beingHit() {

        super.beingHit();
        beingHit = true;
        if (hasShield) {
            System.out.println("Hit shield!");
            return;
        }
        isAlive = false;
    }

    void enterInvincibleTime() {
        invincibleTime--;
        if (invincibleTime == 0) {
            hasShield = false;
            beingHit = false;
        }
    }

    public KeyHandler getKeyHandler() {
        return keyH;
    }

    public void setHasShield(boolean bool) {
        hasShield = bool;
        invincibleTime = UtilityTool.convertTime(1.0);
    }

    public void setBombType(String newType) {
        bombType = newType;
    }

    public void addMoreBombs(int n) {
        maxBombs += n;
    }

    public void addMoreFlame(int n) {
        flameLength += n;
    }

    public void addScore(int point) {
        score += point;
    }

    public void placingBomb() {
        switch (bombType) {
            case "time":
                bombs.add(new TimeBomb(gp, col() * tileSize, row() * tileSize, this, flameLength));
                break;
            case "normal":
                bombs.add(new NormalBomb(gp, col() * tileSize, row() * tileSize, this, flameLength));
                break;
        }
    }


}