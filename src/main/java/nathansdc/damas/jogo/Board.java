package main.java.nathansdc.damas.jogo;

import java.util.Arrays;

public class Board {

    /**
     * Tamanho do tabuleiro
    */
    public static final int SIZE = 8;

    public static final int DEFAULT_PLAYER_PIECE_COUNT = 12;

    Piece[][] pieces = new Piece[SIZE][SIZE];

    public Board() {

    }

    public Piece getPiece(int x, int y) {
        if (isOutOfBounds(x, y))
            return null;
        return pieces[x][y];
    }

    public boolean setPiece(int x, int y, Piece piece) {
        if (isOutOfBounds(x, y))
            return false;
        if (piece == null)
            return false;

        pieces[x][y] = piece;
        return true;
    }

    public boolean removePiece(int x, int y) {
        if (isOutOfBounds(x, y))
            return false;
        if (isThereEmpty(x, y))
            return false;
        pieces[x][y] = Piece.EMPTY;
        return true;
    }

    public boolean movePiece (Movement move, boolean isCapture) {
        if (!move.isValid()) {
            return false;
        }

        int sourceX = move.getSourceX();
        int sourceY = move.getSourceY();

        int targetX = move.getTargetX();
        int targteY = move.getTargetY();

        if (isCapture) {
            pieces[targetX][targteY] = pieces[sourceX][sourceY];
            pieces[sourceX][sourceY] = Piece.EMPTY;
            pieces[move.getCapturedEnemyX()][move.getCapturedEnemyY()] = Piece.EMPTY;
            return true;
        } else {
            pieces[targetX][targteY] = pieces[sourceX][sourceY];
            pieces[sourceX][sourceY] = Piece.EMPTY;
            return true;
        }
    }

    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= SIZE || y < 0 || y >= SIZE;
    }

    // Não é nescessário fazer novamente a verificação de limites já que o getPiece por si só já verifica
    public boolean isThereEmpty(int x, int y) {
        if (getPiece(x, y) == null)
            return false;
        return pieces[x][y] == Piece.EMPTY;
    }

    public Piece[][] getAllPieces() {
        return pieces;
    }

    public boolean populate() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i + j) % 2 != 0) {
                    if (j < 3) {
                        pieces[i][j] = Piece.PLAYER2;
                    } else if (j > 4) {
                        pieces[i][j] = Piece.PLAYER1;
                    } else {
                        pieces[i][j] = Piece.EMPTY;
                    }
                } else {
                    pieces[i][j] = Piece.EMPTY;
                }
            }
        }
        return true;
    }

    public int getPieceCount(Piece type) {
        int iterator = 0;
        for (Piece[] pcs : pieces) {
            for (Piece piece : pcs) {
                if (piece.getCode() == type.getCode()) {
                    iterator++;
                }
            }
        }
        return iterator;
    }

    public boolean clear() {
        pieces = new Piece[SIZE][SIZE];
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(pieces);
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
        Board other = (Board) obj;
        if (!Arrays.deepEquals(pieces, other.pieces))
            return false;
        return true;
    }

}
