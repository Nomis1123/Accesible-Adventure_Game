package views;

import AdventureModel.*;
import com.sun.speech.freetts.en.us.FeatureProcessors;
import controllers.AdventureGameController;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.AccessibleRole;
import views.SoundManagers.DynamicMusicManager;
import views.SoundManagers.SoundEmitter;

import AdventureModel.HealthPotion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 * I could not get zoom recordings to work -Half
 * ZOOM LINK: <https://drive.google.com/file/d/13PeQ9SLYRq0MJ56K8XGjnjVFDAYaEewg/view?usp=sharing>
 * PASSWORD: <N/A>
 */
public class AdventureGameView {

    AdventureGame model; //model of the game

    AdventureGameController controller; //controller of the game, handles keyboard input
    Stage stage; //stage on which all is rendered
    HighlightButton saveButton, loadButton, helpButton, fightButton, retreatButton; //buttons

    HBox fightingOptionButtons;
    Boolean helpToggle = false; //is help on display?

    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label roomDescLabel = new Label(); //to hold room description and/or instructions

    Label healthDescLabel = new Label("HP: "); //to hold health description of the player

    Label enemyHealthLabel = new Label("HP: "); // to hold the hit point of the enemy, if there is one in the room

    // add a fighting label to the game to make it more clear that it's a fight
    Label fightingLabel = new Label("YOU ARE FIGHTING WITH: A");
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    Boolean isInventorySelection; //boolean that represents what item menu is being used (true or inventory, false for room, null for neither)
    Integer selectedItemIndex; //The selected item index
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input

    Room retreatRoom; //this is the room for player to retreat in a fight(typically the last room player stays)
    VBox textEntry; //place holds the input
    public MiniMap map;


    public SoundEmitter soundEmitter; // Soundemitter that manages audio
    private DynamicMusicManager dynamicMusicManager; // Manager for dynamic background music
    public TTSManager ttsManager; //TTS Usage

    public EnemyManager enemyManager; // used to manage the enemies

    String fightingTextDisplay; // text displayed for fight

    int fightTurn = 0; //use to record which turn the fight is at
    Random random = new Random(); // used for random number generation

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
     // TODO: 11/29/2023   decide which place to put enemies
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        this.soundEmitter = new SoundEmitter(new File("./" + model.getDirectoryName() + "/sounds/"));
        this.ttsManager = new TTSManager(new FreeTTS());
        //only call health potion generator here, so we will not generate extra potion during loading
        placeHealthPotionToRoom();
        //only generate enemies here, so we will not generate extra enemy during loading
        ArrayList<Integer> roomContainEnemies = new ArrayList<Integer>();
        roomContainEnemies.add(16); // generate enemy in room 16, 24, 25, 50
        roomContainEnemies.add(24);
        roomContainEnemies.add(25);
        roomContainEnemies.add(50);
        enemyManager = new EnemyManager(roomContainEnemies);
        initUI();

    }

    /**
     * Initialize the UI
     */
    public void initUI() {

        // setting up the stage
        this.stage.setTitle("Sunset Abyss");

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // add another row on the top for the health display.
        // Row constraints
        RowConstraints healthRow = new RowConstraints(100);// the top row for health display
        RowConstraints row1 = new RowConstraints(); // first row for the label that writes
        RowConstraints row2 = new RowConstraints( 650 ); // second row for the objects display()500
        RowConstraints row3 = new RowConstraints(); // third row for the text input
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll(healthRow, row1 , row2 , row3 );

        // Buttons
        saveButton = new HighlightButton("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        saveButton.setFont(new Font("Comic Sans MS", 20));
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new HighlightButton("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        loadButton.setFont(new Font("Comic Sans MS", 20));
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new HighlightButton("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 200, 50);
        helpButton.setFont(new Font("Comic Sans MS", 20));
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, helpButton, loadButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Comic Sans MS", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Comic Sans MS", 16));

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Comic Sans MS", 16));

        healthDescLabel.setStyle("-fx-text-fill: green;"); // create health to display
        healthDescLabel.setFont(new Font("Comic Sans MS", 24));

        enemyHealthLabel.setStyle("-fx-text-fill: red;"); // create health to display
        enemyHealthLabel.setFont(new Font("Comic Sans MS", 24));

        fightingLabel.setStyle("-fx-text-fill: white;"); // indicate if the player is in a fight
        fightingLabel.setFont(new Font("Stencil", 38));

        //role for adding label: add(Node child, int columnIndex, int rowIndex, int colspan, int rowspan)
        //add all the widgets to the GridPane
        gridPane.add(healthDescLabel, 0, 0, 1,1 ); // add health description to the first row
        gridPane.add(fightingLabel, 1, 0, 2,1 ); // add fighting status to the first row
        gridPane.add(enemyHealthLabel, 2, 0, 3,1 ); // add health description to the enemy

        //move the row one unit down so we will have space for putting the health display row
        gridPane.add( objLabel, 0, 1, 1, 1 );  // Add label :add(Node child, int columnIndex, int rowIndex, int colspan, int rowspan)
        gridPane.add( topButtons, 1, 1, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 1, 1, 1 );  // Add label to the second row

        updatePlayerHealth(0);         //display the health

        Label commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Comic Sans MS", 16));

        this.dynamicMusicManager = new DynamicMusicManager(this.soundEmitter, "Serenity", 0.7, 3.0);
        this.dynamicMusicManager.start();

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms
        // adding the text area and submit button to a VBox
        textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);

        gridPane.add( textEntry, 1, 3, 1, 1 ); //note (0,3) is for the mini map



        initMiniMap(); //yuh

        // Render everything
        var scene = new Scene( gridPane ,  1000, 1150); //note we made the scene bigger
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

        this.controller = new AdventureGameController(model, this);
        this.controller.addToScene(scene);

        this.resetFocus();
        //for testing purpose, piece of code to set the player into the room when game start.(move to that room directly)
//        model.getPlayer().setCurrentRoom(model.getRooms().get(15));
//        updateScene("");
//        updateItems();
//        map.updateMap();
    }


    private void initMiniMap()
    {
        map = new MiniMap(this);
        VBox box = new VBox();
        Label maplabel =  new Label("Mini - Map");
        maplabel.setAlignment(Pos.CENTER);
        maplabel.setStyle("-fx-text-fill: white;");
        maplabel.setFont(new Font("Comic Sans MS", 16));
        box.getChildren().addAll((maplabel), map);
        gridPane.add(box, 0,3); //move the map to the bottom
    }






    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    public void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute 
     *
     * Your event handler should respond when users 
     * hits the ENTER or TAB KEY. If the user hits 
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped 
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus 
     * of the scene onto any other node in the scene 
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        this.inputTextField.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if (Objects.equals(key, KeyCode.ENTER)) {
                this.submitEvent(this.inputTextField.getCharacters().toString().strip());
                this.inputTextField.clear();
            }
            else if (Objects.equals(key, KeyCode.TAB)) {
                this.resetFocus();;
            }
        });
    }

    /**
     * generates a sound name for a random term_input sound.
     *
     * @return the name of a random term_input sound.
     */
    public String generateTermInputSound() {
        return "term_input (" + ThreadLocalRandom.current().nextInt(1, 9) + ")";
    }

    /**
     * generates a sound name for a random attack sound.
     *
     * @return the name of a random attack sound.
     */
    public String generateAttackSound() {
        return "attack (" + ThreadLocalRandom.current().nextInt(1, 4) + ")";
    }

    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    public void submitEvent(String text) {

        //use to record whether the player's room is changed
        Room roomBeforeSubmitEvent = model.player.getCurrentRoom();

        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop

        String eventSound = this.generateTermInputSound();

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            this.model.getPlayer().getCurrentRoom().visit();
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
        } else {
            //try to move!
            String output = this.model.interpretAction(text); //process the command!
            if(Objects.equals(output, "BANDAGE")) {
                //deal with health potion(AKA bandage)
                int healthPotionIndex = 0; // keep track with which health potion to drink
                int recoveryAmount = 10;
                for (AdventureObject adventureObject : model.player.inventory) {
                    if (adventureObject.getName().equals("BANDAGE")) { //found a health potion
                        recoveryAmount = ((HealthPotion) adventureObject).getPotionRecoveryAmount();
                        break;
                    }
                    healthPotionIndex++;
                }
                updatePlayerHealth(recoveryAmount);
                if(model.getPlayer().getPlayerHealth()==100){
                    output = "YOU HAVE REACHED THE MAXIMUM HEALTH!!!";
                }
                else {
                    output = "YOU HAVE DRUNK THE HEALTH POTION, YOUR HEALTH BOOSTS BY: " + recoveryAmount;
                }
                model.player.inventory.remove(healthPotionIndex);//After the player drink the health potion, remove it
                updateScene(output);
                updateItems();
                eventSound = "bandage_use";
            } else if (Objects.equals(output, "GAME OVER")) {
                updateScene("");
                updateItems();
                PauseTransition pause = new PauseTransition(Duration.seconds(10));
                pause.setOnFinished(event -> {
                    Platform.exit();
                });
                pause.play();
                eventSound = "game_over";
            } else if (Objects.equals(output, "FORCED")) {
                //write code here to handle "FORCED" events!
                //Your code will need to display the image in the
                //current room and pause, then transition to
                //the forced room.
                updateScene(null);
                updateItems();
                this.controller.pushMute();
                PauseTransition pause = new PauseTransition(Duration.seconds(4));
                String finalOutput = output;
                pause.setOnFinished(event -> {
                    this.submitEvent(finalOutput);
                    this.controller.popMute();
                });
                pause.play();
                eventSound = null;
            } else {
                if (Objects.equals(output, "INVALID COMMAND.")) {
                    eventSound = "term_error";
                }
                updateScene(output);
                updateItems();
            }
        }

        if (!Objects.equals(eventSound, null)) {
            this.soundEmitter.playSound(eventSound);
        }

        map.updateMap();

        //update the retreat room
        if(model.player.getCurrentRoom().getRoomNumber() != roomBeforeSubmitEvent.getRoomNumber()){
            retreatRoom = roomBeforeSubmitEvent; //player enter into a new room, we have to update the retreat room
        }
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the 
     * current room.
     */
    private void showCommands() {
        this.formatText(this.model.getPlayer().getCurrentRoom().getCommands());
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     * 
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {


        getRoomImage(); //get the image of the current room
        formatText(textToDisplay); //format the text to display
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        VBox roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");

        gridPane.add(roomPane, 1, 2);//room pane is at the third row
        stage.sizeToScene();


        if(enemyManager.roomHasEnemy(this.model.getPlayer().getCurrentRoom().getRoomNumber())){ // if there's enemy in room
            if(enemyManager.getEnemyInRoom(model.getPlayer().getCurrentRoom().getRoomNumber()).getHealth().getHealthAmount()!=0){
                fight(); //fight with enemy!!!
            }
        }else {
            objectsInInventory.setDisable(false); //enable the items in room
            objectsInRoom.setDisable(false);
            enemyHealthLabel.setText(""); //clear the enemy's health information
            fightingLabel.setText(""); //clear the fighting label
            if(textEntry != null){
                if(!gridPane.getChildren().contains(textEntry)) {
                    gridPane.getChildren().add(textEntry); //add back the text entry if the player is not at the fight
                }
            }
            if(map != null){
                map.setDisable(false);
            }
            this.dynamicMusicManager.updateBattle(false);
        }


        //finally, articulate the description
        if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();
    }




    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     * 
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        //note if there's no input to the formatText then we will display the room description
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (objectString != null && !objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            else roomDescLabel.setText(roomDesc);
        } else roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Comic Sans MS", 14));
        roomDescLabel.setAlignment(Pos.CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place 
     * it in the roomImageView
     * Note if there is an enemy in the room then the image will describe the enemy
     */
    private void getRoomImage() {

        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";
        if(enemyManager.roomHasEnemy(roomNumber)){ // if there is an enemy in room
            String enemyName = enemyManager.getEnemyInRoom(roomNumber).getEnemyName();
            roomImage = this.model.getDirectoryName() + "/enemyimages/" + enemyName + ".png";
        }
        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setFitWidth(300);
        roomImageView.setFitHeight(300);
        if(enemyManager.roomHasEnemy(roomNumber)){ // if there is an enemy in room
            roomImageView.setFitWidth(300);//adjust the image size of the enemy
            roomImageView.setFitHeight(300);
        }
        roomImageView.setPreserveRatio(true);
        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * getObjectImage
     * __________________________
     *
     * Get a button with the image for the given object that when clicked interacts with the object.
     *
     * @param object The AdventureObject to interact with and get the image for.
     * @return The objects image button.
     */
    private HighlightButton getObjectImage(AdventureObject object, boolean inInventory) {
        String objectName = object.getName();

        ImageView objectImageView = new ImageView(new Image(this.model.getDirectoryName() + "/objectImages/" + objectName + ".png"));
        objectImageView.setPreserveRatio(true);
        objectImageView.setFitWidth(100);
        objectImageView.setFitHeight(75);

        HighlightButton objectImageButton = new HighlightButton(objectName);
        objectImageButton.setFont(new Font("Comic Sans MS", 12));
        objectImageButton.setContentDisplay(ContentDisplay.TOP); //set the object name display correctly
        objectImageButton.setStyle("-fx-background-color: #00ffff");//make the text display style better
        objectImageButton.setId(objectName);
        //customizeButton(objectImageButton, 100, 100);
        makeButtonAccessible(objectImageButton, objectName, object.getDescription(), "This button interacts with the " + object.getDescription() + "object when clicked");
        objectImageButton.setOnAction(e -> {
            if (inInventory) {
                this.submitEvent("DROP " + objectName);
            } else {
                this.submitEvent("TAKE " + objectName);
            }
        });

        objectImageButton.setGraphic(objectImageView);

        return objectImageButton;
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {

        //write some code here to add images of objects in a given room to the objectsInRoom Vbox
        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox
        //please use setAccessibleText to add "alt" descriptions to your images!
        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";

        this.resetSelectedItem();

        this.objectsInRoom.getChildren().clear();
        for (AdventureObject object : this.model.getPlayer().getCurrentRoom().objectsInRoom) {
            this.objectsInRoom.getChildren().add(this.getObjectImage(object, false));
        }

        this.objectsInInventory.getChildren().clear();
        for (AdventureObject object : this.model.getPlayer().inventory) {
            this.objectsInInventory.getChildren().add(this.getObjectImage(object, true));
        }

        // definitely remove these line as the row number changes !!!
//        this.gridPane.getChildren().removeIf(node ->
//                (GridPane.getColumnIndex(node) == 0 && GridPane.getRowIndex(node) == 1)
//                        || (GridPane.getColumnIndex(node) == 2 && GridPane.getRowIndex(node) == 1));

        ScrollPane scO = new ScrollPane(objectsInRoom);
//        scO.setPadding(new Insets(10)); //Why is this here?? It breaks the spacing causing a horizonal scroll bar???
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,2);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI,2,2);
    }

    /**
     * Sets the highlight override of the selected item.
     */
    private void setselectedItemHighlight(boolean on) {
        if (!Objects.equals(this.isInventorySelection, null)) {
            HighlightButton objectImageButton;
            if (this.isInventorySelection) {
                objectImageButton = (HighlightButton) this.objectsInInventory.getChildren().get(this.selectedItemIndex);
            } else {
                objectImageButton = (HighlightButton) this.objectsInRoom.getChildren().get(this.selectedItemIndex);
            }
            objectImageButton.setHighlightOverride(on);
        }
    }

    /**
     * Clears the current item selection.
     */
    public void resetSelectedItem() {
        this.setselectedItemHighlight(false);
        this.isInventorySelection = null;
        this.selectedItemIndex = null;
    }

    /**
     * Selects and loops through the room or inventory items.
     *
     * @param isInventorySelection true if it should loop through the inventory items, false for the room items
     */
    private void selectNextItem(boolean isInventorySelection) {
        VBox targetContainer = isInventorySelection ? this.objectsInInventory : this.objectsInRoom;
        int size = targetContainer.getChildren().size();
        if (size <= 0) {
            this.soundEmitter.playSound("term_error");
            return;
        }

        this.setselectedItemHighlight(false); // Clear highlight of previous item

        if (!Objects.equals(this.isInventorySelection, isInventorySelection)) {
            this.isInventorySelection = isInventorySelection;
            this.selectedItemIndex = 0;
        } else {
            this.selectedItemIndex += 1;
            if (this.selectedItemIndex >= size) {
                this.selectedItemIndex = 0;
            }
        }

        this.setselectedItemHighlight(true); //Set highlight of next item

        StringBuilder objectDataString = new StringBuilder();
        objectDataString.append("ITEM ");
        objectDataString.append(this.selectedItemIndex + 1);
        objectDataString.append(" ");
        objectDataString.append(targetContainer.getChildren().get(this.selectedItemIndex).getId());

        this.ttsManager.playSpeech(objectDataString.toString(), "kevin16");
    }

    /**
     * Selects and loops through inventory items.
     */
    public void selectNextInventoryItem() {
        this.selectNextItem(true);
    }

    /**
     * Selects and loops through room items.
     */
    public void selectNextRoomItem() {
        this.selectNextItem(false);
    }

    /**
     * Uses the currently selected item.
     */
    public void useSelectedItem() {
        if (!Objects.equals(this.isInventorySelection, null)) {
            HighlightButton objectImageButton;
            if (this.isInventorySelection) {
                objectImageButton = (HighlightButton) this.objectsInInventory.getChildren().get(this.selectedItemIndex);
            } else {
                objectImageButton = (HighlightButton) this.objectsInRoom.getChildren().get(this.selectedItemIndex);
            }
            objectImageButton.fire();
        }
    }

    /*
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {

        // definitely remove this line as the row number changes
//        this.gridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 1);
        if (this.helpToggle) {
            updateScene("");
        }
        else {
            this.formatText(this.model.getInstructions());

            roomDescLabel.setPrefWidth(500);
            roomDescLabel.setPrefHeight(500);
            roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
            roomDescLabel.setWrapText(true);
            VBox helpPane = new VBox(roomDescLabel);
            helpPane.setPadding(new Insets(10));
            helpPane.setAlignment(Pos.TOP_CENTER);
            helpPane.setStyle("-fx-background-color: #000000;");

            gridPane.add(helpPane, 1, 2); //The help pane is displayed at the third row
            stage.sizeToScene();
        }
        this.helpToggle = !this.helpToggle;
//        throw new UnsupportedOperationException("showInstructions is not implemented!");
    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            this.soundEmitter.playSound(this.generateTermInputSound());
            showInstructions();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            this.resetFocus();;
            this.soundEmitter.playSound(this.generateTermInputSound());
            SaveView saveView = new SaveView(this);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            this.resetFocus();;
            this.soundEmitter.playSound(this.generateTermInputSound());
            LoadView loadView = new LoadView(this);
        });
    }


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        Room currentRoom = this.model.getPlayer().getCurrentRoom();
        String roomDescription = currentRoom.getRoomDescription();
        String objectDescription = "You also see the following items " + currentRoom.getObjectString().replace(", ", " ");

        // Combine room and object descriptions
        String fullDescription = roomDescription + " " + objectDescription;

        // Use TTS to articulate the full room description with object information
        ttsManager.playSpeech(fullDescription, "kevin16");
    }

    /**
     * This method stops articulations 
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        ttsManager.stopSpeech();
    }


    //Additional methods for group projects, hit points and fighting features.

    /**
     * this method will change the player's current health by this amount and update these
     * changes in screen
     * @param changeAmount : amount of health player changes
     */
    private void updatePlayerHealth(int changeAmount){
        boolean flag = model.player.modifyPlayerHealth(changeAmount);
        if(!flag){
            this.formatText("You have already reached the maximum health 100!!!");
        }
        int currHealth = model.player.getPlayerHealth();
        healthDescLabel.setText("PLAYER HP: " + "\n" + currHealth + "/100");

    }


    /**
     * this method generate some health potion and put them into rooms randomly
     * fighting room will not be distributed with a health potion(bandage)
     */
    private void placeHealthPotionToRoom(){
        //assign a health potion to the first room
        HealthPotion healthPotion = new HealthPotion(model.getRooms().get(1), 10);
        model.getRooms().get(1).objectsInRoom.add(healthPotion);
        //randomly assign health potion to the rest of the rooms, except the forced room
        ArrayList<Integer> rooms = new ArrayList<Integer>(model.getRooms().keySet()); //get the list of all rooms
        Random rand = new Random();
        //fighting rooms and the final room does not contain bondage
        ArrayList<Integer> noBandageRoom = new ArrayList<Integer>(Arrays.asList(16, 24, 25, 50, 100));//fighting rooms and the final room does not contain bondage
        for(int roomNum:rooms){
            if(noBandageRoom.contains(roomNum)){
                continue;
            }
            int randInt = rand.nextInt(2); // generate 0 or 1 randomly
            if(randInt == 0){ //no health potion generate if the randInt is 0
                continue;
            }
            //assign potion to the room

            model.getRooms().get(roomNum).objectsInRoom.add(healthPotion);
        }
    }

    /**
     * this method will generate some health potion to a specific room
     * I made this method mainly to spawn some health potions after the enemy is defeated
     * @param roomNum: destination for health potion to locate at
     */
    private void placeHealthPotionToSpecificRoom(int roomNum){
        HealthPotion healthPotion = new HealthPotion(model.getRooms().get(roomNum), 10);
        model.getRooms().get(roomNum).objectsInRoom.add(healthPotion);
    }

    /**
     * this method will change the enemy's current health by this amount and update these
     * changes in screen
     * @param changeAmount : amount of health enemy changes
     */
    private void updateEnemyHealth(int changeAmount){
        //first check if there is an enemy in room
        if(!enemyManager.roomHasEnemy(model.getPlayer().getCurrentRoom().getRoomNumber())){
            System.out.println("No enemy in this room, sth goes wrong");
            return;
        }
        Enemy currEnemy = enemyManager.getEnemyInRoom(model.getPlayer().getCurrentRoom().getRoomNumber());
        currEnemy.getHealth().modifyHealth(changeAmount);

        // update the enemy's health in UI
        enemyHealthLabel.setText("ENEMY HP: " + "\n" + currEnemy.getHealth().getHealthAmount() + "/" + currEnemy.getHealth().getMaxMumHealth());

    }

    /**
     * precondition: player is staying a room with an enemy
     * set up the ui for the enemy and the fighting system
     * The place where describes the room is replaced by the picture of the enemy
     * Under that picture is the description of this battle.
     */
    private void fight(){
        this.dynamicMusicManager.updateBattle(true);

        fightTurn = 1;
        updateEnemyHealth(0); // display the enemy's hit points in screen
        map.setDisable(true);
        Enemy enemy = enemyManager.getEnemyInRoom(model.getPlayer().getCurrentRoom().getRoomNumber());
        //raise the fighting label
        fightingLabel.setText("         You are fighting with a: "+ "\n" + "          " +
                "        " + enemy.getEnemyName() + " !!!!");

        //set up battle description
        fightingTextDisplay =  enemy.getEnemyDescription() + "\n";
        displayFightingInfo("FIGHT STARTS!!! Please press fight button or retreat button.");

        createFightingButtons(); // create buttons for fighting

        objectsInInventory.setDisable(true); //player are not supposed to access items during the fight
        objectsInRoom.setDisable(true);
    }

    /**
     * Returns if the player is in combat.
     *
     * @return true iff the player is in combat.
     */
    public boolean isInCombat() {
        return !Objects.equals(enemyManager.getEnemyInRoom(model.getPlayer().getCurrentRoom().getRoomNumber()), null);
    }

    /**
     * This method can only be called during the fight
     * it creates an attack button and a retreat button for player to press
     */
    private void createFightingButtons(){

        fightButton = new HighlightButton("ATTACK!!");
        customizeButton(fightButton, 250, 70);
        fightButton.setId("ATTACK");
        fightButton.setStyle("-fx-text-fill: black;");
        fightButton.setFont(new Font("Impact", 25));
        addAttackEvent();

        retreatButton = new HighlightButton("RETREAT??");
        customizeButton(retreatButton, 250, 70);
        retreatButton.setId("RETREAT");
        retreatButton.setStyle("-fx-text-fill: black;");
        retreatButton.setFont(new Font("Impact", 25));
        addRetreatEvent();

        fightingOptionButtons = new HBox();
        fightingOptionButtons.getChildren().addAll(fightButton, retreatButton);
        fightingOptionButtons.setSpacing(50);
        fightingOptionButtons.setAlignment(Pos.CENTER);

        gridPane.add( fightingOptionButtons, 1, 3, 1, 1 ); //note (0,3) is for the mini map
        gridPane.getChildren().remove(textEntry);

    }

    /**
     * Clicks the fightButton if active.
     */
    public void attack() {
        if (!Objects.equals(this.fightButton, null)) {
            this.fightButton.fire();
        }
    }

    /**
     * Clicks the retreatButton if active.
     */
    public void retreat() {
        if (!Objects.equals(this.retreatButton, null)) {
            this.retreatButton.fire();
        }
    }

    /**
     * This method handles the event related to the
     * retreat button.
     */
    public void addRetreatEvent() {
        retreatButton.setOnAction(e -> {
            this.soundEmitter.playSound(this.generateTermInputSound());
            this.resetFocus();;
            if(retreatRoom == null){ // no retreat room, fight or die!!!
                return;
            }
            //move player to the last room he stays at
            model.player.setCurrentRoom(retreatRoom);
            updateScene("");
            updateItems();
            map.updateMap();
        });
    }

    /**
     * This method handles the event related to the attack button
     * note after the player performs an attack, the turn changes to the enemy's.
     * IF the player defeat the enemy, this method will set the player to the corresponding room after the victory
     */
    public void addAttackEvent() {
        fightButton.setOnAction(e -> {
            StringBuilder battleDataString = new StringBuilder();
            this.soundEmitter.playSound(this.generateAttackSound());
            this.resetFocus();;
            fightingTextDisplay = "";
            Enemy enemy = enemyManager.getEnemyInRoom(model.getPlayer().getCurrentRoom().getRoomNumber());
            int attackAmount = random.nextInt(20, 60);
            displayFightingInfo("In the turn " + fightTurn + ":");
            displayFightingInfo("--------------------------------------------------------------------------");
            displayFightingInfo("You strike the foe " + enemy.getEnemyName() + " for " + attackAmount + "HP.");
            updateEnemyHealth(-attackAmount); //cause damage on the enemy
            if(enemy.getHealth().getHealthAmount() <= 0){ // if enemy is dead
                battleDataString.append(" Congratulations! You have defeated the enemy ").append(enemy.getEnemyName()).append(".");
                formatText("congratulations!!!\n" + "You have defeated the enemy: " + enemy.getEnemyName() +"\n Now is a good time to take items left by enemy.");
                objectsInRoom.setDisable(false); // now is a good time to collect the enemy's pressure
                objectsInRoom.setDisable(false);
               // enemyManager.slayedEnemy(model.player.getCurrentRoom().getRoomNumber());
                fightingTextDisplay = ""; //clear the fighting text in this battle
                PauseTransition wait = new PauseTransition(Duration.seconds(5));// let the victory text display for a while
                ////setOnFinished:what will be executed after the wait time ends
                fightingOptionButtons.setDisable(true);
                fightButton.setDisable(true);
                retreatButton.setDisable(true);
                //set the correct room for player to go according to the type of enemy they defeated
                wait.setOnFinished(Event->{
                    //set the correct room for player to go according to the type of enemy they defeated
                    switch (enemy.getEnemyName()) {
                        case "foodMonster" ->   // defeat foodMonster -> room 19
                                model.getPlayer().setCurrentRoom(model.getRooms().get(19));
                        case "mimic" ->  // defeat mimic -> room 27
                                model.getPlayer().setCurrentRoom(model.getRooms().get(27));
                        case "drone" ->  // defeat drone -> room 28
                                model.getPlayer().setCurrentRoom(model.getRooms().get(28));
                        case "theEntity" ->  // defeat the entity -> room 51
                                model.getPlayer().setCurrentRoom(model.getRooms().get(51));
                        default ->
                                System.out.println("Make sure the enemy name in the method addAttackEvent() is same as the enemy name in the enemy classes!!!!!!!!!!! ");
                    }
                    updateScene("");
                    updateItems();
                    map.updateMap();
                    // note after we defeat the entity we will be sent into a forced room!!!!!
                    if(enemy.getEnemyName().equals("theEntity")){
                        this.controller.pushMute();
                        PauseTransition pause = new PauseTransition(Duration.seconds(4));
                        String finalOutput = "FORCED";
                        pause.setOnFinished(event -> {
                            this.submitEvent(finalOutput);
                            this.controller.popMute();
                        });
                        pause.play();
                    }
                    enemy.getHealth().setHealthAmount(enemy.getHealth().getMaxMumHealth()); //regenerate the enemy after the enemy is dead
                });
                wait.play();
            }
            else {
                battleDataString.append(" In the turn ").append(fightTurn).append(": You strike the foe ").append(enemy.getEnemyName()).append(" for ").append(attackAmount).append(" HP.");
                //enemy is not dead, now it's enemy's turn
                battleDataString.append(" Now it's the enemy's turn.");
                enemyTurn();
            }
            ttsManager.playSpeech(battleDataString.toString(), "kevin16");
            fightTurn ++; //next turn
        });
    }

    /**
     * precondition: this method is called during a battle
     * The enemy in the current room conduct an action
     */
    public void enemyTurn(){
        Enemy enemy = enemyManager.getEnemyInRoom(model.getPlayer().getCurrentRoom().getRoomNumber());
        displayFightingInfo("--------------------------------------------------------------------------");
        displayFightingInfo("ENEMY ACTION:");
        String enemyAttackInfo = enemy.attack(model.getPlayer());
        updatePlayerHealth(0); //update the player's health in the screen
        updateEnemyHealth(0); //update the enemy's health amount in case the enemy recovers its health
        displayFightingInfo(enemyAttackInfo);
        displayFightingInfo("--------------------------------------------------------------------------");
        if(model.getPlayer().getPlayerHealth() == 0){  //player is died, end the whole game.
            displayFightingInfo("SORRY \n You lost all your HP and YOU ARE DEAD");
            fightingOptionButtons.setDisable(true);
            fightingLabel.setStyle("-fx-text-fill: RED;"); // indicate if the player is in a fight
            fightingLabel.setFont(new Font("Stencil", 50));
            fightingLabel.setText("    YOU ARE DEAD!!!!!");
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> {
                //set to the room 100, since the player lost the game
                model.getPlayer().setCurrentRoom(model.getRooms().get(100));
                updateScene("");
                updateItems();
                map.updateMap();
            });
            pause.play();
        }else {
            displayFightingInfo("NOW IT'S YOUR TURN"); //after enemy's attack, it's player's turn
        }
    }


    /**
     * This method should only be called during the fight.
     * display the input text on the screen *without* removing the previous text.
     * Note this method will add the change line symbol so no need to add /n in the input.
     * @param additionalTextForDisplay: information of the fighting to display
     */
    public void displayFightingInfo(String additionalTextForDisplay){

        //if there are more than 11 lines inside the text display, clear all the previous lines
        long lineNum = fightingTextDisplay.codePoints().filter(ch -> ch == '\n').count();
        if(lineNum >= 11){
            fightingTextDisplay = "";
        }

        fightingTextDisplay += additionalTextForDisplay + "\n";
        formatText(fightingTextDisplay);

    }

    /**
     * This method resets the focus to the gridPane.
     */
    public void resetFocus() {
        this.gridPane.requestFocus();
    }
}
