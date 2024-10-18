package res;

import main.Main;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LoadResource {
    public static HashMap<String, BufferedImage> itemMap = new HashMap<>();
    public static BufferedImage[] headUp, headDown, headLeft, headRight;
    public static BufferedImage[] verticalBody, horizontalBody;
    static {
        // load item
        itemMap.put("ugly_key", itemImage("ugly_key"));
        itemMap.put("plus_1", itemImage("plus_1"));

    }

    public static BufferedImage itemImage(String name) {

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(Main.res + "\\item\\" + name + ".png"));
            img = UtilityTool.scaleImage(img, 48, 48);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
