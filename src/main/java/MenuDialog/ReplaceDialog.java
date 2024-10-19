package MenuDialog;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.*;

import MenuSetUp.MyButton;

public class ReplaceDialog extends JDialog {
    public MyButton okButton;
    public MyButton cancelButton;

    public ReplaceDialog(JFrame parent) {
        super(parent);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);

        addMessage();

        okButton = new MyButton("yes");
        cancelButton = new MyButton("no");
        addButton(okButton, cancelButton);

        cancelButton.addActionListener(e -> setVisible(false));
        okButton.addActionListener(e -> setVisible(false));
    }

    void addMessage() {
        JLabel message = new JLabel();
        message.setText("<html>Player's name has existed.<br>Do you want to replace it?</html>");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);
        message.setFont(new Font("Courier New", Font.BOLD, 20));
        getContentPane().add(message);
    }

    void addButton(MyButton okButton, MyButton noButton) {
        JPanel button = new JPanel();
        button.add(okButton);
        button.add(noButton);
        button.setOpaque(false);

        getContentPane().add(button, BorderLayout.SOUTH);
    }
}
