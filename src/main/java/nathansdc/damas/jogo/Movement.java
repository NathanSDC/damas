package main.java.nathansdc.damas.jogo;

import java.util.ArrayList;
import java.util.List;

import main.java.nathansdc.damas.App;

public class Movement {

    private int sourceX;
    private int sourceY;

    private int targetX;
    private int targetY;

    private int capturedEnemyX = -1;
    private int capturedEnemyY = -1;

    private boolean isCapture;
    private boolean isPromotion;
    private boolean isValid = false;

    private int score = 0;

    private Board board;

    private Movement() {
    }

    public Movement(int sourceX, int sourceY, int targetX, int targetY, Board board) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;

        if (board == null) {
            throw new NullPointerException();
        }

        this.board = board;

        valide();
        calculeScore(board);
    }

    public Movement(int sourceX, int sourceY, int targetX, int targetY, int capturedEnemyX, int capturedEnemyY,
            Board board) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.capturedEnemyX = capturedEnemyX;
        this.capturedEnemyY = capturedEnemyY;

        if (board == null) {
            throw new NullPointerException();
        }

        this.board = board;

        valide();
        calculeScore(board);
    }

    private void calculeScore(Board board) {
        int score = 0;

        if (isCapture) {
            Piece enemy = board.getPiece(capturedEnemyX, capturedEnemyY);
            boolean isKing = (enemy == Piece.PLAYER1_KING || enemy == Piece.PLAYER2_KING);

            if (!isKing) {
                // Adiciona +50 ao capturar uma peça comum inimiga
                score += 30;
            } else {
                // Adiciona +150 ao capturar uma dama inimiga
                score += 150;
            }

        }

        // Adiciona +100 se a peça se tornar dama com a conclusão deste movimento
        Piece self = board.getPiece(sourceX, sourceY);
        // Verifica se a peça atualmente não é uma dama
        if (!(self == Piece.PLAYER1_KING || self == Piece.PLAYER2_KING)) {

            // Adiciona +5 por casa movida
            score += (int) Math.sqrt(Math.pow(targetX - sourceX, 2) + Math.pow(targetY - sourceY, 2)) * 5;

            // Testa se esta peça faz parte do repertório do player 1
            if (self.getCode() == 1) {
                if (targetY == 0) {
                    score += 100;
                    isPromotion = true;
                }
            } else {
                if (targetY == 7) {
                    score += 100;
                    isPromotion = true;
                }
            }
        } else {
            // Para damas adiciona +1 por casa movida
            score += (int) Math.sqrt(Math.pow(targetX - sourceX, 2) + Math.pow(targetY - sourceY, 2)) * 1;
        }

        this.score = score;
    }

    /**
     * 
     * Calcula de acordo com as regras do jogo estabelecidas ativas atualmente para
     * quais casas a peça que se encontra nas posições:
     * {@code sourceX} {@code sourceY}, referente ao
     * {@link main.java.nathansdc.damas.jogo.Board Board}, pode se
     * mover.
     * 
     * @param sourceX posição {@code X}, no
     *                {@link main.java.nathansdc.damas.jogo.Board Board}, da peça
     *                que será tratada
     * @param sourceY posição {@code Y}, no
     *                {@link main.java.nathansdc.damas.jogo.Board Board}, da peça
     *                que será tratada
     * 
     * @return {@code List<Movement>} uma lista contendo todos os movimentos
     *         possiveis para a peça na posição de origem
     */
    public static List<Movement> getMovements(Board board, int sourceX, int sourceY, int currentTurn) throws IndexOutOfBoundsException {
        List<Movement> allowedMoves = new ArrayList<>();

        if (board.isOutOfBounds(sourceX, sourceY)) {
            throw new IndexOutOfBoundsException();
        }

        // Pega diretamente a peça do tabuleiro em uso atualmente
        Piece sourcePiece = board.getPiece(sourceX, sourceY);

        // Simplesmente retorna a lista vazia, pode mudar para um Throwable depois
        if (sourcePiece == Piece.EMPTY)
            return allowedMoves;

        boolean isKing = (sourcePiece == Piece.PLAYER1_KING || sourcePiece == Piece.PLAYER2_KING);

        // marcar diretamente as peças inimigas para menor repetiçao de código mais a
        // frente
        Piece enemysPiece = (currentTurn == 1) ? Piece.PLAYER2 : Piece.PLAYER1;
        Piece enemysKing = (currentTurn == 1) ? Piece.PLAYER2_KING : Piece.PLAYER1_KING;

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

                    if (board.isOutOfBounds(targetX, targetY))
                        break; // Atingiu a borda do tabuleiro

                    Piece targetTile = board.getPiece(targetX, targetY);

                    Movement move = new Movement();
                    move.sourceX = sourceX;
                    move.sourceY = sourceY;

                    if (!encounteredAnEnemy) {
                        if (targetTile == Piece.EMPTY) { // casa livre
                            move.targetX = targetX;
                            move.targetY = targetY;
                            move.isValid = true;

                            move.calculeScore(board);

                            allowedMoves.add(move);
                        } else if (targetTile == enemysPiece || targetTile == enemysKing) {
                            encounteredAnEnemy = true;
                            enemysX = targetX;
                            enemysY = targetY;
                        } else
                            break; // Bloqueado por peça aliada
                    } else {
                        if (targetTile == Piece.EMPTY) {
                            move.targetX = targetX;
                            move.targetY = targetY;
                            move.capturedEnemyX = enemysX;
                            move.capturedEnemyY = enemysY;

                            move.isCapture = true;
                            move.isValid = true;
                            move.calculeScore(board);

                            allowedMoves.add(move);
                        } else
                            break; // Bloqueado por peça aliada
                    }

                    step++;
                }
            } else {

                Movement move = new Movement();
                move.sourceX = sourceX;
                move.sourceY = sourceY;

                int allowedDirection = (currentTurn == 1) ? -1 : 1;
                if (!App.captureDirectionBoth) {
                    if (!isKing && dy != allowedDirection) {
                        continue;
                    }
                } else {
                    if (!isKing && dy != allowedDirection) {
                        int targetX = sourceX + (2 * dx);
                        int targetY = sourceY + (2 * dy);
                        int enemysX = sourceX + dx;
                        int enemysY = sourceY + dy;

                        if (!board.isOutOfBounds(targetX, targetY)
                                && board.isThereEmpty(targetX, targetY)) {
                            Piece target = board.getPiece(enemysX, enemysY);
                            if (target == enemysPiece || target == enemysKing) {

                                move.targetX = targetX;
                                move.targetY = targetY;
                                move.capturedEnemyX = enemysX;
                                move.capturedEnemyY = enemysY;

                                move.isCapture = true;
                                move.isValid = true;
                                move.calculeScore(board);

                                allowedMoves.add(move);
                            }
                        }
                        continue;
                    }
                }

                int targetXA = sourceX + dx;
                int targetYA = sourceY + dy;

                if (!board.isOutOfBounds(targetXA, targetYA)
                        && board.isThereEmpty(targetXA, targetYA)) {
                    move.targetX = targetXA;
                    move.targetY = targetYA;
                    move.calculeScore(board);
                    move.isValid = true;

                    allowedMoves.add(move);
                }

                int targetX = sourceX + (2 * dx);
                int targetY = sourceY + (2 * dy);
                int enemysX = sourceX + dx;
                int enemysY = sourceY + dy;

                if (!board.isOutOfBounds(targetX, targetY)
                        && board.isThereEmpty(targetX, targetY)) {
                    Piece target = board.getPiece(enemysX, enemysY);
                    if (target == enemysPiece || target == enemysKing) {

                        move.targetX = targetX;
                        move.targetY = targetY;
                        move.capturedEnemyX = enemysX;
                        move.capturedEnemyY = enemysY;

                        move.isCapture = true;
                        move.isValid = true;
                        move.calculeScore(board);

                        allowedMoves.add(move);
                    }
                }

            }
        }

        return allowedMoves;
    }

    private void valide() {

        if (board.isOutOfBounds(sourceX, sourceY)) {
            throw new IndexOutOfBoundsException();
        }

        // Pega diretamente a peça do tabuleiro em uso atualmente
        Piece sourcePiece = board.getPiece(sourceX, sourceY);

        // Simplesmente retorna a lista vazia, pode mudar para um Throwable depois
        if (sourcePiece == Piece.EMPTY)
            isValid = false;

        // Análise vetorial do movimento
        int diffLinha = targetX - sourceX;
        int diffColuna = targetY - sourceY;

        // Movimentos válidos precisam ser perfeitamente diagonais (passo de linha igual
        // ao de coluna)
        if (Math.abs(diffLinha) != Math.abs(diffColuna)) {
            isValid = false;
            return;
        }

        int dx = diffLinha > 0 ? 1 : -1;
        int dy = diffColuna > 0 ? 1 : -1;

        boolean isKing = (sourcePiece == Piece.PLAYER1_KING || sourcePiece == Piece.PLAYER2_KING);

        // marcar diretamente as peças inimigas para menor repetiçao de código mais a
        // frente
        Piece enemysPiece = (App.boardManager.currentTurn == 1) ? Piece.PLAYER2 : Piece.PLAYER1;
        Piece enemysKing = (App.boardManager.currentTurn == 1) ? Piece.PLAYER2_KING : Piece.PLAYER1_KING;

        if (isKing) {

            int step = 1;
            boolean encounteredAnEnemy = false;

            while (true) {
                int nx = sourceX + (dx * step);
                int ny = sourceY + (dy * step);

                // Se chegou ao destino antes de quebrar as regras, o caminho é válido
                if (nx == targetX && ny == targetY) {
                    isValid = true;
                    break;
                }

                if (board.isOutOfBounds(nx, ny)) {
                    isValid = false;
                    break;
                }

                Piece currentPiece = board.getPiece(nx, ny);

                if (currentPiece != Piece.EMPTY) {
                    // Se encontrar uma segunda peça no caminho ou uma peça aliada, o movimento é
                    // bloqueado
                    if (encounteredAnEnemy || (currentPiece != enemysPiece && currentPiece != enemysKing)) {
                        isValid = false;
                        break;
                    } else {
                        isCapture = true;
                    }
                    encounteredAnEnemy = true;
                    // Primeira peça inimiga encontrada
                }
                step++;

            }
        } else {
            int allowedDirection = (App.boardManager.currentTurn == 1) ? -1 : 1;
            if (!App.captureDirectionBoth) {
                if (!isKing && dy != allowedDirection) {
                    isValid = false;
                    return;
                }
            } else {
                if (!isKing && dy != allowedDirection) {
                    int targetX = sourceX + (2 * dx);
                    int targetY = sourceY + (2 * dy);
                    int enemysX = sourceX + dx;
                    int enemysY = sourceY + dy;

                    if (!board.isOutOfBounds(targetX, targetY) && board.isThereEmpty(targetX, targetY)) {
                        Piece target = board.getPiece(enemysX, enemysY);
                        if (target == enemysPiece || target == enemysKing) {
                            isValid = true;
                            isCapture = true;
                        } else {
                            isValid = false;
                        }
                    } else {
                        isValid = false;
                    }
                    return;
                }
            }

            int targetXA = sourceX + dx;
            int targetYA = sourceY + dy;

            if (!board.isOutOfBounds(targetXA, targetYA) && board.isThereEmpty(targetXA, targetYA)) {
                isValid = true;
                return;
            }

            int targetX = sourceX + (2 * dx);
            int targetY = sourceY + (2 * dy);
            int enemysX = sourceX + dx;
            int enemysY = sourceY + dy;

            if (!board.isOutOfBounds(targetX, targetY) && board.isThereEmpty(targetX, targetY)) {
                Piece target = board.getPiece(enemysX, enemysY);
                if (target == enemysPiece || target == enemysKing) {
                    isValid = true;
                    isCapture = true;
                } else {
                    isValid = false;
                }
            } else {
                isValid = false;
            }
        }
    }

    public static boolean execute(Movement movement, BoardManager manager) {
        if (!movement.isValid) {
            App.executionListener.executed(false);
            return false;
        }

        Board test = manager.getBoard();
        test.movePiece(movement, movement.isCapture);

        if (movement.isPromotion) {
            Piece current = test.getPiece(movement.targetX, movement.targetY);
            if (current == Piece.PLAYER1 || current == Piece.PLAYER2) {
                Piece king = (current == Piece.PLAYER1) ? Piece.PLAYER1_KING : Piece.PLAYER2_KING;
                test.setPiece(movement.targetX, movement.targetY, king);
            } else {
                App.executionListener.executed(false);
                return false;
            }
        }

        manager.board = test;
        App.executionListener.executed(true);
        return true;
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
     */
    public int getScore() {
        return score;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    /**
     * <p>
     * Se um {@code Movement} não for válido ele não pode ser executado utilizando a
     * função {@link #execute}.
     * <p>
     * Logo na criação de um {@code Movement} utilizando os construtores públicos é
     * feita a veificação de validez.
     * Caso as informações passadas não atendam as regras estabelecidas o
     * {@code Movement} é considerado inválido.
     * 
     * <pre>
     * 1 -
     */
    public boolean isValid() {
        return isValid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + sourceX;
        result = prime * result + sourceY;
        result = prime * result + targetX;
        result = prime * result + targetY;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movement other = (Movement) obj;
        if (sourceX != other.sourceX)
            return false;
        if (sourceY != other.sourceY)
            return false;
        if (targetX != other.targetX)
            return false;
        if (targetY != other.targetY)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Movement [sourceX=" + sourceX + ", sourceY=" + sourceY + ", targetX=" + targetX + ", targetY=" + targetY
                + ", capturedEnemyX=" + capturedEnemyX + ", capturedEnemyY=" + capturedEnemyY + ", isCapture="
                + isCapture + ", isPromotion=" + isPromotion + ", isValid=" + isValid + ", score=" + score + "]";
    }

}
