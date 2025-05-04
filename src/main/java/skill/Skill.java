package skill;

import entity.Entity;
import main.UtilityTool;

abstract class Skill {

    Entity user;
    private String name;
    private int cooldown;
    private int duration;
    private boolean isActivating;
    private boolean canActivate;
    private int timer = 0;

    public Skill(Entity user, String name, double cooldownSecond, double durationSecond) {
        this.user = user;
        this.name = name;
        setCooldown(cooldownSecond);
        setDuration(durationSecond);
        isActivating = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(double seconds) {
        cooldown = UtilityTool.convertTime(seconds);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(double seconds) {
        duration = UtilityTool.convertTime(seconds);
    }

    public boolean isActivating() {
        return isActivating;
    }

    public boolean canActivate() {
        return canActivate;
    }

    public void update() {
        if (timer == 0) {
            if (isActivating) {
                deactivate();
            } else {
                reset();
            }
        }

        if (timer > 0) {
            timer--;
        }
    }

    public void activate() {
        timer = duration;
        isActivating = true;
        canActivate =false;
        action();
    }

    public void deactivate() {
        timer = cooldown;
        isActivating = false;
    }

    public void reset() {
        canActivate = true;
    }

    abstract void action();
}
