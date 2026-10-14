package main.java.nathansdc.damas.jogo;

public enum Piece {

    EMPTY(0),
    PLAYER1(1),
    PLAYER2(2),
    PLAYER1_KING(1),
    PLAYER2_KING(2);

    int code;

    public int getCode() {
        return code;
    }

    private Piece(int code) {
        this.code = code;
    }

}
