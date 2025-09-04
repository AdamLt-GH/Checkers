package org.example;

import java.util.function.LongSupplier;

public class Players {
    private static final long INVALID_MOVE_TIME_MS = 2000;

    private final MoveRules rules;
    private final LongSupplier clock;
    private boolean blackTurn = true;
    private boolean continuingCapture;
    private long invalidMoveUntil;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public Players(MoveRules rules) {
        this(rules, System::currentTimeMillis);
    }

    Players(MoveRules rules, LongSupplier clock) {
        this.rules = rules;
        this.clock = clock;
    }

    public boolean selectPiece(Board board, int row, int col) {
        if (!board.isInsideBoard(row, col)) {
            return false;
        }
        if (continuingCapture) {
            return row == selectedRow && col == selectedCol;
        }

        PieceType piece = board.getPieceAt(row, col);
        if (!isCurrentPlayersPiece(piece)) {
            return false;
        }

        // when there is a capture only those pieces can be picked
        if (rules.hasCaptureMove(board, blackTurn)
                && !rules.canCaptureFrom(board, row, col, blackTurn)) {
            return false;
        }

        selectedRow = row;
        selectedCol = col;
        return true;
    }

    public boolean moveSelectedPiece(Board board, int toRow, int toCol) {
        if (!hasSelectedPiece()) {
            return false;
        }
        if (!rules.isValidMove(board, selectedRow, selectedCol, toRow, toCol, blackTurn)) {
            return false;
        }

        boolean wasCapture = Math.abs(toRow - selectedRow) == 2;
        board.movePiece(selectedRow, selectedCol, toRow, toCol);

        if (wasCapture && rules.canCaptureFrom(board, toRow, toCol, blackTurn)) {
            // keep this piece selected until all its captures are done
            continuingCapture = true;
            selectedRow = toRow;
            selectedCol = toCol;
            return true;
        }

        blackTurn = !blackTurn;
        continuingCapture = false;
        clearSelection();
        return true;
    }

    public boolean isBlackTurn() {
        return blackTurn;
    }

    public void markInvalidMove() {
        invalidMoveUntil = clock.getAsLong() + INVALID_MOVE_TIME_MS;
    }

    public boolean isInvalidMoveActive() {
        return clock.getAsLong() < invalidMoveUntil;
    }

    public boolean hasSelectedPiece() {
        return selectedRow >= 0 && selectedCol >= 0;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    private boolean isCurrentPlayersPiece(PieceType piece) {
        return blackTurn ? piece.isBlack() : piece.isWhite();
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
    }
}
