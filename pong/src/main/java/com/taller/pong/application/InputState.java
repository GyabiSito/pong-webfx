package com.taller.pong.application;

public class InputState {
    private boolean leftUp;
    private boolean leftDown;
    private boolean rightUp;
    private boolean rightDown;
    private boolean startPressed;
    private boolean resetPressed;
    private boolean leftDashUpRequested;
    private boolean leftDashDownRequested;
    private boolean rightDashUpRequested;
    private boolean rightDashDownRequested;

    public boolean leftUp() {
        return leftUp;
    }

    public void setLeftUp(boolean leftUp) {
        this.leftUp = leftUp;
    }

    public boolean leftDown() {
        return leftDown;
    }

    public void setLeftDown(boolean leftDown) {
        this.leftDown = leftDown;
    }

    public boolean rightUp() {
        return rightUp;
    }

    public void setRightUp(boolean rightUp) {
        this.rightUp = rightUp;
    }

    public boolean rightDown() {
        return rightDown;
    }

    public void setRightDown(boolean rightDown) {
        this.rightDown = rightDown;
    }

    public boolean startPressed() {
        return startPressed;
    }

    public void setStartPressed(boolean startPressed) {
        this.startPressed = startPressed;
    }

    public boolean resetPressed() {
        return resetPressed;
    }

    public void setResetPressed(boolean resetPressed) {
        this.resetPressed = resetPressed;
    }

    public boolean leftDashUpRequested() {
        return leftDashUpRequested;
    }

    public void setLeftDashUpRequested(boolean leftDashUpRequested) {
        this.leftDashUpRequested = leftDashUpRequested;
    }

    public boolean leftDashDownRequested() {
        return leftDashDownRequested;
    }

    public void setLeftDashDownRequested(boolean leftDashDownRequested) {
        this.leftDashDownRequested = leftDashDownRequested;
    }

    public boolean rightDashUpRequested() {
        return rightDashUpRequested;
    }

    public void setRightDashUpRequested(boolean rightDashUpRequested) {
        this.rightDashUpRequested = rightDashUpRequested;
    }

    public boolean rightDashDownRequested() {
        return rightDashDownRequested;
    }

    public void setRightDashDownRequested(boolean rightDashDownRequested) {
        this.rightDashDownRequested = rightDashDownRequested;
    }

    public void clearDashRequests() {
        leftDashUpRequested = false;
        leftDashDownRequested = false;
        rightDashUpRequested = false;
        rightDashDownRequested = false;
    }
}
