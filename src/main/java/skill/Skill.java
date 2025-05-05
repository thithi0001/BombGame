package skill;

import entity.Entity;
import main.UtilityTool;

public abstract class Skill {
    // tat ca ky nang chua duoc kiem tra su dung
    Entity user;
    private String name;
    private int cooldown;
    private int duration;
    private boolean isActivating;
    private boolean isCoolingDown;
    private boolean canActivate;
    private int timer = 0;

    public Skill(Entity user, String name, double cooldownSecond, double durationSecond) {
        this.user = user;
        this.name = name;
        setCooldown(cooldownSecond);
        setDuration(durationSecond);
        isActivating = false;
        isCoolingDown = false;
        canActivate = true;
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

    public boolean isCoolingDown() {return isCoolingDown;}

    public boolean canActivate() {
        return canActivate;
    }

    public void update() {
        if (timer == 0 && isActivating) {
            deactivate();
        }

        if (timer == 0 && isCoolingDown) {
            doneCoolDown();
        }

        if (timer == 0) return;

        timer--;
    }

    public void activate() {
        timer = duration;
        isActivating = true;
        canActivate = false;
        action();
    }

    public void deactivate() {
        timer = cooldown;
        isActivating = false;
        isCoolingDown = true;
        reset();
    }

    public void doneCoolDown() {
        isCoolingDown = false;
        canActivate = true;
    }

    abstract void action();
    abstract void reset();
}
