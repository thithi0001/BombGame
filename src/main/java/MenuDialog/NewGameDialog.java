package MenuDialog;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import MenuSetUp.MyButton;
import res.LoadResource;


public class NewGameDialog extends SuperDialog {
    public JTextField textField;
    public MyButton okButton;
    public MyButton cancelButton;

    public NewGameDialog(JFrame parent) {
        super(parent);

        // BUTTON
        okButton = new MyButton("yes");
        cancelButton = new MyButton("no");

        setTitle("NEW GAME");
        setLabel();
        addButton(okButton, cancelButton);
        setBackground();
    }

    void setLabel() {
        JLabel label = new JLabel("Enter name :");
        label.setFont(LoadResource.dialogContent);
        label.setSize(150, 30);
        label.setLocation((500 - 350) / 2, 180);

        textField = new JTextField();
        textField.setSize(200, 30);
        textField.setLocation((500 - 350) / 2 + 150, 180);
        textField.setFont(LoadResource.dialogContent);
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
