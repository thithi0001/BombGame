package bomb;

import entity.Entity;
import entity.Player;
import main.GamePanel;
import res.LoadResource;

public class TimeBomb extends Bomb {

    public TimeBomb() {
    }

    public TimeBomb(GamePanel gp, int x, int y, Entity owner, int flameLength) {

        super(gp, x, y, owner, flameLength);
        name = "time bomb";
        spriteTime = 6;// draw 1 sprite after every 6 frames

        getBombImage();
    }

    void getBombImage() {

        idle = LoadResource.timeBombIdle;
        explosion = LoadResource.explosion;
        sprites = idle;
    }

    @Override
    public void update() {

        super.update();

        if (owner instanceof Player) {
            if (((Player) owner).getKeyHandler().activateBomb && !exploding) {
                explode();
                return;
            }
        }

        if (exploding) {
            flame.update();
        }

        if (++spriteCounter > spriteTime) {

            if (++spriteNum == sprites.length) {

                if (exploding) {
                    exploded = true;
                } else {
                    spriteNum = 0;
                }
            }
            spriteCounter = 0;
        }

    }

    @Override
    void explode() {

        super.explode();
        spriteTime = 1;
        flame = new Flame(this, x, y, flameLength, explosion.length * (1 + spriteTime));
    }
}
