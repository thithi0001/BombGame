package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

import static MenuSetUp.DimensionSize.*;
import static entity.Direction.*;

public class MonsterBoss extends Monster {
    int width, height;
    private int hp;

    public MonsterBoss(GamePanel gp, int x, int y) {

        super(gp, x, y);
        this.hp = 3;
        this.width = 2 * tileSize;
        this.height = 2 * tileSize;
        this.solidArea = new Rectangle(0, 0, width, height);
        this.name = "monsterBoss";

        setDefaultValues();
    }

    @Override
    public void setDefaultValues() {

        setSpeedLevel(0);
        x = (screenWidth - width) / 2;
        y = (screenHeight - height) / 2;
    }

    @Override
    public void update() {

        if (random.nextInt(100) < changeDirection) {
            randomMove();
        }

        gp.cChecker.checkBombForEntity(this);
        gp.cChecker.checkPlayerForMonster(this);

        move();
    }

    @Override
    public void move() {

        moved = false;

        switch (direction) {
            case UP:
                if (y - speed >= 0 && canMoveUp) {
                    moveUp();
                    moved = true;
                }
                break;

            case DOWN:
                if (y + speed + height <= screenHeight && canMoveDown) {
                    moveDown();
                    moved = true;
                }
                break;

            case LEFT:
                if (x - speed >= 0 && canMoveLeft) {
                    moveLeft();
                    moved = true;
                }
                break;

            case RIGHT:
                if (x + speed + width <= screenWidth && canMoveRight) {
                    moveRight();
                    moved = true;
                }
                break;
        }

        if (!moved) {
            randomMove();
        }

        resetCollision();
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(x, y, width, height);
        // Vẽ thanh HP phía trên đầu quái
        g2.setColor(Color.RED);
        g2.fillRect(x, y - 10, width * (hp / 3), 5);
    }

    @Override
    public void getHit() {

        hp--;
        System.out.println("hit");
        if (hp <= 0) {
            isAlive = false;
        } else {
            increaseSpeed();
        }
    }
}
