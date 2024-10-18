package MenuSetUp;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import main.Main;

public class SettingPanel extends JPanel {
    MyButton back;

    public SettingPanel(){
        MySlider musicSlider;
        MySlider SESlider;
        setFocusable(false);
        setLayout(null);
        //TITLE PANEL
        JLabel setting = new JLabel("SETTING");
        setting.setSize(235, 100);
        setting.setFont(new Font("Courier New", Font.BOLD, 38));
        setting.setLocation((DimensionSize.screenWidth - 150)/2, 20);
        add(setting);

        //BUTTON BACK
        back = new MyButton("back");
        back.setLocateButton(10, 10);
        add(back);

        //SET SOUND
        Sound music= new Sound("Music");
        music.play();

        //SLIDER MUSIC
        musicSlider = new MySlider(250, 25, "music");
        musicSlider.setLocateMySlider((DimensionSize.screenWidth-250)/2, ((DimensionSize.maxScreenRow - 4)/2)* DimensionSize.tileSize);
        add(musicSlider.slider);
        add(musicSlider.nameSlider);
        musicSlider.slider.addChangeListener((e) -> {
            music.musicVolume = musicSlider.slider.getValue();
            music.controlMusic.setValue(music.musicVolume);
        });

        //SLIDER SOUND EFFECT
        SESlider = new MySlider(250, 25,"SE");
        SESlider.setLocateMySlider((DimensionSize.screenWidth - 250)/2, ((DimensionSize.maxScreenRow - 4)/2 + 1) * DimensionSize.tileSize);
        add(SESlider.slider);
        add(SESlider.nameSlider);
        SESlider.slider.addChangeListener((e) -> {
            Sound.SEVolume = SESlider.slider.getValue();
        });

        //BUTTON MUSIC AND SE
        MyButton seButton = new MyButton("sound");
        seButton.setLocateButton(DimensionSize.screenWidth/2 + 50, ((DimensionSize.maxScreenRow - 4)/2 + 3)* DimensionSize.tileSize);
        add(seButton);
        seButton.addActionListener((e) -> {
            Sound.SE.set(!Sound.SE.get());
            seButton.setIcon(Sound.SE.get() ? new ImageIcon(Main.res+"/button/soundButton.png")
                                            :new ImageIcon(Main.res+"/button/soundOffButton.png") );
            SESlider.slider.setEnabled(Sound.SE.get());
        });

        MyButton musicButton =new MyButton("music");
        musicButton.setLocateButton(DimensionSize.screenWidth/2 - 100, ((DimensionSize.maxScreenRow - 4)/2 +3)* DimensionSize.tileSize);
        musicButton.addActionListener((e) -> {
            music.Music.set(!music.Music.get());
            musicButton.setIcon(music.Music.get() ? new ImageIcon(Main.res+"/button/musicButton.png")
                                                  : new ImageIcon(Main.res+"/button/musicOffButton.png") );
            music.checkVolume();
            musicSlider.slider.setEnabled(music.Music.get());
        });
        add(musicButton);
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
            e.printStackTrace();
        }
       
     }
}
