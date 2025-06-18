package views;

import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;

import java.awt.*;

public class HighlightButton extends DirectionButton
{

    private boolean onOverride;

    public HighlightButton(Image i, MoveDirection dir)
    {
        super(i, dir);
        init();


    }

    public HighlightButton()
    {
        super();
        init();


    }

    public HighlightButton(String s)
    {
        super(s);
        init();
    }


    public void init()
    {
        setOnMouseEntered(e ->
                setHighlight(true));

        setOnMouseExited(e ->
                setHighlight(false));
    }

    public void setHighlight(boolean on)
    {
        if (on || this.onOverride)
        {
            Object CornerRadii;
            setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.WHITE,
                    BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
        }
        else
        {
            setBorder(Border.EMPTY);

        }
    }

    public void setHighlightOverride(boolean on) {
        this.onOverride = on;
        this.setHighlight(on);
    }


}
