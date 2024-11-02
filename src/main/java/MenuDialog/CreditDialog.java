package MenuDialog;

import MenuSetUp.MyButton;
import res.LoadResource;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class CreditDialog extends SuperDialog {

    String[] creditsContent = {"""
                    Programmer
                    ----------
                    Le Dinh Thi - N22DCCN179
                    Nguyen Van Dang Quang - N22DCCN162
                    Nguyen Phuc Thinh - N22DCCN182       
                    """, """
                    Pixel arts
                    ----------
                    SOULSAGA777
                    tanledesigner
                    ArtistSaif
                    thithi0001
                    lighten000
                    """, """
                    Images
                    ----------
                    Mixed Sketches
                    Heorhii Aryshtevych
                    pizrex
                    Belebella
                    Quasont
                    """, """
                    Music
                    ----------
                    "Funny BGM" - Sekuora
                    
                    Sounds effect
                    ----------
                    "coin received" - RibhavAgrawal
                    "small explosion" - DennisH18
                    """};

    MyButton left, right;
    int currentCredit = 0;

    public CreditDialog(JFrame parent) {
        super(parent);
        setTitle("CREDITS");

        JTextPane content = new JTextPane();
        setContent(content);

        MyButton back = new MyButton("back");
        back.setLocateButton((500 - 50) / 2, 400);
        getContentPane().add(back);
        back.addActionListener(e -> {
            parent.setEnabled(true);
            setVisible(false);
        });

        left = new MyButton("brightLeft");
        left.setLocateButton(30, 400);
        left.addActionListener(e -> {
            toPreviousCredit();
            content.setText(creditsContent[currentCredit]);
        });

        right = new MyButton("brightRight");
        right.setLocateButton(500 - right.icon.getIconWidth() - 30, 400);
        right.addActionListener(e -> {
            toNextCredit();
            content.setText(creditsContent[currentCredit]);
        });

        getContentPane().add(left);
        getContentPane().add(right);

        setBackground();
    }

    void toNextCredit() {
        currentCredit = (currentCredit == creditsContent.length - 1) ? currentCredit : currentCredit + 1;
    }

    void toPreviousCredit() {
        currentCredit = (currentCredit == 0) ? currentCredit : currentCredit - 1;
    }

    void setContent(JTextPane content) {

        StyledDocument doc = content.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), style, false);

        content.setSize(450, 325);
        content.setLocation(30, 75);
        content.setEditable(false);
        content.setOpaque(false);
        content.setSelectedTextColor(Color.RED);
        content.setSelectionColor(Color.PINK);
        content.setFont(LoadResource.informationFont);

        getContentPane().add(content);
        content.setText(creditsContent[currentCredit]);
    }
}
