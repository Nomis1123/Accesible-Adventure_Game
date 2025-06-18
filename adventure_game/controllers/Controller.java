package controllers;

import javafx.scene.Scene;

/**
 * Class Controller.
 *
 * This class is a template for controller class.
 */
public abstract class Controller extends Muteable {
    protected KeyHandler keyHandler; //keyHandler used to easily listen for specific keys.

    /**
     * Adds the keyHandler to the given scene.
     */
    public void addToScene(Scene scene) {
        scene.setOnKeyPressed(this.keyHandler);
    }

    /**
     * Initializes keybinds.
     */
    abstract protected void initKeyBinds();

    /**
     * sets the mute of the keyHandler.
     */
    protected void setMuted(boolean muted) {
        this.keyHandler.setMuted(muted);
    }
}
