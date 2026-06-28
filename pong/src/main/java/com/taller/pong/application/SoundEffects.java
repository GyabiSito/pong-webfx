package com.taller.pong.application;

public interface SoundEffects {

    void paddleHit();

    void wallHit();

    void score();

    void start();

    static SoundEffects none() {
        return new SoundEffects() {
            @Override
            public void paddleHit() {
            }

            @Override
            public void wallHit() {
            }

            @Override
            public void score() {
            }

            @Override
            public void start() {
            }
        };
    }
}
