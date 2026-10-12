package main.java.nathansdc.damas.jogo;

import java.util.ArrayList;
import java.util.List;

import main.java.nathansdc.damas.ia.Movement;

public class Board {

    public enum PieceType {
        EMPTY(0),
        PLAYER1(1),
        PLAYER2(2),
        PLAYER1_KING(1),
        PLAYER2_KING(2);

        int code;

        public int getCode() {
            return code;
        }

        private PieceType(int code) {
            this.code = code;
        }
    }

    public static final int size = 8; // Default size

    PieceType[][] board = new PieceType[size][size];

    public PieceType[][] getBoard() {
        return board;
    }

    public int currentTurn = 1; // 1 for Player 1, 2 for Player 2

    public Board() {
        // Initialize the board with pieces in their starting positions
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + j) % 2 != 0) { // Only place pieces on light squares
                    if (j < 3) {
                        board[i][j] = PieceType.PLAYER2;
                    } else if (j > 4) {
                        board[i][j] = PieceType.PLAYER1;
                    } else {
                        board[i][j] = PieceType.EMPTY; // Empty square
                    }
                } else {
                    board[i][j] = PieceType.EMPTY; // Empty square
                }
            }
        }
    }

    /**
     * A quantidade de peças que o jogador 1 capturou ate o momento
    */
    public int getPlayer1CapturedPieces() {
        return 12 - getPlayer2PieceCount();
    }

    /**
     * A quantidade de peças que o jogador 2 capturou ate o momento
    */
    public int getPlayer2CapturedPieces() {
        return 12 - getPlayer1PieceCount();
    }

    public int getPlayer1PieceCount() {
        int iterator = 0;
        for (PieceType[] pieceTypes : board)
            for (PieceType pieceType : pieceTypes)
                if (pieceType.code == 1)
                    iterator++;
        return iterator;
    }

    public int getPlayer2PieceCount() {
        int iterator = 0;
        for (PieceType[] pieceTypes : board)
            for (PieceType pieceType : pieceTypes)
                if (pieceType.code == 2)
                    iterator++;
        return iterator;
    }

    public boolean resetBoard() {
        // Initialize the board with pieces in their starting positions
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + j) % 2 != 0) { // Only place pieces on dark squares
                    if (j < 3) {
                        board[i][j] = PieceType.PLAYER2;
                    } else if (j > 4) {
                        board[i][j] = PieceType.PLAYER1;
                    } else {
                        board[i][j] = PieceType.EMPTY; // Empty square
                    }
                } else {
                    board[i][j] = PieceType.EMPTY; // Empty square
                }
            }
        }

        currentTurn = 1;

        return true;
    }

    public boolean isOutOfBounds(int l, int c) {
        return l < 0 || l >= size || c < 0 || c >= size;
    }

    public boolean isPieceOf(int l, int c) {
        return board[l][c] != null && board[l][c].getCode() == currentTurn;
    }

    public boolean isEmptyTarget(int l, int c) {
        return board[l][c] == PieceType.EMPTY;
    }

    public boolean isSimpleMoveValid(int diffX, int diffY, int allowedDirection) {
        return (diffX == allowedDirection || diffX == 0) && diffY == 1;
    }

    public boolean isCaptureMoveValid(int diffX, int diffY, int allowedDirection) {
        return diffX == 2 * allowedDirection && diffY == 2;
    }

    private boolean validarCaminhoDama(int sourceX, int sourceY, int targetX, int targetY, int dx, int dy) {
        int step = 1;
        boolean enemyOnWay = false;

        PieceType enemyPiece = (currentTurn == 1) ? PieceType.PLAYER2 : PieceType.PLAYER1;
        PieceType enemyKing = (currentTurn == 1) ? PieceType.PLAYER2_KING : PieceType.PLAYER1_KING;

        while (true) {
            int nx = sourceX + (dx * step);
            int ny = sourceY + (dy * step);

            // Se chegou ao destino antes de quebrar as regras, o caminho é válido
            if (nx == targetX && ny == targetY) {
                return true;
            }

            if (isOutOfBounds(nx, ny))
                return false;

            PieceType currentPiece = board[nx][ny];

            if (currentPiece != PieceType.EMPTY) {
                // Se encontrar uma segunda peça no caminho ou uma peça aliada, o movimento é
                // bloqueado
                if (enemyOnWay || (currentPiece != enemyPiece && currentPiece != enemyKing)) {
                    return false;
                }
                enemyOnWay = true; // Primeira peça inimiga encontrada
            }
            step++;
        }
    }

    private void kingsCaptureMove(int sourceX, int sourceY, int targetX, int targetY, int dx, int dy) {
        int step = 1;
        while (true) {
            int nx = sourceX + (dx * step);
            int ny = sourceY + (dy * step);

            if (nx == targetX && ny == targetY)
                break;

            // Limpa qualquer peça inimiga que encontrar pelo caminho
            if (board[nx][ny] != PieceType.EMPTY) {
                board[nx][ny] = PieceType.EMPTY;
                break; // remove apenas uma peça por diagonal por salto
            }
            step++;
        }
    }

    private void simpleMove(int sourceX, int sourceY, int targetX, int targetY) {
        board[targetX][targetY] = board[sourceX][sourceY];
        board[sourceX][sourceY] = PieceType.EMPTY;
    }

    private void captureMove(int sourceX, int sourceY, int targetX, int targetY, int capturedX, int capturedY) {
        board[targetX][targetY] = board[sourceX][sourceY];
        board[sourceX][sourceY] = PieceType.EMPTY;
        board[capturedX][capturedY] = PieceType.EMPTY;
    }

    public void toggleTurn() {
        currentTurn = (currentTurn == 1) ? 2 : 1;
    }

    public PieceType getPieceAt(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return null; // Out of bounds
        }
        return board[x][y];
    }

    public boolean movePiece(int sourceX, int sourceY, int targetX, int targetY) {
        if (isOutOfBounds(sourceX, sourceY) || isOutOfBounds(targetX, targetY)) {
            return false;
        }

        PieceType piece = board[sourceX][sourceY];
        boolean allayPiece = false;
        if (currentTurn == 1 && (piece.getCode() == 1))
            allayPiece = true;
        else if (currentTurn == 2 && (piece.getCode() == 2))
            allayPiece = true;

        if (!allayPiece) {
            return false;
        }

        if (!isEmptyTarget(targetX, targetY)) {
            return false;
        }

        // 2. Análise vetorial do movimento
        int diffLinha = targetX - sourceX;
        int diffColuna = targetY - sourceY;

        // Movimentos válidos precisam ser perfeitamente diagonais (passo de linha igual ao de coluna)
        if (Math.abs(diffLinha) != Math.abs(diffColuna)) {
            return false;
        }

        int dx = diffLinha > 0 ? 1 : -1;
        int dy = diffColuna > 0 ? 1 : -1;
        boolean isKing = (piece == PieceType.PLAYER1_KING || piece == PieceType.PLAYER2_KING);

        if (isKing) {
            if (!validarCaminhoDama(sourceX, sourceY, targetX, targetY, dx, dy)) {
                return false;
            }

            kingsCaptureMove(sourceX, sourceY, targetX, targetY, dx, dy);
            board[targetX][targetY] = piece;
            board[sourceX][sourceY] = PieceType.EMPTY;
            toggleTurn();
            return true;

        } else {
            int allowedDirection = (currentTurn == 1) ? -1 : 1;
            int distAbsX = Math.abs(diffLinha); // Mantém o sinal para validar "para frente"
            int distAbsY = Math.abs(diffColuna); // Sempre positivo para aceitar esquerda OU direita

            // Movimento Simples (1 casa para frente, 1 casa para esquerda OU direita)
            if (distAbsX == 1 && distAbsY == 1 && diffColuna == allowedDirection) {
                simpleMove(sourceX, sourceY, targetX, targetY);

                // Verifica promoção a Dama
                if ((currentTurn == 1 && targetY == 0) || (currentTurn == 2 && targetY == 7)) {
                    board[targetX][targetY] = (currentTurn == 1) ? PieceType.PLAYER1_KING : PieceType.PLAYER2_KING;
                }

                toggleTurn();
                return true;
            }

            // Movimento de Captura (2 casas para frente, 2 casas para esquerda OU direita)
            if (diffColuna == (2 * allowedDirection) && distAbsX == 2 && distAbsY == 2) {
                // sourceY + dy calcula o lado correto (esquerda/direita) que o jogador  escolheu
                int lC = sourceX + dx;
                int cC = sourceY + dy;

                PieceType captureTarget = board[lC][cC];
                PieceType pecaInimiga = (currentTurn == 1) ? PieceType.PLAYER2 : PieceType.PLAYER1;
                PieceType damaInimiga = (currentTurn == 1) ? PieceType.PLAYER2_KING : PieceType.PLAYER1_KING;

                if (captureTarget == pecaInimiga || captureTarget == damaInimiga) {
                    captureMove(sourceX, sourceY, targetX, targetY, lC, cC);

                    // Verifica promoção após captura
                    if ((currentTurn == 1 && targetY == 0) || (currentTurn == 2 && targetY == 7)) {
                        board[targetX][targetY] = (currentTurn == 1) ? PieceType.PLAYER1_KING : PieceType.PLAYER2_KING;
                    }

                    toggleTurn();
                    return true;
                }
            }
        }

        return false;
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
    public Integer[][] showavailableMovements(int x, int y) {
        Integer[][] availableMovements = new Integer[size][size];

        for (int i = 0; i < availableMovements.length; i++) {
            for (int j = 0; j < availableMovements.length; j++) {
                availableMovements[i][j] = 0;
            }
        }

        if (isOutOfBounds(x, y)) {
            return availableMovements;
        }

        PieceType piece = board[x][y];

        boolean currentPlayerPiece = false;
        if (currentTurn == 1 && piece.getCode() == 1) {
            currentPlayerPiece = true;
        } else if (currentTurn == 2 && piece.getCode() == 2) {
            currentPlayerPiece = true;
        }

        if (!currentPlayerPiece || piece.getCode() == 0) {
            return availableMovements;
        }

        boolean isKing = (piece == PieceType.PLAYER1_KING || piece == PieceType.PLAYER2_KING);
        PieceType enemyPiece = (currentTurn == 1) ? PieceType.PLAYER2 : PieceType.PLAYER1;
        PieceType enemyKing = (currentTurn == 1) ? PieceType.PLAYER2_KING : PieceType.PLAYER1_KING;

        // As 4 direções diagonais possíveis (X, Y)
        int[][] directions = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            // Se for peça comum, ela só anda na direção permitida para frente (a menos que
            // seja captura, mas no board clássico a peça comum só captura para frente)
            int allowedDirection = (currentTurn == 1) ? -1 : 1;
            if (!isKing && dy != allowedDirection) {
                // TODO: Adicionar opção de captura para trás nas configurações
                continue;
            }

            if (isKing) {
                int step = 1;
                boolean enemyOnWay = false;
                int enemyX = -1, enemyY = -1;

                while (true) {
                    int nx = x + (dx * step);
                    int ny = y + (dy * step);

                    if (isOutOfBounds(nx, ny)) {
                        break;
                    }

                    PieceType current = board[nx][ny];

                    if (!enemyOnWay) {
                        if (current == PieceType.EMPTY) {
                            availableMovements[nx][ny] = 1;
                            availableMovements[x][y] = 3;
                        } else if (current == enemyPiece || current == enemyKing) {
                            // Encontrou um inimigo
                            enemyOnWay = true;
                            enemyX = nx;
                            enemyY = ny;
                            availableMovements[enemyX][enemyY] = 2;
                        } else break; // Encontrou peça própria bloqueando a diagonal
                    } else {
                        // Se já encontramos um inimigo, as próximas casas vazias são opções de pouso da captura
                        if (current == PieceType.EMPTY) {
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

                if (!isOutOfBounds(dxSimplified, dySimplified)
                        && isEmptyTarget(dxSimplified, dySimplified)) {
                    availableMovements[dxSimplified][dySimplified] = 1;
                    availableMovements[x][y] = 3;
                }

                // Movimento de Captura
                int captureTargetX = x + (2 * dx);
                int captureTargetY = y + (2 * dy);
                int capturedX = x + dx;
                int capturedY = y + dy;

                if (!isOutOfBounds(captureTargetX, captureTargetY) && isEmptyTarget(captureTargetX, captureTargetY)) {
                    PieceType target = board[capturedX][capturedY];
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
     * @return {@link java.util.List List}<{@link main.java.nathansdc.damas.ia.Movement Movement}>
    */
    public List<Movement> getPlayablePieces() {
        List<Movement> movements = new ArrayList<>();
        
        // Trata da IndexOuOfBoundsException lançada pelo {@code getMovements()}
        try {
            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board.length; j++) {
                    if (isPieceOf(i, j)){
                        movements.addAll(Movement.getMovements(i, j));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movements;
    }

    public PieceType[][] simuleNextState(PieceType[][] currentState, Movement movement) {
        if (currentState == null) {
            return  null;
        }

        if (movement == null) {
            return currentState;
        }

        PieceType[][] nextState = new PieceType[currentState.length][currentState.length];

        for (int i = 0; i < currentState.length; i++) {
            for (int j = 0; j < currentState.length; j++) {
                nextState[i][j] = nextState[i][j];

                if (i == movement.getSourceX() && j == movement.getSourceY()) {
                    nextState[i][j] = PieceType.EMPTY;
                } else if (i == movement.getTargetX() && j == movement.getTargetY()) {
                    nextState[i][j] = currentState[movement.getSourceX()][movement.getSourceY()];
                } else if (movement.isCapture() && (i == movement.getCapturedEnemyX() && movement.getCapturedEnemyY() == j)) {
                    nextState[i][j] = PieceType.EMPTY;
                }
            }
        }

        return nextState;
    }
}