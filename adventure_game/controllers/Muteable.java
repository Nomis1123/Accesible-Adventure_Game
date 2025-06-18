package controllers;

/**
 * Class Muteable.
 *
 * This is a class that allows for easily disabling and enabling an object from multiple sources.
 */
public abstract class Muteable {
    protected int num_mutes; //The number of mute sources.

    /**
     * mutes the object.
     */
    abstract protected void setMuted(boolean muted);

    /**
     * Adds a mute source.
     */
    public void pushMute() {
        this.num_mutes += 1;
        this.setMuted(this.num_mutes == 0);
    }

    /**
     * Removes a mute source.
     * Will do nothing if there are no mute sources.
     */
    public void popMute() {
        this.num_mutes -= 1;
        this.num_mutes = Math.max(this.num_mutes, 0);
        this.setMuted(this.num_mutes == 0);
    }

    /**
     * Returns the mute state of the object.
     *
     * @return iff the object is muted.
     */
    public boolean isMuted() {
        return this.num_mutes != 0;
    }
}
