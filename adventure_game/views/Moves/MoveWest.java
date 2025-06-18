package views.Moves;

import AdventureModel.AdventureGame;
import views.AdventureGameView;
import views.MoveDirection;

public class MoveWest implements MoveDirection
{
    public final String direction;
    AdventureGameView view;

    public MoveWest(AdventureGameView v)
    {
        view = v;
        this.direction = "West";
    }

    @Override
    public void movePlayer()
    {
        view.submitEvent("West");
        view.map.updateMap();
    }

    @Override
    public String getDirection()
    {
        return direction;
    }
}
