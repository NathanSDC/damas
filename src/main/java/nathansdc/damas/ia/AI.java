package main.java.nathansdc.damas.ia;

import java.util.List;
import java.util.Random;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.jogo.Board;

public class AI {
    
    private Random random = new Random();
    private double mistakeChance = 0.3;
    
    public Movement choseMovement(Board board) {
        List<Movement> playablePieces = board.getPlayablePieces();

        // TOD O: Debug active
        // System.out.println(playablePieces.size());
        
        if (playablePieces.size() == 1) {
            return playablePieces.get(0);
        }

        if (random.nextDouble() < mistakeChance) {
            return playablePieces.get(random.nextInt(playablePieces.size()));
        }

        Movement bestMovement = null;
        int bestScore = 0;

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
            App.board.movePiece(movement.getSourceX(), movement.getSourceY(), movement.getTargetX(), movement.getTargetY());
            
            App.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
