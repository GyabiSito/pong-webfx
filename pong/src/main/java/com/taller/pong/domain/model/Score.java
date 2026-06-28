package com.taller.pong.domain.model;

import com.taller.pong.domain.constants.GameConfig;

public class Score {
    private int left;
    private int right;

    public void addPoint(PlayerSide side) {
        if (side == PlayerSide.LEFT) {
            left++;
        } else {
            right++;
        }
    }

    public boolean hasWinner() {
        return left >= GameConfig.WINNING_SCORE || right >= GameConfig.WINNING_SCORE;
    }

    public PlayerSide winner() {
        if (left >= GameConfig.WINNING_SCORE) {
            return PlayerSide.LEFT;
        }

        if (right >= GameConfig.WINNING_SCORE) {
            return PlayerSide.RIGHT;
        }

        return null;
    }

    public void reset() {
        left = 0;
        right = 0;
    }

    public int left() {
        return left;
    }

    public int right() {
        return right;
    }
}
