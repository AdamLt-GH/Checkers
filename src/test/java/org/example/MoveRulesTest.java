package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveRulesTest {
    private Board board;
    private MoveRules rules;

    @BeforeEach
    void setUp() {
        board = new Board();
        rules = new MoveRules();
        clearBoard();
    }

    @Test
    void blackPieceCanMoveOneRowDown() {
        board.setPieceAt(2, 1, PieceType.BLACK);

        assertTrue(rules.isValidMove(board, 2, 1, 3, 2, true));
    }

    @Test
    void whitePieceCanMoveOneRowUp() {
        board.setPieceAt(5, 2, PieceType.WHITE);

        assertTrue(rules.isValidMove(board, 5, 2, 4, 1, false));
    }

    @Test
    void regularPieceCannotMoveBackwards() {
        board.setPieceAt(2, 1, PieceType.BLACK);

        assertFalse(rules.isValidMove(board, 2, 1, 1, 2, true));
    }

    @Test
    void kingCanMoveInEitherDirection() {
        board.setPieceAt(4, 3, PieceType.BLACK_KING);

        assertTrue(rules.isValidMove(board, 4, 3, 3, 2, true));
        assertTrue(rules.isValidMove(board, 4, 3, 5, 4, true));
    }

    @Test
    void cannotMoveTheOtherPlayersPiece() {
        board.setPieceAt(2, 1, PieceType.WHITE);

        assertFalse(rules.isValidMove(board, 2, 1, 1, 2, true));
    }

    @Test
    void destinationMustBeEmpty() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.WHITE);

        assertFalse(rules.isValidMove(board, 2, 1, 3, 2, true));
    }

    @Test
    void pieceCanCaptureAnOpponent() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.WHITE);

        assertTrue(rules.isValidMove(board, 2, 1, 4, 3, true));
    }

    @Test
    void pieceCannotCaptureItsOwnSide() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.BLACK);

        assertFalse(rules.isValidMove(board, 2, 1, 4, 3, true));
    }

    @Test
    void captureRemovesTheJumpedPiece() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.WHITE);

        board.movePiece(2, 1, 4, 3);

        assertEquals(PieceType.EMPTY, board.getPieceAt(3, 2));
        assertEquals(PieceType.BLACK, board.getPieceAt(4, 3));
    }

    @Test
    void captureMustBeTakenWhenOneIsAvailable() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.WHITE);
        board.setPieceAt(2, 5, PieceType.BLACK);

        assertFalse(rules.isValidMove(board, 2, 5, 3, 4, true));
        assertTrue(rules.isValidMove(board, 2, 1, 4, 3, true));
    }

    @Test
    void followUpCaptureIsFoundForTheSamePiece() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.WHITE);
        board.setPieceAt(5, 4, PieceType.WHITE);

        board.movePiece(2, 1, 4, 3);

        assertTrue(rules.canCaptureFrom(board, 4, 3, true));
    }

    @Test
    void kingCanCaptureBackwards() {
        board.setPieceAt(4, 3, PieceType.BLACK_KING);
        board.setPieceAt(3, 2, PieceType.WHITE);

        assertTrue(rules.isValidMove(board, 4, 3, 2, 1, true));
    }

    @Test
    void pieceBecomesKingAtTheOtherEnd() {
        board.setPieceAt(6, 1, PieceType.BLACK);

        board.movePiece(6, 1, 7, 2);

        assertEquals(PieceType.EMPTY, board.getPieceAt(6, 1));
        assertEquals(PieceType.BLACK_KING, board.getPieceAt(7, 2));
    }

    private void clearBoard() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                board.setPieceAt(row, col, PieceType.EMPTY);
            }
        }
    }
}
