package AI.BehaviorTree;

import java.util.List;

public class Selector extends BehaviorNode{

    List<BehaviorNode> children;
    int currentChild = 0;

    @Override
    public Status tick() {
        for (int i = currentChild; i < children.size(); i++) {
            Status status = children.get(i).tick();
            if (status == Status.SUCCESS) {
                currentChild = 0;
                return Status.SUCCESS;
            } else if (status == Status.RUNNING) {
                currentChild = i;
                return Status.RUNNING;
            }
        }
        currentChild = 0;
        return Status.FAILURE;
    }
}
