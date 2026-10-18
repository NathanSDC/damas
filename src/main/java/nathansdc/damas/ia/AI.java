package main.java.nathansdc.damas.ia;

import java.util.List;
import java.util.Random;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.jogo.Board;
import main.java.nathansdc.damas.jogo.BoardManager;
import main.java.nathansdc.damas.jogo.Movement;
import main.java.nathansdc.damas.jogo.Piece;

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

        if (playablePieces.isEmpty()) {
            return new Movement(-1, -1, -1, -1, boardManager.getBoard());
        }

        if (playablePieces.size() == 1) {
            return playablePieces.get(0);
        }

        if (random.nextDouble() < mistakeChance) {
            return playablePieces.get(random.nextInt(playablePieces.size()));
        }

        boolean allEquals = false;
        Movement preview = null;
        for (Movement movement : playablePieces) {
            if (preview == null){
                preview = movement;
            } else if (movement.getScore() == preview.getScore()) {
                allEquals = true;
            } else if (movement.getScore() != preview.getScore()) {
                allEquals = false;
                break;
            }
        }
        
        // Para que um movimento seja considerado bom de acordo com o score
        // ele tem de ser no minimo uma captura
        int bestScore = 39;
        Movement bestMovement = null;
        if (allEquals) {
            return playablePieces.get(random.nextInt(playablePieces.size()));
        } else {
            for (Movement movement : playablePieces) {
                BoardManager tempManager = new BoardManager(BoardManager.simuleNextState(boardManager.getBoard(), bestMovement));
                List<Movement> pp = tempManager.getPlayablePieces();

                if (movement.getScore() > bestScore) {
                    bestMovement = movement;    
                    bestScore = movement.getScore();
                }

                for (Movement move : pp) {
                    Board tempBoard = BoardManager.simuleNextState(tempManager.getBoard(), move);
                    int self = tempBoard.getPieceCount(Piece.PLAYER2);
                    int enemy = tempBoard.getPieceCount(Piece.PLAYER1);

                    if (self > enemy) {
                        bestMovement = movement;    
                        bestScore = movement.getScore();
                    }
                }
                System.out.println(movement);
            }
            
            if (bestMovement == null) {
                bestMovement = playablePieces.get(random.nextInt(playablePieces.size()));
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
