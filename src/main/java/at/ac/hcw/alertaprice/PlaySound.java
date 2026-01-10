package at.ac.hcw.alertaprice;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

// inspired by https://www.youtube.com/watch?v=-GhCdekgNNM

public class PlaySound {
    public static File sound; //Verweis, enthält selber keinen Ton
    Clip clip; //braucht man in playSound zum Abspielen -> Audio-Abspielobjekt


    public void loadSound(String path){
        sound = new File(path);
    }

    public void playSound(String path){
        loadSound(path);

        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace(); //gibt Liste der Methodenaufrufe, die zu Fehler führten aus
        }
    }
}
