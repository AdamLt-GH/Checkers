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
        if (Math.abs(colChange) != 1) {
            return false;
        }

        if (piece.isKing()) {
            return Math.abs(rowChange) == 1;
        }

        // regular pieces only go towards the other end
        return piece == PieceType.BLACK ? rowChange == 1 : rowChange == -1;
    }

    private boolean isPlayersPiece(PieceType piece, boolean blackTurn) {
        return blackTurn ? piece.isBlack() : piece.isWhite();
    }
}
