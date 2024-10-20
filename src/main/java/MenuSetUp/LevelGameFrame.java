package MenuSetUp;

import javax.swing.JFrame;

import main.GamePanel;

public class LevelGameFrame extends JFrame {
    public GamePanel gamePanel;
    public int lv;
    public LevelPanel levelPanel;
    public LevelGameFrame(int lv ,LevelPanel levelPanel) {
        new JFrame();
        this.lv = lv;
        this.levelPanel = levelPanel;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("BOMB GAME");
        gamePanel = new GamePanel(mapFileNameToString(lv), this, levelPanel);
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(false);
        gamePanel.startGameThread();
    }
    public LevelGameFrame(int lv ,LevelPanel levelPanel, GamePanel gamePanel){
        new JFrame();
        this.lv = lv;
        this.levelPanel = levelPanel;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("BOMB GAME");
        this.gamePanel = gamePanel;
        add(this.gamePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(false);

    }
    public String mapFileNameToString(int lv){
        return "level_"+lv;
    }
}
