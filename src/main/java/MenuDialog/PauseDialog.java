package MenuDialog;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import MenuSetUp.LevelGameFrame;
import MenuSetUp.MyButton;

public class PauseDialog extends SuperDialog {

    public PauseDialog(LevelGameFrame parent) {
        super(parent);
        setTitle("PAUSE");

        MyButton resume = new MyButton("resume");
        resume.addActionListener(e -> {
            setVisible(false);
            parent.gamePanel.isPausing = false;
            LevelGameFrame newParent = new LevelGameFrame(parent.lv, parent.levelPanel, parent.gamePanel);

            parent.setVisible(false);
            newParent.setVisible(true);
        });


        MyButton restart = new MyButton("bigRestart");
        restart.addActionListener(e -> {
            setVisible(false);
            parent.setVisible(false);
            parent.gamePanel.setGameThread(null);
            LevelGameFrame newParent = new LevelGameFrame(parent.lv, parent.levelPanel);
            newParent.setVisible(true);
        });

        MyButton exit = new MyButton("exit");
        exit.addActionListener(e -> {
            parent.setVisible(false);
            parent.gamePanel.setGameThread(null);
            parent.levelPanel.change.frame.setVisible(true);
        });
        MyButton setting = new MyButton("setting");
        setting.addActionListener((e) -> {
            SettingDialog settingDialog = new SettingDialog(parent, parent.levelPanel.change);
            settingDialog.setVisible(true);
        });
        Content(resume, exit, setting, restart);

        setBackground();
    }

    void Content(JButton resume, JButton exit,JButton setting ,JButton restart) {
        JPanel content = new JPanel();
        content.setSize(150, 250);
        content.setLocation((500 - 150) / 2, 125);
        content.setOpaque(false);
        content.setLayout(new GridLayout(4, 1));
        content.add(resume);
        content.add(restart);
        content.add(exit);
        content.add(setting);
        getContentPane().add(content);
    }

}
