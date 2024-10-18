package MenuSetUp;

import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import MenuDialog.NewGameDialog;
import java.awt.*;
import java.awt.image.BufferedImage;
import main.Main;

public class StartPanel extends JPanel {
    MyButton back;
    MyButton newGame;
    MyButton score;
    MyButton continueButton;
    NewGameDialog newUser;

    public StartPanel(JFrame frame ){
        setLayout(null);
        back = new MyButton("back");
        back.setLocateButton(10, 10);
        add(back);

        newGame = new MyButton("newGame");
        newGame.setLocateButton((DimensionSize.screenWidth - 206)/2, ((DimensionSize.maxScreenRow - 5)/2) * DimensionSize.tileSize);
        add(newGame);

        continueButton = new MyButton("continue");
        continueButton.setLocateButton((DimensionSize.screenWidth - 206)/2, ((DimensionSize.maxScreenRow - 5)/2 +2) * DimensionSize.tileSize);
        add(continueButton);

        score = new MyButton("score");
        score.setLocateButton((DimensionSize.screenWidth - 206)/2, ((DimensionSize.maxScreenRow - 5)/2 +4) * DimensionSize.tileSize);
        add(score);

        newUser = new NewGameDialog(frame);

        newGame.addActionListener(e -> newUser.setVisible(true));
        //continueButton.addActionListener(e -> continueDialog.setVisible(true));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background;
        try {
            File a =new File(Main.res + "/background/background.png");
            background= ImageIO.read(a);
            g.drawImage(background, 0, 0, DimensionSize.screenWidth , DimensionSize.screenHeight, null);
        } catch (Exception e) {
            
        }
    }
    // public static void main(String[] args) {
        
    //     JFrame window = new JFrame();
    //     window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     window.setTitle("BOMB GAME");
    //     startPanel a = new startPanel(window);
    //     window.setSize(576, 576);
    //     window.add(a);
    //     window.setResizable(false);
    //     window.setLocationRelativeTo(null);
    //     window.setVisible(true);
    // }
}
