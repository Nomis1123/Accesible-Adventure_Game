package controllers;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Objects;

/**
 * Class KeyHandler.
 *
 * This is a class that allows for easily setting methods for a specific keyevent.
 */
public class KeyHandler implements EventHandler<KeyEvent> {

    private HashMap<KeyCode, Runnable> keyEvents; // maps keys to methods that run when said keys are pressed.

    private Runnable defaultKeyEvent; // stores a function that runs if a key that has not been bound has been pressed.

    private boolean muted; // are we accepting key inputs right now?

    /**
     * Initializes attributes.
     */
    public KeyHandler() {
        this.keyEvents = new HashMap<KeyCode, Runnable>();
        this.muted = true;
    }

    /**
     * Adds a method that runs when the given key is pressed.
     *
     * @param keyCode the code of the key that the given method will run when pressed.
     * @param keyFn the method that will run when the given key is pressed.
     */
    public void addKeyListener(KeyCode keyCode, Runnable keyFn) {
        this.keyEvents.put(keyCode, keyFn);
    }

    /**
     * Removes the current method that runs when the given key is pressed.
     *
     * @param keyCode the code of the key that the method will be cleared for.
     */
    public void removeKeyCallback(KeyCode keyCode) {
        this.keyEvents.put(keyCode, null);
    }

    /**
     * Removes all methods that will run when a key is pressed.
     * Does not include the default method.
     */
    public void removeAllKeyCallbacks() {
        this.keyEvents.clear();
    }

    /**
     * Handles a keyEvent.
     * Will run the method corresponding to the given key event or the default method if non is found.
     *
     * @param keyEvent the key event that will be used to source the target key from.
     */
    public void handle(KeyEvent keyEvent) {
        if (muted) {
            KeyCode keyCode = keyEvent.getCode();
            Runnable KeyFn = this.keyEvents.getOrDefault(keyCode, null);
            if (!Objects.equals(KeyFn, null)) {
                KeyFn.run();
            } else if (!Objects.equals(this.defaultKeyEvent, null)) {
                this.defaultKeyEvent.run();
            }
        }
    }

    /**
     * Disables or enables the keylistener.
     *
     * @param muted if the KeyListener should listen for keys or not.
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    /**
     * Sets the method that will run if no method is mapped to a key.
     *
     * @param defaultKeyEvent The method to set as the default key listener.
     */
    public void setDefaultKeyListener(Runnable defaultKeyEvent) {
        this.defaultKeyEvent = defaultKeyEvent;
    }
}
