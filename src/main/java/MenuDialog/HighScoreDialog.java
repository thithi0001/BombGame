package MenuDialog;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import java.awt.*;
import MenuSetUp.MyButton;
import main.Main;
import userClass.User;
import userClass.UserList;
import java.util.*;

public class HighScoreDialog extends JDialog {
    MyButton back;
    UserList userList;
    public HighScoreDialog(JFrame parent , UserList List){
        super(parent);
        this.userList = List;
        Collections.sort(userList.list , new Comparator<User>(){
            @Override
            public int compare(User o1, User o2) {
                if(o1.getScore() < o2.getScore())return 1;
                else{ if(o1.getScore() == o2.getScore())return 0;
                    else return -1;
                }
            }
        });
        setSize(500, 520);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        Icon background;
        JLabel bg;
        background= new ImageIcon(Main.res + "/background/dialogBackground.png");
        bg = new JLabel(background);
        bg.setLocation(-5,-15);
        bg.setSize(500, 520);

        JLabel title = new JLabel("HIGH-SCORE");
        title.setFont(new Font("Courier New", Font.BOLD, 30));
        title.setSize(200, 30);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setLocation(150, 25);

        JPanel content = new JPanel();
        content.setSize(150, 200);
        content.setLocation(150, 100);
        content.setOpaque(false);
        int numHighScore;
        if(userList.size <= 5)numHighScore = userList.size;
        else numHighScore = 5;
        for (int i = 0 ; i<numHighScore ; i++){    
            if(userList.list.get(i).getScore() == 0)break;
            JLabel name = new JLabel();
            name.setText(userList.list.get(i).getUserName());
            name.setFont(new Font("Courier New", Font.BOLD, 20));
            content.add(name);

            Border border = new BevelBorder(1);
            JLabel score = new JLabel();
            score.setText(" " +userList.list.get(i).getScore());
            score.setBorder(border);
            score.setFont(new Font("Courier New", Font.BOLD, 20));
            content.add(score);
            
        }
        content.setLayout(new GridLayout(5, 2, 10, 10));
        back = new MyButton("back");
        back.setLocateButton((500 - 50)/2, 400);
        getContentPane().add(back);
        getContentPane().add(title);
        getContentPane().add(content);
        getContentPane().add(bg);
        back.addActionListener(e -> setVisible(false));
    }
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("BOMB GAME");
        HighScoreDialog a = new HighScoreDialog(window, new UserList());
        window.setSize(576, 576);
        a.setVisible(true);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
       // window.setVisible(true);
    }
}
