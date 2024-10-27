package MenuDialog;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import MenuSetUp.MyButton;
import res.LoadResource;

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
        content.setSize(450, 325);
        content.setLocation(45, 75);
        content.setEditable(false);
        content.setLineWrap(true);//DISPLAY MULTIPLE LINE
        content.setOpaque(false);
        getContentPane().add(content);
        content.setFont(LoadResource.instructionContent);
        content.setText("""
                CONTROL:
                + W: Go up
                + A: Go left
                + S: Go down
                + D: Go right
                + R: Activate time bomb
                ------------------------------
                RULE:
                + Eliminate all monster in map
                + Don't touch any monster
                + Don't get hit by bomb
                
                """);
    }

}
