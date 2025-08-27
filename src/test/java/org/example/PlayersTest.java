package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayersTest {
    private Board board;
    private Players players;

    @BeforeEach
    void setUp() {
        board = new Board();
        players = new Players(new MoveRules());
        clearBoard();
    }

    @Test
    void blackTakesTheFirstTurn() {
        assertTrue(players.isBlackTurn());
    }

    @Test
    void playerCanOnlySelectTheirOwnPiece() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(5, 2, PieceType.WHITE);

        assertFalse(players.selectPiece(board, 5, 2));
        assertTrue(players.selectPiece(board, 2, 1));
    }

    @Test
    void validMoveSwitchesTheTurn() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        players.selectPiece(board, 2, 1);

        assertTrue(players.moveSelectedPiece(board, 3, 2));

        assertFalse(players.isBlackTurn());
        assertFalse(players.hasSelectedPiece());
        assertEquals(PieceType.BLACK, board.getPieceAt(3, 2));
    }

    @Test
    void invalidMoveDoesNotSwitchTheTurn() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        players.selectPiece(board, 2, 1);

        assertFalse(players.moveSelectedPiece(board, 2, 3));

        assertTrue(players.isBlackTurn());
        assertTrue(players.hasSelectedPiece());
    }

    @Test
    void pieceWithoutCaptureCannotBeSelectedWhenAnotherPieceCanCapture() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.WHITE);
        board.setPieceAt(2, 5, PieceType.BLACK);

        assertFalse(players.selectPiece(board, 2, 5));
        assertTrue(players.selectPiece(board, 2, 1));
    }

    @Test
    void samePieceStaysSelectedDuringMultipleCaptures() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.WHITE);
        board.setPieceAt(5, 4, PieceType.WHITE);
        players.selectPiece(board, 2, 1);

        assertTrue(players.moveSelectedPiece(board, 4, 3));

        assertTrue(players.isBlackTurn());
        assertTrue(players.hasSelectedPiece());
        assertEquals(4, players.getSelectedRow());
        assertEquals(3, players.getSelectedCol());

        assertTrue(players.moveSelectedPiece(board, 6, 5));
        assertFalse(players.isBlackTurn());
        assertFalse(players.hasSelectedPiece());
    }

    private void clearBoard() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                board.setPieceAt(row, col, PieceType.EMPTY);
            }
        }
    }
}
