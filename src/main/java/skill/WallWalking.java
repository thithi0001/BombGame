package skill;

import entity.Entity;

public class WallWalking extends Skill{
    // ky nang bi loi, chua su dung duoc
    public WallWalking(Entity user, double cooldownSecond, double durationSecond) {
        super(user, "Wall Walking", cooldownSecond, durationSecond);
    }

    @Override
    void action() {
        user.collisionOn = false;
    }

    @Override
    void reset() {
        user.collisionOn = true;
    }
}
