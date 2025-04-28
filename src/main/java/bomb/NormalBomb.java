package bomb;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;
import res.LoadResource;

public class NormalBomb extends Bomb {

    public NormalBomb() {
    }

    public NormalBomb(GamePanel gp, int x, int y, Entity owner, int flameLength) {

        super(gp, x, y, owner, flameLength);
        name = "normal bomb";
        spriteTime = 6;// draw 1 sprite after every 6 frames

        countDown = UtilityTool.convertTime(2.0);

        getBombImage();
    }

    void getBombImage() {

        idle = LoadResource.normalBombIdle;
        explosion = LoadResource.explosion;
        sprites = idle;
    }

    @Override
    public void update() {

        super.update();

        if (--countDown == 0) {
            explode();
            return;
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