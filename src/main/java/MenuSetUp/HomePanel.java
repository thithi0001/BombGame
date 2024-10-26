package MenuSetUp;

import javax.swing.JPanel;

import res.LoadResource;

import java.awt.Graphics;

public class HomePanel extends JPanel {
    MyButton start;
    MyButton quit;

    public HomePanel(int width, int height) {

        setLayout(null);

        start = new MyButton("start");
        quit = new MyButton("quit");

        //thiet lap vi tri
        start.setLocateButton(DimensionSize.screenWidth / 2 - 71, (DimensionSize.maxScreenRow - 4) * DimensionSize.tileSize - 71);
        quit.setLocateButton(DimensionSize.screenWidth / 2 - 71, (DimensionSize.maxScreenRow - 2) * DimensionSize.tileSize - 71);

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
