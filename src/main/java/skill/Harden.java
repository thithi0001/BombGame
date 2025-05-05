package skill;

import entity.Entity;
import entity.EntityStatus;

public class Harden extends Skill{

    private final int defaultInvincibleTime;

    public Harden(Entity user, double cooldownSecond, double durationSecond) {
        super(user, "Harden", cooldownSecond, durationSecond);
        defaultInvincibleTime = user.getInvincibleTime();
    }

    @Override
    void action() {
        user.setVulnerable(false);
        user.setInvincibleTime(getDuration());
        user.enterInvincibleTime();
        user.setStatus(EntityStatus.HARDEN);
        user.canMove = false;
    }

    @Override
    void reset() {
        user.setInvincibleTime(defaultInvincibleTime);
        user.setStatus(EntityStatus.NORMAL);
        user.canMove = true;
    }
}
