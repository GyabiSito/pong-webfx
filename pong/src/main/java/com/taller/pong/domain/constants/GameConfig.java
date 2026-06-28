package com.taller.pong.domain.constants;

public final class GameConfig {
    private GameConfig() {
    }

    public static final double WIDTH = 900;
    public static final double HEIGHT = 600;

    public static final double PADDLE_WIDTH = 16;
    public static final double PADDLE_INITIAL_HEIGHT = 120;
    public static final double PADDLE_MIN_HEIGHT = 50;
    public static final double PADDLE_SPEED = 420;

    public static final double BALL_SIZE = 14;
    public static final double BALL_INITIAL_SPEED = 380;

    public static final double BALL_MIN_ANGLE_DEGREES = -35;
    public static final double BALL_MAX_ANGLE_DEGREES = 35;
    public static final double BALL_MAX_BOUNCE_ANGLE_DEGREES = 55;

    public static final double SPEED_MULTIPLIER_ON_HIT = 1.06;
    public static final double PADDLE_SHRINK_ON_HIT = 8;
    public static final double DASH_SPEED = 1600;
    public static final double DASH_DURATION_SECONDS = 0.12;
    public static final double DASH_DOUBLE_TAP_WINDOW_SECONDS = 0.25;
    public static final double DASH_COOLDOWN_SECONDS = 0.60;
    public static final double SCORE_FLASH_DURATION_SECONDS = 0.35;

    public static final int WINNING_SCORE = 10;
}
