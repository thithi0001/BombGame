package MenuSetUp;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import main.Main;

public class Sound {
    Clip clip;
    File file;
    FloatControl controlMusic;
    FloatControl controlSE;
    String name;
    float musicVolume = 0;
    public static float SEVolume = 0;
    public static AtomicBoolean SE = new AtomicBoolean(true);
    AtomicBoolean Music = new AtomicBoolean(true);


    public Sound(String a){
        name = a;
        file = new File(Main.res+ nameFile(a));
        this.setFile();
        if(name.equals("Music"))
            controlMusic = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        else
            controlSE = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        checkVolume();
    }

    public void setFile(){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            this.clip = AudioSystem.getClip();
            this.clip.open(ais);
        } catch (Exception e) {        
        }
        
    } 
    public void play(){
        checkVolume();
        clip.start();
    }
    public void loop(){
        clip.loop(-1);
    }
    public void stopMusic(boolean a){
            clip.stop();
    }
    public String nameFile(String a){
        return "/sound/"+a+".wav";
    }
    
    public void checkVolume(){
        if(name.equals("Music")){
            if(Music.get() == true){
                controlMusic.setValue(musicVolume);
                clip.start();
            }
            else clip.stop();
        }
        else{
            if(SE.get() == true)controlSE.setValue(SEVolume);
            else controlSE.setValue(-80);
        }
    }
}