package MenuDialog;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import MenuSetUp.MyButton;

import java.awt.*;

import res.LoadResource;

public class RemindDialog extends JDialog {
    public MyButton okButton;
    public MyButton noButton;

    public RemindDialog(JFrame parent, String note) {
        //create new dialog
        super(parent);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //add 
        okButton = new MyButton("yes");
        noButton = new MyButton("no");
        addButton(okButton, noButton);
        addMessage(note);
    }

    void addButton(MyButton okButton, MyButton noButton) {
        JPanel button = new JPanel();
        button.add(okButton);
        button.add(noButton);
        button.setOpaque(false);

        getContentPane().add(button, BorderLayout.SOUTH);
    }

    void addMessage(String note) {
        JLabel message = new JLabel();
        message.setText("Do you want " + note + "?");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);
        message.setFont(LoadResource.dialogContent);
        getContentPane().add(message);
    }
}
