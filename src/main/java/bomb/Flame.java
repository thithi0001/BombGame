package bomb;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.UtilityTool;
import tile.Tile;

public class Flame {

    GamePanel gp;
    int x, y;

    BufferedImage[] sprites = null;
    BufferedImage[] headUp, headDown, headLeft, headRight;
    BufferedImage[] bodyUp, bodyDown, bodyLeft, bodyRight;
    BufferedImage[] intersection = null;
    public int spriteTime;// time between 2 sprites
    public int spriteNum = 0;// index of the using sprite
    public int spriteCounter = 0;// should be frame counter

    public Rectangle solidArea;
    public int length;
    int tempLength = 8;

    public Flame(GamePanel gp, int x, int y, int length) {

        this.gp = gp;
        this.x = x;
        this.y = y;
        this.length = length;

        getFlameImage();
    }

    void getFlameImage() {

    }

    public void update() {

        if (++spriteCounter > spriteTime) {

            if (++spriteNum == tempLength) {

                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        // intersection
//        g2.setColor(Color.ORANGE);
//        g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        // head and body
        drawUp(g2);
        drawDown(g2);
        drawLeft(g2);
        drawRight(g2);
    }

    void drawUp(Graphics2D g2) {

        Tile tile;
        g2.setColor(Color.YELLOW);
        for (int i = 1, offset = 0; i <= length; i++) {
            offset = i * gp.tileSize;
            tile = gp.tileManager.tile[gp.tileManager.mapTileNum[x / gp.tileSize][(y - offset) / gp.tileSize]];
            if (tile.collision && !tile.destructible) break;
            // head
            if (i == length) g2.setColor(Color.PINK);
            // body
            g2.fillRect(x, y - offset, gp.tileSize, gp.tileSize);

            //a function to change the object tile to the base tile
            if (tile.destructible) {
                gp.tileManager.mapTileNum[x / gp.tileSize][(y - offset) / gp.tileSize] = 0;
                break;
            }
        }
    }

    void drawDown(Graphics2D g2) {

        Tile tile;
        g2.setColor(Color.YELLOW);
        for (int i = 1, offset = 0; i <= length; i++) {
            offset = i * gp.tileSize;
            tile = gp.tileManager.tile[gp.tileManager.mapTileNum[x / gp.tileSize][(y + offset) / gp.tileSize]];
            if (tile.collision && !tile.destructible) break;
            // head
            if (i == length) g2.setColor(Color.PINK);
            // body
            g2.fillRect(x, y + offset, gp.tileSize, gp.tileSize);// down

            //a function to change the object tile to the base tile
            if (tile.destructible) {
                gp.tileManager.mapTileNum[x / gp.tileSize][(y + offset) / gp.tileSize] = 0;
                break;
            }
        }
    }

    void drawLeft(Graphics2D g2) {

        Tile tile;
        g2.setColor(Color.YELLOW);
        for (int i = 1, offset = 0; i <= length; i++) {
            offset = i * gp.tileSize;
            tile = gp.tileManager.tile[gp.tileManager.mapTileNum[(x - offset) / gp.tileSize][y / gp.tileSize]];
            if (tile.collision && !tile.destructible) break;
            // head
            if (i == length) g2.setColor(Color.PINK);
            // body
            g2.fillRect(x - offset, y, gp.tileSize, gp.tileSize);// left

            //a function to change the destructible tile to the base tile -> create a Map class
            if (tile.destructible) {
                gp.tileManager.mapTileNum[(x - offset) / gp.tileSize][y / gp.tileSize] = 0;
                break;
            }
        }
    }

    void drawRight(Graphics2D g2) {

        Tile tile;
        g2.setColor(Color.YELLOW);
        for (int i = 1, offset = 0; i <= length; i++) {
            offset = i * gp.tileSize;
            tile = gp.tileManager.tile[gp.tileManager.mapTileNum[(x + offset) / gp.tileSize][y / gp.tileSize]];
            if (tile.collision && !tile.destructible) break;
            // head
            if (i == length) g2.setColor(Color.PINK);
            // body
            g2.fillRect(x + offset, y, gp.tileSize, gp.tileSize);// right

            //a function to change the object tile to the base tile
            if (tile.destructible) {
                gp.tileManager.mapTileNum[(x + offset) / gp.tileSize][y / gp.tileSize] = 0;
                break;
            }
        }
    }
}
