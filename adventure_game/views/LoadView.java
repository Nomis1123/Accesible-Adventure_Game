package views;

import AdventureModel.AdventureGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;


/**
 * Class LoadView.
 *
 * Loads Serialized adventure games.
 */
public class LoadView {

    private AdventureGameView adventureGameView;
    private Label selectGameLabel;
    private Button selectGameButton;
    private Button closeWindowButton;

    private ListView<String> GameList;
    private String filename = null;

    public LoadView(AdventureGameView adventureGameView){

        //note that the buttons in this view are not accessible!!
        this.adventureGameView = adventureGameView;
        selectGameLabel = new Label(String.format(""));

        GameList = new ListView<>(); //to hold all the file names

        final Stage dialog = new Stage(); //dialogue box
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        selectGameLabel.setId("CurrentGame"); // DO NOT MODIFY ID
        GameList.setId("GameList");  // DO NOT MODIFY ID
        GameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        getFiles(GameList); //get files for file selector
        selectGameButton = new HighlightButton("Change Game");
        selectGameButton.setId("ChangeGame"); // DO NOT MODIFY ID
        AdventureGameView.makeButtonAccessible(selectGameButton, "select game", "This is the button to select a game", "Use this button to indicate a game file you would like to load.");

        closeWindowButton = new HighlightButton("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> {
            dialog.close();
            this.adventureGameView.soundEmitter.playSound(this.adventureGameView.generateTermInputSound());
        });
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the load game window", "Use this button to close the load game window.");

        //on selection, do something
        selectGameButton.setOnAction(e -> {
            try {
                selectGame(selectGameLabel, GameList);
                this.adventureGameView.soundEmitter.playSound(this.adventureGameView.generateTermInputSound());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox selectGameBox = new VBox(10, selectGameLabel, GameList, selectGameButton);

        // Default styles which can be modified
        GameList.setPrefHeight(100);
        selectGameLabel.setStyle("-fx-text-fill: #e8e6e3");
        selectGameLabel.setFont(new Font(16));
        selectGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        selectGameButton.setPrefSize(200, 50);
        selectGameButton.setFont(new Font(16));
        selectGameBox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(selectGameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Get Files to display in the on screen ListView
     * Populate the listView attribute with .ser file names
     * Files will be located in the Games/Saved directory
     *
     * @param listView the ListView containing all the .ser files in the Games/Saved directory.
     */
    private void getFiles(ListView<String> listView) {
        listView.getItems().clear(); //Just incase...

        File[] files = new File("Games/Saved").listFiles();
        if (!Objects.equals(files, null)) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    int extensionIndex = fileName.lastIndexOf(".");
                    if (!Objects.equals(extensionIndex, -1) && Objects.equals(fileName.substring(extensionIndex + 1), "ser")) {
                        listView.getItems().add(fileName); //TODO: include or exclude extension?
                    }
                }
            }
        }
        String[] cat = new String[5];
    }

    /**
     * Select the Game
     * Try to load a game from the Games/Saved
     * If successful, stop any articulation and put the name of the loaded file in the selectGameLabel.
     * If unsuccessful, stop any articulation and start an entirely new game from scratch.
     * In this case, change the selectGameLabel to indicate a new game has been loaded.
     *
     * @param selectGameLabel the label to use to print errors and or successes to the user.
     * @param GameList the ListView to populate
     */
    private void selectGame(Label selectGameLabel, ListView<String> GameList) throws IOException, InterruptedException {
        //saved games will be in the Games/Saved folder!
        String SavefileName = GameList.getSelectionModel().getSelectedItem();

        // Create New Model
        AdventureGame new_model;
        try {
            new_model = this.loadGame("Games/Saved/" + SavefileName);
        }
        catch (Exception e) {
            new_model = null;
        }

        // Clear View
        this.adventureGameView.controller = null;
        this.adventureGameView.saveButton = null;
        this.adventureGameView.loadButton = null;
        this.adventureGameView.helpButton = null;
        this.adventureGameView.helpToggle = false;
        this.adventureGameView.gridPane = new GridPane();
        this.adventureGameView.roomDescLabel = new Label();
        this.adventureGameView.objectsInRoom = new VBox();
        this.adventureGameView.objectsInInventory = new VBox();
        this.adventureGameView.roomImageView = null;
        this.adventureGameView.inputTextField = null;
        this.adventureGameView.gridPane = new GridPane();
        this.adventureGameView.stopArticulation();

        // Load New Model
        if (!Objects.equals(new_model, null)) {
            selectGameLabel.setText("Selected game: " + SavefileName);
            this.adventureGameView.model = new_model;
        }
        else {
            selectGameLabel.setText("Started new game");
            this.adventureGameView.model = new AdventureGame("TinyGame");
        }

        // Init New Model
        this.adventureGameView.initUI();
    }

    /**
     * Load the Game from a file
     *
     * @param GameFile file to load
     * @return loaded Tetris Model
     */
    public AdventureGame loadGame(String GameFile) throws IOException, ClassNotFoundException {
        // Reading the object from a file
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream(GameFile);
            in = new ObjectInputStream(file);
            return (AdventureGame) in.readObject();
        } finally {
            if (in != null) {
                in.close();
                file.close();
            }
        }
    }

}
