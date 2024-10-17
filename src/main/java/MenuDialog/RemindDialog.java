package MenuDialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import MenuSetUp.MyButton;

import java.awt.*;

import userClass.UserList;

public class RemindDialog extends JDialog {
    public MyButton okButton;
    public MyButton noButton;
    public RemindDialog(UserList list, JFrame parent,String note){
        super(parent);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        JLabel message= new JLabel();
        message.setText("Do you want " + note +"?" );
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);
        message.setFont(new Font("Courier New", Font.BOLD, 20));

        okButton = new MyButton("yes");
        noButton = new MyButton("no");
        JPanel button = new JPanel();
        button.add(okButton);
        button.add(noButton);
        button.setOpaque(false);
        button.setBackground(Color.WHITE);
        
        getContentPane().add(button,BorderLayout.SOUTH);
        getContentPane().add(message);
        noButton.addActionListener(e -> setVisible(false));
    }
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("BOMB GAME");
        RemindDialog a = new RemindDialog(new UserList(), window,"save game");
        window.setSize(576, 576);
        a.setVisible(true);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
       // window.setVisible(true);
    }
}
