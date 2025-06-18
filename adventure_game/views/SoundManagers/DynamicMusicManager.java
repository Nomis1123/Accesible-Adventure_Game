package views.SoundManagers;

public class DynamicMusicManager {

    private String musicName; //Name of the music to play
    private double volume; //Volume the music plays at
    private double lerpTime; //Time it takes to turn off music and swap between battle and explore variants
    private SoundEmitter soundEmitter; //The SoundEmitter to play the music on

    /**
     * Initializes attributes
     *
     * @param soundEmitter soundEmitter to play music on.
     * @param musicName name of the music to play.
     * @param volume volume the music plays at.
     * @param lerpTime time it takes to turn off msuic and swap between battle and explore variants
     */
    public DynamicMusicManager(SoundEmitter soundEmitter, String musicName, double volume, double lerpTime) {
        this.soundEmitter = soundEmitter;
        this.musicName = musicName;
        this.volume = volume;
        this.lerpTime = lerpTime;
    }

    /**
     * Starts playing music.
     */
    public void start() {
        this.soundEmitter.playSound("ExploreTone", this.musicName + " (Explore)", true);
        this.soundEmitter.playSound("BattleTone", this.musicName + " (Battle)", true);
        this.soundEmitter.setSoundVolume("BattleTone", 0.0);
    }

    /**
     * Stops playing music.
     */
    public void stop() {
        this.soundEmitter.setSoundVolume("ExploreTone", 0.0, this.lerpTime);
        this.soundEmitter.setSoundVolume("BattleTone", 0.0, this.lerpTime);
    }

    /**
     * Switches to and from battle music.
     *
     * @param battle if it should swap to battle music.
     */
    public void updateBattle(boolean battle) {
        if (battle) {
            this.soundEmitter.setSoundVolume("ExploreTone", 0.0, this.lerpTime);
            this.soundEmitter.setSoundVolume("BattleTone", this.volume, this.lerpTime);
        } else {
            this.soundEmitter.setSoundVolume("ExploreTone", this.volume, this.lerpTime);
            this.soundEmitter.setSoundVolume("BattleTone", 0.0, this.lerpTime);
        }
    }
}
