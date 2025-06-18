package views;

import AdventureModel.*;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import views.Moves.MoveEast;
import views.Moves.MoveNorth;
import views.Moves.MoveSouth;
import views.Moves.MoveWest;

public class MiniMap extends GridPane
{
    private final AdventureGameView view; //data from the game //yuh


    private final MoveNorth movenorth;
    private final MoveEast moveeast;
    private final MoveSouth movesouth;
    private final MoveWest movewest;


    private HighlightButton north; // The north button

    private HighlightButton east; // The east button

    private HighlightButton south; // The south button

    private HighlightButton west; // The west button


    GridPane grid; // The gridpane to hold the buttons

    MiniMap(AdventureGameView v)
    {
        super();
        this.view = v;

        //Three columns, three rows for the GridPane
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100);
        cc.setHalignment(HPos.CENTER);

        // Row constraints
        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100);
        rc.setValignment(VPos.CENTER);

        getColumnConstraints().addAll( cc , cc , cc );
        getRowConstraints().addAll( rc , rc , rc );

        setBorder(new Border(new BorderStroke(Color.WHITE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setGridLinesVisible(false);

        Image img = new Image(view.model.getDirectoryName() + "/gameimages/arrow.png");

        setPrefSize(3,3); // making a 3x3 grid
        setAlignment(Pos.CENTER); //set alignment

        setStyle("-fx-background: #000000;");


        movenorth = new MoveNorth(view);
        moveeast = new MoveEast(view);
        movesouth = new MoveSouth(view);
        movewest = new MoveWest(view);





        this.north = new HighlightButton(img, movenorth);// making all buttons to put into grid
        north.setId("North");
        AdventureGameView.makeButtonAccessible(north, "North Button","This button points to the north room",
                "This button points to the north room and upon activation will transport the player into the north room");
        north.setOnAction(e ->
                north.execute());
        this.add(north,1,0);
        north.setStyle("-fx-background-color:transparent;");

        this.east = new HighlightButton(img, moveeast);
        east.setId("East");
        AdventureGameView.makeButtonAccessible(east, "East Button","This button points to the east room",
                "This button points to the east room and upon activation will transport the player into the east room");
        east.setOnAction(e ->
                east.execute());
        this.add(east,2,1);
        east.setStyle("-fx-background-color:transparent;");

        this.south = new HighlightButton(img, movesouth);
        south.setId("South");
        AdventureGameView.makeButtonAccessible(south, "South Button","This button points to the south room",
                "This button points to the south room and upon activation will transport the player into the south room");
        south.setOnAction(e ->
                south.execute());
        this.add(south,1,2);
        south.setStyle("-fx-background-color:transparent;");

        this.west = new HighlightButton(img, movewest);
        west.setId("West");
        AdventureGameView.makeButtonAccessible(west, "West Button","This button points to the west room",
                "This button points to the west room and upon activation will transport the player into the west room");
        west.setOnAction(e ->
                west.execute());
        this.add(west,0,1);
        west.setStyle("-fx-background-color:transparent;");



        updateMap();// update the map once created

    }


    //check for if direction there, if so change bool to true
    //then check if direction is blocked, if so check if player has key,
        // if so change bool to true, if not change bool to false
    public void updateMap()
    {
        PassageTable directions = view.model.player.getCurrentRoom().getMotionTable();
        boolean North = false;
        boolean East = false;
        boolean South = false;
        boolean West = false;

        for (Passage passage: directions.passageTable)  //checks for all available paths
        {
            if (!passage.getIsBlocked() || (passage.getIsBlocked() && (view.model.player.getInventory().contains(passage.getKeyName()))))
            {
                switch (passage.getDirection()) {
                    case "NORTH" -> {
                        North = true;
                    }
                    case "EAST" -> {
                        East = true;
                    }
                    case "SOUTH" -> {
                        South = true;
                    }
                    case "WEST" -> {
                        West = true;
                    }
                }


            }
        }

        north.setVisible(North);
        east.setVisible(East);
        south.setVisible(South);
        west.setVisible(West);

    }
}
