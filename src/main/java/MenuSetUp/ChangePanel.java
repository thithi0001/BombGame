package MenuSetUp;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import MenuDialog.ContinueDialog;
import MenuDialog.NoticeDialog;
import MenuDialog.RemindDialog;
import MenuDialog.ReplaceDialog;
import MenuDialog.SettingDialog;
import MenuDialog.HighScoreDialog;
import MenuDialog.NewGameDialog;
import userClass.User;
import userClass.UserList;

public class ChangePanel {
    public Container contentPane;
    public CardLayout cardLayout;
    public UserList userList;
    public HomePanel home;
    public MenuPanel menu;
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

        contentPane.setPreferredSize(new Dimension(DimensionSize.screenWidth, DimensionSize.screenHeight));
        contentPane.add(home, "menu");//panel 1 in contentPane
        contentPane.add(menu, "start");//panel 2 in contentPane

        music = new Sound("Music");
        music.play();
        music.loop();

    }

    public void actionChange() {
        ReplaceDialog replace = new ReplaceDialog(frame);
        
        //SET UP OTHER BUTTON IN START PANEL
        
        menu.newGame.addActionListener(event-> {
            NewGameDialog newUser = new NewGameDialog(frame);
            frame.setEnabled(false);
            newUser.setVisible(true);
            
            // SET UP DIALOG NEW GAME
            newUserAction NA = new newUserAction(newUser, this, replace);
            newUser.okButton.addActionListener(NA);
            newUser.textField.addActionListener(NA);
            newUser.cancelButton.addActionListener(e -> {
                frame.setEnabled(true);
                newUser.setVisible(false);
                newUser.textField.setText("");
            });

            //SET UP DIALOG REPLACE IF USER HAS EXISTED
            replace.okButton.addActionListener(new replaceAction(newUser,this, replace));
            replace.cancelButton.addActionListener(e -> {
                newUser.setEnabled(true);
                replace.setVisible(false);
            });
        });

        menu.continueButton.addActionListener(e -> {
            frame.setEnabled(false);
            ContinueDialog dialog = new ContinueDialog(frame, this);
            dialog.setVisible(true);
        });

        menu.score.addActionListener(e -> {
            frame.setEnabled(false);
            HighScoreDialog highScore = new HighScoreDialog(frame, userList);
            highScore.setVisible(true);
        });
        
        menu.back.addActionListener(e -> cardLayout.previous(contentPane));
        menu.setting.addActionListener(e -> {
            frame.setEnabled(false);
            SettingDialog settingDialog = new SettingDialog(frame, this);
            settingDialog.setVisible(true);
            settingDialog.back.addActionListener(event -> frame.setEnabled(true) );
        });

        // SET UP MENU PANEL BUTTON
        home.start.addActionListener((e) -> cardLayout.next(contentPane));
        home.quit.addActionListener((e) -> {
            RemindDialog a = new RemindDialog(frame, "exit Game ");
            a.setVisible(true);
            frame.setEnabled(false);
            //SET UP BUTTON FOR RemindDialog
            a.okButton.addActionListener(event -> {
                userList.saveGame();
                System.exit(0);
            });
            a.noButton.addActionListener(event -> {
                frame.setEnabled(true);
                a.setVisible(false);
            });
        });

    }

}

class newUserAction implements ActionListener {
    NewGameDialog newUser;
    ChangePanel change;
    ReplaceDialog replace;

    public newUserAction(NewGameDialog newUser, ChangePanel change, ReplaceDialog replace) {
        this.newUser = newUser;
        this.change = change;
        this.replace = replace;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String x = newUser.textField.getText();
        if (x.isEmpty()) {
            NoticeDialog note = new NoticeDialog(change.frame);
            newUser.setEnabled(false);
            note.setVisible(true);
            note.okButton.addActionListener(event -> {
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
                change.frame.setEnabled(true);
                change.contentPane.add(new LevelPanel(A, change), "newLevel");
                change.cardLayout.show(change.contentPane, "newLevel");
                newUser.setVisible(false);
            }
        }
    }

}

class replaceAction implements ActionListener {
    ChangePanel changePanel;
    ReplaceDialog replace;
    NewGameDialog newUser;
    public replaceAction(NewGameDialog newUser, ChangePanel change, ReplaceDialog replace) {
        this.changePanel = change;
        this.replace = replace;
        this.newUser = newUser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String x = newUser.textField.getText();
        newUser.setVisible(false);
        changePanel.frame.setEnabled(true);
        newUser.textField.setText("");
        changePanel.contentPane.add(new LevelPanel(new User(x), changePanel), "level");
        changePanel.cardLayout.show(changePanel.contentPane, "level");
        replace.setVisible(false);
    }
}