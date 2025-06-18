package views.SoundManagers;

/**
 * Class SoundKiller.  Helper class used to remove sounds that have finished playing.
 */
class SoundKiller implements Runnable {
    private SoundEmitter soundEmitter; // An attribute that stores a reference to the sound emitter the target sound is played in.
    private String soundType; // An attribute that stores a reference to the sound type that corresponds to the target sound.

    /**
     * Initializes attributes
     *
     * @param soundEmitter sound emitter the target sound is played in.
     * @param soundType sound type corresponding to the target sound.
     */
    public SoundKiller(SoundEmitter soundEmitter, String soundType) {
        this.soundEmitter = soundEmitter;
        this.soundType = soundType;
    }

    /**
     * Kills the sound stored in the sound emitter attribute with the corresponding to the sound type attribute.
     */
    public void run() {
        this.soundEmitter.killSound(this.soundType);
    }
}

