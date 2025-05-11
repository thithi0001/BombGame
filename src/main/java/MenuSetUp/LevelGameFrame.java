package MenuSetUp;

import javax.swing.JFrame;

import main.GamePanel;

public class LevelGameFrame extends JFrame {
    public GamePanel gamePanel;
    public int lv;
    public LevelPanel levelPanel;
    public String mode;

    public LevelGameFrame(int lv, LevelPanel levelPanel, String mode) {
        new JFrame();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("BOMB GAME");

        this.lv = lv;
        this.levelPanel = levelPanel;
        this.mode = mode;
        gamePanel = new GamePanel(mapFileNameToString(lv), this, levelPanel, mode);
        add(gamePanel);
        pack();

        setLocationRelativeTo(null);
        setVisible(false);

        gamePanel.startGameThread();
    }

    public LevelGameFrame(int lv, LevelPanel levelPanel, GamePanel oldGamePanel, String mode) {
        new JFrame();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("BOMB GAME");

        this.lv = lv;
        this.levelPanel = levelPanel;
        this.mode = mode;
        oldGamePanel.setParentFrame(this);
        this.gamePanel = oldGamePanel;
        add(this.gamePanel);
        pack();

        setLocationRelativeTo(null);
        setVisible(false);
    }

    public String mapFileNameToString(int lv) {
        return "level_" + lv;
    }
}
