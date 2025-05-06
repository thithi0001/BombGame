package AI.pathFinding;

import MenuSetUp.DimensionSize;
import entity.Entity;
import main.GamePanel;
import tile.Tile;

import java.awt.Point;
import java.util.*;

public class PathFindingAdapter {

    int width;
    int height;
    Point start;
    Point goal;
    Set<Point> obstacles;

    public PathFindingAdapter(GamePanel gp, Entity self, Entity target) {
        this.width = DimensionSize.maxScreenCol;
        this.height = DimensionSize.maxScreenRow;
        this.start = self.getPosition();
        this.goal = target.getPosition();
        obstacles = new HashSet<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile tile = gp.tileManager.tile[gp.map.mapTileNum[i][j]];
                if (tile.collision) {
                    obstacles.add(new Point(i, j));
                }
            }
        }
    }
}