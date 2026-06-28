
package com.taller.pong.infrastructure.webfx;

import com.taller.pong.application.GameUseCase;
import com.taller.pong.application.InputState;
import com.taller.pong.domain.constants.GameConfig;
import com.taller.pong.domain.rules.BallSpawnService;
import com.taller.pong.domain.rules.CollisionService;
import com.taller.pong.domain.rules.DifficultyService;
import com.taller.pong.domain.rules.ScoringService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WebFxGameApp extends Application {
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setPrefSize(GameConfig.WIDTH, GameConfig.HEIGHT);
        root.setMinSize(GameConfig.WIDTH, GameConfig.HEIGHT);
        root.setMaxSize(GameConfig.WIDTH, GameConfig.HEIGHT);
        root.setFocusTraversable(true);

        InputState inputState = new InputState();
        GameUseCase gameUseCase = new GameUseCase(
                new CollisionService(),
                new ScoringService(),
                new DifficultyService(),
                new BallSpawnService(),
                new WebFxSoundManager()
        );

        WebFxSceneGraphRenderer renderer = new WebFxSceneGraphRenderer(root);
        WebFxKeyboardInputHandler keyboardInputHandler = new WebFxKeyboardInputHandler(inputState);

        Scene scene = new Scene(root, GameConfig.WIDTH, GameConfig.HEIGHT);
        keyboardInputHandler.attachTo(scene);

        WebFxGameLoop gameLoop = new WebFxGameLoop(gameUseCase, renderer, inputState);

        stage.setTitle("Pong - WebFX");
        stage.setScene(scene);
        stage.show();

        root.requestFocus();
        gameLoop.start();
    }
}
