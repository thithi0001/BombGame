package skill;

import entity.Entity;

public class Harden extends Skill{

    private final int defaultInvincibleTime;

    public Harden(Entity user, String name, double cooldownSecond, double durationSecond) {
        super(user, name, cooldownSecond, durationSecond);
        defaultInvincibleTime = user.getInvincibleTime();
    }

    @Override
    void action() {
        user.setInvincibleTime(getDuration());
    }

    @Override
    public void reset() {
        super.reset();
        user.setInvincibleTime(defaultInvincibleTime);
    }
}
