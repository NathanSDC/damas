package main.java.nathansdc.damas.jogo;

import java.util.ArrayList;
import java.util.List;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.util.ExecutionListener;

public class BoardManager implements ExecutionListener{

    public static final int size = 8; // Default size

    List<Board> previewStates = new ArrayList<>();
    List<Board> backupStates = new ArrayList<>();

    protected Board board = new Board();

    public Board getBoard() {
        return board;
    }
    
    public int currentTurn = 1; // 1 for Player 1, 2 for Player 2

    public BoardManager() {
        // Initialize the boardManager with pieces in their starting positions
        board.populate();
    }

    public boolean restorePreviewState(){
        // Pega o indice do ultimo jogo salvo
        int index = previewStates.size() - 2;
        // Faz um backup do jogo atual
        backupStates.add(board);
        // Restaura o ultimo jogo salvo
        board = previewStates.get(index);
        // Remove o jogo atual desta lista
        previewStates.removeLast();
        return true;
    }

    public boolean restoreNextState() {
        // Faz backup do jogo atual
        previewStates.add(board);
        // Restaura o jogo que havia sido passado
        board = backupStates.getLast();
        // Remove o jogo atual desta lista
        backupStates.removeLast();
        return true;
    }

    public boolean reset() {
        board.clear();
        board.populate();
        currentTurn = 1;
        return true;
    }

    public boolean isPieceOf(int x, int y) {
        return board.getPiece(x, y) != null && board.getPiece(x, y).getCode() == currentTurn;
    }

    public void toggleTurn() {
        currentTurn = (currentTurn == 1) ? 2 : 1;
    }

    /**
     * Retorna uma matriz contendo sinalizadores indicando os tipos de movimentos
     *  possiveis disponíveis para a peça localizada nas coordenadas ({@code x}, {@code y}).
     * 
     * <br>
     * <h4>Sinalizadores:</h4>
     * <pre>
     *      0 - impossível alcançar
     *      1 - casa livre para a qual pode se mover
     *      2 - peça inimiga marcada para possivel captura
     *      3 - peça atualmente selecionada
     * </pre>
     * 
     * @param x posição {@code x} da peça no tabuleiro
     * @param y posição {@code y} da peça no tabuleiro
    */
    public Integer[][] showavailableMovements(Board board, int x, int y) {
        Integer[][] availableMovements = new Integer[size][size];
        
        for (int i = 0; i < availableMovements.length; i++) {
            for (int j = 0; j < availableMovements.length; j++) {
                availableMovements[i][j] = 0;
            }
        }

        if (board.isOutOfBounds(x, y)) {
            return availableMovements;
        }

        Piece piece = board.getPiece(x, y);

        boolean currentPlayerPiece = false;
        if (currentTurn == 1 && piece.getCode() == 1) {
            currentPlayerPiece = true;
        } else if (currentTurn == 2 && piece.getCode() == 2) {
            currentPlayerPiece = true;
        }

        if (!currentPlayerPiece || piece.getCode() == 0) {
            return availableMovements;
        }

        boolean isKing = (piece == Piece.PLAYER1_KING || piece == Piece.PLAYER2_KING);
        Piece enemyPiece = (currentTurn == 1) ? Piece.PLAYER2 : Piece.PLAYER1;
        Piece enemyKing = (currentTurn == 1) ? Piece.PLAYER2_KING : Piece.PLAYER1_KING;

        // As 4 direções diagonais possíveis (X, Y)
        int[][] directions = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            // Se for peça comum, ela só anda na direção permitida para frente (a menos que
            // seja captura, mas no tabuleiro clássico a peça comum só captura para frente)
            int allowedDirection = (currentTurn == 1) ? -1 : 1;
            if (!App.captureDirectionBoth) {
                if (!isKing && dy != allowedDirection) {
                    continue;
                }
            } else {
                if (!isKing && dy != allowedDirection) {
                    int TargetX = x + (2 * dx);
                    int TargetY = y + (2 * dy);
                    int capturedX = x + dx;
                    int capturedY = y + dy;
    
                    Movement m = new Movement(x, y, TargetX, TargetY, capturedX, capturedY, board);
                    if (m.isValid() && m.isCapture()){
                        availableMovements[capturedX][capturedY] = 2;
                        availableMovements[TargetX][TargetY] = 1;
                        availableMovements[x][y] = 3;
                    } else {
                        continue;
                    }    
                }
            }

            if (isKing) {
                int step = 1;
                boolean enemyOnWay = false;
                int enemyX = -1, enemyY = -1;

                while (true) {
                    int nx = x + (dx * step);
                    int ny = y + (dy * step);

                    if (board.isOutOfBounds(nx, ny)) {
                        break;
                    }

                    Piece current = board.getPiece(nx,ny);

                    if (!enemyOnWay) {
                        if (current == Piece.EMPTY) {
                            availableMovements[nx][ny] = 1;
                            availableMovements[x][y] = 3;
                        } else if (current == enemyPiece || current == enemyKing) {
                            // Encontrou um inimigo
                            enemyOnWay = true;
                            enemyX = nx;
                            enemyY = ny;
                        } else break; // Encontrou peça própria bloqueando a diagonal
                    } else {
                        // Se já encontramos um inimigo, as próximas casas vazias são opções de pouso da captura
                        if (current == Piece.EMPTY) {
                            availableMovements[enemyX][enemyY] = 2;
                            availableMovements[nx][ny] = 1;
                            availableMovements[x][y] = 3;
                        } else break; // Outra peça (amiga ou inimiga) bloqueando o pouso do salto
                    }
                    step++;
                }
            } else {
                // Movimento Simples
                int dxSimplified = x + dx;
                int dySimplified = y + dy;

                if (!board.isOutOfBounds(dxSimplified, dySimplified)
                        && board.isThereEmpty(dxSimplified, dySimplified)) {
                    availableMovements[dxSimplified][dySimplified] = 1;
                    availableMovements[x][y] = 3;
                }

                // Movimento de Captura
                int captureTargetX = x + (2 * dx);
                int captureTargetY = y + (2 * dy);
                int capturedX = x + dx;
                int capturedY = y + dy;

                if (!board.isOutOfBounds(captureTargetX, captureTargetY) && board.isThereEmpty(captureTargetX, captureTargetY)) {
                    Piece target = board.getPiece(capturedX, capturedY);
                    if (target == enemyPiece || target == enemyKing) {
                        availableMovements[capturedX][capturedY] = 2;
                        availableMovements[captureTargetX][captureTargetY] = 1;
                        availableMovements[x][y] = 3;
                    }
                }
            }
        }

        return availableMovements;
    }

    /**
     * 
     * Retorna uma lista contendo todos os movimentos disponíveis para o jogador do turno atual
     * 
     * @return {@link java.util.List List}<{@link main.java.nathansdc.damas.jogo.Movement Movement}>
    */
    public List<Movement> getPlayablePieces() {
        List<Movement> movements = new ArrayList<>();
        
        // Trata da IndexOuOfBoundsException lançada pelo {@code getMovements()}
        try {
            for (int i = 0; i < Board.SIZE; i++){
                for (int j = 0; j < Board.SIZE; j++) {
                    if (isPieceOf(i, j)){
                        movements.addAll(Movement.getMovements(board, i, j));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movements;
    }

    /**
     * Simula o p´róximo estado de um tabuleiro ao excutar determinado movimento
     * 
     * @param tabuleiro
    */
    public static Board simuleNextState(Board currentState, Movement movement) {
        if (currentState == null)
            return  null;
        if (movement == null)
            return currentState;

        Board nextState = new Board();
        for (int i = 0; i < Board.SIZE; i++)
            for (int j = 0; j < Board.SIZE; j++) {
                nextState.pieces[i][j] = currentState.pieces[i][j];

                if (i == movement.getSourceX() && j == movement.getSourceY()) {
                    nextState.removePiece(i, j);
                } else if (i == movement.getTargetX() && j == movement.getTargetY()) {
                    nextState.setPiece(i, j, currentState.getPiece(movement.getSourceX(), movement.getSourceY()));
                } else if (movement.isCapture() && (i == movement.getCapturedEnemyX() && movement.getCapturedEnemyY() == j)) {
                    nextState.removePiece(i, j);
                }
            }

        return nextState;
    }

    @Override
    public void executed(boolean action) {
        if (action) {
            previewStates.add(board);
            backupStates = new ArrayList<>();
            toggleTurn();
        }
    }
}