package entity;

import AI.InputContext;
import AI.pathFinding.DStartLite;
import main.GamePanel;
import res.LoadResource;

import static MenuSetUp.DimensionSize.tileSize;
import static entity.Direction.*;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Monster extends Entity {

    Random random;

    protected int changeDirection = 2;
    boolean moved;
    InputContext inputContext;
    DStartLite pathFinder;
    private List<Point> currentPath;
    private int pathIndex;

    public Monster(GamePanel gp, int x, int y) {
        this.gp = gp;
        this.name = "monster";
        this.x = x;
        this.y = y;
        random = new Random();

        setDefaultValues();
        getMonsterImage();
        System.out.println(getPosition());
    }

    public void initAI() {

        inputContext = new InputContext(gp, this);
        pathFinder = new DStartLite(inputContext);
        currentPath = pathFinder.findPath();
        pathIndex = 0;
    }

    public List<Point> getCurrentPath() {return currentPath;}

    public int getPathIndex() {return pathIndex;}

    public void getMonsterImage() {
        sprites = LoadResource.monster;
    }

    public void setDefaultValues() {

        setSpeedLevel(0);
        direction = DOWN;
        spriteTime = 6;
        x = x * tileSize;
        y = y * tileSize;
        solidArea = new Rectangle(x + 12, y + 20, 24, 24);
    }

    public void update() {

//        gp.cChecker.checkTile(this);
//        gp.cChecker.checkBombForEntity(this);
//        gp.cChecker.checkPlayerForMonster(this);

        move2();
//        move();

        if (++spriteCounter > spriteTime) {

            if (moved) spriteNum++;
            if (spriteNum == sprites.length) {

                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }

    void randomMove() {
        // tuong duong voi setDirection
        int randomDirection = random.nextInt(4);
        switch (randomDirection) {
            case 0:
                if (canMoveUp) {
                    direction = UP;
                }
                break;
            case 1:
                if (canMoveDown) {
                    direction = DOWN;
                }
                break;
            case 2:
                if (canMoveLeft) {
                    direction = LEFT;
                }
                break;
            case 3:
                if (canMoveRight) {
                    direction = RIGHT;
                }
                break;
        }
    }

    public void setDirection(Point current, Point next) {

        int x = next.x - current.x;
        int y = next.y - current.y;

        if (x == 0) {
            if (y == -1) direction = UP;
            if (y == 1) direction = DOWN;
        }
        if (y == 0) {
            if (x == -1) direction = LEFT;
            if (x == 1) direction = RIGHT;
        }
    }

    public void move2() {

        Point newGoal = inputContext.getTargetPosition();
        if (!pathFinder.getGoal().equals(newGoal)) {
            pathFinder.setNewGoal(newGoal);
            currentPath = pathFinder.findPath();
            pathIndex = 0;
        }

        Point current = getPosition();
        if (pathIndex < currentPath.size()) {
            Point next = currentPath.get(pathIndex);

            // neu chua den next thi tiep tuc di chuyen theo huong cu
            if (current.equals(next)) {
                if (++pathIndex >= currentPath.size()) {
                    // toi day co nghia la da toi goal
                    // xu ly chuyen sang trang thai truy duoi hoac trang thai nhan roi
                    --pathIndex; // xu ly cai cho nay
                    return;
                }

                next = currentPath.get(pathIndex);
                pathFinder.moveAndRescan(next);

                setDirection(current, next);
            }
        }

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
    }

    public void move() {

        if (random.nextInt(100) < changeDirection) {
            randomMove();
        }
        moved = false;

        switch (direction) {
            case UP:
                if (canMoveUp) {
                    moveUp();
                    moved = true;
                }
                break;
            case DOWN:
                if (canMoveDown) {
                    moveDown();
                    moved = true;
                }
                break;
            case LEFT:
                if (canMoveLeft) {
                    moveLeft();
                    moved = true;
                }
                break;
            case RIGHT:
                if (canMoveRight) {
                    moveRight();
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


