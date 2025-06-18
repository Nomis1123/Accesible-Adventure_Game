package views.Moves;

import AdventureModel.AdventureGame;
import views.AdventureGameView;
import views.MoveDirection;

public class MoveNorth implements MoveDirection
{

    public final String direction;
    AdventureGameView view;

    public MoveNorth (AdventureGameView v)
    {
        view = v;

        //gets data from selected game
        this.direction = "North";

    }

    @Override
    public void movePlayer()
    {
        view.submitEvent("North");
        view.map.updateMap();
    }

    public String getDirection()
    {
        return direction;
    }
}
