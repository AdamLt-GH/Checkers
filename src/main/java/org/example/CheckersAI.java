package org.example;

import java.util.ArrayList;
import java.util.List;

public class CheckersAI {
    private static final int PIECE_VALUE = 5;
    private static final int KING_VALUE = 10;
    private static final int WIN_SCORE = 100000;

    private final MoveRules rules;

    public CheckersAI(MoveRules rules) {
        this.rules = rules;
    }

    public List<Move> generateMoves(Board board, boolean blackTurn) {
        List<Move> moves = new ArrayList<>();

        if (rules.hasCaptureMove(board, blackTurn)) {
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    addCaptureMoves(board, row, col, blackTurn, new ArrayList<>(), moves);
                }
            }
            return moves;
        }

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                addRegularMoves(board, row, col, blackTurn, moves);
            }
        }
        return moves;
    }

    public void applyMove(Board board, Move move) {
        for (Step step : move.steps()) {
            board.movePiece(step.fromRow(), step.fromCol(), step.toRow(), step.toCol());
        }
    }

    public int evaluate(Board board, boolean forBlack) {
        int score = 0;
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                PieceType piece = board.getPieceAt(row, col);
                if (piece == PieceType.EMPTY) {
                    continue;
                }

                int value = piece.isKing() ? KING_VALUE : PIECE_VALUE;
                value += positionBonus(piece, row, col);
                boolean ownPiece = forBlack ? piece.isBlack() : piece.isWhite();
                score += ownPiece ? value : -value;
            }
        }
        return score;
    }

    public Move findBestMove(Board board, boolean aiBlack, int depth) {
        List<Move> moves = generateMoves(board, aiBlack);
        if (moves.isEmpty()) {
            return null;
        }

        int searchDepth = Math.max(1, depth);
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = moves.get(0);

        for (Move move : moves) {
            Board nextBoard = board.copy();
            applyMove(nextBoard, move);
            int score = minimax(nextBoard, searchDepth - 1, !aiBlack, aiBlack,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(Board board, int depth, boolean currentTurn, boolean aiBlack,
                        int alpha, int beta) {
        List<Move> moves = generateMoves(board, currentTurn);
        if (moves.isEmpty()) {
            return currentTurn == aiBlack ? -WIN_SCORE - depth : WIN_SCORE + depth;
        }
        if (depth == 0) {
            return evaluate(board, aiBlack);
        }

        boolean maximizing = currentTurn == aiBlack;
        int bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Move move : moves) {
            Board nextBoard = board.copy();
            applyMove(nextBoard, move);
            int score = minimax(nextBoard, depth - 1, !currentTurn, aiBlack, alpha, beta);

            if (maximizing) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
            } else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
            }

            if (beta <= alpha) {
                break;
            }
        }
        return bestScore;
    }

    private void addRegularMoves(Board board, int row, int col, boolean blackTurn,
                                 List<Move> moves) {
        int[][] changes = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] change : changes) {
            int toRow = row + change[0];
            int toCol = col + change[1];
            if (rules.isValidMove(board, row, col, toRow, toCol, blackTurn)) {
                moves.add(new Move(List.of(new Step(row, col, toRow, toCol))));
            }
        }
    }

    private void addCaptureMoves(Board board, int row, int col, boolean blackTurn,
                                 List<Step> steps, List<Move> moves) {
        int[][] changes = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
        boolean foundCapture = false;

        for (int[] change : changes) {
            int toRow = row + change[0];
            int toCol = col + change[1];
            if (!rules.isValidMove(board, row, col, toRow, toCol, blackTurn)) {
                continue;
            }

            foundCapture = true;
            Board nextBoard = board.copy();
            nextBoard.movePiece(row, col, toRow, toCol);

            List<Step> nextSteps = new ArrayList<>(steps);
            nextSteps.add(new Step(row, col, toRow, toCol));
            addCaptureMoves(nextBoard, toRow, toCol, blackTurn, nextSteps, moves);
        }

        if (!foundCapture && !steps.isEmpty()) {
            moves.add(new Move(steps));
        }
    }

    private int positionBonus(PieceType piece, int row, int col) {
        int bonus = 0;
        if (row >= 2 && row <= 5 && col >= 2 && col <= 5) {
            bonus++;
        }
        if (!piece.isKing()) {
            bonus += piece.isBlack() ? row / 2 : (Board.SIZE - 1 - row) / 2;
        }
        return bonus;
    }

    public record Step(int fromRow, int fromCol, int toRow, int toCol) {
    }

    public record Move(List<Step> steps) {
        public Move {
            steps = List.copyOf(steps);
        }
    }
}
