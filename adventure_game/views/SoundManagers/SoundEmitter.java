package views.SoundManagers;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class SoundEmitter.  Manages sounds and groups them into types, where only a sound of each type may play at one time.
 */
public class SoundEmitter {
    private static final String[] FORMATS = new String[]{".mp3", ".wav"}; // The supported extensions for sounds.
    private File directory; // An attribute that stores the directory from which sounds will be procured.
    private HashMap<String, MediaPlayer> mediaPlayers; // An attribute that stores sounds based on their type.
    private VolumeManager volumeManager; // An attribute that stores a helper class that is used for managing volume changing over time.

    private Integer typelessSoundId; // An attribute that stores an integer used to create a unique soundtype

    /**
     * Initializes attributes
     *
     * @param directory directory to procure sound files.
     */
    public SoundEmitter(File directory) {
        this.directory = directory;
        this.mediaPlayers = new HashMap<String, MediaPlayer>();
        this.volumeManager = new VolumeManager();
        this.typelessSoundId = 0;
    }

    /**
     * Generates and returns a unique type identifier for a sound.
     *
     * @return a unique type identifier for a sound.
     */
    private String getEmptyType() {
        return "typeless" + ++this.typelessSoundId;
    }

    /**
     * Returns the sound file uri of a given soundName or null if no such supported file exists.
     *
     * @return a sound file uri
     */
    private String getSoundFileURI(String soundName) {
        File file = null;
        for (String extension : FORMATS) {
            file = new File(this.directory, soundName + extension);
            if (file.exists()) {
                return file.toURI().toString();
            }
        }
        return null;
    }

    /**
     * Starts playing a sound, if a sound with the same sound type already exists it will be replaced.
     *
     * @param soundType type of the sound.
     * @param soundName name of the sound file not including extension.
     * @param loop whether this should reset when finished and loop forever.
     */
    public void playSound(String soundType, String soundName, boolean loop) {
        this.killSound(soundType);
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(this.getSoundFileURI(soundName)));
        mediaPlayer.play();
        // This if statement is needed because setOnEndOfMedia is bugged and still runs even if the media will repeat... -Half
        if (loop) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        else {
            mediaPlayer.setOnEndOfMedia(new SoundKiller(this, soundType));
        }
        this.mediaPlayers.put(soundType, mediaPlayer);
    }

    /**
     * Starts playing a sound, if a sound with the same sound type already exists it will be replaced.
     *
     * @param soundType type of the sound.
     * @param soundName name of the sound file not including extension.
     */
    public void playSound(String soundType, String soundName) {
        this.playSound(soundType, soundName, false);
    }

    /**
     * Starts playing a sound.
     *
     * @param soundName name of the sound file not including extension.
     */
    public void playSound(String soundName) {
        this.playSound(this.getEmptyType(), soundName);
    }

    /**
     * Returns a boolean based on if the sound type is in use.
     *
     * @param soundType type of the sound.
     * @return boolean corresponding to the usage of the given sound type.
     */
    public boolean isPlayingSound(String soundType) {
        MediaPlayer mediaPlayer = this.mediaPlayers.getOrDefault(soundType, null);
        return !Objects.equals(mediaPlayer, null);
    }

    /**
     * Kills the sound associated with the given sound type.
     *
     * @param soundType type of the sound.
     */
    public void killSound(String soundType) {
        if (this.isPlayingSound(soundType)) {
            this.volumeManager.killVolumeManagement(soundType);
            this.mediaPlayers.get(soundType).stop();
            this.mediaPlayers.put(soundType, null);
        }
    }

    /**
     * Kills all sounds.
     */
    public void killAllSounds() {
        for (String soundType : this.mediaPlayers.keySet()) {
            this.killSound(soundType);
        }
    }

    /**
     * Returns the mediaPlayers used to play sounds.
     *
     * @return a hashmap that maps sound types to media-players.
     */
    public HashMap<String, MediaPlayer> getMediaPlayers() {
        return this.mediaPlayers;
    }

    /**
     * Sets the volume of the given sound type interpolated over the given duration.
     *
     * @param soundType type of the sound.
     * @param volume volume to set the sound to.
     * @param duration duration of time in seconds to interpolate the volume over.
     */
    public void setSoundVolume(String soundType, double volume, double duration) {
        if (this.isPlayingSound(soundType)) {
            DoubleProperty volumeProperty = this.mediaPlayers.get(soundType).volumeProperty();
            this.volumeManager.manageVolume(soundType, volumeProperty, volume, duration);
        }
    }

    /**
     * Sets the volume of the given sound type.
     *
     * @param soundType type of the sound.
     * @param volume volume to set the sound to.
     */
    public void setSoundVolume(String soundType, double volume) {
        this.setSoundVolume(soundType, volume, 0.0);
    }

    /**
     * Returns if the sound associated with the given sound type is audible.
     *
     * @param soundType type of the sound.
     * @return boolean corresponding to if the sound is audible or not.
     */
    public boolean isSoundAudible(String soundType) {
        if (this.isPlayingSound(soundType)) {
            MediaPlayer mediaPlayer = this.mediaPlayers.get(soundType);
            return mediaPlayer.getVolume() > 0 && !mediaPlayer.isMute() && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
        }
        return false;
    }

    /**
     * Sets the mute of the given sound type.
     *
     * @param soundType type of the sound.
     * @param mute the boolean value to set the mute of the sound to.
     */
    public void muteSound(String soundType, boolean mute) {
        if (this.isPlayingSound(soundType)) {
            this.mediaPlayers.get(soundType).setMute(mute);
        }
    }
}
