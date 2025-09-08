package org.example;

import java.util.List;

public class CpuPlayer {
    private final CheckersAI ai;

    public CpuPlayer(MoveRules rules) {
        ai = new CheckersAI(rules);
    }

    public boolean takeTurn(Board board, Players players) {
        if (players.isBlackTurn()) {
            return false;
        }

        List<CheckersAI.Move> moves = ai.generateMoves(board, false);
        if (moves.isEmpty()) {
            return false;
        }

        CheckersAI.Move move = moves.get(0);
        for (CheckersAI.Step step : move.steps()) {
            if (!players.hasSelectedPiece()
                    && !players.selectPiece(board, step.fromRow(), step.fromCol())) {
                return false;
            }
            if (!players.moveSelectedPiece(board, step.toRow(), step.toCol())) {
                return false;
            }
        }
        return true;
    }
}
