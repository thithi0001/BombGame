package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static MenuSetUp.DimensionSize.originalTileSize;
import static MenuSetUp.DimensionSize.tileSize;

public class UtilityTool {

    public UtilityTool() {

    }

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }

    public static BufferedImage[] loadSpriteSheet(String spriteSheetPath) {

        BufferedImage spriteSheet = null;
        try {
            spriteSheet = ImageIO.read(new File(Main.res + spriteSheetPath));

        } catch (IOException e) {
            e.printStackTrace();
        }

        int col = 0;
        int sizeOfEachSprite = originalTileSize;
        String[] tmp = spriteSheetPath.split("_");
        for (String str : tmp) {
            try {
                if (col == 0) {
                    col = Integer.parseInt(str);
                } else {
                    sizeOfEachSprite = Integer.parseInt(str);
                    col /= sizeOfEachSprite;
                    break;
                }
            } catch (NumberFormatException _) {

            }
        }

        BufferedImage[] sprites = new BufferedImage[col];
        for (int i = 0; i < sprites.length; i++) {
            assert spriteSheet != null;
            sprites[i] = spriteSheet.getSubimage(i * sizeOfEachSprite, 0, sizeOfEachSprite, sizeOfEachSprite);
            sprites[i] = scaleImage(sprites[i], tileSize, tileSize);
        }

        return sprites;
    }

    public static int convertTime(double timeInSecond) {
        double timeInFrame = timeInSecond * GamePanel.FPS;
        return (int) timeInFrame;
    }
}