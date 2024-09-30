package tile;

import java.awt.image.BufferedImage;

public class Tile {

    public BufferedImage image = null;
    public boolean collision = false;
    public boolean destructible = false;
    /*
    * bomb can destroy destructible tiles and objects
    * if an entity is in the intangible state, it can pass through destructible tiles and objects
    * */
}