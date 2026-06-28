package com.taller.pong.domain.model;

import com.taller.pong.domain.constants.GameConfig;

public class Paddle {
    private final PlayerSide side;
    private final double x;
    private final double width;
    private double y;
    private double height;

    public Paddle(PlayerSide side, double x, double y) {
        this.side = side;
        this.x = x;
        this.y = y;
        this.width = GameConfig.PADDLE_WIDTH;
        this.height = GameConfig.PADDLE_INITIAL_HEIGHT;
    }

    public void moveUp(double deltaTime) {
        y -= GameConfig.PADDLE_SPEED * deltaTime;
        clampInsideScreen();
    }

    public void moveDown(double deltaTime) {
        y += GameConfig.PADDLE_SPEED * deltaTime;
        clampInsideScreen();
    }

    public void dashMove(DashDirection direction, double deltaTime) {
        if (direction == DashDirection.UP) {
            y -= GameConfig.DASH_SPEED * deltaTime;
        }

        if (direction == DashDirection.DOWN) {
            y += GameConfig.DASH_SPEED * deltaTime;
        }

        clampInsideScreen();
    }

    public void shrink() {
        double oldCenter = centerY();
        height = Math.max(GameConfig.PADDLE_MIN_HEIGHT, height - GameConfig.PADDLE_SHRINK_ON_HIT);
        y = oldCenter - height / 2;
        clampInsideScreen();
    }

    public void reset() {
        height = GameConfig.PADDLE_INITIAL_HEIGHT;
        y = GameConfig.HEIGHT / 2 - height / 2;
    }

    private void clampInsideScreen() {
        if (y < 0) {
            y = 0;
        }

        if (y + height > GameConfig.HEIGHT) {
            y = GameConfig.HEIGHT - height;
        }
    }

    public Rect bounds() {
        return new Rect(x, y, width, height);
    }

    public double centerY() {
        return y + height / 2;
    }

    public PlayerSide side() {
        return side;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double width() {
        return width;
    }

    public double height() {
        return height;
    }
}
