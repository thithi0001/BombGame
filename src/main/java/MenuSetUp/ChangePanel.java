package MenuSetUp;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

import MenuDialog.ContinueDialog;
import MenuDialog.NoticDialog;
import MenuDialog.RemindDialog;
import MenuDialog.ReplaceDialog;
import MenuDialog.HighScoreDialog;
import userClass.User;
import userClass.UserList;

public class ChangePanel {
    public Container contentPane;
    public User currentUser;
    LevelPanel level;
    public ChangePanel(JFrame frame){
        CardLayout cardlayout = new CardLayout();
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(cardlayout);
        UserList userlist =new UserList();
        MenuPanel menu = new MenuPanel(DimensionSize.screenWidth , DimensionSize.screenHeight);
        StartPanel start = new StartPanel(frame);
        level = new LevelPanel(currentUser, cardlayout, contentPane, frame, userlist);
        SettingPanel setting = new SettingPanel();
        menu.setBackground(null);
        level.setBackground(null);
        setting.setBackground(null);

        contentPane.setPreferredSize(new Dimension(DimensionSize.screenWidth , DimensionSize.screenHeight));
        contentPane.add(menu, "Panel 1");
        contentPane.add(start, "Panel 2");
        contentPane.add(level, "Panel 3");
        contentPane.add(setting, "Panel 4");

        

        ReplaceDialog replace = new ReplaceDialog(frame);
        NoticDialog note = new NoticDialog(frame);
        
        
        start.newUser.okButton.addActionListener(e -> {
            String x = start.newUser.textField.getText();
            if(x.equals(""))
                note.setVisible(true);
            else{
                boolean check = false;
                for(User a : userlist.list){
                    if(a.getUserName().equals(x)){
                        check = true;
                        replace.setVisible(true);
                        break;
                    }
                }
                if(check == false){
                    currentUser = new userClass.User(x);
                    start.newUser.setVisible(false);
                    start.newUser.textField.setText("");
                    contentPane.add(new LevelPanel(currentUser ,cardlayout ,contentPane ,frame ,userlist), "Panel 3");
                    cardlayout.show(contentPane, "Panel 3");

                }
            }
        });
        start.newUser.textField.addActionListener(e -> {
            String x = start.newUser.textField.getText();
            if(x.equals(""))
                note.setVisible(true);
            else{
                boolean check = false;
                for(User a : userlist.list){
                    if(a.getUserName().equals(x)){
                        check = true;
                        replace.setVisible(true);
                        break;
                    }
                }
                if(check == false){
                    currentUser = new userClass.User(x);
                    start.newUser.setVisible(false);
                    start.newUser.textField.setText("");
                    contentPane.add(new LevelPanel(currentUser ,cardlayout ,contentPane ,frame ,userlist), "Panel 3");
                    cardlayout.show(contentPane, "Panel 3");

                }
            }
        });
        replace.okButton.addActionListener(e -> {
            String x = start.newUser.textField.getText();
            currentUser = new userClass.User(x);
            start.newUser.setVisible(false);
            start.newUser.textField.setText("");
            contentPane.add(new LevelPanel(currentUser ,cardlayout ,contentPane ,frame ,userlist), "Panel 3");
            cardlayout.show(contentPane, "Panel 3");
            replace.setVisible(false);
        });
       
        start.score.addActionListener(e -> {
            HighScoreDialog highScore = new HighScoreDialog(frame, userlist);
            highScore.setVisible(true);
        });

        start.continueButton.addActionListener(e ->{
            ContinueDialog dialog = new ContinueDialog(frame, userlist, cardlayout, contentPane);
            dialog.setVisible(true);
        });

        menu.start.addActionListener((e) -> cardlayout.next(contentPane));
        menu.quit.addActionListener((e) ->{
            RemindDialog a = new RemindDialog(userlist, frame,"exit game");
            a.setVisible(true);
            a.okButton.addActionListener(event -> {
                userlist.saveGame();
                System.exit(0);
            });
        });


        setting.back.addActionListener((e) -> cardlayout.previous(contentPane));
        start.back.addActionListener(e -> cardlayout.previous(contentPane));
    }
}
