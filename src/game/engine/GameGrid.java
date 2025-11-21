package game.engine;

import game.occupants.HoleOccupant;
import exceptions.InvalidGameStateException;

public class GameGrid {

    private HoleOccupant[] grid;

    public GameGrid(int size) {
        grid = new HoleOccupant[size];
    }

    public synchronized HoleOccupant get(int index) {
        return grid[index];
    }

    public synchronized void spawn(int index, HoleOccupant occupant) {
        if (grid[index] != null) {
            throw new InvalidGameStateException("Hole already occupied!");
        }
        grid[index] = occupant;
    }

    public synchronized void tickAll() {
        for (int i = 0; i < grid.length; i++) {
            HoleOccupant o = grid[i];
            if (o != null) {
                o.tick();
                if (!o.isVisible()) {
                    grid[i] = null; // FREE THE HOLE
                }
            }
        }
    }
}