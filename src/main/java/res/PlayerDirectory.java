package res;

import main.Main;
import main.UtilityTool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class PlayerDirectory {

    private final String character;
    public BufferedImage[] up, down, left , right;

    public PlayerDirectory(String directoryName) {
        character = directoryName;
        String path = "\\player\\" + directoryName;
        String[] files = Objects.requireNonNull(new File(Main.res + path).list());

        down = UtilityTool.loadSpriteSheet(path + "\\" + files[0]);
        left = UtilityTool.loadSpriteSheet(path + "\\" + files[1]);
        right = UtilityTool.loadSpriteSheet(path + "\\" + files[2]);
        up = UtilityTool.loadSpriteSheet(path + "\\" + files[3]);
    }

    public String getCharacter() {
        return character;
    }
}
