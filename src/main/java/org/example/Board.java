package org.example;

public class Board {
    public static final int SIZE = 8;

    private final PieceType[][] squares = new PieceType[SIZE][SIZE];

    public Board() {
        setBoard();
    }

    public void setBoard() {
        // clear it first so this also works when starting a new game
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                squares[row][col] = PieceType.EMPTY;
            }
        }

        // black starts at the top and white starts at the bottom
        for (int row = 0; row < 3; row++) {
            placePiecesOnRow(row, PieceType.BLACK);
        }

        for (int row = 5; row < SIZE; row++) {
            placePiecesOnRow(row, PieceType.WHITE);
        }
    }

    private void placePiecesOnRow(int row, PieceType piece) {
        for (int col = 0; col < SIZE; col++) {
            if ((row + col) % 2 == 1) {
                squares[row][col] = piece;
            }
        }
    }

    public PieceType getPieceAt(int row, int col) {
        checkPosition(row, col);
        return squares[row][col];
    }

    public void setPieceAt(int row, int col, PieceType piece) {
        checkPosition(row, col);
        if (piece == null) {
            throw new IllegalArgumentException("Piece cannot be null");
        }
        squares[row][col] = piece;
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        checkPosition(fromRow, fromCol);
        checkPosition(toRow, toCol);

        PieceType piece = squares[fromRow][fromCol];
        squares[fromRow][fromCol] = PieceType.EMPTY;
        squares[toRow][toCol] = promoteIfNeeded(piece, toRow);
    }

    private PieceType promoteIfNeeded(PieceType piece, int row) {
        if (piece == PieceType.BLACK && row == SIZE - 1) {
            return PieceType.BLACK_KING;
        }
        if (piece == PieceType.WHITE && row == 0) {
            return PieceType.WHITE_KING;
        }
        return piece;
    }

    public boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    private void checkPosition(int row, int col) {
        if (!isInsideBoard(row, col)) {
            throw new IndexOutOfBoundsException("Position is outside the board");
        }
    }
}
