package MenuDialog;

import javax.swing.*;

import MenuSetUp.MyButton;
import res.LoadResource;

import java.awt.*;

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
        content.setLocation(35, 75);
        content.setEditable(false);
        content.setLineWrap(true);//DISPLAY MULTIPLE LINE
        content.setOpaque(false);
        getContentPane().add(content);
        content.setSelectedTextColor(Color.RED);
        content.setSelectionColor(Color.PINK);
        content.setFont(LoadResource.textDialogContent);
        content.setText("""
                CONTROL:
                + WASD or ARROW keys: Move
                + ENTER: Place bomb
                + R or SPACE: Activate time
                bomb
                -------------------------------
                RULE:
                To gain your victory
                + Eliminate all monsters in map
                + Don't touch any monster
                + Don't get hit by bomb
                """);
    }

}
