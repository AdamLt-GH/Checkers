package org.example;

import processing.core.PApplet;
import processing.core.PImage;

public class PieceImages {
    private final PImage whitePiece;
    private final PImage blackPiece;
    private final PImage whiteKing;
    private final PImage blackKing;

    public PieceImages(PApplet app) {
        // load them once here instead of doing it every frame
        whitePiece = app.loadImage("regular_white_pieces.png");
        blackPiece = app.loadImage("regular_black_pieces.png");
        whiteKing = app.loadImage("white_king.png");
        blackKing = app.loadImage("black_king.png");
    }

    public PImage get(PieceType piece) {
        return switch (piece) {
            case WHITE -> whitePiece;
            case BLACK -> blackPiece;
            case WHITE_KING -> whiteKing;
            case BLACK_KING -> blackKing;
            case EMPTY -> null;
        };
    }
}
