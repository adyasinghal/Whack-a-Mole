package game.occupants;

import javax.swing.*;

public class Mole extends HoleOccupant {
    public Mole() {
        super(3);
    }

    @Override
    public int whack() {
        hide();
        return 100;
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon("images/mole.png");
    }
}
