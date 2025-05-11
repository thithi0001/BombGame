package entity.monster;

import entity.Monster;
import entity.monster.PatrolState;
import entity.monster.FleeState;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

/**
 * Trạng thái đuổi theo của Monster
 * Monster sẽ đuổi theo người chơi khi phát hiện
 */
public class ChaseState implements MonsterState {

    @Override
    public void updateState(Monster monster) {
        if (!monster.getCrossLoS().isVisible()) {
            monster.setState(new PatrolState());
        } else if (monster.getHp() <= monster.getLowHpThreshold()) {
            monster.setState(new FleeState());
        }
    }

    @Override
    public void recalculatePath(Monster monster) {
        Point playerPos = monster.getPathFinder().getTargetPosition();
        if (!monster.getPathFinder().getGoal().equals(playerPos)) {
            monster.getPathFinder().setNewGoal(playerPos);
            monster.setCurrentPath(monster.getPathFinder().findPath());
            monster.setPathIndex(0);
        }
    }

    // @Override
    // public boolean canTransition(Monster monster) {
    //     return !monster.getCrossLoS().isVisible() || 
    //            monster.getHp() <= monster.getLowHpThreshold();
    // }

    @Override
    public String getName() {
        return "CHASE";
    }
} 