package MenuDialog;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import MenuSetUp.ChangePanel;
import MenuSetUp.MyButton;
import MenuSetUp.MySlider;
import MenuSetUp.Sound;
import res.LoadResource;

public class SettingDialog extends SuperDialog {
    MySlider musicSlider;
    MySlider SESlider;
    public MyButton back;
    public SettingDialog(JFrame parent, ChangePanel change) {
        super(parent);
        setTitle("SETTING");
        //SET BACK BUTTON
        back = new MyButton("back");
        back.setLocateButton((500 - 50) / 2, 400);
        getContentPane().add(back);
        back.addActionListener(e -> {
            change.music.saveSetting(change.music);
            setVisible(false);
        });

        setContent(change.music);

        setBackground();
    }

    void setContent(Sound music) {
        JPanel content = new JPanel();
        content.setSize(300, 250);
        content.setLocation(100, 100);
        content.setOpaque(false);
        content.setLayout(new GridLayout(3, 2));

        //SLIDER MUSIC
        musicSlider = new MySlider(250, 25, (int) music.musicVolume, "music");
        musicSlider.addMySlider(content);
        musicSlider.slider.setEnabled(music.Music);
        musicSlider.slider.addChangeListener((e) -> {
            music.musicVolume = musicSlider.slider.getValue();
            music.controlMusic.setValue(music.musicVolume);
        });

        //SLIDER SOUND EFFECT
        SESlider = new MySlider(250, 25, (int) Sound.SEVolume, "SE");
        SESlider.addMySlider(content);
        SESlider.slider.setEnabled(Sound.SE);

        SESlider.slider.addChangeListener((e) -> {
            Sound.SEVolume = SESlider.slider.getValue();
        });

        addMusicButton(content, music);

        addSEButton(content);

        getContentPane().add(content);
    }

    void addMusicButton(JPanel content, Sound music) {
        MyButton musicButton = music.Music ? new MyButton("music") : new MyButton("musicOff");
        content.add(musicButton);
        musicButton.addActionListener((e) -> {
            music.Music = !music.Music;
            music.checkVolume();

            //set icon
            musicButton.icon = music.Music ? LoadResource.musicOnBtnIcon : LoadResource.musicOffBtnIcon;

            //slider unmoved
            musicSlider.slider.setEnabled(music.Music);
        });
    }

    void addSEButton(JPanel content) {

        MyButton seButton = Sound.SE ? new MyButton("sound") : new MyButton("soundOff");
        content.add(seButton);
        seButton.addActionListener((e) -> {
            Sound.SE = !Sound.SE;
            //set icon
            seButton.icon = Sound.SE ? LoadResource.soundOnBtnIcon : LoadResource.soundOffBtnIcon;

            //slider unmoved
            SESlider.slider.setEnabled(Sound.SE);
        });
    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        ChangePanel change = new ChangePanel(window);
        SettingDialog a = new SettingDialog(window, change);
        a.setVisible(true);
    }
}
