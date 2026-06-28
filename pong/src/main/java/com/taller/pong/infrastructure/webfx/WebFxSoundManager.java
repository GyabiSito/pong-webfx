package com.taller.pong.infrastructure.webfx;

import com.taller.pong.application.SoundEffects;
import org.teavm.jso.JSBody;

public final class WebFxSoundManager implements SoundEffects {

    @Override
    public void paddleHit() {
        playAudio("sounds/paddle.wav");
    }

    @Override
    public void wallHit() {
        playAudio("sounds/wall.wav");
    }

    @Override
    public void score() {
        playAudio("sounds/score.wav");
    }

    @Override
    public void start() {
        playAudio("sounds/start.wav");
    }

    @JSBody(
            params = {"path"},
            script = ""
                    + "try {"
                    + "  if (!window.__pongSounds) { window.__pongSounds = {}; }"
                    + "  var audio = window.__pongSounds[path];"
                    + "  if (!audio) {"
                    + "    audio = new Audio(path);"
                    + "    audio.preload = 'auto';"
                    + "    window.__pongSounds[path] = audio;"
                    + "  }"
                    + "  audio.currentTime = 0;"
                    + "  var promise = audio.play();"
                    + "  if (promise && promise.catch) {"
                    + "    promise.catch(function(e) { console.log('[audio] blocked:', path); });"
                    + "  }"
                    + "} catch (e) {"
                    + "  console.log('[audio] error:', path, e);"
                    + "}"
    )
    private static native void playAudio(String path);
}
