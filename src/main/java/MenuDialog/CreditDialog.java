package MenuDialog;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import MenuSetUp.MyButton;

public class CreditDialog extends SuperDialog {
    public CreditDialog( JFrame parent){
        super(parent);
        setTitle("CREDIT");

        setContent();
        
        MyButton back = new MyButton("back");
        back.setLocateButton((500 - 50) / 2, 425);
        getContentPane().add(back);
        back.addActionListener(e -> {
            parent.setEnabled(true);
            setVisible(false);
        });

        setBackground();
    }
    void setContent(){
        JTextArea content = new JTextArea();
        content.setSize(350  , 325);
        content.setLocation(75, 75);
        content.setEditable(false);
        content.setLineWrap(true);//DISPLAY MULTIPLE LINE
        content.setOpaque(false);
        getContentPane().add(content);

    }

    public static void main(String[] args) {
        JFrame winFrame = new JFrame();
        CreditDialog a = new CreditDialog(winFrame);
        a.setVisible(true);
    }
}
