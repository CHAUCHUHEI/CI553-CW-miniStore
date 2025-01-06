package clients.cashier;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for playing sound effects.
 */
public class SoundPlayer {
    /**
     * Plays a sound file.
     * @param soundFile Path to the sound file.
     */
    public static void playSound(String soundFile) {
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundFile))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}