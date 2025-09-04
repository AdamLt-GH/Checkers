package org.example;

public class GameStatus {
    private final MoveRules rules;

    public GameStatus(MoveRules rules) {
        this.rules = rules;
    }

    public GameResult getResult(Board board, boolean blackTurn) {
        int currentPieces = countPieces(board, blackTurn);
        if (currentPieces == 0 || !rules.hasAnyLegalMove(board, blackTurn)) {
            return blackTurn ? GameResult.WHITE_WINS : GameResult.BLACK_WINS;
        }
        return GameResult.IN_PROGRESS;
    }

    private int countPieces(Board board, boolean blackPieces) {
        int count = 0;
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                PieceType piece = board.getPieceAt(row, col);
                if (blackPieces ? piece.isBlack() : piece.isWhite()) {
                    count++;
                }
            }
        }
        return count;
    }
}
