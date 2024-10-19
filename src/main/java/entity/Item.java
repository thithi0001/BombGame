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

import static MenuSetUp.DimensionSize.tileSize;

public class Item extends Entity {

    public BufferedImage itemImg = null;

    public enum States {hidden, isPickedUp, isHit, shown}

    public States state = States.hidden;

    public Item(GamePanel gp, String name) {

        this.gp = gp;
        this.name = name;

        this.itemImg = LoadResource.itemMap.get(name);
    }

    public Item(GamePanel gp, int col, int row) {
        // default item
        this.gp = gp;
        this.x = col * tileSize;
        this.y = row * tileSize;
        this.name = "ugly key";

        solidArea = new Rectangle(x, y, tileSize, tileSize);
        getItemImage(name);
    }

    public void getItemImage(String name) {

        try {
            itemImg = ImageIO.read(new File(Main.res + "\\item\\" + name + ".png"));
            itemImg = UtilityTool.scaleImage(itemImg, tileSize, tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        gp.cChecker.checkPlayerForItem(this);
    }

    public void draw(Graphics2D g2) {

        if (state == States.shown) {
            g2.drawImage(itemImg, x, y, tileSize, tileSize, null);
        }
    }
}
