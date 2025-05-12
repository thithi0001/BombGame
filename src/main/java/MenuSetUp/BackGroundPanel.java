package MenuSetUp;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackGroundPanel extends JPanel {
    private BufferedImage backgroundImage;
    private int width, height;
    public BackGroundPanel(String imagePath){
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
            width = backgroundImage.getWidth();
            height = backgroundImage.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Không thể tải hình nền từ đường dẫn: " + imagePath);
        }
        
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, this);
        }
    }
}
