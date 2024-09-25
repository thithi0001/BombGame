package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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

    public static BufferedImage[] loadSpriteSheet(GamePanel gp, String spriteSheetPath) {

        BufferedImage spriteSheet = null;
        try {
            spriteSheet = ImageIO.read(new File(spriteSheetPath));

        } catch (IOException e) {
            e.printStackTrace();
        }

        int col = 0;
        int row = 0;
        String[] tmp = spriteSheetPath.split("_");
        for (String str : tmp) {
            try {
                if (col == 0) {
                    col = Integer.parseInt(str) / gp.originalTileSize;
                } else {
                    row = Integer.parseInt(str) / gp.originalTileSize;
                    break;
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }

        BufferedImage[] sprites = new BufferedImage[col * row];
        for (int i = 0; i < sprites.length; i++) {
            assert spriteSheet != null;
            sprites[i] = spriteSheet.getSubimage(i * gp.originalTileSize, 0, gp.originalTileSize, gp.originalTileSize);
            sprites[i] = scaleImage(sprites[i], gp.tileSize, gp.tileSize);
        }

        return sprites;
    }
}