package main.java.nathansdc.damas.ia;

import java.util.List;
import java.util.Random;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.jogo.BoardManager;
import main.java.nathansdc.damas.jogo.Movement;

public class AI {

    public enum Difficulty {
        EASY(0.7),
        NORMAL(0.3),
        HARD(0);

        double value;

        private Difficulty(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }

    private Random random = new Random();
    private double mistakeChance = 0.3;

    public AI(Difficulty difficulty) {
        mistakeChance = difficulty.getValue();
    }

    public Movement choseMovement(BoardManager boardManager) {
        List<Movement> playablePieces = boardManager.getPlayablePieces();

        if (playablePieces.size() == 1) {
            return playablePieces.get(0);
        }

        if (random.nextDouble() < mistakeChance) {
            return playablePieces.get(random.nextInt(playablePieces.size()));
        }

        
        boolean allEquals = false;
        Movement preview = null;
        for (Movement movement : playablePieces) {
            if (movement.equals(preview)) {
                allEquals = true;
            } else if (preview == null) {
                preview = movement;
                allEquals = false;
            } else if (!movement.equals(preview)) {
                allEquals = false;
                break;
            }
        }
        
        Movement bestMovement = null;
        int bestScore = 0;
        if (allEquals) {
            return playablePieces.get(random.nextInt(playablePieces.size()));
        } else
            for (Movement movement : playablePieces) {
                if (movement.getScore() > bestScore) {
                    bestScore = movement.getScore();
                    bestMovement = movement;
                }
            }

        return bestMovement;
    }

    public void execute(Movement movement) {
        try {
            Movement.execute(movement, App.boardManager);
            App.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
