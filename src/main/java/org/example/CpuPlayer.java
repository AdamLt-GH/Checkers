package org.example;

public class CpuPlayer {
    private final MoveRules rules;

    public CpuPlayer(MoveRules rules) {
        this.rules = rules;
    }

    public boolean takeTurn(Board board, Players players) {
        if (players.isBlackTurn()) {
            return false;
        }

        boolean moved = false;
        int movesMade = 0;

        // the limit is just here so a broken board can never loop forever
        while (!players.isBlackTurn() && movesMade < 12) {
            Move move = findMove(board, players);
            if (move == null) {
                return moved;
            }

            if (!players.hasSelectedPiece()
                    && !players.selectPiece(board, move.fromRow, move.fromCol)) {
                return moved;
            }
            if (!players.moveSelectedPiece(board, move.toRow, move.toCol)) {
                return moved;
            }

            moved = true;
            movesMade++;
        }
        return moved;
    }

    private Move findMove(Board board, Players players) {
        if (players.hasSelectedPiece()) {
            return findMoveFrom(board, players.getSelectedRow(), players.getSelectedCol());
        }

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Move move = findMoveFrom(board, row, col);
                if (move != null) {
                    return move;
                }
            }
        }
        return null;
    }

    private Move findMoveFrom(Board board, int row, int col) {
        int[][] changes = {
                {-2, -2}, {-2, 2}, {2, -2}, {2, 2},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] change : changes) {
            int toRow = row + change[0];
            int toCol = col + change[1];
            if (rules.isValidMove(board, row, col, toRow, toCol, false)) {
                return new Move(row, col, toRow, toCol);
            }
        }
        return null;
    }

    private record Move(int fromRow, int fromCol, int toRow, int toCol) {
    }
}
