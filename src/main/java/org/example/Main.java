package org.example;

import processing.core.PApplet;

public class Main extends PApplet {
    private enum GameMode {
        MENU,
        PLAYER_VS_PLAYER
    }

    private static final int BUTTON_X = 180;
    private static final int BUTTON_Y = 280;
    private static final int BUTTON_WIDTH = 280;
    private static final int BUTTON_HEIGHT = 60;

    private Board board;
    private BoardRenderer boardRenderer;
    private Players players;
    private GameMode gameMode = GameMode.MENU;

    public void settings() {
        size(BoardRenderer.BOARD_SIZE, BoardRenderer.BOARD_SIZE);
    }

    public void setup() {
        board = new Board();
        PieceImages pieceImages = new PieceImages(this);
        boardRenderer = new BoardRenderer(board, pieceImages);
        players = new Players(new MoveRules());
        noStroke();
    }

    public void draw() {
        background(255);
        if (gameMode == GameMode.MENU) {
            drawMenu();
            return;
        }

        boardRenderer.draw(this);
        drawSelectedPiece();
    }

    public void mousePressed() {
        if (gameMode == GameMode.MENU) {
            if (insidePlayButton(mouseX, mouseY)) {
                startPlayerGame();
            }
            return;
        }

        int row = mouseY / BoardRenderer.SQUARE_SIZE;
        int col = mouseX / BoardRenderer.SQUARE_SIZE;
        if (!board.isInsideBoard(row, col)) {
            return;
        }

        if (players.hasSelectedPiece()) {
            if (!players.moveSelectedPiece(board, row, col)) {
                players.selectPiece(board, row, col);
            }
        } else {
            players.selectPiece(board, row, col);
        }
    }

    private void drawMenu() {
        fill(0);
        textAlign(CENTER, CENTER);
        textSize(42);
        text("Checkers", width / 2, 150);

        fill(180);
        rect(BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        fill(0);
        textSize(22);
        text("Player vs Player", width / 2, BUTTON_Y + BUTTON_HEIGHT / 2);
    }

    private void drawSelectedPiece() {
        if (!players.hasSelectedPiece()) {
            return;
        }

        int x = players.getSelectedCol() * BoardRenderer.SQUARE_SIZE;
        int y = players.getSelectedRow() * BoardRenderer.SQUARE_SIZE;
        noFill();
        stroke(255, 200, 0);
        strokeWeight(4);
        rect(x + 3, y + 3, BoardRenderer.SQUARE_SIZE - 6, BoardRenderer.SQUARE_SIZE - 6);
        noStroke();
    }

    private boolean insidePlayButton(int x, int y) {
        return x >= BUTTON_X && x <= BUTTON_X + BUTTON_WIDTH
                && y >= BUTTON_Y && y <= BUTTON_Y + BUTTON_HEIGHT;
    }

    private void startPlayerGame() {
        board.setBoard();
        players = new Players(new MoveRules());
        gameMode = GameMode.PLAYER_VS_PLAYER;
    }

    public static void main(String[] args) {
        PApplet.main(Main.class.getName());
    }
}
