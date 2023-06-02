import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {

    public static synchronized void playGameOverSound() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("resources/game_over.wav").getAbsoluteFile());
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception exc) {
                    System.err.println(exc.getMessage());
                }

            }

        }).start();

    }

    public static synchronized void playChompSound() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("resources/chomp.wav").getAbsoluteFile());
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception exc) {
                    System.err.println(exc.getMessage());
                }

            }

        }).start();

    }
    
}
