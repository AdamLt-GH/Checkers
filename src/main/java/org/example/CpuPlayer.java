package org.example;

public class CpuPlayer {
    private final CheckersAI ai;
    private CpuDifficulty difficulty = CpuDifficulty.MEDIUM;

    public CpuPlayer(MoveRules rules) {
        ai = new CheckersAI(rules);
    }

    public void setDifficulty(CpuDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean takeTurn(Board board, Players players) {
        if (players.isBlackTurn()) {
            return false;
        }

        CheckersAI.Move move = ai.findBestMove(board, false, difficulty.getSearchDepth());
        if (move == null) {
            return false;
        }

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
