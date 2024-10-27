package MenuDialog;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import MenuSetUp.MyButton;

public class InstructionDialog extends SuperDialog {

    public InstructionDialog(JFrame parent) {
        super(parent);
        setTitle("INSTRUCTION");

        setContent();
        MyButton back = new MyButton("back");
        back.setLocateButton((500 - 50) / 2, 400);
        getContentPane().add(back);
        back.addActionListener(e -> {
            parent.setEnabled(true);
            setVisible(false);
        });

        setBackground();
    }

    void setContent() {
        JTextArea content = new JTextArea();
        content.setSize(350, 325);
        content.setLocation(75, 75);
        content.setEditable(false);
        content.setLineWrap(true);//DISPLAY MULTIPLE LINE
        content.setOpaque(false);
        getContentPane().add(content);
    }

}
