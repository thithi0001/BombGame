package MenuSetUp;

import javax.swing.JPanel;

import res.LoadResource;

import java.awt.Graphics;

public class HomePanel extends JPanel {
    MyButton start;
    MyButton quit;
    MyButton instruction;

    public HomePanel(int width, int height) {
        setLayout(null);

        start = new MyButton("start");
        quit = new MyButton("quit");
        instruction = new MyButton("game");

        start.setLocateButton(DimensionSize.screenWidth / 2 - 71, (DimensionSize.maxScreenRow - 4) * DimensionSize.tileSize - 71);
        quit.setLocateButton(DimensionSize.screenWidth / 2 - 71, (DimensionSize.maxScreenRow - 2) * DimensionSize.tileSize - 71);
        instruction.setLocateButton(DimensionSize.screenWidth - 71, 10);

        add(start);
        add(quit);
        add(instruction);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(LoadResource.background, 0, 0, DimensionSize.screenWidth, DimensionSize.screenHeight, null);
        g.drawImage(LoadResource.logo, 0, 0, DimensionSize.screenWidth, DimensionSize.screenHeight, null);
    }

}
