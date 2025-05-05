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
import skill.Accelerate;
import skill.Harden;
import skill.Heal;
import skill.Skill;

import static MenuSetUp.DimensionSize.tileSize;
import static entity.Direction.*;

public class Player extends Entity {

    private final KeyHandler keyH;

    BufferedImage[] playerUp, playerDown, playerLeft, playerRight;

    ArrayList<Bomb> bombs = new ArrayList<>();
    public BufferedImage statusBombTypeImg = LoadResource.itemImgMap.get("plus_bomb");
    private int cooldown;

    private int maxBombs;
    private int flameLength;
    private String bombType;

    private boolean hasGlove = false;
    private boolean canKickBomb = true;
    private boolean isHoldingBomb = false;
    public int score = 0;

    public Skill skill;

    public Player() {
        keyH = new KeyHandler();
    }

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;
        this.name = "player";

        setDefaultValues();
        getPlayerImage();
//        setSkill();
    }

    void setSkill() {
//        skill = new Harden(this, 5.0, 2.0);
//        skill = new Accelerate(this, 5, 5.0, 2.0);
//        skill = new Heal(this, 5.0);
    }

    void setDefaultValues() {

//        collisionOn = false;
        setInvincibleTime(1.0);
        setMaxSpeedLevel(3);
        setSpeedLevel(1);
        setMaxHp(3);
        maxBombs = 2;
        flameLength = 1;
        bombType = "time";
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
        gp.map.bombs.addAll(bombs);
        boolean hasBombHere = gp.cChecker.hasBombHere(col(), row());
        if (keyH.enterPressed && bombs.size() < maxBombs
                && timer == 0 && !hasBombHere) {
            placeBomb();
            timer = cooldown;
        }

        // pick up and throw bomb
        if (keyH.useGlove && hasGlove) {
            if (hasBombHere && !isHoldingBomb) {
                pickUpBomb();
            } else if (isHoldingBomb) {
                throwBomb();
            }
        }

        // kick bomb
        if (keyH.kickBomb && canKickBomb) {
            kickBomb();
        }

        // skills
//        if (keyH.activateSkill && skill.canActivate()) {
//            skill.activate();
//        }
//        skill.update();

        bombs.forEach(bomb -> {
            bomb.update();
            if (bomb.exploded)
                gp.map.removeBomb(bomb);
        });
        bombs.removeIf(bomb -> bomb.exploded);

        if (isGettingHit || status == EntityStatus.HARDEN) {
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

    @Override
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
        drawEffect(g2);

        bombs.forEach(bomb -> bomb.draw(g2));
    }

    void move() {

        if (!canMove) return;
        switch (direction) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
        }
        solidArea.x = x + 12;
        solidArea.y = y + 20;
        resetCollision();
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