package bomb;

import entity.Entity;
import entity.Player;
import main.GamePanel;
import res.LoadResource;

import java.awt.*;

import static MenuSetUp.DimensionSize.tileSize;

public class TimeBomb extends Bomb {

    public TimeBomb() {
    }

    public TimeBomb(GamePanel gp, int x, int y, Entity owner, int flameLength) {

        super(gp, x, y, owner, flameLength);
        name = "time bomb";
        spriteTime = 6;// draw 1 sprite after every 6 frames

        solidArea = new Rectangle(x, y, tileSize, tileSize);

        getBombImage();
    }

    void getBombImage() {

        idle = LoadResource.timeBombIdle;
        explosion = LoadResource.explosion;
        sprites = idle;
    }

    @Override
    public void update() {

        checkPlayer();

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
