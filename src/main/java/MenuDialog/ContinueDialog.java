package MenuDialog;


import javax.swing.*;
import java.awt.*;
import MenuSetUp.MyButton;
import MenuSetUp.LevelPanel;
import main.Main;
import userClass.*;


public class ContinueDialog extends JDialog {
    public JComboBox<User> userBox;
    public UserList userlist ;
    public MyButton okButton;
    public ContinueDialog(JFrame parent, UserList userList,CardLayout cardlayout,Container contentPane ){
        super(parent);
        setSize(500, 520);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        this.userlist = userList;
        userBox = new JComboBox<>();
        for(int i = 0 ;i <userlist.size ; i++){
            userBox.addItem(userlist.list.get(i));
        }
        userBox.setSize(200, 35);
        userBox.setLocation(150, 200);

        JLabel title = new JLabel("CONTINUE");
        title.setFont(new Font("Courier New", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setSize(200 , 30);
        title.setLocation(150, 25);

        okButton = new MyButton("yes");
        MyButton noButton = new MyButton("no");
        JPanel button = new JPanel();
        button.add(okButton);
        button.add(noButton);
        button.setSize(500, 55);
        button.setLocation(0, 400);
        button.setOpaque(false);

        Icon background;
        JLabel bg;
        background= new ImageIcon(Main.res + "/background/dialogBackground.png");
        bg = new JLabel(background);
        bg.setLocation(-5,-15);
        bg.setSize(500, 520);

        // getContentPane().add(box,BorderLayout.CENTER);
        getContentPane().add(title);
        getContentPane().add(userBox);
        getContentPane().add(button);
        getContentPane().add(bg);
        noButton.addActionListener(e -> setVisible(false));
        okButton.addActionListener(e -> {
            setVisible(false);
            User A =userBox.getItemAt(userBox.getSelectedIndex());
            User currentUser = new User(A.getUserName());
            currentUser.setLevel(A.getLevel());
            for (int i = 1; i <= A.getLevel(); i++) {
                currentUser.setScore(i, A.getScoreLv(i));
            }
            contentPane.add(new LevelPanel(currentUser ,cardlayout ,contentPane ,parent ,userList), "Panel 3");
            cardlayout.show(contentPane, "Panel 3");
        });
    }
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("BOMB GAME");
        window.setSize(576, 576);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
       // window.setVisible(true);
    }
}
