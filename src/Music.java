import javax.sound.sampled.*;
import java.io.File;

public class Music {

    private Clip clip;

    public void playMusic(String location) {
        new Thread(() -> {
            try {
                File musicPath = new File(location);
                if (musicPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    System.out.println("Can't find file");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}