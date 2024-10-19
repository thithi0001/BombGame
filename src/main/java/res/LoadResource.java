package res;

import main.Main;
import main.UtilityTool;
import tile.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import static MenuSetUp.DimensionSize.tileSize;

public class LoadResource {
    public static HashMap<String, BufferedImage> itemImgMap = new HashMap<>();
    public static HashMap<String, Integer> itemPointMap = new HashMap<>();
    public static HashMap<String, BufferedImage> monsterImgMap = new HashMap<>();

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

    static void loadTile() {

    }

    static void loadItem() {

        File file = new File(Main.res + "\\item\\itemList.txt");
        try (Scanner reader = new Scanner(file)) {
            String[] nameAndPoint;
            String itemName;
            int itemPoint;
            while (reader.hasNextLine()) {
                nameAndPoint = reader.nextLine().split(" ");
                itemName = nameAndPoint[0];
                itemPoint = Integer.parseInt(nameAndPoint[1]);
                itemImgMap.put(itemName, itemImage(itemName));
                itemPointMap.put(itemName, itemPoint);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage itemImage(String name) {

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(Main.res + "\\item\\" + name + ".png"));
            img = UtilityTool.scaleImage(img, tileSize, tileSize);

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
