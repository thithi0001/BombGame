package skill;

import entity.Entity;
import entity.EntityStatus;

public class Heal extends Skill{

    public Heal(Entity user, double cooldownSecond) {
        super(user, "Heal", cooldownSecond, 1.0);
    }

    @Override
    void action() {
        user.increaseHp();
        user.setStatus(EntityStatus.HEAL);
    }

    @Override
    void reset() {
        user.setStatus(EntityStatus.NORMAL);
    }
}
