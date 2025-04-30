package AI.pathFinding;

import AI.InputContext;
import MenuSetUp.DimensionSize;
import tile.Tile;

import java.awt.Point;
import java.util.*;

// class dung de chuyen doi thong tin tu InputContext sang thong tin ma D* Lite co the hieu duoc
public class PathFindingAdapter {

    int width;
    int height;
    Point start;
    Point goal;
    Set<Point> obstacles;

    public PathFindingAdapter(InputContext input) {
        this.width = DimensionSize.maxScreenCol;
        this.height = DimensionSize.maxScreenRow;
        this.start = input.getSelfPosition();
        this.goal = input.getTargetPosition();
        obstacles = new HashSet<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile tile = input.gp.tileManager.tile[input.gp.map.mapTileNum[i][j]];
                if (tile.collision && !tile.destructible) {
                    obstacles.add(new Point(i, j));
                }
            }
        }
    }
}