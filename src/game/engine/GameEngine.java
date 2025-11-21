package game.engine;

import game.occupants.*;
import game.ui.GameUI;
import java.util.Random;

public class GameEngine implements Runnable {

    private final GameGrid grid;
    private final GameUI ui;
    private final Random rand = new Random();
    private volatile boolean running = true;

    private int timeRemaining = 30;

    public GameEngine(GameGrid grid, GameUI ui) {
        this.grid = grid;
        this.ui = ui;
    }

    @Override
    public void run() {
        try {
            while (running && timeRemaining > 0) {
                timeRemaining--;
                ui.updateTime(timeRemaining);

                grid.tickAll();
                spawnRandom();

                ui.updateGrid();

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            running = false;
        }

        ui.showGameOver();
    }

    private void spawnRandom() {
        int index;

        // find an empty hole
        do {
            index = rand.nextInt(18);
        } while (grid.get(index) != null);

        int type = rand.nextInt(3);

        switch (type) {
            case 0 -> grid.spawn(index, new Mole());
            case 1 -> grid.spawn(index, new Bomb());
            case 2 -> grid.spawn(index, new BonusMole());
        }
    }

    public void stopGame() {
        running = false;
    }
}