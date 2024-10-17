package MenuSetUp;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import MenuDialog.RemindDialog;
import main.Main;
import userClass.User;
import userClass.UserList;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Font;

public class LevelPanel extends JPanel {
    MyButton back;
    MyButton setting;
    User user;
    CardLayout cardLayout;
    Container contentPane;
    MyButton level[]= new MyButton[3];
    public LevelPanel(User currentUser, CardLayout cardlayout, Container contentPane, JFrame frame, UserList list){
        if(currentUser != null){
            user = currentUser;
        }
        else user = new User();
        back =new MyButton("back");
        back.setLocateButton(10, 10);
        setting =new MyButton("setting");
        setting.setLocateButton(DimensionSize.screenWidth - 60, 10);
        setFocusable(false);
        setLayout(null);
        add(back);
        add(setting);

        for(int i = 0 ; i<3 ; i++ ){
            String x ="level" +(i+1);
            if(i+1 <= user.getLevel())level[i] = new MyButton(x);
            else level[i] = new MyButton("levelBlock");
            level[i].setLocateButton(((DimensionSize.maxScreenCol -5)/2 + i*2)*DimensionSize.tileSize, DimensionSize.screenHeight/2);
            add(level[i]);
        }

        JLabel lv = new JLabel("LEVEL");
        lv.setSize(200, 100);
        lv.setFont(new Font("Courier New", Font.BOLD, 38));
        lv.setLocation((DimensionSize.screenWidth - 130)/2, 20);
        add(lv);
        back.addActionListener((event) -> { 
            RemindDialog saveGame = new RemindDialog(list, frame, "save game");
            saveGame.setVisible(true);
            saveGame.okButton.addActionListener(e ->{
            list.addUser(currentUser);
            list.saveGame();
            cardlayout.show(contentPane,"Panel 2");
            saveGame.setVisible(false);
            });
            saveGame.noButton.addActionListener(e -> cardlayout.show(contentPane,"Panel 2"));
        });
        setting.addActionListener((event) -> cardlayout.show(contentPane,"Panel 4"));
        for (int i = 0; i < 3; i++) {
            if(i < user.getLevel()){
                level[i].addActionListener(e -> System.out.println("ok"));
            }
        }
    }
    public void setUser(User currentUser){
        this.user = currentUser;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background;
        try {
            File a =new File(Main.res + "/background/background.png");
            background= ImageIO.read(a);
            g.drawImage(background, 0, 0, DimensionSize.screenWidth , DimensionSize.screenHeight, null);
        } catch (Exception e) {
            
        } 
    }
}
