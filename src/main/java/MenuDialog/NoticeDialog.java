package MenuDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import MenuSetUp.MyButton;

public class NoticeDialog extends JDialog{
    public NoticeDialog(JFrame parent){
        super(parent);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel message= new JLabel();
        message.setText("Please Enter Player name.");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);
        message.setFont(new Font("Courier New", Font.BOLD, 20));
        JPanel text = new JPanel();
        text.setBackground(Color.WHITE);
        text.add(message);
        MyButton okButton = new MyButton("yes");
        okButton.setBorder(null);
        getContentPane().add(text,BorderLayout.CENTER);
        getContentPane().add(okButton,BorderLayout.SOUTH);
        getContentPane().setBackground(Color.WHITE);
        okButton.addActionListener(e -> setVisible(false));
    }
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("BOMB GAME");
        NoticeDialog a = new NoticeDialog(window);
        window.setSize(576, 576);
        a.setVisible(true);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
       // window.setVisible(true);
    }
}
