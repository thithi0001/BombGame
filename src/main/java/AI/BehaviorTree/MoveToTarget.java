package AI.BehaviorTree;

import AI.pathFinding.DStartLite;
import entity.Entity;

import java.awt.Point;
import java.util.List;

public class MoveToTarget extends BehaviorNode{

    Entity self;
    Entity target;
    DStartLite pathFinder;
    List<Point> path;

    @Override
    Status tick() {
        return null;
    }
}
