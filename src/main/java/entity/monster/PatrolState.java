package entity.monster;

import entity.Monster;
import entity.monster.FleeState;
import entity.monster.ChaseState;
import java.awt.Point;
import java.util.List;
import static MenuSetUp.DimensionSize.*;

/**
 * Trạng thái tuần tra của Monster
 * Monster sẽ di chuyển ngẫu nhiên trong khu vực
 */
public class PatrolState implements MonsterState {
    private static final int RANDOM_MOVE_CHANCE = 20; // 20% cơ hội di chuyển ngẫu nhiên
    private static final int RANDOM_MOVE_RANGE = 3; // Phạm vi di chuyển ngẫu nhiên

    @Override
    public void updateState(Monster monster) {
        if (monster.getCrossLoS().isVisible()) {
            if (monster.getHp() <= monster.getLowHpThreshold()) {
                monster.setState(new FleeState());
            } else {
                monster.setState(new ChaseState());
            }
        }
    }

    @Override
    public void recalculatePath(Monster monster) {
        if (monster.getCurrentPath().isEmpty() || 
            monster.getRandom().nextInt(100) < RANDOM_MOVE_CHANCE) {
            
            Point current = monster.getPosition();
            int randomX = current.x + monster.getRandom().nextInt(RANDOM_MOVE_RANGE) - 1;
            int randomY = current.y + monster.getRandom().nextInt(RANDOM_MOVE_RANGE) - 1;
            
            // Đảm bảo điểm đích nằm trong bản đồ
            randomX = Math.max(1, Math.min(randomX, maxScreenCol - 2));
            randomY = Math.max(1, Math.min(randomY, maxScreenRow - 2));
            
            Point randomPoint = new Point(randomX, randomY);
            if (!monster.getGamePanel().map.isObstacle(randomPoint.x, randomPoint.y)) {
                monster.getPathFinder().setNewGoal(randomPoint);
                monster.setCurrentPath(monster.getPathFinder().findPath());
                monster.setPathIndex(0);
            }
        }
    }

    // @Override
    // public boolean canTransition(Monster monster) {
    //     return monster.getCrossLoS().isVisible();
    // }

    @Override
    public String getName() {
        return "PATROL";
    }
} 
