package MenuSetUp;

import javax.swing.JPanel;

import res.LoadResource;

import java.awt.Graphics;

public class MenuPanel extends JPanel {
    MyButton start;
    MyButton quit;

    public MenuPanel(int width, int height) {

        setLayout(null);

        start = new MyButton("start");
        quit = new MyButton("quit");

        //thiet lap vi tri
        start.setLocateButton(width / 2 - 71, (DimensionSize.maxScreenCol - 4) * DimensionSize.tileSize);
        quit.setLocateButton(width / 2 - 71, (DimensionSize.maxScreenCol - 2) * DimensionSize.tileSize);

        add(start);
        add(quit);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(LoadResource.background, 0, 0, DimensionSize.screenWidth, DimensionSize.screenHeight, null);
        g.drawImage(LoadResource.logo, 0, 0, DimensionSize.screenWidth, DimensionSize.screenHeight, null);
    }

}
