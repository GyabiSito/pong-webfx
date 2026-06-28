package com.taller.pong.domain.model;

import com.taller.pong.domain.constants.GameConfig;

public class Ball {
    private final double size;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    public Ball() {
        this.size = GameConfig.BALL_SIZE;
        center();
    }

    public void update(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
    }

    public void center() {
        x = GameConfig.WIDTH / 2 - size / 2;
        y = GameConfig.HEIGHT / 2 - size / 2;
        velocityX = 0;
        velocityY = 0;
    }

    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void bounceVertical() {
        velocityY = -velocityY;
    }

    public void bounceHorizontal() {
        velocityX = -velocityX;
    }

    public double centerY() {
        return y + size / 2;
    }

    public double speed() {
        return Math.sqrt(velocityX * velocityX + velocityY * velocityY);
    }

    public void increaseSpeed() {
        velocityX *= GameConfig.SPEED_MULTIPLIER_ON_HIT;
        velocityY *= GameConfig.SPEED_MULTIPLIER_ON_HIT;
    }

    public Rect bounds() {
        return new Rect(x, y, size, size);
    }

    public double left() {
        return x;
    }

    public double right() {
        return x + size;
    }

    public double top() {
        return y;
    }

    public double bottom() {
        return y + size;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double size() {
        return size;
    }

    public double velocityX() {
        return velocityX;
    }

    public double velocityY() {
        return velocityY;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
