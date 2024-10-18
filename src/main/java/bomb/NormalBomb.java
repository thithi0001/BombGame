package bomb;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;
import main.Main;
import main.UtilityTool;

import static MenuSetUp.DimensionSize.tileSize;

public class NormalBomb extends Bomb {

    public NormalBomb(GamePanel gp, int x, int y, Entity owner) {

        this.gp = gp;
        name = "normal bomb";
        this.x = x;
        this.y = y;
        this.owner = owner;
        spriteTime = 6;// draw 1 sprite after every 6 frames

        solidArea = new Rectangle(x, y, tileSize, tileSize);

        countdownInSecond = 3;
        countdownInFrame = countdownInSecond * gp.FPS;
        countDown = (int) countdownInFrame;

        getBombImage();
    }

    void getBombImage() {

        idle = UtilityTool.loadSpriteSheet(gp, Main.res + "\\bomb\\normal\\bomb_64_x_16_.png");
        explosion = UtilityTool.loadSpriteSheet(gp, Main.res + "\\bomb\\normal\\explosion_128_x_16_.png");
        sprites = idle;
    }

    public void update() {

        checkPlayer();
        // countDown--;
        if (--countDown == 0) {

            exploding = true;
            sprites = explosion;
            spriteCounter = 0;
            spriteNum = 0;
            spriteTime = 1;
            flame = new Flame(this, x, y, 3, explosion.length * (1 + spriteTime));
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

    public void checkPlayer() {

        if (letPlayerPassThrough) {

            Rectangle pSolidArea = new Rectangle(owner.solidArea);
            pSolidArea.x += owner.x;
            pSolidArea.y += owner.y;

            if (!pSolidArea.intersects(solidArea)) {
                letPlayerPassThrough = false;
            }
        }
    }

    public void draw(Graphics2D g2) {

        g2.drawImage(sprites[spriteNum], x, y, null);
        if (exploding) {
            flame.draw(g2);
        }
    }
}