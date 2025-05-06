package AI.LoS;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

import static MenuSetUp.DimensionSize.tileSize;

public class CrossLoS extends LineOfSight {

    private final int distance;
    private int distanceUp, distanceDown, distanceLeft, distanceRight;
    public Rectangle vertical, horizontal;

    public CrossLoS(GamePanel gp, Entity self, Entity target, int distance) {
        super(gp, self, target);
        this.distance = distance;
    }

    public int getDistance() {return distance;}

    public void calculateDistance() {
        int col = self.col();
        int row = self.row();
        calculateDistanceUp(col, row);
        calculateDistanceDown(col, row);
        calculateDistanceLeft(col, row);
        calculateDistanceRight(col, row);
    }

    public void calculateDistanceUp(int col, int row) {
        distanceUp = 0;
        for (int i = row; i >= 0 && distanceUp <= distance; i--) {
            if (gp.map.isObstacle(col, i)) break;
            distanceUp++;
        }
    }

    public void calculateDistanceDown(int col, int row) {
        distanceDown = 0;
        for (int i = row; i < HEIGHT && distanceDown <= distance; i++) {
            if (gp.map.isObstacle(col, i)) break;
            distanceDown++;
        }
    }

    public void calculateDistanceLeft(int col, int row) {
        distanceLeft = 0;
        for (int i = col; i >= 0 && distanceLeft <= distance; i--) {
            if (gp.map.isObstacle(i, row)) break;
            distanceLeft++;
        }
    }

    public void calculateDistanceRight(int col, int row) {
        distanceRight = 0;
        for (int i = col; i < WIDTH && distanceRight <= distance; i++) {
            if (gp.map.isObstacle(i, row)) break;
            distanceRight++;
        }
    }

    public boolean checkVertical() {
        int x = self.col() * tileSize;
        int y = (self.row() - (distanceUp - 1)) * tileSize;
        int h = (distanceUp + distanceDown - 1) * tileSize;
        vertical = new Rectangle(x, y, tileSize, h);
        return target.solidArea.intersects(vertical);
    }

    public boolean checkHorizontal() {
        int x = (self.col() - (distanceLeft - 1)) * tileSize;
        int y = self.row() * tileSize;
        int w = (distanceLeft + distanceRight - 1) * tileSize;
        horizontal = new Rectangle(x, y, w, tileSize);
        return target.solidArea.intersects(horizontal);
    }

    @Override
    public void update() {
        calculateDistance();
        isVisible = checkVertical() || checkHorizontal();
//        System.out.println(distanceUp + " " + distanceDown + " " + distanceLeft + " " + distanceRight);
//        System.out.println(vertical);
//        System.out.println(horizontal);
//        System.out.println(isVisible);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0.855f, 0f, 1f, 0.3f));
        g2.fill(vertical);
        g2.fill(horizontal);
    }
}
