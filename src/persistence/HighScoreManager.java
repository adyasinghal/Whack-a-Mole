package persistence;

import exceptions.HighScoreException;
import java.io.*;
import java.util.*;

public class HighScoreManager {
    private final String FILE = "scores.dat";

    public void saveScores(List<PlayerScore> scores) throws HighScoreException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(scores);
        } catch (IOException e) {
            throw new HighScoreException("Error saving scores", e);
        }
    }

    public List<PlayerScore> loadScores() throws HighScoreException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            return (List<PlayerScore>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new HighScoreException("Error loading scores", e);
        }
    }
}
