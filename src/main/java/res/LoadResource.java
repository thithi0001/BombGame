package res;

import main.Main;
import main.UtilityTool;
import tile.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LoadResource {
    public static HashMap<String, BufferedImage> itemMap = new HashMap<>();
    public static HashMap<String, BufferedImage> monsterMap = new HashMap<>();

    // tiles
    public static Tile[] tiles = new Tile[10];

    // player
    public static BufferedImage[] playerUp, playerDown, playerLeft, playerRight;

    // flame
    public static BufferedImage[] headUp, headDown, headLeft, headRight;
    public static BufferedImage[] verticalBody, horizontalBody;

    // bomb
    public static BufferedImage[] idle, explosion;

    static {

        loadTile();
        loadPlayer();
        loadBomb();
        loadFlame();
        loadItem();
    }

    public static void loadTile() {

    }

    public static void loadItem() {
        itemMap.put("ugly_key", itemImage("ugly_key"));
        itemMap.put("plus_1", itemImage("plus_1"));
    }

    public static BufferedImage itemImage(String name) {

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(Main.res + "\\item\\" + name + ".png"));
            img = UtilityTool.scaleImage(img, 48, 48);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    static void loadFlame() {
        headUp = UtilityTool.loadSpriteSheet("\\flame\\headUpFlame_48_x_16_.png");
        headDown = UtilityTool.loadSpriteSheet("\\flame\\headDownFlame_48_x_16_.png");
        headLeft = UtilityTool.loadSpriteSheet("\\flame\\headLeftFlame_48_x_16_.png");
        headRight = UtilityTool.loadSpriteSheet("\\flame\\headRightFlame_48_x_16_.png");
        verticalBody = UtilityTool.loadSpriteSheet("\\flame\\verticalBodyFlame_48_x_16_.png");
        horizontalBody = UtilityTool.loadSpriteSheet("\\flame\\horizontalBodyFlame_48_x_16_.png");
    }

    static void loadBomb() {
        idle = UtilityTool.loadSpriteSheet("\\bomb\\normal\\bomb_64_x_16_.png");
        explosion = UtilityTool.loadSpriteSheet("\\bomb\\normal\\explosion_128_x_16_.png");
    }

    static void loadPlayer() {
        playerUp = UtilityTool.loadSpriteSheet("\\player\\up_32_x_16_.png");
        playerDown = UtilityTool.loadSpriteSheet("\\player\\down_32_x_16_.png");
        playerLeft = UtilityTool.loadSpriteSheet("\\player\\left_32_x_16_.png");
        playerRight = UtilityTool.loadSpriteSheet("\\player\\right_32_x_16_.png");
    }
}
