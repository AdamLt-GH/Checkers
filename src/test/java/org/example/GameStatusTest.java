package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStatusTest {
    private Board board;
    private GameStatus gameStatus;

    @BeforeEach
    void setUp() {
        board = new Board();
        gameStatus = new GameStatus(new MoveRules());
    }

    @Test
    void startingBoardIsStillInProgress() {
        assertEquals(GameResult.IN_PROGRESS, gameStatus.getResult(board, true));
    }

    @Test
    void sideWithNoPiecesLoses() {
        clearBoard();
        board.setPieceAt(2, 1, PieceType.BLACK);

        assertEquals(GameResult.BLACK_WINS, gameStatus.getResult(board, false));
    }

    @Test
    void sideWithNoLegalMovesLosesEvenWithMorePieces() {
        clearBoard();
        board.setPieceAt(7, 0, PieceType.BLACK);
        board.setPieceAt(7, 2, PieceType.BLACK);
        board.setPieceAt(0, 1, PieceType.WHITE);

        assertEquals(GameResult.WHITE_WINS, gameStatus.getResult(board, true));
    }

    @Test
    void blockedOpponentDoesNotEndTheCurrentPlayersTurn() {
        clearBoard();
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(0, 1, PieceType.WHITE);

        assertEquals(GameResult.IN_PROGRESS, gameStatus.getResult(board, true));
    }

    private void clearBoard() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                board.setPieceAt(row, col, PieceType.EMPTY);
            }
        }
    }
}
