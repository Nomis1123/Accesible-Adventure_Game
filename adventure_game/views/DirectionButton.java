package views;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DirectionButton extends Button
{
    Image img; // Image displayed on the button
    ImageView view; // Will be the view for the button
    MoveDirection direction; // will be the direction placed upon the button


    public DirectionButton(Image i, MoveDirection dir)
    {
        super();

        img = i;
        direction = dir;
        view = new ImageView(img);
        view.setFitWidth(25);
        view.setFitHeight(25);
        view.setPreserveRatio(false);
        setGraphic(view);


        switch (direction.getDirection())  // rotates the view depending on the direction
        {
            case "North" ->
            {
                view.setRotate(270);
            }
            case "East" ->
            {
                view.setRotate(0);
            }
            case "South" ->
            {
                view.setRotate(90);
            }
            case "West" ->
            {
                view.setRotate(180);
            }
        }
    }

    public DirectionButton()
    {
        super();
    }

    public DirectionButton(String s)
    {
        super(s);
    }

    public void execute()
    {
        direction.movePlayer();
    }

}
