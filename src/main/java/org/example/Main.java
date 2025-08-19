package org.example;

import processing.core.PApplet;

public class Main extends PApplet {
    private Board board;
    private BoardRenderer boardRenderer;

    public void settings() {
        size(BoardRenderer.BOARD_SIZE, BoardRenderer.BOARD_SIZE);
    }

    public void setup() {
        board = new Board();
        PieceImages pieceImages = new PieceImages(this);
        boardRenderer = new BoardRenderer(board, pieceImages);
        noStroke();
    }

    public void draw() {
        boardRenderer.draw(this);
    }

    public static void main(String[] args) {
        PApplet.main(Main.class.getName());
    }
}
