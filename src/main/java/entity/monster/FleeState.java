package entity.monster;

import entity.Monster;
import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;
import static MenuSetUp.DimensionSize.*;

/**
 * Trạng thái chạy trốn của Monster
 * Monster sẽ chạy trốn khi HP thấp
 */
public class FleeState implements MonsterState {
    private static final int SAFE_DISTANCE = 8; // Tăng khoảng cách an toàn từ 5 lên 8
    private static final int EMERGENCY_DISTANCE = 2; // Tăng khoảng cách khẩn cấp từ 1 lên 2
    private static final int MIN_SAFE_DISTANCE = 5; // Khoảng cách tối thiểu phải đạt được
    private static final int MAX_ATTEMPTS = 3; // Giảm từ 5 xuống 3

    @Override
    public void updateState(Monster monster) {
        if (monster.isReachedSafePoint()) {
            monster.setState(new PatrolState());
            monster.setReachedSafePoint(false);
            monster.setSafePoint(null);
        }
    }

    @Override
    public void recalculatePath(Monster monster) {
        Point currentPos = monster.getPosition();
        Point playerPos = new Point(monster.getGamePanel().player.col(), 
                                  monster.getGamePanel().player.row());

        // Kiểm tra đường đi hiện tại
        if (!monster.getCurrentPath().isEmpty() && 
            !monster.pathCrossingPlayer(monster.getCurrentPath().subList(
                monster.getPathIndex(), monster.getCurrentPath().size()))) {
            Point nextPoint = monster.getCurrentPath().get(monster.getCurrentPath().size() - 1);
            int distanceToPlayer = Math.abs(nextPoint.x - playerPos.x) + 
                                 Math.abs(nextPoint.y - playerPos.y);
            if (distanceToPlayer >= MIN_SAFE_DISTANCE) {
                return;
            }
        }

        // Trường hợp khẩn cấp
        if (Math.abs(currentPos.x - playerPos.x) <= EMERGENCY_DISTANCE && 
            Math.abs(currentPos.y - playerPos.y) <= EMERGENCY_DISTANCE) {
            Point emergencyPoint = getEmergencyEscapePoint(monster, currentPos, playerPos);
            monster.getPathFinder().setNewGoal(emergencyPoint);
            monster.setCurrentPath(monster.getPathFinder().findPath());
            monster.setPathIndex(0);
            return;
        }

        // Tạo và chọn điểm an toàn
        List<Point> potentialSafePoints = generateSafePoints(monster, currentPos, playerPos);
        Point bestSafePoint = null;
        List<Point> bestPath = null;
        int maxDistance = MIN_SAFE_DISTANCE - 1;

        for (Point safePoint : potentialSafePoints) {
            if (monster.getGamePanel().map.isObstacle(safePoint.x, safePoint.y)) {
                continue;
            }

            int distance = Math.abs(safePoint.x - playerPos.x) + 
                          Math.abs(safePoint.y - playerPos.y);
            if (distance < MIN_SAFE_DISTANCE) {
                continue;
            }

            monster.getPathFinder().setNewGoal(safePoint);
            List<Point> path = monster.getPathFinder().findPath();
            if (path.isEmpty() || monster.pathCrossingPlayer(path)) {
                continue;
            }

            if (distance > maxDistance) {
                maxDistance = distance;
                bestSafePoint = safePoint;
                bestPath = path;
            }
        }

        if (bestPath != null) {
            monster.setSafePoint(bestSafePoint);
            monster.setCurrentPath(bestPath);
            monster.setPathIndex(0);
        } else {
            Point randomEscapePoint = getRandomEscapePoint(monster, currentPos, playerPos);
            monster.getPathFinder().setNewGoal(randomEscapePoint);
            monster.setCurrentPath(monster.getPathFinder().findPath());
            monster.setPathIndex(0);
        }
    }

    private List<Point> generateSafePoints(Monster monster, Point currentPos, Point playerPos) {
        List<Point> points = new ArrayList<>();
        int dx = currentPos.x - playerPos.x;
        int dy = currentPos.y - playerPos.y;

        // Điểm chính theo hướng ngược người chơi
        int targetX = currentPos.x + (dx > 0 ? SAFE_DISTANCE : -SAFE_DISTANCE);
        int targetY = currentPos.y + (dy > 0 ? SAFE_DISTANCE : -SAFE_DISTANCE);
        targetX = Math.max(1, Math.min(targetX, maxScreenCol - 2));
        targetY = Math.max(1, Math.min(targetY, maxScreenRow - 2));
        if (Math.abs(targetX - playerPos.x) + Math.abs(targetY - playerPos.y) >= MIN_SAFE_DISTANCE) {
            points.add(new Point(targetX, targetY));
        }

        // Điểm chéo
        int diagX = currentPos.x + (dx > 0 ? SAFE_DISTANCE / 2 : -SAFE_DISTANCE / 2);
        int diagY = currentPos.y + (dy > 0 ? SAFE_DISTANCE / 2 : -SAFE_DISTANCE / 2);
        diagX = Math.max(1, Math.min(diagX, maxScreenCol - 2));
        diagY = Math.max(1, Math.min(diagY, maxScreenRow - 2));
        if (Math.abs(diagX - playerPos.x) + Math.abs(diagY - playerPos.y) >= MIN_SAFE_DISTANCE) {
            points.add(new Point(diagX, diagY));
        }

        // Điểm từ pathFinder
        Point fleeingPoint = monster.getPathFinder().fleeingPoint();
        if (Math.abs(fleeingPoint.x - playerPos.x) + Math.abs(fleeingPoint.y - playerPos.y) >= MIN_SAFE_DISTANCE) {
            points.add(fleeingPoint);
        }

        return points;
    }

    private Point getEmergencyEscapePoint(Monster monster, Point currentPos, Point playerPos) {
        int dx = currentPos.x - playerPos.x;
        int dy = currentPos.y - playerPos.y;
        List<Point> candidates = new ArrayList<>();

        // Ưu tiên các hướng xa người chơi
        for (int i = 0; i < 8; i++) {
            int testX = currentPos.x + (i % 3) - 1;
            int testY = currentPos.y + (i / 3) - 1;
            if (testX == currentPos.x && testY == currentPos.y) {
                continue;
            }
            if (testX >= 1 && testX < maxScreenCol - 1 &&
                testY >= 1 && testY < maxScreenRow - 1 &&
                !monster.getGamePanel().map.isObstacle(testX, testY) &&
                (testX != playerPos.x || testY != playerPos.y)) {
                candidates.add(new Point(testX, testY));
            }
        }

        // Chọn điểm có khoảng cách xa người chơi nhất
        Point bestPoint = null;
        int maxDistance = -1;
        for (Point candidate : candidates) {
            int distance = Math.abs(candidate.x - playerPos.x) + Math.abs(candidate.y - playerPos.y);
            if (distance > maxDistance) {
                maxDistance = distance;
                bestPoint = candidate;
            }
        }

        return bestPoint != null ? bestPoint : getRandomEscapePoint(monster, currentPos, playerPos);
    }

    private Point getRandomEscapePoint(Monster monster, Point currentPos, Point playerPos) {
        int dx = currentPos.x - playerPos.x;
        int dy = currentPos.y - playerPos.y;
        dx = dx > 0 ? SAFE_DISTANCE : -SAFE_DISTANCE;
        dy = dy > 0 ? SAFE_DISTANCE : -SAFE_DISTANCE;

        int targetX = currentPos.x + dx;
        int targetY = currentPos.y + dy;
        targetX = Math.max(1, Math.min(targetX, maxScreenCol - 2));
        targetY = Math.max(1, Math.min(targetY, maxScreenRow - 2));

        return new Point(targetX, targetY);
    }

    @Override
    public String getName() {
        return "FLEE";
    }
} 