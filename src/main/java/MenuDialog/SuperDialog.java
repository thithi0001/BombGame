package MenuDialog;

import javax.swing.*;
import java.awt.*;

import MenuSetUp.MyButton;
import main.Main;

public class SuperDialog extends JDialog {
    public SuperDialog(JFrame parent) {
        super(parent);
        setSize(500, 520);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

    }


    public void setBackground() {
        Icon background;
        JLabel bg;
        background = new ImageIcon(Main.res + "/background/dialogBackground.png");
        bg = new JLabel(background);
        bg.setLocation(-5, -15);
        bg.setSize(500, 520);
        getContentPane().add(bg);
    }


    public void setTitle(String Title) {
        JLabel title = new JLabel(Title);
        title.setFont(new Font("Courier New", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setSize(200, 30);
        title.setLocation(150, 25);
        getContentPane().add(title);
    }

    public void addButton(MyButton yes, MyButton no) {
        JPanel button = new JPanel();
        button.add(yes);
        button.add(no);
        button.setSize(500, 55);
        button.setLocation(0, 400);
        button.setOpaque(false);
        getContentPane().add(button);
    }
}
