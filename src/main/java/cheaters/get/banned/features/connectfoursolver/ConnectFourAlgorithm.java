/*
 * Decompiled with CFR 0.150.
 */
package cheaters.get.banned.features.connectfoursolver;

public class ConnectFourAlgorithm {
    public Board board;
    private int nextMoveLocation = -1;
    private int MAX_DEPTH = 9;

    public ConnectFourAlgorithm(Board board) {
        this.board = board;
    }

    public int gameResult(Board b) {
        int aiScore = 0;
        int humanScore = 0;
        for (int i = 5; i >= 0; --i) {
            for (int j = 0; j <= 6; ++j) {
                int k;
                if (b.board[i][j] == 0) continue;
                if (j <= 3) {
                    for (k = 0; k < 4; ++k) {
                        if (b.board[i][j + k] == 1) {
                            ++aiScore;
                            continue;
                        }
                        if (b.board[i][j + k] != 2) break;
                        ++humanScore;
                    }
                    if (aiScore == 4) {
                        return 1;
                    }
                    if (humanScore == 4) {
                        return 2;
                    }
                    aiScore = 0;
                    humanScore = 0;
                }
                if (i >= 3) {
                    for (k = 0; k < 4; ++k) {
                        if (b.board[i - k][j] == 1) {
                            ++aiScore;
                            continue;
                        }
                        if (b.board[i - k][j] != 2) break;
                        ++humanScore;
                    }
                    if (aiScore == 4) {
                        return 1;
                    }
                    if (humanScore == 4) {
                        return 2;
                    }
                    aiScore = 0;
                    humanScore = 0;
                }
                if (j <= 3 && i >= 3) {
                    for (k = 0; k < 4; ++k) {
                        if (b.board[i - k][j + k] == 1) {
                            ++aiScore;
                            continue;
                        }
                        if (b.board[i - k][j + k] != 2) break;
                        ++humanScore;
                    }
                    if (aiScore == 4) {
                        return 1;
                    }
                    if (humanScore == 4) {
                        return 2;
                    }
                    aiScore = 0;
                    humanScore = 0;
                }
                if (j < 3 || i < 3) continue;
                for (k = 0; k < 4; ++k) {
                    if (b.board[i - k][j - k] == 1) {
                        ++aiScore;
                        continue;
                    }
                    if (b.board[i - k][j - k] != 2) break;
                    ++humanScore;
                }
                if (aiScore == 4) {
                    return 1;
                }
                if (humanScore == 4) {
                    return 2;
                }
                aiScore = 0;
                humanScore = 0;
            }
        }
        for (int j = 0; j < 7; ++j) {
            if (b.board[0][j] != 0) continue;
            return -1;
        }
        return 0;
    }

    int calculateScore(int aiScore, int moreMoves) {
        int moveScore = 4 - moreMoves;
        if (aiScore == 0) {
            return 0;
        }
        if (aiScore == 1) {
            return 1 * moveScore;
        }
        if (aiScore == 2) {
            return 10 * moveScore;
        }
        if (aiScore == 3) {
            return 100 * moveScore;
        }
        return 1000;
    }

    public int evaluateBoard(Board b) {
        int aiScore = 1;
        int score = 0;
        int blanks = 0;
        int k = 0;
        int moreMoves = 0;
        for (int i = 5; i >= 0; --i) {
            for (int j = 0; j <= 6; ++j) {
                int row;
                int m;
                int m2;
                int column;
                int c;
                if (b.board[i][j] == 0 || b.board[i][j] == 2) continue;
                if (j <= 3) {
                    for (k = 1; k < 4; ++k) {
                        if (b.board[i][j + k] == 1) {
                            ++aiScore;
                            continue;
                        }
                        if (b.board[i][j + k] == 2) {
                            aiScore = 0;
                            blanks = 0;
                            break;
                        }
                        ++blanks;
                    }
                    moreMoves = 0;
                    if (blanks > 0) {
                        for (c = 1; c < 4; ++c) {
                            column = j + c;
                            for (m2 = i; m2 <= 5 && b.board[m2][column] == 0; ++m2) {
                                ++moreMoves;
                            }
                        }
                    }
                    if (moreMoves != 0) {
                        score += this.calculateScore(aiScore, moreMoves);
                    }
                    aiScore = 1;
                    blanks = 0;
                }
                if (i >= 3) {
                    for (k = 1; k < 4; ++k) {
                        if (b.board[i - k][j] == 1) {
                            ++aiScore;
                            continue;
                        }
                        if (b.board[i - k][j] != 2) continue;
                        aiScore = 0;
                        break;
                    }
                    moreMoves = 0;
                    if (aiScore > 0) {
                        int column2 = j;
                        for (int m3 = i - k + 1; m3 <= i - 1 && b.board[m3][column2] == 0; ++m3) {
                            ++moreMoves;
                        }
                    }
                    if (moreMoves != 0) {
                        score += this.calculateScore(aiScore, moreMoves);
                    }
                    aiScore = 1;
                    blanks = 0;
                }
                if (j >= 3) {
                    for (k = 1; k < 4; ++k) {
                        if (b.board[i][j - k] == 1) {
                            ++aiScore;
                            continue;
                        }
                        if (b.board[i][j - k] == 2) {
                            aiScore = 0;
                            blanks = 0;
                            break;
                        }
                        ++blanks;
                    }
                    moreMoves = 0;
                    if (blanks > 0) {
                        for (c = 1; c < 4; ++c) {
                            column = j - c;
                            for (m2 = i; m2 <= 5 && b.board[m2][column] == 0; ++m2) {
                                ++moreMoves;
                            }
                        }
                    }
                    if (moreMoves != 0) {
                        score += this.calculateScore(aiScore, moreMoves);
                    }
                    aiScore = 1;
                    blanks = 0;
                }
                if (j <= 3 && i >= 3) {
                    for (k = 1; k < 4; ++k) {
                        if (b.board[i - k][j + k] == 1) {
                            ++aiScore;
                            continue;
                        }
                        if (b.board[i - k][j + k] == 2) {
                            aiScore = 0;
                            blanks = 0;
                            break;
                        }
                        ++blanks;
                    }
                    moreMoves = 0;
                    if (blanks > 0) {
                        block11: for (c = 1; c < 4; ++c) {
                            column = j + c;
                            for (m = row = i - c; m <= 5; ++m) {
                                if (b.board[m][column] == 0) {
                                    ++moreMoves;
                                    continue;
                                }
                                if (b.board[m][column] != 1) continue block11;
                            }
                        }
                        if (moreMoves != 0) {
                            score += this.calculateScore(aiScore, moreMoves);
                        }
                        aiScore = 1;
                        blanks = 0;
                    }
                }
                if (i < 3 || j < 3) continue;
                for (k = 1; k < 4; ++k) {
                    if (b.board[i - k][j - k] == 1) {
                        ++aiScore;
                        continue;
                    }
                    if (b.board[i - k][j - k] == 2) {
                        aiScore = 0;
                        blanks = 0;
                        break;
                    }
                    ++blanks;
                }
                moreMoves = 0;
                if (blanks <= 0) continue;
                block14: for (c = 1; c < 4; ++c) {
                    column = j - c;
                    for (m = row = i - c; m <= 5; ++m) {
                        if (b.board[m][column] == 0) {
                            ++moreMoves;
                            continue;
                        }
                        if (b.board[m][column] != 1) continue block14;
                    }
                }
                if (moreMoves != 0) {
                    score += this.calculateScore(aiScore, moreMoves);
                }
                aiScore = 1;
                blanks = 0;
            }
        }
        return score;
    }

    public int minimax(int depth, int turn, int alpha, int beta) {
        if (beta <= alpha) {
            if (turn == 1) {
                return Integer.MAX_VALUE;
            }
            return Integer.MIN_VALUE;
        }
        int gameResult = this.gameResult(this.board);
        if (gameResult == 1) {
            return 0x3FFFFFFF;
        }
        if (gameResult == 2) {
            return -1073741824;
        }
        if (gameResult == 0) {
            return 0;
        }
        if (depth == this.MAX_DEPTH) {
            return this.evaluateBoard(this.board);
        }
        int maxScore = Integer.MIN_VALUE;
        int minScore = Integer.MAX_VALUE;
        for (int j = 0; j <= 6; ++j) {
            int currentScore = 0;
            if (!this.board.isLegalMove(j)) continue;
            if (turn == 1) {
                this.board.placeMove(j, 1);
                currentScore = this.minimax(depth + 1, 2, alpha, beta);
                if (depth == 0) {
                    if (currentScore > maxScore) {
                        this.nextMoveLocation = j;
                    }
                    if (currentScore == 0x3FFFFFFF) {
                        this.board.undoMove(j);
                        break;
                    }
                }
                maxScore = Math.max(currentScore, maxScore);
                alpha = Math.max(currentScore, alpha);
            } else if (turn == 2) {
                this.board.placeMove(j, 2);
                currentScore = this.minimax(depth + 1, 1, alpha, beta);
                minScore = Math.min(currentScore, minScore);
                beta = Math.min(currentScore, beta);
            }
            this.board.undoMove(j);
            if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;
        }
        return turn == 1 ? maxScore : minScore;
    }

    public int getAIMove() {
        this.nextMoveLocation = -1;
        this.minimax(0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return this.nextMoveLocation;
    }

    public static class Board {
        byte[][] board = new byte[6][7];

        public Board(byte[][] board) {
            this.board = board;
        }

        public Board() {
            this.board = new byte[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        }

        public boolean isLegalMove(int column) {
            if (column < 0 || column > this.board[0].length) {
                return false;
            }
            return this.board[0][column] == 0;
        }

        public boolean placeMove(int column, int player) {
            if (!this.isLegalMove(column)) {
                return false;
            }
            for (int i = 5; i >= 0; --i) {
                if (this.board[i][column] != 0) continue;
                this.board[i][column] = (byte)player;
                return true;
            }
            return false;
        }

        public void undoMove(int column) {
            for (int i = 0; i <= 5; ++i) {
                if (this.board[i][column] == 0) continue;
                this.board[i][column] = 0;
                break;
            }
        }
    }
}

