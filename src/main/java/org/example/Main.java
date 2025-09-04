package org.example;

import processing.core.PApplet;

public class Main extends PApplet {
    private enum GameMode {
        MENU,
        PLAYER_VS_PLAYER,
        PLAYER_VS_CPU
    }

    private static final int BUTTON_X = 180;
    private static final int BUTTON_WIDTH = 280;
    private static final int BUTTON_HEIGHT = 60;
    private static final int PLAYER_BUTTON_Y = 250;
    private static final int CPU_BUTTON_Y = 340;
    private static final long GAME_OVER_TIME_MS = 4000;

    private Board board;
    private BoardRenderer boardRenderer;
    private Players players;
    private CpuPlayer cpuPlayer;
    private GameStatus gameStatus;
    private GameResult gameResult = GameResult.IN_PROGRESS;
    private long returnToMenuAt;
    private GameMode gameMode = GameMode.MENU;

    public void settings() {
        size(BoardRenderer.BOARD_SIZE, BoardRenderer.BOARD_SIZE);
    }

    public void setup() {
        board = new Board();
        PieceImages pieceImages = new PieceImages(this);
        boardRenderer = new BoardRenderer(board, pieceImages);
        players = new Players(new MoveRules());
        cpuPlayer = new CpuPlayer(new MoveRules());
        gameStatus = new GameStatus(new MoveRules());
        noStroke();
    }

    public void draw() {
        background(255);
        if (gameMode == GameMode.MENU) {
            drawMenu();
            return;
        }

        checkGameOver();
        if (gameResult == GameResult.IN_PROGRESS
                && gameMode == GameMode.PLAYER_VS_CPU && !players.isBlackTurn()) {
            cpuPlayer.takeTurn(board, players);
            checkGameOver();
        }
        boardRenderer.draw(this);
        drawSelectedPiece();
        if (gameResult == GameResult.IN_PROGRESS) {
            drawHud();
        } else {
            drawGameOver();
        }
    }

    public void mousePressed() {
        if (gameMode == GameMode.MENU) {
            if (insideButton(mouseX, mouseY, PLAYER_BUTTON_Y)) {
                startGame(GameMode.PLAYER_VS_PLAYER);
            } else if (insideButton(mouseX, mouseY, CPU_BUTTON_Y)) {
                startGame(GameMode.PLAYER_VS_CPU);
            }
            return;
        }

        if (gameResult != GameResult.IN_PROGRESS) {
            return;
        }

        if (gameMode == GameMode.PLAYER_VS_CPU && !players.isBlackTurn()) {
            return;
        }

        int row = mouseY / BoardRenderer.SQUARE_SIZE;
        int col = mouseX / BoardRenderer.SQUARE_SIZE;
        if (!board.isInsideBoard(row, col)) {
            return;
        }

        boolean clickWorked;
        if (players.hasSelectedPiece()) {
            clickWorked = players.moveSelectedPiece(board, row, col);
            if (!clickWorked) {
                clickWorked = players.selectPiece(board, row, col);
            }
        } else {
            clickWorked = players.selectPiece(board, row, col);
        }

        if (!clickWorked) {
            players.markInvalidMove();
        }
    }

    private void drawMenu() {
        fill(0);
        textAlign(CENTER, CENTER);
        textSize(42);
        text("Checkers", width / 2, 150);

        fill(180);
        rect(BUTTON_X, PLAYER_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        fill(0);
        textSize(22);
        text("Player vs Player", width / 2, PLAYER_BUTTON_Y + BUTTON_HEIGHT / 2);

        fill(180);
        rect(BUTTON_X, CPU_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        fill(0);
        text("Player vs CPU", width / 2, CPU_BUTTON_Y + BUTTON_HEIGHT / 2);
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

    private void drawHud() {
        int boxHeight = players.isInvalidMoveActive() ? 48 : 28;
        fill(0, 160);
        rect(6, 6, 125, boxHeight);

        textAlign(LEFT, TOP);
        textSize(14);
        fill(255);
        String turn = players.isBlackTurn() ? "Turn: Black" : "Turn: White";
        text(turn, 12, 11);

        if (players.isInvalidMoveActive()) {
            fill(255, 90, 90);
            text("Invalid move", 12, 29);
        }
    }

    private void drawGameOver() {
        fill(0, 180);
        rect(0, 0, width, height);

        fill(255);
        textAlign(CENTER, CENTER);
        textSize(38);
        text("Game Over", width / 2, height / 2 - 45);
        textSize(24);
        String winner = gameResult == GameResult.BLACK_WINS ? "Black wins" : "White wins";
        text(winner, width / 2, height / 2);

        long timeLeft = Math.max(0, returnToMenuAt - System.currentTimeMillis());
        long secondsLeft = (timeLeft + 999) / 1000;
        textSize(16);
        text("Returning to menu in " + secondsLeft, width / 2, height / 2 + 42);

        if (timeLeft == 0) {
            gameMode = GameMode.MENU;
            gameResult = GameResult.IN_PROGRESS;
        }
    }

    private void checkGameOver() {
        if (gameResult != GameResult.IN_PROGRESS) {
            return;
        }

        gameResult = gameStatus.getResult(board, players.isBlackTurn());
        if (gameResult != GameResult.IN_PROGRESS) {
            returnToMenuAt = System.currentTimeMillis() + GAME_OVER_TIME_MS;
        }
    }

    private boolean insideButton(int x, int y, int buttonY) {
        return x >= BUTTON_X && x <= BUTTON_X + BUTTON_WIDTH
                && y >= buttonY && y <= buttonY + BUTTON_HEIGHT;
    }

    private void startGame(GameMode mode) {
        board.setBoard();
        players = new Players(new MoveRules());
        gameResult = GameResult.IN_PROGRESS;
        returnToMenuAt = 0;
        gameMode = mode;
    }

    public static void main(String[] args) {
        PApplet.main(Main.class.getName());
    }
}
