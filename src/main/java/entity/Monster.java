package entity;

import AI.LoS.CrossLoS;
import AI.pathFinding.DStartLite;
import AI.pathFinding.PathFindingAdapter;
import main.GamePanel;
import main.UtilityTool;
import res.LoadResource;

import static MenuSetUp.DimensionSize.tileSize;
import static entity.Direction.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Monster extends Entity {

    Random random;

    protected int changeDirection = 2;
    boolean moved;

    private CrossLoS crossLoS;

    private DStartLite pathFinder;
    private List<Point> currentPath;
    private int pathIndex;
    private final int recalculatePathTime = UtilityTool.convertTime((double) 1 /2);

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

        crossLoS = new CrossLoS(gp, this, gp.player, 5);
        crossLoS.update();
        pathFinder = new DStartLite(new PathFindingAdapter(gp, this, gp.player));
        currentPath = pathFinder.findPath();
        pathIndex = 0;
    }

    public List<Point> getCurrentPath() {return currentPath;}

    public int getPathIndex() {return pathIndex;}

    public void getMonsterImage() {
        sprites = LoadResource.monster;
    }

    public void setDefaultValues() {

        setInvincibleTime(1.0);
        setMaxSpeedLevel(3);
        setSpeedLevel(0);
        setMaxHp(1);
        spriteTime = 6;
        x = x * tileSize;
        y = y * tileSize;
        solidArea = new Rectangle(x + 12, y + 20, 24, 24);
    }

    public DStartLite getPathFinder() {
        return pathFinder;
    }

    public CrossLoS getCrossLoS() {
        return crossLoS;
    }

    public void update() {

        if (isGettingHit) {
            enterInvincibleTime();
        }

        // logic game
        gp.cChecker.checkTile(this);// khong can
        gp.cChecker.checkBombForEntity(this);// khong can
        gp.cChecker.checkPlayerForMonster(this);

        ArrayList<Point> removed = gp.map.getNewlyRemovedObstacles();
        ArrayList<Point> added = gp.map.getNewlyAddedObstacles();
        pathFinder.updateObstacles(removed, added);

        if (timer > 0) {
            timer--;
        } else {
            timer = recalculatePathTime;
            recalculatePath();
        }

        move2();
//        move();

        crossLoS.update();

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
            if (y == -1 && canMoveUp) direction = UP;
            if (y == 1 && canMoveDown) direction = DOWN;
        }
        if (y == 0) {
            if (x == -1 && canMoveLeft) direction = LEFT;
            if (x == 1 && canMoveRight) direction = RIGHT;
        }
    }

    public void recalculatePath() {
        // kiem tra thay doi cua goal (player)
        Point newGoal = gp.player.getPosition();
        if (!pathFinder.getGoal().equals(newGoal)) {
            pathFinder.setNewGoal(newGoal);
//            if (!crossLoS.isVisible()) return;
            currentPath = pathFinder.findPath();
            pathIndex = 0;
        }
    }

    public void move2() {

        if (pathIndex < currentPath.size()) {
            Point current = getPosition();
            Point next = currentPath.get(pathIndex);

            // neu chua den next thi tiep tuc di chuyen theo huong cu
            if (current.equals(next) &&
                    (this.x == next.x * tileSize && this.y == next.y * tileSize)) {
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

        if (!canMove) return;
        moved = false;
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

    public void move() {

        if (random.nextInt(100) < changeDirection) {
            randomMove();
        }
        moved = false;

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

        if (!moved) {
            randomMove();
        }

        resetCollision();
    }

    @Override
    public void moveUp() {
        super.moveUp();
        moved = true;
    }

    @Override
    public void moveDown() {
        super.moveDown();
        moved = true;
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        moved = true;
    }

    @Override
    public void moveRight() {
        super.moveRight();
        moved = true;
    }

    @Override
    public void draw(Graphics2D g2) {

        if (isAlive) {
            g2.drawImage(sprites[spriteNum], x, y, null);
            drawEffect(g2);
        }
    }
}


