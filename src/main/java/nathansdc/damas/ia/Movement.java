package main.java.nathansdc.damas.ia;

import java.util.ArrayList;
import java.util.List;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.jogo.Board;
import main.java.nathansdc.damas.jogo.Board.PieceType;

public class Movement {
    
    private int sourceX;
    private int sourceY;

    private int targetX;
    private int targetY;

    /**
     * O score aumenta de acordo com o tipo de movimento, se no movimento for possivel capturar uma peça do oponenete
     * ele será maior doq simplesmente mover mais casas no tabuleiro
     * 
     * +10 por mover a peça
     * +50 por captura normal
     * +100 por nova dama
     * +150 por captura de dama
    */
    private int score = 0;
    
    public Movement(){

    }

    public Movement(int sourceX, int sourceY, int targetX, int targetY) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    private static int calculeScore(int sourceX, int sourceY, int targetX, int targetY){
        int score = 10;

        PieceType source = App.board.getPieceAt(sourceX, sourceY);
        // PieceType target = App.board.getPieceAt(targetX, targetY);

        boolean isKing = (source == Board.PieceType.PLAYER1_KING || source == Board.PieceType.PLAYER2_KING);
        double diff = Math.sqrt(Math.pow(targetX-sourceX, 2) + Math.pow(targetY - sourceY, 2));

        if (diff < 1.5 && !isKing) {
            score += 10;
        } else if (diff >= 1.5 && !isKing) {
            score += 50;
        } else if (isKing) {
            score += (int) (diff * 5);
        }

        if (!isKing) {
            score += (7 - targetY) * 3;
        }

        return score;
    }
    
    public static List<Movement> getMovements(int sourceX, int sourceY) {
        Integer[][] possibleMove = App.board.showavailableMovements(sourceX, sourceY);
        List<Movement> movements = new ArrayList<>();

        for (int i = 0; i < possibleMove.length; i++) {
            for (int j = 0; j < possibleMove[i].length; j++) {
                if (possibleMove[i][j] == 1) {
                    Movement m = new Movement();

                    m.setSourceX(sourceX);
                    m.setSourceY(sourceY);

                    m.setTargetX(i);
                    m.setTargetY(j);

                    m.setScore(calculeScore(sourceX, sourceY, i, j));

                    movements.add(m);
                }
            }
        }

        return movements;
    }

    public int getSourceX() {
        return sourceX;
    }

    public void setSourceX(int sourceX) {
        this.sourceX = sourceX;
    }

    public int getSourceY() {
        return sourceY;
    }

    public void setSourceY(int sourceY) {
        this.sourceY = sourceY;
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
