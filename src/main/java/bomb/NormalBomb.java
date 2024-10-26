package bomb;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;
import res.LoadResource;

import static MenuSetUp.DimensionSize.tileSize;

public class NormalBomb extends Bomb {

    public NormalBomb() {
    }

    public NormalBomb(GamePanel gp, int x, int y, Entity owner, int flameLength) {

        super(gp, x, y, owner, flameLength);
        name = "normal bomb";
        spriteTime = 6;// draw 1 sprite after every 6 frames

        solidArea = new Rectangle(x, y, tileSize, tileSize);
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

        checkPlayer();

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
    public void draw(Graphics2D g2) {

        g2.drawImage(sprites[spriteNum], x, y, null);
        if (exploding) {
            flame.draw(g2);
        }
    }

    @Override
    void explode() {

        super.explode();
        spriteTime = 1;
        flame = new Flame(this, x, y, flameLength, explosion.length * (1 + spriteTime));
    }
}