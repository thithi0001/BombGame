package AI.LoS;

import MenuSetUp.DimensionSize;
import entity.Entity;
import main.GamePanel;

import java.awt.*;

public abstract class LineOfSight {

    GamePanel gp;
    protected final int WIDTH, HEIGHT;
    protected Entity self;
    protected Entity target;
    protected boolean isVisible;

    public LineOfSight(GamePanel gp, Entity self, Entity target) {
        this(gp, DimensionSize.maxScreenCol, DimensionSize.maxScreenRow, self, target);
    }

    public LineOfSight(GamePanel gp, int width, int height, Entity self, Entity target) {
        this.gp = gp;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.self = self;
        this.target = target;
    }

    public abstract void update();
    public abstract void draw(Graphics2D g2);

    public boolean isVisible() {return isVisible;}
}
