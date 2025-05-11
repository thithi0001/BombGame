package MenuSetUp;

import javax.swing.JLabel;
import javax.swing.JPanel;
// import javax.swing.SwingConstants;

import MenuDialog.RemindDialog;
import MenuDialog.SettingDialog;
import main.Main;
import res.LoadResource;
import userClass.User;
import java.util.List;
import java.awt.Insets;
// import java.awt.List;
import java.util.ArrayList;
import java.awt.Dimension;
// import java.awt.Dimension;
// import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class LevelPanel extends JPanel {
    MyButton back;
    MyButton setting;
    MyButton skin;
    public User user;
    MyButton[] level = new MyButton[LoadResource.maxMap];
    List<MyButton[]> levelButton = new ArrayList<>();
    public JPanel levelButtonPanel;
    public ChangePanel change;

    public LevelPanel(User currentUser, ChangePanel change) {
        setLayout(null);
        this.change = change;

        if (currentUser != null) {
            user = currentUser;
        } else user = new User(); 

        // SET UP LEVEL BUTTON
        levelButtonPanel = new JPanel();
        addButton(user, levelButtonPanel);
        add(levelButtonPanel);
        actionLevelButton();

        //back button
        back = new MyButton("back");
        back.setLocateButton(10, 10);
        add(back);
        addActionBackButton();

        // SET LABEL
        JLabel title = new JLabel("LEVEL");
        title.setSize(200, 100);
        title.setFont(LoadResource.panelTitle);
        title.setLocation((DimensionSize.screenWidth - 130) / 2, 20);
        add(title);

        // skin button
        skin = new MyButton("avatar");
        skin.setLocateButton(DimensionSize.screenWidth - 71, 71);
        add(skin);
        skin.addActionListener(_ -> {
            change.cardLayout.show(change.contentPane, "chooseSkin");
            change.skinPanel.back.addActionListener(_ -> change.cardLayout.show(change.contentPane, "level"));
        });
        
        //setting button
        setting = new MyButton("setting");
        setting.setLocateButton(DimensionSize.screenWidth - 71, 10);
        add(setting);
        setting.addActionListener(_ -> {
            change.frame.setEnabled(false);
            SettingDialog settingDialog = new SettingDialog(change.frame, change);
            settingDialog.setVisible(true);
            settingDialog.back.addActionListener(_ -> change.frame.setEnabled(true));
        });
    }

    public void addButton(User a, JPanel levelButtonPanel){
        levelButtonPanel.setLayout(new GridBagLayout());
        levelButtonPanel.setLocation(50, 150);
        levelButtonPanel.setSize(DimensionSize.screenWidth - 100, DimensionSize.screenHeight - 250);
        levelButtonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE; // Không mở rộng nút
        gbc.anchor = GridBagConstraints.CENTER; // Đặt nút ở giữa ô
        gbc.insets = new Insets(20, 20, 20, 20); // Khoảng cách giữa các nút
        JPanel[] smallLevel = new JPanel[LoadResource.maxMap-1];

        for (int i = 0; i < LoadResource.maxMap - 1; i++) {
            gbc.gridx = i % 4; // Cột
            gbc.gridy = i / 4; // Hàng
            String x = Main.res + "/button/level" +(i+1)+".png";
            smallLevel[i] = new BackGroundPanel(x);   
            smallLevel[i].setLayout(null);
            smallLevel[i].setPreferredSize(new Dimension(150, 150));
            MyButton[] lv = new MyButton[2];
            if(user.getLevel() > i ){
                lv[0] = new MyButton("normal");
                if(user.getScoreLv(i+1) > 0) lv[1] = new MyButton("hard");// đã vượt qua màn dễ
                else lv[1] = new MyButton("DLevelLocked");
            }
            else{
                lv[0] = new MyButton("levelLocked");
                lv[1] = new MyButton("DLevelLocked");
            }
            levelButton.add(lv);
            lv[0].setBounds(20, 75, 55, 55); // Nút lv[0] (ví dụ: Normal)
            lv[1].setBounds(80, 75, 55, 55); // Nút lv[1] (ví dụ: Hard)
            smallLevel[i].add(lv[0]);
            smallLevel[i].add(lv[1]);
            levelButtonPanel.add(smallLevel[i], gbc);
        }
        levelButtonPanel.revalidate();
        levelButtonPanel.repaint();
        // gbc.insets = new Insets(20, 20, 20, 20); // Khoảng cách giữa các nút
    }
    public void actionLevelButton() {
        for (int i = 0; i < LoadResource.maxMap - 1; i++) {
            int a = i + 1;
            if (i < user.getLevel()) {
                levelButton.get(i)[0].addActionListener(_ -> {
                    LevelGameFrame lv = new LevelGameFrame(a, this, "normal");
                    lv.setVisible(true);
                    change.frame.setVisible(false);
                });
                levelButton.get(i)[1].addActionListener(_ -> {
                    LevelGameFrame lv = new LevelGameFrame(a, this, "hard");
                    lv.setVisible(true);
                    change.frame.setVisible(false);
                });
            }
        }
    }

    // public void addButton(User a, JPanel levelButtonPanel) {
    //     levelButtonPanel.setLayout(new GridBagLayout());
    //     levelButtonPanel.setLocation(50, 150);
    //     levelButtonPanel.setSize(DimensionSize.screenWidth - 100, DimensionSize.screenHeight - 250);
    //     levelButtonPanel.setOpaque(false);

    //     GridBagConstraints gbc = new GridBagConstraints();
    //     gbc.fill = GridBagConstraints.NONE; // Không mở rộng nút
    //     gbc.anchor = GridBagConstraints.CENTER; // Đặt nút ở giữa ô
    //     gbc.insets = new Insets(20, 50, 20, 50); // Khoảng cách giữa các nút

    //     for (int i = 0; i < LoadResource.maxMap - 1; i++) {
    //         gbc.gridx = i % 4; // Cột
    //         gbc.gridy = i / 4; // Hàng

    //         String x = "level" + (i + 1);
    //         if (i < a.getLevel()) {
    //             level[i] = new MyButton(x);
    //         } else {
    //             level[i] = new MyButton("levelLocked"); // Nếu vượt cấp, khóa nút
    //         }

    //         // Thiết lập kích thước cố định cho nút
    //         level[i].setPreferredSize(new Dimension(level[i].width, level[i].height));
    //         level[i].setBorderPainted(false);
    //         level[i].setContentAreaFilled(false);
    //         level[i].setFocusPainted(false);

    //         levelButtonPanel.add(level[i], gbc);
    //     }
        
    // }

    // public void actionLevelButton() {
    //     for (int i = 0; i < LoadResource.maxMap - 1; i++) {
    //         int a = i + 1;
    //         if (i < user.getLevel()) {
    //             level[i].addActionListener(_ -> {
    //                 LevelGameFrame lv = new LevelGameFrame(a, this);
    //                 lv.setVisible(true);
    //                 change.frame.setVisible(false);
    //             });
    //         }
    //     }
    // }

    public void resetLevelPanel(User a, JPanel levelButtonPanel) {
        levelButtonPanel.removeAll();
        //add new button
        addButton(a, levelButtonPanel);

        //add action for new button
        actionLevelButton();
    }

    void addActionBackButton() {
        back.addActionListener(_ -> {
            RemindDialog saveGame = new RemindDialog(change.frame, "save game ");
            saveGame.setVisible(true);
            change.frame.setEnabled(false);
            //saveGame ok button
            saveGame.okButton.addActionListener(_ -> {
                change.userList.addUser(user);
                change.userList.saveGame();
                change.frame.setEnabled(true);
                change.cardLayout.show(change.contentPane, "menu");
                saveGame.setVisible(false);
            });
            //saveGame no button
            saveGame.noButton.addActionListener(_ -> {
                change.frame.setEnabled(true);
                change.cardLayout.show(change.contentPane, "menu");
                saveGame.setVisible(false);
            });
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(LoadResource.background, 0, 0, DimensionSize.screenWidth
                , DimensionSize.screenHeight, null);
    }
}

