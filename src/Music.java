import javax.sound.sampled.*;
import java.io.File;

public class Music {

    // Make this static so the whole game shares one audio player
    private static Clip clip;

    public static void playMusic(String location) {
        // 1. STOP whatever is currently playing before starting new music
        stopMusic();

        new Thread(() -> {
            try {
                File musicPath = new File(location);
                if (musicPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);

                    // assign to the static variable
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    System.out.println("Can't find file: " + location);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void stopMusic() {
        // Check if clip exists and is actually running
        if (clip != null && clip.isOpen()) {
            clip.stop();
            clip.close(); // Important to free up memory
        }
    }
}