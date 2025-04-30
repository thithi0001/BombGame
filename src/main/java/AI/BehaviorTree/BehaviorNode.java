package AI.BehaviorTree;

import AI.InputContext;

abstract class BehaviorNode {

    enum Status {SUCCESS, FAILURE, RUNNING}
    abstract Status tick();
    InputContext inputContext;
}
