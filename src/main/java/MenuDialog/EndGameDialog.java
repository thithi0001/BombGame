package MenuDialog;

import java.awt.Font;
import javax.swing.*;

import MenuSetUp.ChangePanel;
import MenuSetUp.LevelGameFrame;
import MenuSetUp.MyButton;
import main.GamePanel;
import res.LoadResource;

public class EndGameDialog extends SuperDialog {
    MyButton restart, quit;

    public EndGameDialog(String title, LevelGameFrame parent, int score, String time) {
        super(parent);
        setTitle(title);
        parent.gamePanel.setGameThread(null);
        
        setRestartBtn(parent);
        setQuitBtn(parent, parent.levelPanel.change);

        addButton(restart, quit);

        String b = String.format("%2d", score);

        getContentPane().add(setLabel("YOUR SCORE", b, 150));
        getContentPane().add(setLabel("TIME", time, 220));

        setBackground();
    }

    public EndGameDialog(String title, GamePanel gp) {
        super(gp.parent);

        LevelGameFrame parent = gp.parent;
        int score = gp.player.score;
        String time = gp.clock.toString();
        ChangePanel change = gp.levelPanel.change;

        setTitle(title);
        parent.gamePanel.setGameThread(null);

        // setNextBtn(parent);
        setRestartBtn(parent);
        setQuitBtn(parent, change);

        addButton(restart, quit);

        String b = String.format("%2d", score);

        getContentPane().add(setLabel("YOUR SCORE", b, 150));
        getContentPane().add(setLabel("TIME", time, 220));

        setBackground();
    }

    void setRestartBtn(LevelGameFrame parent) {
        restart = new MyButton("restart");
        restart.addActionListener(_ -> {
            setVisible(false);
            parent.setVisible(false);
            parent.gamePanel.setGameThread(null);
            LevelGameFrame newParent = new LevelGameFrame(parent.lv, parent.levelPanel, parent.mode);
            newParent.setVisible(true);
        });
    }

    void setQuitBtn(LevelGameFrame parent, ChangePanel change) {
        quit = new MyButton("level");
        quit.addActionListener(_ -> {
            
            parent.gamePanel.setGameThread(null);
            parent.levelPanel.change.frame.setVisible(true);
            parent.levelPanel.change.frame.setEnabled(true);    
            parent.setVisible(false);
        });
    }

    JPanel setLabel(String name, String value, int y) {
        //Panel
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(null);
        labelPanel.setSize(500, 520);
        labelPanel.setLocation(10, 0);

        Font font = LoadResource.dialogContent;
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(font);
        nameLabel.setSize(150, 30);
        nameLabel.setLocation((500 - 350) / 2 + 10, y);
        labelPanel.add(nameLabel);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(font);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setSize(200, 30);
        valueLabel.setLocation((500 - 350) / 2 + 150, y);
        labelPanel.add(valueLabel);

        return labelPanel;
    }
}
