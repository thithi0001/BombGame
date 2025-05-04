package skill;

import entity.Entity;

public class WallWalking extends Skill{

    public WallWalking(Entity user, String name, double cooldownSecond, double durationSecond) {
        super(user, name, cooldownSecond, durationSecond);
    }

    @Override
    void action() {
        user.collisionOn = false;
    }

    @Override
    public void reset() {
        super.reset();
        user.collisionOn = true;
    }
}
