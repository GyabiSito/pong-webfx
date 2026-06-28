package com.taller.pong.infrastructure.webfx;

import com.taller.pong.application.InputState;
import com.taller.pong.domain.constants.GameConfig;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class WebFxKeyboardInputHandler {
    private final InputState inputState;
    private long lastWPressTime;
    private long lastSPressTime;
    private long lastUpPressTime;
    private long lastDownPressTime;
    private boolean wPressed;
    private boolean sPressed;
    private boolean upPressed;
    private boolean downPressed;

    public WebFxKeyboardInputHandler(InputState inputState) {
        this.inputState = inputState;
    }

    public void attachTo(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.W && !wPressed) {
                wPressed = true;
                inputState.setLeftUp(true);
                long now = System.nanoTime();

                if (isDoubleTap(now, lastWPressTime)) {
                    inputState.setLeftDashUpRequested(true);
                }

                lastWPressTime = now;
            }

            if (code == KeyCode.S && !sPressed) {
                sPressed = true;
                inputState.setLeftDown(true);
                long now = System.nanoTime();

                if (isDoubleTap(now, lastSPressTime)) {
                    inputState.setLeftDashDownRequested(true);
                }

                lastSPressTime = now;
            }

            if (code == KeyCode.UP && !upPressed) {
                upPressed = true;
                inputState.setRightUp(true);
                long now = System.nanoTime();

                if (isDoubleTap(now, lastUpPressTime)) {
                    inputState.setRightDashUpRequested(true);
                }

                lastUpPressTime = now;
            }

            if (code == KeyCode.DOWN && !downPressed) {
                downPressed = true;
                inputState.setRightDown(true);
                long now = System.nanoTime();

                if (isDoubleTap(now, lastDownPressTime)) {
                    inputState.setRightDashDownRequested(true);
                }

                lastDownPressTime = now;
            }

            if (code == KeyCode.SPACE) {
                inputState.setStartPressed(true);
            }

            if (code == KeyCode.R) {
                inputState.setResetPressed(true);
            }
        });

        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.W) {
                wPressed = false;
                inputState.setLeftUp(false);
            }

            if (code == KeyCode.S) {
                sPressed = false;
                inputState.setLeftDown(false);
            }

            if (code == KeyCode.UP) {
                upPressed = false;
                inputState.setRightUp(false);
            }

            if (code == KeyCode.DOWN) {
                downPressed = false;
                inputState.setRightDown(false);
            }
        });
    }

    private boolean isDoubleTap(long now, long lastPressTime) {
        double elapsedSeconds = (now - lastPressTime) / 1_000_000_000.0;
        return lastPressTime > 0 && elapsedSeconds <= GameConfig.DASH_DOUBLE_TAP_WINDOW_SECONDS;
    }
}
