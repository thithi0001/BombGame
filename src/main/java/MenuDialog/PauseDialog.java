package MenuDialog;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import MenuSetUp.LevelGameFrame;
import main.GamePanel;


public class PauseDialog extends SuperDialog {
    public JButton resume;

    public PauseDialog(LevelGameFrame parent) {
        super(parent);
        setTitle("PAUSE");

        resume = new JButton("resume");
        resume.addActionListener(e -> {
            setVisible(false);
            parent.gamePanel.state = GamePanel.States.playing;
            LevelGameFrame newParent = new LevelGameFrame(parent.lv, parent.levelPanel, parent.gamePanel);
            // newParent.gamePanel.setParentFrame(newParent);
            newParent.setTitle("new Parent");
            
            parent.setVisible(false);
            newParent.setVisible(true);
        });

        JButton restart = new JButton("restart");
        restart.addActionListener(e -> {
            setVisible(false);
            parent.setVisible(false);
            parent.gamePanel.gameThread = null;
            LevelGameFrame newParent = new LevelGameFrame(parent.lv, parent.levelPanel); 
            newParent.setVisible(true);
            // newParent.gamePanel.startGameThread();
        });

        JButton quit = new JButton("quit");
        quit.addActionListener(e -> {
            parent.setVisible(false);
            parent.levelPanel.change.frame.setVisible(true);
        });
        Content(resume, quit, restart);

        setBackground();
    }

    void Content(JButton resume, JButton quit, JButton restart) {
        JPanel content = new JPanel();
        content.setSize(150, 250);
        content.setLocation((500 - 150) / 2, 150);
        content.setOpaque(false);
        content.setLayout(new GridLayout(3, 1));
        content.add(resume);
        content.add(restart);
        content.add(quit);
        getContentPane().add(content);
    }

}
