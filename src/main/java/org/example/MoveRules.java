package org.example;

public class MoveRules {
    public boolean isValidMove(Board board, int fromRow, int fromCol,
                               int toRow, int toCol, boolean blackTurn) {
        if (!board.isInsideBoard(fromRow, fromCol) || !board.isInsideBoard(toRow, toCol)) {
            return false;
        }

        PieceType piece = board.getPieceAt(fromRow, fromCol);
        if (!isPlayersPiece(piece, blackTurn)) {
            return false;
        }
        if (board.getPieceAt(toRow, toCol) != PieceType.EMPTY) {
            return false;
        }

        int rowChange = toRow - fromRow;
        int colChange = toCol - fromCol;

        if (isCaptureMove(board, piece, fromRow, fromCol, toRow, toCol)) {
            return true;
        }

        // if a capture is there then the player has to take it
        if (hasCaptureMove(board, blackTurn)) {
            return false;
        }

        return isBasicMove(piece, rowChange, colChange);
    }

    public boolean hasCaptureMove(Board board, boolean blackTurn) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (canCaptureFrom(board, row, col, blackTurn)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canCaptureFrom(Board board, int row, int col, boolean blackTurn) {
        if (!board.isInsideBoard(row, col)) {
            return false;
        }

        PieceType piece = board.getPieceAt(row, col);
        if (!isPlayersPiece(piece, blackTurn)) {
            return false;
        }

        int[][] jumps = {{2, 2}, {2, -2}, {-2, 2}, {-2, -2}};
        for (int[] jump : jumps) {
            int toRow = row + jump[0];
            int toCol = col + jump[1];
            if (isCaptureMove(board, piece, row, col, toRow, toCol)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBasicMove(PieceType piece, int rowChange, int colChange) {
        if (Math.abs(colChange) != 1) {
            return false;
        }
        if (piece.isKing()) {
            return Math.abs(rowChange) == 1;
        }

        // regular pieces only go towards the other end
        return piece == PieceType.BLACK ? rowChange == 1 : rowChange == -1;
    }

    private boolean isCaptureMove(Board board, PieceType piece, int fromRow, int fromCol,
                                  int toRow, int toCol) {
        if (!board.isInsideBoard(toRow, toCol)) {
            return false;
        }
        if (board.getPieceAt(toRow, toCol) != PieceType.EMPTY) {
            return false;
        }

        int rowChange = toRow - fromRow;
        int colChange = toCol - fromCol;
        if (Math.abs(colChange) != 2) {
            return false;
        }
        if (piece.isKing()) {
            if (Math.abs(rowChange) != 2) {
                return false;
            }
        } else if (piece == PieceType.BLACK ? rowChange != 2 : rowChange != -2) {
            return false;
        }

        int middleRow = (fromRow + toRow) / 2;
        int middleCol = (fromCol + toCol) / 2;
        PieceType middlePiece = board.getPieceAt(middleRow, middleCol);
        return piece.isBlack() ? middlePiece.isWhite() : middlePiece.isBlack();
    }

    private boolean isPlayersPiece(PieceType piece, boolean blackTurn) {
        return blackTurn ? piece.isBlack() : piece.isWhite();
    }
}
