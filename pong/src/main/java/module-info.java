// File managed by WebFX (DO NOT EDIT MANUALLY)

module pong {

    // Direct dependencies modules
    requires javafx.graphics;
    requires org.teavm.jso;

    // Exported packages
    exports com.taller.pong;
    exports com.taller.pong.application;
    exports com.taller.pong.domain.constants;
    exports com.taller.pong.domain.model;
    exports com.taller.pong.domain.rules;
    exports com.taller.pong.infrastructure.webfx;

    // Resources packages


    // Provided services
    provides javafx.application.Application with com.taller.pong.infrastructure.webfx.WebFxGameApp;

}
