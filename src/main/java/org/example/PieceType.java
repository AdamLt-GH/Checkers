package org.example;

public enum PieceType {
    EMPTY,
    WHITE,
    BLACK,
    WHITE_KING,
    BLACK_KING;

    public boolean isBlack() {
        return this == BLACK || this == BLACK_KING;
    }

    public boolean isWhite() {
        return this == WHITE || this == WHITE_KING;
    }

    public boolean isKing() {
        return this == WHITE_KING || this == BLACK_KING;
    }
}
