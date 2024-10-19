package MenuDialog;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import MenuSetUp.MyButton;


public class NewGameDialog extends SuperDialog {
    public JTextField textField;
    public MyButton okButton;

    public NewGameDialog(JFrame parent) {
        super(parent);

        // BUTTON
        okButton = new MyButton("yes");
        MyButton cancelButton = new MyButton("no");


        // Thêm các thành phần vào dialog
        setLabel();
        addButton(okButton, cancelButton);
        setBackground();

        //BUTTON ACTION
        cancelButton.addActionListener(e -> {
            setVisible(false);
            textField.setText("");
        });
    }

    void setLabel() {
        JLabel label = new JLabel("Enter name :");
        label.setFont(new Font("Courier New", Font.BOLD, 20));
        label.setSize(150, 30);
        label.setLocation((500 - 350) / 2, 180);

        textField = new JTextField();
        textField.setSize(200, 30);
        textField.setLocation((500 - 350) / 2 + 150, 180);
        textField.setFont(new Font("Courier New", Font.BOLD, 20));
        textField.setOpaque(true);
        textField.setBorder(null);

        JPanel textPanel = new JPanel(null);
        textPanel.setSize(500, 520);
        textPanel.setLocation(0, 0);
        textPanel.setOpaque(false);
        textPanel.add(label);
        textPanel.add(textField);

        getContentPane().add(textPanel);
    }
}
