package MenuDialog;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import MenuSetUp.MyButton;
import main.Main;


public class NewGameDialog extends JDialog{
    public JTextField textField;
    public MyButton okButton;
    public NewGameDialog(JFrame parent){
        super(parent);
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

        // Định nghĩa các thành phần của dialog
        okButton = new MyButton("yes");
        okButton.setLocateButton(175, 400);
        
        MyButton cancleButton = new MyButton("no");
        cancleButton.setLocateButton(275, 400);

        JLabel label = new JLabel("Enter name :");
        label.setFont(new Font("Courier New", Font.BOLD, 20));
        label.setSize(150, 30);
        label.setLocation((500-350)/2, 180);

        textField = new JTextField();
        textField.setSize(200, 30);
        textField.setLocation((500 - 350) /2 +150, 180);
        textField.setFont(new Font("Courier New", Font.BOLD, 20));
        textField.setOpaque(true);
        textField.setBorder(null);

        // Thêm các thành phần vào dialog
        JPanel textPanel = new JPanel();
        textPanel.add(label);
        textPanel.add(textField);
        getContentPane().add(label);
        getContentPane().add(okButton);
        getContentPane().add(cancleButton);
        getContentPane().add(textField);
        getContentPane().add(bg);
        cancleButton.addActionListener(e -> {setVisible(false);
            textField.setText("");
        });
    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("BOMB GAME");
        NewGameDialog a = new NewGameDialog(window);
        window.setSize(576, 576);
        a.setVisible(true);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
       // window.setVisible(true);
    }
}
