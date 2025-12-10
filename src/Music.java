import javax.sound.sampled.*;
import java.io.File;

public class Music {
    private static int currentVolumeLevel = 100;

    private static Clip clip;

    public static void playMusic(String location) {
        stopMusic();

        new Thread(() -> {
            try {
                File musicPath = new File(location);
                if (musicPath.exists()) {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);

                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);

                    setVolume(currentVolumeLevel);

                } else {
                    System.out.println("Can't find file: " + location);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void stopMusic() {
        if (clip != null && clip.isOpen()) {
            clip.stop();
            clip.close();
        }
    }


    public static void setVolume(int level) {
        currentVolumeLevel = level;
        if (clip == null) return;

        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        float min = gain.getMinimum();
        float max = gain.getMaximum();
        float dB = min + (max - min) * (level / 100f);
        gain.setValue(dB);
    }

}
