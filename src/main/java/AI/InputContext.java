package AI;

import bomb.Bomb;
import bomb.Flame;
import entity.Entity;
import entity.Item;
import main.GamePanel;

import java.awt.*;
import java.util.List;

import static MenuSetUp.DimensionSize.*;

// class nay dung de lay thong tin dau vao cho agent xu ly
public class InputContext {

    GamePanel gp;
    private Entity self;
    private Entity target;

    private List<Bomb> bombs;
    private List<Flame> flames;
    private List<Item> items;

    public InputContext(GamePanel gp, Entity self, Entity target) {
        this.gp = gp;
        this.self = self;
        this.target = target;

        this.bombs = gp.bombs;
        this.items = gp.map.items;
    }

    public Point getTargetPosition() {
        return new Point(target.col(), target.row());
    }

    public boolean isTileWalkable(int col, int row) {
        return gp.tileManager.tile[gp.map.mapTileNum[col][row]].collision;
    }

    public boolean isTileHasItem(int col, int row) {
        for (Item item: items) {
            if (item.col() == col && item.row() == row)
                return true;
        }
        return false;
    }

    public boolean isTileHasBomb(int col, int row) {
        return gp.cChecker.hasBombHere(col, row);
    }

    public boolean isTileFlaming(int col, int row) {
        return true;
    }

    public boolean isTileDangerous(int col, int row) {
        return isTileHasBomb(col, row) || isTileFlaming(col, row);
    }

    public void printContext() {}


}
