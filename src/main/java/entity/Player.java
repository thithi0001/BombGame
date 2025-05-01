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
import static entity.Direction.*;

public class Player extends Entity {

    private final KeyHandler keyH;

    BufferedImage[] playerUp, playerDown, playerLeft, playerRight;

    ArrayList<Bomb> bombs = new ArrayList<>();
    public BufferedImage statusBombTypeImg = LoadResource.itemImgMap.get("plus_bomb");
    int cooldown;
    int timer = 0;
    int invincibleTime = 0;

    private int maxBombs;
    private int flameLength;
    private String bombType;
    private boolean beingHit = false;
    private boolean hasShield = false;
    private boolean hasGlove = false;
    private boolean canKickBomb = true;
    private boolean holdingBomb = false;
    public int score = 0;

    public Player() {
        keyH = new KeyHandler();
    }

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;
        this.name = "player";

        setDefaultValues();
        getPlayerImage();
    }

    void setDefaultValues() {

        maxBombs = 1;
        flameLength = 1;
        bombType = "normal";
        setSpeedLevel(1);
        direction = DOWN;
        spriteTime = 6;
        cooldown = UtilityTool.convertTime(0.1);
        x = gp.map.checkPos.x * tileSize;
        y = gp.map.checkPos.y * tileSize;
        solidArea = new Rectangle(x + 12, y + 20, 24, 24);
    }

    void getPlayerImage() {

        playerUp = LoadResource.charactersSprites[LoadResource.characterIndex].up;
        playerDown = LoadResource.charactersSprites[LoadResource.characterIndex].down;
        playerLeft = LoadResource.charactersSprites[LoadResource.characterIndex].left;
        playerRight = LoadResource.charactersSprites[LoadResource.characterIndex].right;
        sprites = playerDown;
    }

    public void setDirection() {
        if (gp.keyH.upPressed) {
            direction = UP;
        }
        if (gp.keyH.downPressed) {
            direction = DOWN;
        }
        if (gp.keyH.leftPressed) {
            direction = LEFT;
        }
        if (gp.keyH.rightPressed) {
            direction = RIGHT;
        }
    }

    public void update() {

        // place bomb
        gp.bombs.addAll(bombs);
        boolean hasBombHere = gp.cChecker.hasBombHere(col(), row());
        if (keyH.enterPressed && bombs.size() < maxBombs
                && timer == 0 && !hasBombHere) {
            placeBomb();
            timer = cooldown;
        }

        // pick up and throw bomb
        if (keyH.useGlove && hasGlove) {
            if (hasBombHere && !holdingBomb) {
                pickUpBomb();
            } else if (holdingBomb) {
                throwBomb();
            }
        }

        // kick bomb
        if (keyH.kickBomb && canKickBomb) {
            kickBomb();
        }

        bombs.forEach(bomb -> {
            bomb.update();
            if (bomb.exploded)
                gp.map.removeBomb(bomb);
        });
        bombs.removeIf(bomb -> bomb.exploded);

        if (hasShield && beingHit) {
            enterInvincibleTime();
        }

        if (timer > 0) {
            timer--;
        }

        if (keyH.movePressed) {

            setDirection();

            // CHECK COLLISION
            gp.cChecker.checkTile(this);
            gp.cChecker.checkBombForEntity(this);

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
            case UP:
                sprites = playerUp;
                break;
            case DOWN:
                sprites = playerDown;
                break;
            case LEFT:
                sprites = playerLeft;
                break;
            case RIGHT:
                sprites = playerRight;
                break;
        }

        g2.drawImage(sprites[spriteNum], x, y, null);
        if (hasShield) {
            g2.drawImage(LoadResource.itemImgMap.get("protection_effect_2"), x, y, null);
        }

        bombs.forEach(bomb -> bomb.draw(g2));
    }

    void move() {

        switch (direction) {
            case UP:
                if (canMoveUp) moveUp();
                break;
            case DOWN:
                if (canMoveDown) moveDown();
                break;
            case LEFT:
                if (canMoveLeft) moveLeft();
                break;
            case RIGHT:
                if (canMoveRight) moveRight();
                break;
        }
        solidArea.x = x + 12;
        solidArea.y = y + 20;
        resetCollision();
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

    public int getFlameLength() {
        return flameLength;
    }

    public int getMaxBombs() {
        return maxBombs;
    }

    public void setHasShield(boolean bool) {
        hasShield = bool;
        invincibleTime = UtilityTool.convertTime(1.0);
    }

    public void setHasGlove() {
        hasGlove = true;
    }

    public void activeKickBomb() {
        canKickBomb = true;
    }

    public void setBombType(String newType) {
        bombType = newType;
        statusBombTypeImg = LoadResource.timeBombIdle[0];
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

    void placeBomb() {
        Bomb bomb = switch (bombType) {
            case "time" -> new TimeBomb(gp, col() * tileSize, row() * tileSize, this, flameLength);
            case "normal" -> new NormalBomb(gp, col() * tileSize, row() * tileSize, this, flameLength);
            default -> new Bomb();
        };
        bombs.add(bomb);
        gp.map.addBomb(bomb);
    }

    void pickUpBomb() {

    }

    void throwBomb() {

    }

    void kickBomb() {
        // find
        int col = col(), row = row();
        Bomb bomb = null;
        switch (direction) {
            case UP:
                row -= 1;
                break;
            case DOWN:
                row += 1;
                break;
            case LEFT:
                col -= 1;
                break;
            case RIGHT:
                col += 1;
                break;
        }
        for (int i = 0; i < gp.map.bombs.size(); i++) {
            Bomb b = gp.map.bombs.get(i);
            if (col == b.col() && row == b.row()) {
                bomb = b;
                break;
            }
        }

        // kick
        if (bomb == null) return;
        bomb.moving = true;
        bomb.direction = direction;
        bomb.move();
    }
}