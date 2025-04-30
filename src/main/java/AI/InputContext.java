package AI;

import bomb.Bomb;
import bomb.Flame;
import entity.Entity;
import entity.Item;
import main.GamePanel;

import java.awt.Point;
import java.util.ArrayList;

// class nay dung de lay thong tin dau vao cho agent xu ly
public class InputContext {

    public GamePanel gp;
    private Entity self;// start point
    private Entity target;// goal point

    // chi can lay vi tri cua cac doi tuong la duoc
    private ArrayList<Bomb> bombs;
    private ArrayList<Flame> flames;
    private ArrayList<Item> items;

    public InputContext(GamePanel gp, Entity self) {
        this(gp, self, gp.player);
    }

    public InputContext(GamePanel gp, Entity self, Entity target) {
        this.gp = gp;
        this.self = self;
        this.target = target;

        this.bombs = gp.bombs;
        this.items = gp.map.items;
    }

    public Point getTargetPosition() {
        return target.getPosition();
    }
    public Point getSelfPosition() {
        return self.getPosition();
    }

    public ArrayList<Point> getBombsPosition() {
        ArrayList<Point> bombsPos = new ArrayList<>();
        bombs.forEach(bomb -> {
            bombsPos.add(new Point(bomb.col(), bomb.row()));
        });
        return bombsPos;
    }
    public ArrayList<Point> getFlamesPosition() {
        ArrayList<Point> flamesPos = new ArrayList<>();
        flames.forEach(flame -> {
            flamesPos.add(new Point(flame.col(), flame.row()));
        });
        return flamesPos;
    }
    public ArrayList<Point> getItemsPosition() {
        ArrayList<Point> itemsPos = new ArrayList<>();
        items.forEach(item -> {
            itemsPos.add(item.getPosition());
        });
        return itemsPos;
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
