package MenuSetUp;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import main.Main;

public class SettingPanel extends JPanel {
    MyButton back;
    Sound music;
    MySlider musicSlider;
    MySlider SESlider;

    public SettingPanel(Sound music) {

        this.music = music;
        setFocusable(false);
        setLayout(null);


        //TITLE PANEL
        JLabel setting = new JLabel("SETTING");
        setting.setSize(235, 100);
        setting.setFont(new Font("Courier New", Font.BOLD, 38));
        setting.setLocation((DimensionSize.screenWidth - 150) / 2, 20);
        add(setting);

        //BUTTON BACK
        back = new MyButton("back");
        back.setLocateButton(10, 10);
        add(back);

        //SET SOUND
        // music.stopMusic();

        //SLIDER MUSIC
        musicSlider = new MySlider(250, 25, (int) music.musicVolume, "music");
        musicSlider.setLocateMySlider((DimensionSize.screenWidth - 250) / 2
                , ((DimensionSize.maxScreenRow - 4) / 2) * DimensionSize.tileSize);
        musicSlider.addMySlider(this);

        musicSlider.slider.addChangeListener((e) -> {
            music.musicVolume = musicSlider.slider.getValue();
            music.controlMusic.setValue(music.musicVolume);
        });

        //SLIDER SOUND EFFECT
        SESlider = new MySlider(250, 25, (int) Sound.SEVolume, "SE");
        SESlider.setLocateMySlider((DimensionSize.screenWidth - 250) / 2
                , ((DimensionSize.maxScreenRow - 4) / 2 + 1) * DimensionSize.tileSize);
        SESlider.addMySlider(this);

        SESlider.slider.addChangeListener((e) -> {
            Sound.SEVolume = SESlider.slider.getValue();
        });


        //BUTTON MUSIC AND SE
        addMusicButton();
        addSEButton();

    }

    void addMusicButton() {
        MyButton musicButton = music.Music ? new MyButton("music") : new MyButton("musicOff");
        musicButton.setLocateButton(DimensionSize.screenWidth / 2 - 100
                , ((DimensionSize.maxScreenRow - 4) / 2 + 3) * DimensionSize.tileSize);
        add(musicButton);
        musicButton.addActionListener((e) -> {
            music.Music = !music.Music;
            //set icon
            musicButton.setIcon(music.Music ? new ImageIcon(Main.res + "/button/musicButton.png")
                    : new ImageIcon(Main.res + "/button/musicOffButton.png"));
            music.checkVolume();

            //enabled slider(làm slider có hoặc khong thể di chuyển)
            musicSlider.slider.setEnabled(music.Music);
        });
    }


    void addSEButton() {
        MyButton seButton = Sound.SE ? new MyButton("sound") : new MyButton("soundOff");
        seButton.setLocateButton(DimensionSize.screenWidth / 2 + 50
                , ((DimensionSize.maxScreenRow - 4) / 2 + 3) * DimensionSize.tileSize);
        add(seButton);
        seButton.addActionListener((e) -> {
            Sound.SE = !Sound.SE;
            //set icon
            seButton.setIcon(Sound.SE ? new ImageIcon(Main.res + "/button/soundButton.png")
                    : new ImageIcon(Main.res + "/button/soundOffButton.png"));

            //enabled slider(làm slider có hoặc khong thể di chuyển)
            SESlider.slider.setEnabled(Sound.SE);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background;
        try {
            File a = new File(Main.res + "/background/background.png");
            background = ImageIO.read(a);
            g.drawImage(background, 0, 0, DimensionSize.screenWidth, DimensionSize.screenHeight, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
