package com.taller.pong.application;

import com.taller.pong.domain.model.Ball;
import com.taller.pong.domain.model.GameState;
import com.taller.pong.domain.model.Paddle;
import com.taller.pong.domain.model.PlayerSide;
import com.taller.pong.domain.model.Score;

public class GameSnapshot {
    private final Ball ball;
    private final Paddle leftPaddle;
    private final Paddle rightPaddle;
    private final Score score;
    private final GameState state;
    private final boolean hitFlashActive;
    private final double leftDashCooldownRemaining;
    private final double rightDashCooldownRemaining;
    private final double leftDashCooldownRatio;
    private final double rightDashCooldownRatio;
    private final PlayerSide lastScorer;
    private final boolean scoreFlashActive;

    public GameSnapshot(
            Ball ball,
            Paddle leftPaddle,
            Paddle rightPaddle,
            Score score,
            GameState state,
            boolean hitFlashActive,
            double leftDashCooldownRemaining,
            double rightDashCooldownRemaining,
            double leftDashCooldownRatio,
            double rightDashCooldownRatio,
            PlayerSide lastScorer,
            boolean scoreFlashActive
    ) {
        this.ball = ball;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        this.score = score;
        this.state = state;
        this.hitFlashActive = hitFlashActive;
        this.leftDashCooldownRemaining = leftDashCooldownRemaining;
        this.rightDashCooldownRemaining = rightDashCooldownRemaining;
        this.leftDashCooldownRatio = leftDashCooldownRatio;
        this.rightDashCooldownRatio = rightDashCooldownRatio;
        this.lastScorer = lastScorer;
        this.scoreFlashActive = scoreFlashActive;
    }

    public Ball ball() {
        return ball;
    }

    public Paddle leftPaddle() {
        return leftPaddle;
    }

    public Paddle rightPaddle() {
        return rightPaddle;
    }

    public Score score() {
        return score;
    }

    public GameState state() {
        return state;
    }

    public boolean hitFlashActive() {
        return hitFlashActive;
    }

    public double leftDashCooldownRemaining() {
        return leftDashCooldownRemaining;
    }

    public double rightDashCooldownRemaining() {
        return rightDashCooldownRemaining;
    }

    public double leftDashCooldownRatio() {
        return leftDashCooldownRatio;
    }

    public double rightDashCooldownRatio() {
        return rightDashCooldownRatio;
    }

    public PlayerSide lastScorer() {
        return lastScorer;
    }

    public boolean scoreFlashActive() {
        return scoreFlashActive;
    }
}
