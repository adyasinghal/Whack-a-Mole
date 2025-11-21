package game.occupants;

import javax.swing.*;

public class BonusMole extends HoleOccupant {
    public BonusMole() {
        super(2);
    }

    @Override
    public int whack() {
        hide();
        return 1000;
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon("images/bonus.png");
    }
}
