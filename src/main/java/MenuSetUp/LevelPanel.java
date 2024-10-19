package MenuSetUp;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import MenuDialog.RemindDialog;
import main.GamePanel;
import main.Main;
import userClass.User;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Font;

public class LevelPanel extends JPanel {
    MyButton back;
    MyButton setting;
    User user;
    MyButton[] level = new MyButton[3];

    public LevelPanel(User currentUser, ChangePanel change) {
        setLayout(null);

        if (currentUser != null) {
            user = currentUser;
        } else user = new User();

        for (int i = 0; i < 3; i++) {
            String x = "level" + (i + 1);
            if (i + 1 <= user.getLevel()) level[i] = new MyButton(x);
            else level[i] = new MyButton("levelBlock");
            level[i].setLocateButton(((DimensionSize.maxScreenCol - 5) / 2 + i * 2) * DimensionSize.tileSize, DimensionSize.screenHeight / 2);
            add(level[i]);
        }


        // SET LABEL
        JLabel lv = new JLabel("LEVEL");
        lv.setSize(200, 100);
        lv.setFont(new Font("Courier New", Font.BOLD, 38));
        lv.setLocation((DimensionSize.screenWidth - 130) / 2, 20);
        add(lv);

        //setting button
        setting = new MyButton("setting");
        setting.setLocateButton(DimensionSize.screenWidth - 60, 10);
        add(setting);
        setting.addActionListener((event) -> {
            SettingPanel setting = new SettingPanel(change.menu.music);
            change.contentPane.add(setting, "settingInLevel");
            change.cardLayout.show(change.contentPane, "settingInLevel");
            setting.back.addActionListener((e) -> {
                change.cardLayout.previous(change.contentPane);
                change.menu.music.saveSetting(change.menu.music);
            });
        });

        //back button
        back = new MyButton("back");
        back.setLocateButton(10, 10);
        add(back);
        back.addActionListener((event) -> {
            RemindDialog saveGame = new RemindDialog(change.userList, change.frame, "save game ");
            saveGame.setVisible(true);

            //saveGame ok button
            saveGame.okButton.addActionListener(e -> {
                change.userList.addUser(currentUser);
                change.userList.saveGame();
                change.cardLayout.show(change.contentPane, "start");
                saveGame.setVisible(false);
            });
            //saveGame no button
            saveGame.noButton.addActionListener(e -> {
                change.cardLayout.show(change.contentPane, "start");
                saveGame.setVisible(false);
            });
        });


        //SET UP LEVEL BUTTON
        for (int i = 0; i < 3; i++) {
            String levelFileName = "level_" + (i + 1);
            if (i < user.getLevel()) {
                level[i].addActionListener(e -> {
                    levelGame lv1 = new levelGame(levelFileName);
                    lv1.setVisible(true);
                    change.frame.setVisible(false);
                });
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background;
        try {
            File a = new File(Main.res + "/background/background.png");
            background = ImageIO.read(a);
            g.drawImage(background, 0, 0, DimensionSize.screenWidth, DimensionSize.screenHeight, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class levelGame extends JFrame {
    public levelGame(String mapFileName) {
        new JFrame();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window.setSize(576, 576);
        setResizable(false);
        setTitle("BOMB GAME");

        GamePanel gamePanel = new GamePanel(mapFileName);
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(false);
        gamePanel.startGameThread();
    }

}