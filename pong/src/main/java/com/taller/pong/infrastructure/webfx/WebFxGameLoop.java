package com.taller.pong.infrastructure.webfx;

import com.taller.pong.application.GameUseCase;
import com.taller.pong.application.InputState;
import javafx.animation.AnimationTimer;

public class WebFxGameLoop extends AnimationTimer {
    private final GameUseCase gameUseCase;
    private final WebFxSceneGraphRenderer renderer;
    private final InputState inputState;
    private long previousTime;

    public WebFxGameLoop(GameUseCase gameUseCase, WebFxSceneGraphRenderer renderer, InputState inputState) {
        this.gameUseCase = gameUseCase;
        this.renderer = renderer;
        this.inputState = inputState;
    }

    @Override
    public void handle(long now) {
        if (previousTime == 0) {
            previousTime = now;
            renderer.render(gameUseCase.snapshot());
            return;
        }

        // ponytail: cap long frame gaps; a fixed-step accumulator is the upgrade path.
        double deltaTime = Math.min((now - previousTime) / 1_000_000_000.0, 1.0 / 30.0);
        previousTime = now;

        gameUseCase.update(deltaTime, inputState);
        renderer.render(gameUseCase.snapshot());
    }
}
