package MenuSetUp;


import javax.swing.JLabel;
import javax.swing.JPanel;

import MenuDialog.RemindDialog;
import MenuDialog.SettingDialog;
import res.LoadResource;
import userClass.User;

import java.awt.GridLayout;
import java.awt.Graphics;

public class LevelPanel extends JPanel {
    MyButton back;
    MyButton setting;
    public User user;
    public MyButton[] level = new MyButton[3];
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
        title.setFont(LoadResource.Courier_New_Bold_38);
        title.setLocation((DimensionSize.screenWidth - 130) / 2, 20);
        add(title);

        //setting button
        setting = new MyButton("setting");
        setting.setLocateButton(DimensionSize.screenWidth - 71, 10);
        add(setting);

        setting.addActionListener((event) -> {
            change.frame.setEnabled(false);
            SettingDialog settingDialog = new SettingDialog(change.frame, change);
            settingDialog.setVisible(true);
            settingDialog.back.addActionListener(e -> change.frame.setEnabled(true));
        });
    }

    public void addButton(User a, JPanel levelButtonPanel) {
        levelButtonPanel.setSize(DimensionSize.screenWidth - 100, DimensionSize.screenHeight - 250);
        int row = 0, col = 4;
        if (LoadResource.maxMap % col == 0) row = LoadResource.maxMap / col;
        else row = LoadResource.maxMap / col + 1;

        levelButtonPanel.setLayout(new GridLayout(row, col));
        levelButtonPanel.setLocation(50, 150);
        levelButtonPanel.setOpaque(false);
        for (int i = 0; i < 3; i++) {
            String x = "level" + (i + 1);
            if (i + 1 <= a.getLevel()) level[i] = new MyButton(x);

            else level[i] = new MyButton("levelBlock"); //nếu levelButton bé hơn level -> block button;
            level[i].setLocateButton(((DimensionSize.maxScreenCol - 5) / 2 + i * 2) * DimensionSize.tileSize
                    , DimensionSize.screenHeight / 2);
            levelButtonPanel.add(level[i]);
        }
    }

    public void actionLevelButton() {
        for (int i = 0; i < LoadResource.maxMap; i++) {
            int a = i + 1;
            if (i < user.getLevel()) {
                level[i].addActionListener(e -> {
                    LevelGameFrame lv1 = new LevelGameFrame(a, this);
                    lv1.setVisible(true);
                    change.frame.setVisible(false);
                });
            }
        }
    }

    public void resetLevelPanel(User a, JPanel levelButtonPanel) {
        for (int i = 0; i < LoadResource.maxMap; i++) {
            levelButtonPanel.remove(level[i]); //delete old button
        }
        //add new button
        addButton(a, levelButtonPanel);

        //add action for new button
        actionLevelButton();
    }

    void addActionBackButton() {
        back.addActionListener((event) -> {
            RemindDialog saveGame = new RemindDialog(change.frame, "save game ");
            saveGame.setVisible(true);
            change.frame.setEnabled(false);
            //saveGame ok button
            saveGame.okButton.addActionListener(e -> {
                change.userList.addUser(user);
                change.userList.saveGame();
                change.frame.setEnabled(true);
                change.cardLayout.show(change.contentPane, "start");
                saveGame.setVisible(false);
            });
            //saveGame no button
            saveGame.noButton.addActionListener(e -> {
                change.frame.setEnabled(true);
                change.cardLayout.show(change.contentPane, "start");
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

