package main;

import bomb.Bomb;
import bomb.Flame;
import entity.Entity;
import entity.Item;

import java.awt.*;
import java.util.List;

import static MenuSetUp.DimensionSize.tileSize;

public class Gizmo {

    GamePanel gp;

    public Gizmo(GamePanel gp) {
        this.gp = gp;
    }

    void flame(Flame flame, Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fill(flame.verticalSolidArea);
        g2.fill(flame.horizontalSolidArea);
    }

    void bomb(Bomb bomb, Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.draw(bomb.solidArea);
    }

    void entity(Entity entity, Color color, Graphics2D g2) {
        if (!entity.isAlive) return;
        if (entity instanceof Item)
            if (((Item) entity).state == Item.States.hidden) return;
        g2.setColor(color);
        g2.draw(entity.solidArea);
        g2.drawRect(entity.x, entity.y, tileSize, tileSize);
    }

    void draw(Graphics2D g2) {
        gp.map.monsters.forEach(m -> entity(m, Color.BLACK, g2));
        gp.bombs.forEach(b -> bomb(b, g2));
        gp.map.items.forEach(i -> entity(i, Color.MAGENTA, g2));
        entity(gp.player, Color.BLACK, g2);
        gp.map.monsters.forEach(m -> agentPath(m.getCurrentPath(), m.getPathIndex(), g2));
    }

    void agentPath(List<Point> path, int startIndex, Graphics2D g2) {
        g2.setColor(Color.BLACK);
        int lines = path.size() - startIndex;
        for (int i = startIndex; i < path.size() - 1; i++) {
            int x1 = path.get(i).x * tileSize + tileSize / 2;
            int y1 = path.get(i).y * tileSize + tileSize / 2;
            int x2 = path.get(i + 1).x * tileSize + tileSize / 2;
            int y2 = path.get(i + 1).y * tileSize + tileSize / 2;
            g2.drawLine(x1, y1, x2, y2);
        }
    }
    void agentState() {}
    void agent() {}
}
