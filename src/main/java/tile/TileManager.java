package tile;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.Main;
import main.UtilityTool;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;

    public TileManager(GamePanel gp) {

        this.gp = gp;
        tile = new Tile[10];
        getTileImage();
    }

    public void getTileImage() {

        setup(0, "grass", false, false);
        setup(1, "water", true, false);
        setup(2, "wood_box", true, true);
    }

    public void setup(int index, String imageName, boolean collision, boolean destructible) {

        try {
            tile[index] = new Tile();
            tile[index].name = imageName;
            tile[index].image = ImageIO.read(new File(Main.res + "\\tiles\\" + imageName + ".png"));
            tile[index].image = UtilityTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
            tile[index].destructible = destructible;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, Map map) {

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {

            int tileNum = map.mapTileNum[col][row];

            g2.drawImage(tile[tileNum].image, x, y, null);
            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }

        map.items.forEach(item -> {
            if(!item.isHit && !item.isPickedUp){
                item.draw(g2);
            }
        });
    }
}