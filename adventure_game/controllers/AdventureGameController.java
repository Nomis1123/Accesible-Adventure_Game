package controllers;

import AdventureModel.AdventureGame;
import AdventureModel.Enemy;
import javafx.scene.input.KeyCode;
import views.AdventureGameView;

/**
 * Class AdventureGameController.
 *
 * This is a class that manages keyboard input for the AdventureGameView.
 */
public class AdventureGameController extends Controller {

    private AdventureGame model; //model of the game.

    private AdventureGameView view; //view of the game.

    /**
     * Initializes attributes.
     */
    public AdventureGameController(AdventureGame model, AdventureGameView view) {
        this.model = model;
        this.view = view;
        this.initKeyBinds();
    }

    /**
     * Initializes keybinds.
     */
    protected void initKeyBinds() {
        this.keyHandler = new KeyHandler();

        // NOTE: due to how textfields work, TAB and ENTER must be defined to prevent duplicate sounds -Half
        // Used in javafx's focus traversal stuff, so I can't modify it -Half
        this.keyHandler.addKeyListener(KeyCode.TAB, this::dummy);

        this.keyHandler.addKeyListener(KeyCode.ACCEPT, this::accept);
        this.keyHandler.addKeyListener(KeyCode.ENTER, this::accept);
        this.keyHandler.addKeyListener(KeyCode.E, this::openPlayerInventory);
        this.keyHandler.addKeyListener(KeyCode.Q, this::openRoomInventory);
        this.keyHandler.addKeyListener(KeyCode.F, this::exitInventory);

        this.keyHandler.addKeyListener(KeyCode.W, this::up);
        this.keyHandler.addKeyListener(KeyCode.A, this::left);
        this.keyHandler.addKeyListener(KeyCode.D, this::right);
        this.keyHandler.addKeyListener(KeyCode.S, this::down);
        this.keyHandler.addKeyListener(KeyCode.UP, this::up);
        this.keyHandler.addKeyListener(KeyCode.LEFT, this::left);
        this.keyHandler.addKeyListener(KeyCode.RIGHT, this::right);
        this.keyHandler.addKeyListener(KeyCode.DOWN, this::down);

        this.keyHandler.addKeyListener(KeyCode.L, this::look);
        this.keyHandler.addKeyListener(KeyCode.H, this::help);
        this.keyHandler.addKeyListener(KeyCode.C, this::commands);
        this.keyHandler.addKeyListener(KeyCode.R, this::health);

        this.keyHandler.setDefaultKeyListener(this::onDefaultKeyPress);
    }


    /**
     * Dummy method used to disable the error sound for a key.
     */
    private void dummy() {}

    /**
     * Use selected item.
     */
    public void accept() {
        this.view.useSelectedItem();
    }

    /**
     * Selects and loops through inventory items.
     */
    public void openPlayerInventory() {
        this.view.selectNextInventoryItem();
    }

    /**
     * Selects and loops through room items.
     */
    public void openRoomInventory() {
        this.view.selectNextRoomItem();
    }

    /**
     * Clears the selected item.
     */
    public void exitInventory() {
        this.view.resetSelectedItem();
    }

    /**
     * Moves the player north.
     *
     * Attacks if in a battle.
     */
    public void up() {
        if (!this.view.isInCombat()) {
            this.view.submitEvent("north");
        } else {
            this.view.attack();
        }
    }

    /**
     * Moves the player west.
     *
     * Does nothing if in a battle.
     */
    public void left() {
        if (!this.view.isInCombat()) {
            this.view.submitEvent("west");
        }
    }

    /**
     * Moves the player east.
     *
     * Does nothing if in a battle.
     */
    public void right() {
        if (!this.view.isInCombat()) {
            this.view.submitEvent("east");
        }
    }

    /**
     * Moves the player south.
     *
     * Retreats if in a battle.
     */
    public void down() {
        if (!this.view.isInCombat()) {
            this.view.submitEvent("south");
        } else {
            this.view.retreat();
        }
    }

    /**
     * Runs the LOOK command.
     */
    public void look() {
        this.view.submitEvent("L");
    }

    /**
     * Runs the HELP command.
     */
    public void help() {
        this.view.submitEvent("H");
    }

    /**
     * Runs the COMMANDS command.
     */
    public void commands() {
        this.view.submitEvent("C");
    }

    /**
     * Says the player current health (and the enemy's health if present).
     */
    public void health() {
        StringBuilder healthDataString = new StringBuilder();
        healthDataString.append("Player Health ");
        healthDataString.append(this.model.getPlayer().getPlayerHealth());
        if(this.view.enemyManager.roomHasEnemy(model.getPlayer().getCurrentRoom().getRoomNumber())){
            Enemy currEnemy = this.view.enemyManager.getEnemyInRoom(model.getPlayer().getCurrentRoom().getRoomNumber());
            healthDataString.append("Enemy Health: ");
            healthDataString.append(currEnemy.getHealth().getHealthAmount());
        }
        this.view.ttsManager.playSpeech(healthDataString.toString(), "kevin16");
    }

    /**
     * Makes an error sound when a key that is not bound is pressed.
     */
    public void onDefaultKeyPress() {
        this.view.soundEmitter.playSound("term_error");
    }
}
