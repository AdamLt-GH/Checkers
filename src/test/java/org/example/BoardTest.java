package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardTest {
    @Test
    void startsWithTwelvePiecesOnEachSide() {
        Board board = new Board();

        assertEquals(12, countPieces(board, PieceType.BLACK));
        assertEquals(12, countPieces(board, PieceType.WHITE));
        assertEquals(40, countPieces(board, PieceType.EMPTY));
    }

    @Test
    void piecesOnlyStartOnDarkSquares() {
        Board board = new Board();

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (board.getPieceAt(row, col) != PieceType.EMPTY) {
                    assertEquals(1, (row + col) % 2);
                }
            }
        }
    }

    @Test
    void settingTheBoardAgainResetsIt() {
        Board board = new Board();
        board.setPieceAt(0, 1, PieceType.EMPTY);

        board.setBoard();

        assertEquals(PieceType.BLACK, board.getPieceAt(0, 1));
    }

    @Test
    void positionsOutsideTheBoardAreRejected() {
        Board board = new Board();

        assertThrows(IndexOutOfBoundsException.class, () -> board.getPieceAt(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.getPieceAt(8, 0));
    }

    private int countPieces(Board board, PieceType piece) {
        int count = 0;
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (board.getPieceAt(row, col) == piece) {
                    count++;
                }
            }
        }
        return count;
    }
}
