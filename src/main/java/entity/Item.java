package entity;

import main.GamePanel;
import main.Main;
import main.UtilityTool;
import res.LoadResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Item extends Entity {

    public BufferedImage itemImg = null;
    public boolean hidden = true;
    public boolean isPickedUp = false;

    public Item(GamePanel gp, String name) {

        this.gp = gp;
        this.name = name;

        this.itemImg = LoadResource.itemMap.get(name);
    }

    public Item(GamePanel gp, int col, int row) {
        // default item
        this.gp = gp;
        this.x = col * gp.tileSize;
        this.y = row * gp.tileSize;
        this.name = "ugly key";

        solidArea = new Rectangle(x, y, gp.tileSize, gp.tileSize);
        getItemImage(name);
    }

    public void getItemImage(String name) {

        try {
            itemImg = ImageIO.read(new File(Main.res + "\\item\\" + name + ".png"));
            itemImg = UtilityTool.scaleImage(itemImg, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        gp.cChecker.checkPlayerForItem(this);
    }

    public void draw(Graphics2D g2) {

        if (!hidden && !isPickedUp && !isHit) {
            g2.drawImage(itemImg, x, y, gp.tileSize, gp.tileSize, null);
        }
    }
}
