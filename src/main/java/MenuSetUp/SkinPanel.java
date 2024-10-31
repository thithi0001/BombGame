package MenuSetUp;

import res.LoadResource;

import javax.swing.*;

import java.awt.*;

import static MenuSetUp.DimensionSize.screenHeight;
import static MenuSetUp.DimensionSize.screenWidth;

public class SkinPanel extends JPanel {
    MyButton left, right, back;
    private String currentCharacter = "White-guy";
    private int currentCharacterIndex = 2;

    public SkinPanel() {
        setLayout(null);
        setCharacter();

        back = new MyButton("back");
        back.setLocateButton(10, 10);
        add(back);

        left = new MyButton("darkLeft");
        left.setLocateButton(150, (screenHeight - left.icon.getIconHeight()) / 2);
        left.addActionListener(_ -> {
            toPreviousCharacter();
            repaint();
        });
        add(left);

        right = new MyButton("darkRight");
        right.setLocateButton(screenWidth - right.icon.getIconWidth() - 150,
                (screenHeight - right.icon.getIconHeight()) / 2);
        right.addActionListener(_ -> {
            toNextCharacter();
            repaint();
        });
        add(right);

    }

    private void setCharacter() {
        LoadResource.character = currentCharacter;
        LoadResource.characterIndex = currentCharacterIndex;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(LoadResource.background, 0, 0, screenWidth, screenHeight, null);

        String name = currentCharacter.replace('-', ' ');
        g.setColor(Color.BLACK);
        g.setFont(LoadResource.instructionContent);
//        g.drawString("|", screenWidth / 2, 80);
        g.drawString(name, screenWidth / 2 - name.length() * 6, 100);

        int imgSize = 128;
        g.drawImage(LoadResource.charactersSprites[currentCharacterIndex].down[0], (screenWidth - imgSize) / 2, 200, imgSize, imgSize, null);
    }

    void toNextCharacter() {
        currentCharacterIndex++;
        if (currentCharacterIndex == LoadResource.charactersSprites.length)
            currentCharacterIndex = 0;
        currentCharacter = LoadResource.charactersSprites[currentCharacterIndex].getCharacter();
        setCharacter();
    }

    void toPreviousCharacter() {
        currentCharacterIndex--;
        if (currentCharacterIndex < 0)
            currentCharacterIndex = LoadResource.charactersSprites.length - 1;
        currentCharacter = LoadResource.charactersSprites[currentCharacterIndex].getCharacter();
        setCharacter();
    }

    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(DimensionSize.screenWidth, DimensionSize.screenHeight);
        window.setResizable(false);
        window.setTitle("TEST");

        SkinPanel skin = new SkinPanel();
        window.add(skin);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
