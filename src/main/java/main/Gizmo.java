package main;

import bomb.Bomb;
import bomb.Flame;
import entity.Entity;
import entity.Item;

import java.awt.*;

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
    }

    void draw(Graphics2D g2) {
        gp.map.monsters.forEach(m -> entity(m, Color.BLACK, g2));
        gp.bombs.forEach(b -> bomb(b, g2));
        gp.map.items.forEach(i -> entity(i, Color.MAGENTA, g2));
        entity(gp.player, Color.BLACK, g2);
    }

    void agentPath() {}
    void agentState() {}
    void agent() {}
}
