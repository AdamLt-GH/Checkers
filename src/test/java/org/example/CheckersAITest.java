package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckersAITest {
    private Board board;
    private CheckersAI ai;

    @BeforeEach
    void setUp() {
        board = new Board();
        ai = new CheckersAI(new MoveRules());
        clearBoard();
    }

    @Test
    void generatesRegularMovesForAPlayer() {
        board.setPieceAt(2, 1, PieceType.BLACK);

        List<CheckersAI.Move> moves = ai.generateMoves(board, true);

        assertEquals(2, moves.size());
        assertTrue(moves.stream().allMatch(move -> move.steps().size() == 1));
    }

    @Test
    void onlyGeneratesCapturesWhenOneIsAvailable() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.WHITE);
        board.setPieceAt(2, 5, PieceType.BLACK);

        List<CheckersAI.Move> moves = ai.generateMoves(board, true);

        assertEquals(1, moves.size());
        assertEquals(new CheckersAI.Step(2, 1, 4, 3), moves.get(0).steps().get(0));
    }

    @Test
    void generatesAndAppliesAFullCaptureSequence() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.WHITE);
        board.setPieceAt(5, 4, PieceType.WHITE);

        CheckersAI.Move move = ai.generateMoves(board, true).get(0);
        ai.applyMove(board, move);

        assertEquals(2, move.steps().size());
        assertEquals(PieceType.EMPTY, board.getPieceAt(3, 2));
        assertEquals(PieceType.EMPTY, board.getPieceAt(5, 4));
        assertEquals(PieceType.BLACK, board.getPieceAt(6, 5));
    }

    @Test
    void evaluationKeepsTheSamePlayersPointOfView() {
        board.setPieceAt(3, 2, PieceType.BLACK_KING);
        board.setPieceAt(5, 6, PieceType.WHITE);

        int blackScore = ai.evaluate(board, true);
        int whiteScore = ai.evaluate(board, false);

        assertTrue(blackScore > 0);
        assertEquals(-blackScore, whiteScore);
    }

    @Test
    void searchReturnsTheOnlyLegalMove() {
        board.setPieceAt(2, 0, PieceType.BLACK);

        CheckersAI.Move move = ai.findBestMove(board, true, 3);

        assertEquals(new CheckersAI.Step(2, 0, 3, 1), move.steps().get(0));
    }

    @Test
    void searchPrefersCapturingAKing() {
        board.setPieceAt(4, 3, PieceType.BLACK_KING);
        board.setPieceAt(3, 2, PieceType.WHITE);
        board.setPieceAt(3, 4, PieceType.WHITE_KING);

        CheckersAI.Move move = ai.findBestMove(board, true, 1);

        assertEquals(new CheckersAI.Step(4, 3, 2, 5), move.steps().get(0));
    }

    @Test
    void searchLooksAheadToAvoidBeingCaptured() {
        board.setPieceAt(2, 3, PieceType.BLACK);
        board.setPieceAt(4, 1, PieceType.WHITE);

        CheckersAI.Move move = ai.findBestMove(board, true, 2);

        assertEquals(new CheckersAI.Step(2, 3, 3, 4), move.steps().get(0));
    }

    @Test
    void searchDoesNotChangeTheRealBoard() {
        board.setPieceAt(2, 1, PieceType.BLACK);
        Board beforeSearch = board.copy();

        ai.findBestMove(board, true, 3);

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                assertEquals(beforeSearch.getPieceAt(row, col), board.getPieceAt(row, col));
            }
        }
    }

    @Test
    void searchReturnsNothingWhenThereAreNoMoves() {
        assertNull(ai.findBestMove(board, true, 3));
    }

    private void clearBoard() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                board.setPieceAt(row, col, PieceType.EMPTY);
            }
        }
    }
}
