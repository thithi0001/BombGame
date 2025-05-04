package skill;

import entity.Entity;

public class Heal extends Skill{

    public Heal(Entity user, String name, double cooldownSecond) {
        super(user, name, cooldownSecond, 0);
    }

    @Override
    void action() {
        user.increaseHp();
    }
}
