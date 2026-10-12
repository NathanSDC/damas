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

    private int capturedEnemyX = -1;
    private int capturedEnemyY = -1;

    private boolean isCapture;

    private int score = 0;

    private Movement() {
    }

    private Movement(int sourceX, int sourceY, int targetX, int targetY) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    private void calculeScore() {
        int score = 10;

        // Adiciona +10 para cada casa movida
        score += (int) Math.sqrt(Math.pow(targetX - sourceX, 2) + Math.pow(targetY - sourceY, 2)) * 5;
        
        if (isCapture) {
            PieceType enemy = App.board.getPieceAt(capturedEnemyX, capturedEnemyY);
            boolean isKing = (enemy == Board.PieceType.PLAYER1_KING || enemy == Board.PieceType.PLAYER2_KING);
            
            if (!isKing) {
                // Adiciona +50 ao capturar uma peça comum inimiga
                score += 50;
            } else {
                // Adiciona +150 ao capturar uma dama inimiga
                score += 150;
            }
            
        }
        
        // Adiciona +100 se a peça se tornar dama com a conclusão deste movimento
        PieceType self = App.board.getPieceAt(sourceX, sourceY);
        // Verifica se a peça atualmente não é uma dama
        if (!(self == Board.PieceType.PLAYER1_KING || self == Board.PieceType.PLAYER2_KING)) {
            // Testa se esta peça faz parte do repertório do player 1
            if (self.getCode() == 1) {
                if (targetY == 0) {
                    score += 100;
                }
            } else {
                if (targetY == 7) {
                    score += 100;
                }
            }
        }
        
        this.score = score;
    }

    /**
     * 
     * Calcula de acordo com as regras do jogo estabelecidas ativas atualmente para
     * quais casas a peça que se encontra nas posições:
     * {@code sourceX} {@code sourceY}, referente ao
     * {@link main.java.nathansdc.damas.jogo.Board Board}, pode se mover.
     * 
     * @param sourceX posição {@code X} no
     *                {@link main.java.nathansdc.damas.jogo.Board Board} da peça que
     *                será tratada
     * @param sourceY posição {@code Y} no
     *                {@link main.java.nathansdc.damas.jogo.Board Board} da peça que
     *                será tratada
     * 
     * @return {@code List<Movement>} uma lista contendo todos os movimentos
     *         possiveis para a peça na posição de origem
     */
    public static List<Movement> getMovements(int sourceX, int sourceY) throws IndexOutOfBoundsException {
        List<Movement> allowedMoves = new ArrayList<>();

        if (App.board.isOutOfBounds(sourceX, sourceY)) {
            throw new IndexOutOfBoundsException();
        }

        // Pega diretamente a peça do tabuleiro em uso atualmente
        PieceType sourcePiece = App.board.getPieceAt(sourceX, sourceY);

        // Simplesmente retorna a lista vazia, pode mudar para um Throwable depois
        if (sourcePiece == PieceType.EMPTY)
            return allowedMoves;

        boolean isKing = (sourcePiece == PieceType.PLAYER1_KING || sourcePiece == PieceType.PLAYER2_KING);

        // marcar diretamente as peças inimigas para menor repetiçao de código mais a
        // frente
        PieceType enemysPiece = (App.board.currentTurn == 1) ? PieceType.PLAYER2 : PieceType.PLAYER1;
        PieceType enemysKing = (App.board.currentTurn == 1) ? PieceType.PLAYER2_KING : PieceType.PLAYER1_KING;

        // As quatro direções de movimento possiveis para uma peça {x, y}
        int[][] directions = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            if (isKing) {

                int step = 1;
                boolean encounteredAnEnemy = false;
                int enemysX = -1;
                int enemysY = -1;

                while (true) {
                    int targetX = sourceX + (dx * step);
                    int targetY = sourceY + (dy * step);

                    if (App.board.isOutOfBounds(targetX, targetY))
                        break; // Atingiu a borda do tabuleiro

                    PieceType targetTile = App.board.getPieceAt(targetX, targetY);

                    Movement move = new Movement();
                    move.sourceX = sourceX;
                    move.sourceY = sourceY;

                    if (!encounteredAnEnemy) {
                        if (targetTile == PieceType.EMPTY) { // casa livre
                            move.targetX = targetX;
                            move.targetY = targetY;

                            move.calculeScore();

                            allowedMoves.add(move);
                        } else if (targetTile == enemysPiece || targetTile == enemysKing) {
                            encounteredAnEnemy = true;
                            enemysX = targetX;
                            enemysY = targetY;
                        } else
                            break; // Bloqueado por peça aliada
                    } else {
                        if (targetTile == PieceType.EMPTY) {
                            move.targetX = targetX;
                            move.targetY = targetY;
                            move.capturedEnemyX = enemysX;
                            move.capturedEnemyY = enemysY;

                            move.isCapture = true;
                            move.calculeScore();;

                            allowedMoves.add(move);
                        } else
                            break; // Bloqueado por peça aliada
                    }

                    step++;
                }
            } else {
                int allowedMoveDirection = App.board.currentTurn == 1 ? -1 : 1;

                Movement move = new Movement();
                move.sourceX = sourceX;
                move.sourceY = sourceY;

                if (dx == allowedMoveDirection) {
                    int targetX = sourceX + dx;
                    int targetY = sourceY + dy;

                    if (!App.board.isOutOfBounds(targetX, targetY) && App.board.isEmptyTarget(targetX, targetY)) {
                        move.targetX = targetX;
                        move.targetY = targetY;
                        move.calculeScore();

                        allowedMoves.add(move);
                    }
                }

                int targetX = sourceX + (2 * dx);
                int targetY = sourceY + (2 * dy);
                int enemysX = sourceX + dx;
                int enemysY = sourceY + dy;

                if (!App.board.isOutOfBounds(targetX, targetY) && App.board.isEmptyTarget(targetX, targetY)) {
                    PieceType target = App.board.getPieceAt(targetX, targetY);
                    if (target == enemysPiece || target == enemysKing) {

                        move.targetX = targetX;
                        move.targetY = targetY;
                        move.capturedEnemyX = enemysX;
                        move.capturedEnemyY = enemysY;

                        move.isCapture = true;
                        move.calculeScore();

                        allowedMoves.add(move);
                    }
                }

            }
        }

        return allowedMoves;
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

    public int getCapturedEnemyX() {
        return capturedEnemyX;
    }

    public void setCapturedEnemyX(int capturedEnemyX) {
        this.capturedEnemyX = capturedEnemyX;
    }

    public int getCapturedEnemyY() {
        return capturedEnemyY;
    }

    public void setCapturedEnemyY(int capturedEnemyY) {
        this.capturedEnemyY = capturedEnemyY;
    }

    public boolean isCapture() {
        return isCapture;
    }

    /**
     * O score aumenta de acordo com o tipo de movimento, se no movimento for
     * possivel capturar uma peça do oponenete
     * ele será maior do que simplesmente mover mais casas no tabuleiro
     * 
     * <pre>
     * +5 por casa movida
     * +50 por captura normal
     * +100 por nova dama
     * +150 por captura de dama
     * </pre>
     * 
     * 
     */
    public int getScore() {
        return score;
    }

}
