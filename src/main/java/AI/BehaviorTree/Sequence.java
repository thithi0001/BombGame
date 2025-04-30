package AI.BehaviorTree;

import java.util.List;

public class Sequence extends BehaviorNode{

    List<BehaviorNode> children;
    int currentChild = 0;

    @Override
    public Status tick() {
        while (currentChild < children.size()) {
            Status status = children.get(currentChild).tick();
            if (status == Status.FAILURE) {
                currentChild = 0;
                return Status.FAILURE;
            } else if (status == Status.RUNNING) {
                return Status.RUNNING;
            } else {
                currentChild++;
            }
        }
        currentChild = 0; 
        return Status.SUCCESS;
    }
}
