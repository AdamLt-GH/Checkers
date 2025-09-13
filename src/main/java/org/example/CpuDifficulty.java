package org.example;

public enum CpuDifficulty {
    EASY(2),
    MEDIUM(4),
    HARD(6);

    private final int searchDepth;

    CpuDifficulty(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public int getSearchDepth() {
        return searchDepth;
    }
}
