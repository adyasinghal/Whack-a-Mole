package game.ui;

import game.engine.*;
import game.occupants.HoleOccupant;
import persistence.*;
import exceptions.HighScoreException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameUI extends JFrame {

    private JLabel scoreLabel = new JLabel("Score: 0");
    private JLabel timeLabel = new JLabel("Time: 30");
    private JButton[] holes = new JButton[18]; // Changed from 9 to 18

    private GameGrid grid;
    private int score = 0;

    private GameEngine engine;
    private Thread engineThread;

    private HighScoreManager highScoreManager = new HighScoreManager();
    private List<PlayerScore> scores = new ArrayList<>();

    public GameUI() {
        super("Whack-a-Mole");

        setSize(900, 550); // Adjusted window size for 3x6 grid
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadScores();

        // MAIN LAYOUT
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        root.setBackground(new Color(180, 220, 255)); // soft light blue
        add(root);

        // SCORE + TIME BAR
        JPanel topBar = new JPanel(new GridLayout(1, 2));
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));
        topBar.setBackground(new Color(0, 32, 96)); // dark navy blue

        scoreLabel.setForeground(Color.WHITE);
        timeLabel.setForeground(Color.WHITE);

        scoreLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        timeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));

        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topBar.add(scoreLabel);
        topBar.add(timeLabel);
        root.add(topBar, BorderLayout.NORTH);

        // HOLES GRID - Changed to 3 rows x 6 columns
        JPanel gridPanel = new JPanel(new GridLayout(3, 6, 15, 15));
        gridPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        gridPanel.setOpaque(false);

        for (int i = 0; i < 18; i++) { // Changed from 9 to 18
            int idx = i;

            JButton hole = new JButton();
            hole.setBackground(Color.BLACK);
            hole.setOpaque(true);
            hole.setBorderPainted(false);
            hole.setFocusPainted(false);
            hole.setPreferredSize(new Dimension(100, 100)); // Adjusted size

            // Rounded shape
            hole.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            hole.setContentAreaFilled(true);

            hole.addActionListener(e -> onHoleClick(idx));
            holes[i] = hole;
            gridPanel.add(hole);
        }

        root.add(gridPanel, BorderLayout.CENTER);

        grid = new GameGrid(18); // Changed from 9 to 18

        engine = new GameEngine(grid, this);
        engineThread = new Thread(engine);
        engineThread.start();

        // Stop game when closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                engine.stopGame();
                engineThread.interrupt();
            }
        });

        setVisible(true);
    }

    // GAMEPLAY LOGIC

    private void loadScores() {
        try {
            scores = highScoreManager.loadScores();
        } catch (HighScoreException e) {
            scores = new ArrayList<>();
        }
    }

    private void onHoleClick(int index) {
        HoleOccupant o = grid.get(index);
        if (o != null && o.isVisible()) {
            score += o.whack();
            scoreLabel.setText("Score: " + score);
            updateGrid();
        }
    }

    public void updateGrid() {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 18; i++) { // Changed from 9 to 18
                HoleOccupant o = grid.get(i);

                if (o != null && o.isVisible()) {
                    holes[i].setIcon(o.getImage());
                } else {
                    holes[i].setIcon(null);
                }
            }
        });
    }

    public void updateTime(int t) {
        SwingUtilities.invokeLater(() -> timeLabel.setText("Time: " + t));
    }

    // GAME OVER SCREEN

    public void showGameOver() {
        SwingUtilities.invokeLater(() -> {
            saveScore(score);

            int highScore = getHighScore();

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(255, 230, 150));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel over = new JLabel("GAME OVER!", SwingConstants.CENTER);
            over.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
            over.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel yourScore = new JLabel("Your Score: " + score);
            yourScore.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
            yourScore.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel high = new JLabel("High Score: " + highScore);
            high.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
            high.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton playAgain = new JButton("Play Again");
            playAgain.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            playAgain.setBackground(new Color(255, 180, 70));
            playAgain.setBorderPainted(false);
            playAgain.setFocusPainted(false);
            playAgain.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton exit = new JButton("Exit");
            exit.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            exit.setBackground(new Color(255, 140, 70));
            exit.setBorderPainted(false);
            exit.setFocusPainted(false);
            exit.setAlignmentX(Component.CENTER_ALIGNMENT);

            playAgain.addActionListener(e -> {
                dispose();
                new GameUI();
            });

            exit.addActionListener(e -> System.exit(0));

            panel.add(over);
            panel.add(Box.createVerticalStrut(20));
            panel.add(yourScore);
            panel.add(high);
            panel.add(Box.createVerticalStrut(30));
            panel.add(playAgain);
            panel.add(Box.createVerticalStrut(10));
            panel.add(exit);

            JOptionPane.showMessageDialog(
                    this,
                    panel,
                    "Game Over",
                    JOptionPane.PLAIN_MESSAGE
            );
        });
    }

    private void saveScore(int score) {
        scores.add(new PlayerScore("Player", score));
        try {
            highScoreManager.saveScores(scores);
        } catch (HighScoreException ignored) {}
    }

    private int getHighScore() {
        int max = 0;
        for (PlayerScore ps : scores) {
            if (ps.getScore() > max) max = ps.getScore();
        }
        return max;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameUI::new);
    }
}