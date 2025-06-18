package views.Moves;

import AdventureModel.AdventureGame;
import views.AdventureGameView;
import views.MoveDirection;

public class MoveEast implements MoveDirection
{
    public final String direction;
    AdventureGameView view;

    public MoveEast(AdventureGameView v)
    {
        view = v;
        this.direction = "East";
    }

    @Override
    public void movePlayer()
    {
        view.submitEvent("East");
        view.map.updateMap();
    }

    @Override
    public String getDirection()
    {
        return direction;
    }
}
