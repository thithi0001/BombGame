package AI.BehaviorTree;

public abstract class BehaviorNode {

    enum Status {SUCCESS, FAILURE, RUNNING}
    abstract Status tick();
}
