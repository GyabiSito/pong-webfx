package com.taller.pong.domain.rules;

import com.taller.pong.domain.constants.GameConfig;
import com.taller.pong.domain.model.Ball;
import com.taller.pong.domain.model.PlayerSide;

public class ScoringService {
    public PlayerSide detectPoint(Ball ball) {
        if (ball.right() < 0) {
            return PlayerSide.RIGHT;
        }

        if (ball.left() > GameConfig.WIDTH) {
            return PlayerSide.LEFT;
        }

        return null;
    }
}
