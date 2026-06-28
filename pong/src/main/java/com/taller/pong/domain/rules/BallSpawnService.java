package com.taller.pong.domain.rules;

import com.taller.pong.domain.constants.GameConfig;
import com.taller.pong.domain.model.Ball;
import com.taller.pong.domain.model.PlayerSide;

import java.util.Random;

public class BallSpawnService {
    private final Random random;

    public BallSpawnService() {
        this(new Random());
    }

    public BallSpawnService(Random random) {
        this.random = random;
    }

    public void spawnFromCenterRandomDirection(Ball ball) {
        spawnFromCenter(ball, random.nextBoolean() ? PlayerSide.LEFT : PlayerSide.RIGHT);
    }

    public void spawnFromCenter(Ball ball, PlayerSide direction) {
        ball.center();

        double angleRadians = Math.toRadians(randomAngleDegrees());
        double directionSign = direction == PlayerSide.LEFT ? -1 : 1;
        double velocityX = Math.cos(angleRadians) * GameConfig.BALL_INITIAL_SPEED * directionSign;
        double velocityY = Math.sin(angleRadians) * GameConfig.BALL_INITIAL_SPEED;

        ball.setVelocity(velocityX, velocityY);
    }

    private double randomAngleDegrees() {
        double range = GameConfig.BALL_MAX_ANGLE_DEGREES - GameConfig.BALL_MIN_ANGLE_DEGREES;
        return GameConfig.BALL_MIN_ANGLE_DEGREES + random.nextDouble() * range;
    }
}
