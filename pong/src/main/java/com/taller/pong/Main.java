package com.taller.pong;

import com.taller.pong.infrastructure.webfx.WebFxGameApp;
import javafx.application.Application;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Application.launch(WebFxGameApp.class, args);
    }
}
