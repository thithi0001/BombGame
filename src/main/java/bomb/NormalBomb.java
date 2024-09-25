package bomb;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Player;
import main.GamePanel;
import main.Main;
import main.UtilityTool;

public class NormalBomb extends Bomb {

    public NormalBomb(GamePanel gp, int x, int y) {

        this.gp = gp;
        name = "normal bomb";
        this.x = x;
        this.y = y;
        spriteTime = 6;// draw 1 sprite after every 6 frames

        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);

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

        // countDown--;
        if (--countDown == 0) {

            exploding = true;
            sprites = explosion;
            spriteCounter = 0;
            spriteNum = 0;
            spriteTime = 1;

            return;
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

    public void checkPlayer(Player player) {

        if (letPlayerPassThrough) {

            if (!player.solidArea.intersects(solidArea)) {

                letPlayerPassThrough = false;
            }
        }
    }

    public void draw(Graphics2D g2) {

        g2.drawImage(sprites[spriteNum], x, y, null);
    }
}