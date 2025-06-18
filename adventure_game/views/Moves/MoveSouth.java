package views.Moves;

import AdventureModel.AdventureGame;
import views.AdventureGameView;
import views.MoveDirection;

public class MoveSouth implements MoveDirection
{
    public final String direction;
    AdventureGameView view;

    public MoveSouth(AdventureGameView v)
    {
        view = v;
        this.direction = "South";
    }

    @Override
    public void movePlayer()
    {
        view.submitEvent("South");
        view.map.updateMap();
    }

    @Override
    public String getDirection()
    {
        return direction;
    }
}
