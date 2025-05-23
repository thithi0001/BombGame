package entity;

import MenuSetUp.Sound;
import main.GamePanel;
import res.LoadResource;

import java.awt.*;
import java.awt.image.BufferedImage;

import static MenuSetUp.DimensionSize.tileSize;

public class Item extends Entity {

    public int score = 0;
    public BufferedImage itemImg = null;

    public enum States {hidden, isPickedUp, shown, isHit}

    public States state = States.hidden;
    public boolean isCheckPoint = false;

    Sound pickedUpSound = LoadResource.receiveItemSound;

    public Item(GamePanel gp, String name) {

        this.gp = gp;
        this.name = name;
        getItemImage();
    }

    public Item(GamePanel gp, int col, int row) {
        // default item
        this.gp = gp;
        this.x = col * tileSize;
        this.y = row * tileSize;
        this.name = "ugly_key";

        solidArea = new Rectangle(x, y, tileSize, tileSize);
        getItemImage();
    }

    void getItemImage() {

        itemImg = LoadResource.itemImgMap.get(name);
        score = LoadResource.itemScoreMap.get(name);
    }

    public void update() {

        gp.cChecker.checkPlayerForItem(this);
    }

    @Override
    public void draw(Graphics2D g2) {

        if (state == States.shown) {
            g2.drawImage(itemImg, x, y, tileSize, tileSize, null);
        }
    }

    @Override
    public void getHit() {
        if (isCheckPoint) return;
        super.getHit();
        state = States.isHit;
    }

    public void beingPickedUp() {
        if (isCheckPoint) return;
        pickedUpSound.play();
        state = States.isPickedUp;
        System.out.println("+1 " + name);
    }
}
