package MenuDialog;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.*;

import MenuSetUp.MyButton;
public class ReplaceDialog extends JDialog{
    public MyButton okButton;
    public MyButton cancelButton;
    public ReplaceDialog(JFrame parent){
        super(parent);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);


        JLabel message= new JLabel();
        message.setText("<html>Player's name has existed.<br>Do you want to replace it?</html>");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);
        message.setFont(new Font("Courier New", Font.BOLD, 20));
        JPanel text = new JPanel();
        text.add(message);

        okButton = new MyButton("yes");
        cancelButton = new MyButton("no");
        JPanel panel = new JPanel();
        panel.add(okButton);
        panel.add(cancelButton);
        getContentPane().add(text,BorderLayout.CENTER);
        getContentPane().add(panel,BorderLayout.SOUTH);
        cancelButton.addActionListener(e -> setVisible(false));
        okButton.addActionListener(e -> setVisible(false));
    }
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("BOMB GAME");
        ReplaceDialog a = new ReplaceDialog(window);
        window.setSize(576, 576);
        a.setVisible(true);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
       // window.setVisible(true);
    }
}
