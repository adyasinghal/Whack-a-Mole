package game.occupants;

import javax.swing.*;

public class Bomb extends HoleOccupant {
    public Bomb() {
        super(3);
    }

    @Override
    public int whack() {
        hide();
        return -500;
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon("images/bomb.png");
    }
}
