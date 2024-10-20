package MenuDialog;


import javax.swing.*;

import MenuSetUp.MyButton;
import MenuSetUp.ChangePanel;
import MenuSetUp.LevelPanel;
import userClass.*;


public class ContinueDialog extends SuperDialog {
    public MyButton okButton;
    UserList userList;

    public ContinueDialog(JFrame parent, ChangePanel change) {
        super(parent);
        setTitle("CONTINUE");

        // USER BOX
        this.userList = change.userList;
        JComboBox<User> userBox = new JComboBox<>();
        for (int i = 0; i < userList.size; i++) {
            userBox.addItem(userList.list.get(i));
        }
        userBox.setSize(200, 35);
        userBox.setLocation(150, 200);
        getContentPane().add(userBox);

        //BUTTON
        okButton = new MyButton("yes");
        MyButton noButton = new MyButton("no");
        addButton(okButton, noButton);

        setBackground();

        //SET BUTTON ACTION
        noButton.addActionListener(e -> setVisible(false));
        okButton.addActionListener(e -> {
            setVisible(false);
            User A = userBox.getItemAt(userBox.getSelectedIndex());
            User currentUser = new User(A.getUserName());
            currentUser.setLevel(A.getLevel());
            for (int i = 1; i <= A.getLevel(); i++) {
                currentUser.setScore(i, A.getScoreLv(i));
            }
            change.contentPane.add(new LevelPanel(currentUser, change), "level");
            change.cardLayout.show(change.contentPane, "level");
        });
    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChangePanel a = new ChangePanel(window);
        ContinueDialog x = new ContinueDialog(window, a);
        window.add(x);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}

