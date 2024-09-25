package bomb;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.UtilityTool;

public class Flame {

    GamePanel gp;
    int x, y;

    BufferedImage[] flame = null;
    int length = 1;

    public Flame(GamePanel gp, int x, int y) {

        this.gp = gp;
        this.x = x;
        this.y = y;

        getFlameImage();
    }

    void getFlameImage() {

    }

    public void update() {

    }

    public void draw(Graphics2D g2) {

    }
}
