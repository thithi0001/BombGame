package skill;

import entity.Entity;

public class Accelerate extends Skill{

    private final int currentSpeedLevel;
    private final int maxSpeedLevel;
    private final int speedLevel;

    public Accelerate(int level, Entity user, String name, double cooldownSecond, double durationSecond) {
        super(user, name, cooldownSecond, durationSecond);
        this.speedLevel = level;
        currentSpeedLevel = user.getCurrentSpeedLevel();
        maxSpeedLevel = user.getMaxSpeedLevel();
    }

    @Override
    void action() {
        user.setMaxSpeedLevel(speedLevel);
    }

    @Override
    public void reset() {
        super.reset();
        user.setMaxSpeedLevel(maxSpeedLevel);
        user.setSpeedLevel(currentSpeedLevel);
    }
}
