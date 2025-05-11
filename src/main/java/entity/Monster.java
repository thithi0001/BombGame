package entity;

import AI.LoS.CrossLoS;
import AI.pathFinding.DStartLite;
import entity.monster.*;
import main.GamePanel;
import main.UtilityTool;
import res.LoadResource;

import static MenuSetUp.DimensionSize.tileSize;
import static MenuSetUp.DimensionSize.maxScreenCol;
import static MenuSetUp.DimensionSize.maxScreenRow;
import static entity.Direction.*;
//import static entity.EntityState.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Monster extends Entity {
    private static final Logger LOGGER = Logger.getLogger(Monster.class.getName());
    private static final Random SHARED_RANDOM = new Random();

    private Random random;

    protected int changeDirection = 2;
    boolean moved;
    
    // State Pattern
    private MonsterState currentState;
    private boolean reachedSafePoint = false;
    private Point safePoint = null;
    private final int lowHpThreshold = 1; // Ngưỡng HP thấp để chuyển sang FLEE

    private CrossLoS crossLoS;

    private DStartLite pathFinder;
    private List<Point> currentPath;
    private int pathIndex;
    private final int recalculatePathTime = UtilityTool.convertTime(0.5f);

    public Monster(GamePanel gp, int x, int y) {
        this.gp = gp;
        this.name = "monster";
        this.x = x;
        this.y = y;
        this.random = SHARED_RANDOM; // Sử dụng instance Random chung
        this.currentState = new PatrolState(); // Khởi tạo với trạng thái tuần tra

        setDefaultValues();
        getMonsterImage();
        LOGGER.info("Monster initialized at position: " + getPosition());
    }

    // Getters và Setters cho State Pattern
    public MonsterState getCurrentState() {
        return currentState;
    }

    public void setState(MonsterState newState) {
        if (newState == null) {
            LOGGER.warning("Attempted to set null state for monster: " + name);
            return;
        }
        
        String previousState = currentState != null ? currentState.getName() : "null";
        String newStateName = newState.getName();
        
        currentState = newState;
        LOGGER.info(String.format("%s chuyển từ %s sang %s", name, previousState, newStateName));
        timer = 0; // Recalculate path ngay lập tức
    }

    public boolean isReachedSafePoint() {
        return reachedSafePoint;
    }

    public void setReachedSafePoint(boolean reachedSafePoint) {
        this.reachedSafePoint = reachedSafePoint;
    }

    public Point getSafePoint() {
        return safePoint;
    }

    public void setSafePoint(Point safePoint) {
        this.safePoint = safePoint;
    }

    public int getLowHpThreshold() {
        return lowHpThreshold;
    }

    public GamePanel getGamePanel() {
        return gp;
    }

    public Random getRandom() {
        return random;
    }

    public List<Point> getCurrentPath() {
        return currentPath != null ? currentPath : new ArrayList<>();
    }

    public void setCurrentPath(List<Point> path) {
        this.currentPath = path;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(int index) {
        this.pathIndex = index;
    }

    public void initAI() {

        crossLoS = new CrossLoS(gp, this, gp.player, 5);
        crossLoS.update();
        pathFinder = new DStartLite(gp, this, gp.player);
        currentPath = pathFinder.findPath();
        pathIndex = 0;
    }

    public void getMonsterImage() {
        sprites = LoadResource.monster;
    }

    public void setDefaultValues() {

        setInvincibleTime(1.0);
        setMaxSpeedLevel(3);
        setSpeedLevel(0);
        setMaxHp(2);
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
        try {
            if (isGettingHit) {
                enterInvincibleTime();
            }

            // Logic game
            gp.cChecker.checkTile(this);
            gp.cChecker.checkBombForEntity(this);
            gp.cChecker.checkPlayerForMonster(this);

            ArrayList<Point> removed = gp.map.getNewlyRemovedObstacles();
            ArrayList<Point> added = gp.map.getNewlyAddedObstacles();
            pathFinder.updateObstacles(removed, added);

            // Cập nhật trạng thái dựa trên các điều kiện
            currentState.updateState(this);

            if (timer > 0) {
                timer--;
            } else {
                timer = recalculatePathTime;
                currentState.recalculatePath(this);
            }

            // Đảm bảo quái luôn có đường đi khi ở trạng thái FLEE
            if (currentState instanceof FleeState && 
                (currentPath == null || currentPath.isEmpty())) {
                currentState.recalculatePath(this);
            }

            move2();

            crossLoS.update();

            if (++spriteCounter > spriteTime) {
                if (moved) spriteNum++;
                if (spriteNum == sprites.length) {
                    spriteNum = 0;
                }
                spriteCounter = 0;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating monster: " + e.getMessage(), e);
        }
    }

    public void move2() {
        try {
            if (currentPath == null || currentPath.isEmpty()) {
                if (currentState instanceof FleeState) {
                    currentState.recalculatePath(this);
                }
                return;
            }

            if (pathIndex >= currentPath.size()) {
                pathIndex = currentPath.size() - 1;
                return;
            }

            Point current = getPosition();
            Point next = currentPath.get(pathIndex);

            // Kiểm tra đường đi trong trạng thái FLEE
            if (currentState instanceof FleeState && 
                pathCrossingPlayer(currentPath.subList(pathIndex, currentPath.size()))) {
                currentState.recalculatePath(this);
                return;
            }

            // Kiểm tra xem đã đến vị trí tiếp theo chưa
            if (current.equals(next) && x == next.x * tileSize && y == next.y * tileSize) {
                if (currentState instanceof FleeState && pathIndex == currentPath.size() - 1) {
                    reachedSafePoint = true;
                    LOGGER.info(name + " đã đến điểm an toàn!");
                }
                pathIndex++;
                if (pathIndex >= currentPath.size()) {
                    pathIndex--;
                    return;
                }
                next = currentPath.get(pathIndex);
                pathFinder.moveAndRescan(next);
            }

            setDirection(current, next);
            performMove();
            updateSolidArea();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in move2: " + e.getMessage(), e);
        }
    }

    private void performMove() {
        if (!canMove) return;
        moved = false;
        switch (direction) {
            case UP -> moveUp();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
        }
    }

    private void updateSolidArea() {
        solidArea.x = x + 12;
        solidArea.y = y + 20;
        resetCollision();
    }

    // Phương thức kiểm tra đường đi có đi qua người chơi không
    public boolean pathCrossingPlayer(List<Point> path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        Point playerPos = new Point(gp.player.col(), gp.player.row());
        Set<Point> blockedArea = new HashSet<>();

        // Tạo tập hợp các ô bị chặn (1 ô xung quanh người chơi)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                blockedArea.add(new Point(playerPos.x + dx, playerPos.y + dy));
            }
        }

        // Chỉ kiểm tra các điểm trong đường đi
        for (int i = Math.max(1, pathIndex); i < path.size(); i++) {
            if (blockedArea.contains(path.get(i))) {
                return true;
            }
        }
        return false;
    }

    // void randomMove() {
    //     // tuong duong voi setDirection
    //     int randomDirection = random.nextInt(4);
    //     switch (randomDirection) {
    //         case 0:
    //             if (canMoveUp) {
    //                 direction = UP;
    //             }
    //             break;
    //         case 1:
    //             if (canMoveDown) {
    //                 direction = DOWN;
    //             }
    //             break;
    //         case 2:
    //             if (canMoveLeft) {
    //                 direction = LEFT;
    //             }
    //             break;
    //         case 3:
    //             if (canMoveRight) {
    //                 direction = RIGHT;
    //             }
    //             break;
    //     }
    // }

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

    // public void move() {

    //     if (random.nextInt(100) < changeDirection) {
    //         randomMove();
    //     }
    //     moved = false;

    //     switch (direction) {
    //         case UP:
    //             moveUp();
    //             break;
    //         case DOWN:
    //             moveDown();
    //             break;
    //         case LEFT:
    //             moveLeft();
    //             break;
    //         case RIGHT:
    //             moveRight();
    //             break;
    //     }
    //     solidArea.x = x + 12;
    //     solidArea.y = y + 20;

    //     if (!moved) {
    //         randomMove();
    //     }

    //     resetCollision();
    // }

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