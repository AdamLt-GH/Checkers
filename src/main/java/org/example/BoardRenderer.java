package org.example;

import processing.core.PApplet;

public class BoardRenderer {
    public static final int SQUARE_SIZE = 80;
    public static final int BOARD_SIZE = Board.SIZE * SQUARE_SIZE;

    public void draw(PApplet app) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                drawSquare(app, row, col);
            }
        }
    }

    private void drawSquare(PApplet app, int row, int col) {
        if ((row + col) % 2 == 0) {
            app.fill(240);
        } else {
            app.fill(100);
        }

        int x = col * SQUARE_SIZE;
        int y = row * SQUARE_SIZE;
        app.rect(x, y, SQUARE_SIZE, SQUARE_SIZE);
    }
}
