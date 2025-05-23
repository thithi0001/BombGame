package MenuDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import MenuSetUp.MyButton;
import res.LoadResource;

public class NoticeDialog extends JDialog {
    public MyButton okButton;

    public NoticeDialog(JFrame parent) {

        super(parent);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);

        addMessage();

        okButton = new MyButton("yes");
        getContentPane().add(okButton, BorderLayout.SOUTH);
    }

    void addMessage() {
        JLabel message = new JLabel();
        message.setText("Please enter Player's name ");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);
        message.setFont(LoadResource.dialogContent);
        getContentPane().add(message);
    }
}
