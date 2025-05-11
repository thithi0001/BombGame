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
        resume.addActionListener(_ -> {  
            parent.gamePanel.isPausing = false;
            LevelGameFrame newParent = new LevelGameFrame(parent.lv, parent.levelPanel, parent.gamePanel, parent.mode);
            setVisible(false);
            parent.setVisible(false);
            newParent.setVisible(true);
        });


        MyButton restart = new MyButton("bigRestart");
        restart.addActionListener(_ -> {
            setVisible(false);
            parent.setVisible(false);
            parent.gamePanel.setGameThread(null);
            LevelGameFrame newParent = new LevelGameFrame(parent.lv, parent.levelPanel, parent.mode);
            newParent.setVisible(true);
        });

        MyButton exit = new MyButton("exit");
        exit.addActionListener(_ -> {
            parent.setVisible(false);
            parent.gamePanel.setGameThread(null);
            parent.levelPanel.change.frame.setVisible(true);
        });

        MyButton setting = new MyButton("setting");
        setting.addActionListener(_ -> {
            setEnabled(false);
            SettingDialog settingDialog = new SettingDialog(parent, parent.levelPanel.change);
            settingDialog.setVisible(true);
            settingDialog.back.addActionListener(_ -> setEnabled(true));
        });
        Content(resume, exit, setting, restart);

        setBackground();
    }

    void Content(JButton resume, JButton exit, JButton setting, JButton restart) {
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
