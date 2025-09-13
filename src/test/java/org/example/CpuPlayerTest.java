package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpuPlayerTest {
    private Board board;
    private Players players;
    private CpuPlayer cpu;

    @BeforeEach
    void setUp() {
        MoveRules rules = new MoveRules();
        board = new Board();
        players = new Players(rules);
        cpu = new CpuPlayer(rules);
        clearBoard();
        moveToWhiteTurn();
        clearBoard();
    }

    @Test
    void cpuMakesARegularMoveForWhite() {
        board.setPieceAt(5, 2, PieceType.WHITE);

        assertTrue(cpu.takeTurn(board, players));

        assertTrue(players.isBlackTurn());
        assertEquals(PieceType.EMPTY, board.getPieceAt(5, 2));
        boolean movedLeft = board.getPieceAt(4, 1) == PieceType.WHITE;
        boolean movedRight = board.getPieceAt(4, 3) == PieceType.WHITE;
        assertTrue(movedLeft || movedRight);
    }

    @Test
    void cpuTakesACaptureWhenOneIsAvailable() {
        board.setPieceAt(5, 0, PieceType.WHITE);
        board.setPieceAt(5, 4, PieceType.WHITE);
        board.setPieceAt(4, 3, PieceType.BLACK);

        assertTrue(cpu.takeTurn(board, players));

        assertEquals(PieceType.EMPTY, board.getPieceAt(4, 3));
        assertEquals(PieceType.WHITE, board.getPieceAt(3, 2));
    }

    @Test
    void cpuFinishesMultipleCaptures() {
        board.setPieceAt(6, 5, PieceType.WHITE);
        board.setPieceAt(5, 4, PieceType.BLACK);
        board.setPieceAt(3, 2, PieceType.BLACK);

        assertTrue(cpu.takeTurn(board, players));

        assertEquals(PieceType.EMPTY, board.getPieceAt(5, 4));
        assertEquals(PieceType.EMPTY, board.getPieceAt(3, 2));
        assertEquals(PieceType.WHITE, board.getPieceAt(2, 1));
        assertTrue(players.isBlackTurn());
    }

    @Test
    void cpuDoesNothingDuringThePlayersTurn() {
        players = new Players(new MoveRules());
        board.setPieceAt(5, 2, PieceType.WHITE);

        assertFalse(cpu.takeTurn(board, players));
        assertEquals(PieceType.WHITE, board.getPieceAt(5, 2));
    }

    @Test
    void cpuLooksAheadInsteadOfTakingTheFirstMove() {
        cpu.setDifficulty(CpuDifficulty.EASY);
        board.setPieceAt(5, 4, PieceType.WHITE);
        board.setPieceAt(3, 2, PieceType.BLACK);

        assertTrue(cpu.takeTurn(board, players));

        assertEquals(PieceType.EMPTY, board.getPieceAt(4, 3));
        assertEquals(PieceType.WHITE, board.getPieceAt(4, 5));
    }

    @Test
    void difficultiesUseDifferentSearchDepths() {
        assertEquals(2, CpuDifficulty.EASY.getSearchDepth());
        assertEquals(4, CpuDifficulty.MEDIUM.getSearchDepth());
        assertEquals(6, CpuDifficulty.HARD.getSearchDepth());
    }

    private void moveToWhiteTurn() {
        board.setPieceAt(0, 1, PieceType.BLACK);
        players.selectPiece(board, 0, 1);
        players.moveSelectedPiece(board, 1, 2);
    }

    private void clearBoard() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                board.setPieceAt(row, col, PieceType.EMPTY);
            }
        }
    }
}
