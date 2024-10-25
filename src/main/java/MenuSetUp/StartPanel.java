package MenuSetUp;

import javax.swing.JFrame;
import javax.swing.JPanel;

import MenuDialog.NewGameDialog;

import java.awt.*;

import res.LoadResource;

public class StartPanel extends JPanel {
    MyButton back;
    MyButton setting;
    MyButton newGame;
    MyButton score;
    MyButton continueButton;
    NewGameDialog newUser;

    public StartPanel(JFrame frame) {
        setLayout(null);

        back = new MyButton("back");
        back.setLocateButton(10, 10);
        add(back);

        setting = new MyButton("setting");
        setting.setLocateButton(DimensionSize.screenWidth - 71, 10);
        add(setting);

        newGame = new MyButton("newGame");
        newGame.setLocateButton((DimensionSize.screenWidth - 206) / 2, ((DimensionSize.maxScreenRow - 5) / 2) * DimensionSize.tileSize);
        add(newGame);

        continueButton = new MyButton("continue");
        continueButton.setLocateButton((DimensionSize.screenWidth - 206) / 2, ((DimensionSize.maxScreenRow - 5) / 2 + 2) * DimensionSize.tileSize);
        add(continueButton);

        score = new MyButton("score");
        score.setLocateButton((DimensionSize.screenWidth - 206) / 2, ((DimensionSize.maxScreenRow - 5) / 2 + 4) * DimensionSize.tileSize);
        add(score);

        newUser = new NewGameDialog(frame);//Dialog announce enter new player's name
        newGame.addActionListener(e -> newUser.setVisible(true));
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(LoadResource.background, 0, 0, DimensionSize.screenWidth, DimensionSize.screenHeight, null);
    }
}
