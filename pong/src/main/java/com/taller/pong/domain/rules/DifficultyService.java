package com.taller.pong.domain.rules;

import com.taller.pong.domain.model.Ball;
import com.taller.pong.domain.model.Paddle;

public class DifficultyService {
    public void applyHitDifficulty(Ball ball, Paddle paddle) {
        ball.increaseSpeed();
        paddle.shrink();
    }

    public void resetPaddles(Paddle leftPaddle, Paddle rightPaddle) {
        leftPaddle.reset();
        rightPaddle.reset();
    }
}
