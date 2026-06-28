package com.taller.pong.domain.rules;

import com.taller.pong.domain.constants.GameConfig;
import com.taller.pong.domain.model.Ball;
import com.taller.pong.domain.model.Paddle;
import com.taller.pong.domain.model.PlayerSide;

public class CollisionService {

    public boolean handleWallCollision(Ball ball) {
        boolean hitWall = false;

        if (ball.top() <= 0) {
            ball.setY(0);
            ball.bounceVertical();
            hitWall = true;
        }

        if (ball.bottom() >= GameConfig.HEIGHT) {
            ball.setY(GameConfig.HEIGHT - ball.size());
            ball.bounceVertical();
            hitWall = true;
        }

        return hitWall;
    }

    public boolean collidesWithPaddle(Ball ball, Paddle paddle) {
        return ball.bounds().intersects(paddle.bounds());
    }

    public void resolvePaddleCollision(Ball ball, Paddle paddle) {
        double relativeImpact = (ball.centerY() - paddle.centerY()) / (paddle.height() / 2);
        relativeImpact = clamp(relativeImpact, -1, 1);

        double bounceAngleRadians = Math.toRadians(relativeImpact * GameConfig.BALL_MAX_BOUNCE_ANGLE_DEGREES);
        double directionSign = paddle.side() == PlayerSide.LEFT ? 1 : -1;
        double speed = ball.speed();

        ball.setVelocity(
                Math.cos(bounceAngleRadians) * speed * directionSign,
                Math.sin(bounceAngleRadians) * speed
        );

        if (paddle.side() == PlayerSide.LEFT) {
            ball.setX(paddle.x() + paddle.width());
        } else {
            ball.setX(paddle.x() - ball.size());
        }
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
