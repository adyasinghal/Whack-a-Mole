package game.occupants;

import javax.swing.*;
import java.awt.*;

public abstract class HoleOccupant {
    protected boolean visible;
    protected int timeRemaining;

    public HoleOccupant(int timeRemaining) {
        this.timeRemaining = timeRemaining;
        this.visible = true;
    }

    public void tick() {
        timeRemaining--;
        if (timeRemaining <= 0) hide();
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public abstract int whack();
    public abstract ImageIcon getImage();
}
