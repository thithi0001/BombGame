package main;

import javax.swing.JFrame;

import MenuSetUp.ChangePanel;
import java.io.File;

public class Main {

    public static String res = new File("..src\\main\\java\\res").getAbsolutePath().replace("..","");
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(576, 576);
        window.setResizable(false);
        window.setTitle("BOMB GAME");

        // GamePanel gamePanel = new GamePanel();
        // window.add(gamePanel);
        // window.pack();
        ChangePanel change = new ChangePanel(window);
        change.actionChange();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        //gamePanel.startGameThread();
    }
}