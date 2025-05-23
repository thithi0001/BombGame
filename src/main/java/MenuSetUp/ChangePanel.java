package MenuSetUp;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import MenuDialog.*;
import userClass.User;
import userClass.UserList;

public class ChangePanel {
    public Container contentPane;
    public CardLayout cardLayout;
    public UserList userList;
    HomePanel home;
    MenuPanel menu;
    public SkinPanel skinPanel;
    public JFrame frame;
    public Sound music;

    public ChangePanel(JFrame frame) {

        this.frame = frame;
        cardLayout = new CardLayout();
        contentPane = frame.getContentPane();
        contentPane.setLayout(cardLayout);
        userList = new UserList();

        home = new HomePanel(DimensionSize.screenWidth, DimensionSize.screenHeight);
        menu = new MenuPanel(frame);
        skinPanel = new SkinPanel();

        contentPane.setPreferredSize(new Dimension(DimensionSize.screenWidth, DimensionSize.screenHeight));
        contentPane.add(home, "home");//panel 1 in contentPane
        contentPane.add(menu, "menu");//panel 2 in contentPane
        contentPane.add(skinPanel, "chooseSkin");//panel 3 in contentPane

        music = new Sound("Music");
        music.play();
        music.loop();

    }

    public void actionChange() {
        // SET UP HOME PANEL BUTTON
        home.start.addActionListener(_ -> cardLayout.next(contentPane));
        home.quit.addActionListener(_ -> {
            RemindDialog a = new RemindDialog(frame, "exit Game ");
            a.setVisible(true);
            frame.setEnabled(false);
            //SET UP BUTTON FOR RemindDialog
            a.okButton.addActionListener(_ -> {
                userList.saveGame();
                System.exit(0);
            });
            a.noButton.addActionListener(_ -> {
                frame.setEnabled(true);
                a.setVisible(false);
            });
        });
        home.instruction.addActionListener(_ -> {
            frame.setEnabled(false);
            InstructionDialog a = new InstructionDialog(frame);
            a.setVisible(true);
        });
        home.credits.addActionListener(_ -> {
            frame.setEnabled(false);
            CreditDialog a = new CreditDialog(frame);
            a.setVisible(true);
        });

        //SET UP MENU PANEL BUTTON
        ReplaceDialog replace = new ReplaceDialog(frame);
        menu.newGame.addActionListener(_ -> {
            NewGameDialog newUser = new NewGameDialog(frame);
            frame.setEnabled(false);
            newUser.setVisible(true);

            // SET UP DIALOG NEW GAME
            newUserAction NA = new newUserAction(newUser, this, replace);
            newUser.okButton.addActionListener(NA);
            newUser.textField.addActionListener(NA);
            newUser.cancelButton.addActionListener(_ -> {
                frame.setEnabled(true);
                newUser.setVisible(false);
                newUser.textField.setText("");
            });

            //SET UP DIALOG REPLACE IF USER HAS EXISTED
            replace.okButton.addActionListener(new replaceAction(newUser, this, replace));
            replace.cancelButton.addActionListener(_ -> {
                newUser.setEnabled(true);
                replace.setVisible(false);
            });
        });

        menu.continueButton.addActionListener(_ -> {
            frame.setEnabled(false);
            ContinueDialog dialog = new ContinueDialog(frame, this);
            dialog.setVisible(true);
        });

        menu.score.addActionListener(_ -> {
            frame.setEnabled(false);
            HighScoreDialog highScore = new HighScoreDialog(frame, userList);
            highScore.setVisible(true);
        });

        menu.back.addActionListener(_ -> cardLayout.previous(contentPane));
        menu.setting.addActionListener(_ -> {
            frame.setEnabled(false);
            SettingDialog settingDialog = new SettingDialog(frame, this);
            settingDialog.setVisible(true);
            settingDialog.back.addActionListener(_ -> frame.setEnabled(true));
        });

    }
}

class newUserAction implements ActionListener {
    NewGameDialog newUser;
    ChangePanel change;
    ReplaceDialog replace;
    InstructionDialog a;

    public newUserAction(NewGameDialog newUser, ChangePanel change, ReplaceDialog replace) {
        this.newUser = newUser;
        this.change = change;
        this.replace = replace;
        a = new InstructionDialog(change.frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String x = newUser.textField.getText();
        if (x.isEmpty()) {
            NoticeDialog note = new NoticeDialog(change.frame);
            newUser.setEnabled(false);
            note.setVisible(true);
            note.okButton.addActionListener(_ -> {
                newUser.setEnabled(true);
                note.setVisible(false);
            });
        } else {
            boolean check = false;
            for (User a : change.userList.list) {
                if (a.getUserName().equals(x)) {
                    check = true;
                    newUser.setEnabled(false);
                    replace.setVisible(true);
                    break;
                }
            }
            if (!check) {
                User A = new userClass.User(x);
                newUser.textField.setText("");
                a.setVisible(true);
                change.contentPane.add(new LevelPanel(A, change), "level");
                change.cardLayout.show(change.contentPane, "level");
                newUser.setVisible(false);
            }
        }
    }
}

class replaceAction implements ActionListener {
    ChangePanel change;
    ReplaceDialog replace;
    NewGameDialog newUser;
    InstructionDialog a;
    public replaceAction(NewGameDialog newUser, ChangePanel change, ReplaceDialog replace) {
        this.change = change;
        this.replace = replace;
        this.newUser = newUser;
        a = new InstructionDialog(change.frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String x = newUser.textField.getText();
        User userReplace = new User(x);
        change.userList.addUser(userReplace);
        change.userList.saveGame();
        change.contentPane.add(new LevelPanel(userReplace, change), "level");
        change.cardLayout.show(change.contentPane, "level");
        a.setVisible(true);
        newUser.setVisible(false);
        newUser.textField.setText("");
        replace.setVisible(false);
    }
}