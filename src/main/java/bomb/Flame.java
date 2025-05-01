package bomb;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.CollisionChecker;
import main.GamePanel;
import res.LoadResource;
import tile.Tile;

import static MenuSetUp.DimensionSize.tileSize;

public class Flame {

    Bomb creator;

    GamePanel gp;
    int x, y;

    BufferedImage[] headUp, headDown, headLeft, headRight;
    BufferedImage[] verticalBody, horizontalBody;
    int spriteTime = 3;// time between 2 sprites
    int spriteNum = 0;// index of the using sprite
    int spriteCounter = 0;// should be frame counter
    private int duration;

    public Rectangle verticalSolidArea, horizontalSolidArea;
    boolean solidAreaIsCreated = false;
    int length;

    public Flame(Bomb creator, int x, int y, int length, int duration) {

        this.creator = creator;
        this.gp = creator.gp;
        this.x = x;
        this.y = y;
        this.length = length;
        this.duration = duration;

        verticalSolidArea = new Rectangle(x, y, tileSize, tileSize);
        horizontalSolidArea = new Rectangle(x, y, tileSize, tileSize);
        getFlameImage();
    }

    void getFlameImage() {
        headUp = LoadResource.headUp;
        headDown = LoadResource.headDown;
        headLeft = LoadResource.headLeft;
        headRight = LoadResource.headRight;
        verticalBody = LoadResource.verticalBody;
        horizontalBody = LoadResource.horizontalBody;
    }

    public void update() {

        CollisionChecker.checkEntityForFlame(this, creator.owner);
        gp.map.items.forEach(item -> CollisionChecker.checkItemForFlame(this, item));
        gp.bombs.forEach(bomb -> CollisionChecker.checkBombForFlame(this, bomb));
        gp.map.monsters.forEach(monster -> CollisionChecker.checkEntityForFlame(this, monster));
//        CollisionChecker.checkEntityForFlame(this, gp.map.boss);

        duration--;
        if (++spriteCounter > spriteTime) {

            if (++spriteNum == headUp.length) {

                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }

    public int col() {
        return x / tileSize;
    }

    public int row() {
        return y / tileSize;
    }

    public void draw(Graphics2D g2) {

        extendUp(g2);
        extendDown(g2);
        extendLeft(g2);
        extendRight(g2);
        solidAreaIsCreated = true;
    }

    void extendUp(Graphics2D g2) {

        Tile tile;
        int fx, fy, offset = 0;
        for (int i = 1; i <= length; i++) {
            offset = i * tileSize;
            fx = x;
            fy = y - offset;
            tile = gp.tileManager.tile[gp.map.mapTileNum[fx / tileSize][fy / tileSize]];
            if (tile.collision && !tile.destructible) {
                offset -= tileSize;
                break;
            }
            drawFlame(i, fx, fy, headUp, verticalBody, g2);

            //a function to change the object tile to the base tile
            if (tile.destructible) {
                destroyTile(fx, fy);
                break;
            }
        }

        if (!solidAreaIsCreated) {
            verticalSolidArea.y -= offset;
            verticalSolidArea.height += offset;
        }
    }

    void extendDown(Graphics2D g2) {

        Tile tile;
        int fx, fy, offset = 0;
        for (int i = 1; i <= length; i++) {
            offset = i * tileSize;
            fx = x;
            fy = y + offset;
            tile = gp.tileManager.tile[gp.map.mapTileNum[fx / tileSize][fy / tileSize]];
            if (tile.collision && !tile.destructible) {
                offset -= tileSize;
                break;
            }
            drawFlame(i, fx, fy, headDown, verticalBody, g2);

            if (tile.destructible) {
                destroyTile(fx, fy);
                break;
            }
        }

        if (!solidAreaIsCreated) {
            verticalSolidArea.height += offset;
        }
    }

    void extendLeft(Graphics2D g2) {

        Tile tile;
        int fx, fy, offset = 0;
        for (int i = 1; i <= length; i++) {
            offset = i * tileSize;
            fx = x - offset;
            fy = y;
            tile = gp.tileManager.tile[gp.map.mapTileNum[fx / tileSize][fy / tileSize]];
            if (tile.collision && !tile.destructible) {
                offset -= tileSize;
                break;
            }
            drawFlame(i, fx, fy, headLeft, horizontalBody, g2);

            if (tile.destructible) {
                destroyTile(fx, fy);
                break;
            }
        }

        if (!solidAreaIsCreated) {
            horizontalSolidArea.x -= offset;
            horizontalSolidArea.width += offset;
        }
    }

    void extendRight(Graphics2D g2) {

        Tile tile;
        int fx, fy, offset = 0;
        for (int i = 1; i <= length; i++) {
            offset = i * tileSize;
            fx = x + offset;
            fy = y;
            tile = gp.tileManager.tile[gp.map.mapTileNum[fx / tileSize][fy / tileSize]];
            if (tile.collision && !tile.destructible) {
                offset -= tileSize;
                break;
            }
            drawFlame(i, fx, fy, headRight, horizontalBody, g2);

            if (tile.destructible) {
                destroyTile(fx, fy);
                break;
            }
        }

        if (!solidAreaIsCreated) {
            horizontalSolidArea.width += offset;
        }
    }

    public void drawFlame(int i, int fx, int fy, BufferedImage[] head, BufferedImage[] body, Graphics2D g2) {
        if (i == length) {
            g2.drawImage(head[spriteNum], fx, fy, null);
        } else {
            g2.drawImage(body[spriteNum], fx, fy, null);
        }
    }

    public void destroyTile(int fx, int fy) {
        if (duration == 1) {
            gp.map.toBaseTile(fx / tileSize, fy / tileSize);
        }
    }

    public int getDuration() {
        return duration;
    }
}
