package skill;

import entity.Entity;
import entity.EntityStatus;

public class Accelerate extends Skill{

    private int currentSpeedLevel;
    private final int maxSpeedLevel;
    private final int speedLevel;

    public Accelerate(Entity user, int level, double cooldownSecond, double durationSecond) {
        super(user, "Accelerate", cooldownSecond, durationSecond);
        this.speedLevel = level;
        maxSpeedLevel = user.getMaxSpeedLevel();
    }

    @Override
    void action() {
        currentSpeedLevel = user.getCurrentSpeedLevel();
        user.setMaxSpeedLevel(speedLevel);
        user.setStatus(EntityStatus.ACCELERATE);
    }

    @Override
    void reset() {
        user.setMaxSpeedLevel(maxSpeedLevel);
        user.setSpeedLevel(currentSpeedLevel);
        user.setStatus(EntityStatus.NORMAL);
    }
}
