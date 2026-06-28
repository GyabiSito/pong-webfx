package com.taller.pong.infrastructure.webfx;

import com.taller.pong.application.GameSnapshot;
import com.taller.pong.domain.constants.GameConfig;
import com.taller.pong.domain.model.Ball;
import com.taller.pong.domain.model.GameState;
import com.taller.pong.domain.model.Paddle;
import com.taller.pong.domain.model.PlayerSide;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WebFxSceneGraphRenderer {
    private static final double DASH_BAR_WIDTH = 70;
    private static final double DASH_BAR_HEIGHT = 6;
    private static final Font SCORE_FONT = Font.font("Consolas", 52);
    private static final Font TITLE_FONT = Font.font("Arial", 26);
    private static final Font MAIN_FONT = Font.font("Arial", 20);
    private static final Font DETAIL_FONT = Font.font("Arial", 16);
    private static final Font SMALL_FONT = Font.font("Arial", 10);

    private final Rectangle background = new Rectangle(GameConfig.WIDTH, GameConfig.HEIGHT);
    private final List<Rectangle> middleLineNodes = new ArrayList<>();
    private final Text leftScoreText = new Text();
    private final Text rightScoreText = new Text();
    private final Rectangle leftPaddleNode = new Rectangle();
    private final Rectangle rightPaddleNode = new Rectangle();
    private final Circle ballNode = new Circle();
    private final Text stateText1 = new Text();
    private final Text stateText2 = new Text();
    private final Text stateText3 = new Text();
    private final Text matchPointText = new Text();
    private final Rectangle leftDashCooldownFrame = new Rectangle();
    private final Rectangle leftDashCooldownFill = new Rectangle();
    private final Text leftDashCooldownText = new Text();
    private final Rectangle rightDashCooldownFrame = new Rectangle();
    private final Rectangle rightDashCooldownFill = new Rectangle();
    private final Text rightDashCooldownText = new Text();
    private final Rectangle scoreFlashLeft = new Rectangle(0, 0, GameConfig.WIDTH / 2, GameConfig.HEIGHT);
    private final Rectangle scoreFlashRight = new Rectangle(
            GameConfig.WIDTH / 2,
            0,
            GameConfig.WIDTH / 2,
            GameConfig.HEIGHT
    );
    private final Rectangle fieldBorder = new Rectangle(1, 1, GameConfig.WIDTH - 2, GameConfig.HEIGHT - 2);

    public WebFxSceneGraphRenderer(Pane root) {
        background.setFill(Color.rgb(8, 9, 12));
        configureScoreFlash(scoreFlashLeft);
        configureScoreFlash(scoreFlashRight);
        configureScoreText(leftScoreText);
        configureScoreText(rightScoreText);
        configurePaddle(leftPaddleNode, Color.rgb(74, 222, 128));
        configurePaddle(rightPaddleNode, Color.rgb(96, 165, 250));
        ballNode.setFill(Color.WHITE);
        configureStateText(stateText1, TITLE_FONT);
        configureStateText(stateText2, MAIN_FONT);
        configureStateText(stateText3, DETAIL_FONT);
        configureStateText(matchPointText, MAIN_FONT);
        configureDashCooldown(leftDashCooldownFrame, leftDashCooldownFill, leftDashCooldownText);
        configureDashCooldown(rightDashCooldownFrame, rightDashCooldownFill, rightDashCooldownText);
        configureFieldBorder();
        createMiddleLine();

        root.getChildren().add(background);
        root.getChildren().add(scoreFlashLeft);
        root.getChildren().add(scoreFlashRight);
        root.getChildren().addAll(middleLineNodes);
        root.getChildren().add(leftScoreText);
        root.getChildren().add(rightScoreText);
        root.getChildren().add(matchPointText);
        root.getChildren().add(leftPaddleNode);
        root.getChildren().add(rightPaddleNode);
        root.getChildren().add(leftDashCooldownFrame);
        root.getChildren().add(leftDashCooldownFill);
        root.getChildren().add(leftDashCooldownText);
        root.getChildren().add(rightDashCooldownFrame);
        root.getChildren().add(rightDashCooldownFill);
        root.getChildren().add(rightDashCooldownText);
        root.getChildren().add(ballNode);
        root.getChildren().add(stateText1);
        root.getChildren().add(stateText2);
        root.getChildren().add(stateText3);
        root.getChildren().add(fieldBorder);
    }

    public void render(GameSnapshot snapshot) {
        updateScoreFlash(snapshot);
        updateMiddleLine(snapshot);
        updateScore(snapshot);
        updateMatchPoint(snapshot);
        updatePaddle(leftPaddleNode, snapshot.leftPaddle());
        updatePaddle(rightPaddleNode, snapshot.rightPaddle());
        updateDashCooldown(
                leftDashCooldownFrame,
                leftDashCooldownFill,
                leftDashCooldownText,
                snapshot.leftPaddle(),
                snapshot.leftDashCooldownRatio(),
                snapshot.leftDashCooldownRemaining()
        );
        updateDashCooldown(
                rightDashCooldownFrame,
                rightDashCooldownFill,
                rightDashCooldownText,
                snapshot.rightPaddle(),
                snapshot.rightDashCooldownRatio(),
                snapshot.rightDashCooldownRemaining()
        );
        updateBall(snapshot.ball(), snapshot.hitFlashActive());
        updateStateMessage(snapshot);
    }

    private void configureScoreFlash(Rectangle scoreFlash) {
        scoreFlash.setFill(Color.WHITE);
        scoreFlash.setOpacity(0.18);
        scoreFlash.setVisible(false);
    }

    private void configureScoreText(Text text) {
        text.setFill(Color.WHITE);
        text.setFont(SCORE_FONT);
    }

    private void configurePaddle(Rectangle paddleNode, Color color) {
        paddleNode.setFill(color);
        paddleNode.setArcWidth(6);
        paddleNode.setArcHeight(6);
    }

    private void configureStateText(Text text, Font font) {
        text.setFill(Color.WHITE);
        text.setFont(font);
        text.setVisible(false);
    }

    private void configureDashCooldown(Rectangle frame, Rectangle fill, Text text) {
        frame.setFill(Color.TRANSPARENT);
        frame.setStroke(Color.WHITE);
        frame.setWidth(DASH_BAR_WIDTH);
        frame.setHeight(DASH_BAR_HEIGHT);

        fill.setFill(Color.WHITE);
        fill.setHeight(DASH_BAR_HEIGHT);

        text.setFill(Color.WHITE);
        text.setFont(SMALL_FONT);
    }

    private void configureFieldBorder() {
        fieldBorder.setFill(Color.TRANSPARENT);
        fieldBorder.setStroke(Color.GRAY);
        fieldBorder.setStrokeWidth(2);
    }

    private void createMiddleLine() {
        for (int y = 20; y < GameConfig.HEIGHT; y += 32) {
            Rectangle segment = new Rectangle(GameConfig.WIDTH / 2 - 1.5, y, 3, 16);
            segment.setFill(Color.GRAY);
            middleLineNodes.add(segment);
        }
    }

    private void updateScoreFlash(GameSnapshot snapshot) {
        boolean leftVisible = snapshot.scoreFlashActive() && snapshot.lastScorer() == PlayerSide.LEFT;
        boolean rightVisible = snapshot.scoreFlashActive() && snapshot.lastScorer() == PlayerSide.RIGHT;
        scoreFlashLeft.setVisible(leftVisible);
        scoreFlashRight.setVisible(rightVisible);
    }

    private void updateMiddleLine(GameSnapshot snapshot) {
        Color color;
        double width;

        if (snapshot.scoreFlashActive()) {
            color = Color.WHITE;
            width = 4;
        } else if (snapshot.state() == GameState.PLAYING) {
            color = Color.GRAY;
            width = 3;
        } else {
            color = Color.DARKGRAY;
            width = 2;
        }

        for (Rectangle segment : middleLineNodes) {
            segment.setFill(color);
            segment.setWidth(width);
            segment.setX(GameConfig.WIDTH / 2 - width / 2);
        }
    }

    private void updateScore(GameSnapshot snapshot) {
        leftScoreText.setText(String.valueOf(snapshot.score().left()));
        rightScoreText.setText(String.valueOf(snapshot.score().right()));
        placeCentered(leftScoreText, GameConfig.WIDTH / 2 - 90, 72);
        placeCentered(rightScoreText, GameConfig.WIDTH / 2 + 90, 72);
    }

    private void updateMatchPoint(GameSnapshot snapshot) {
        if (snapshot.state() != GameState.PLAYING) {
            matchPointText.setVisible(false);
            return;
        }

        boolean leftMatchPoint = snapshot.score().left() == GameConfig.WINNING_SCORE - 1;
        boolean rightMatchPoint = snapshot.score().right() == GameConfig.WINNING_SCORE - 1;

        if (!leftMatchPoint && !rightMatchPoint) {
            matchPointText.setVisible(false);
            return;
        }

        if (leftMatchPoint && rightMatchPoint) {
            matchPointText.setText("MATCH POINT");
        } else if (leftMatchPoint) {
            matchPointText.setText("MATCH POINT J1");
        } else {
            matchPointText.setText("MATCH POINT J2");
        }

        matchPointText.setVisible(true);
        placeCentered(matchPointText, GameConfig.WIDTH / 2, 110);
    }

    private void updatePaddle(Rectangle paddleNode, Paddle paddle) {
        paddleNode.setX(paddle.x());
        paddleNode.setY(paddle.y());
        paddleNode.setWidth(paddle.width());
        paddleNode.setHeight(paddle.height());
    }

    private void updateDashCooldown(
            Rectangle frame,
            Rectangle fill,
            Text text,
            Paddle paddle,
            double cooldownRatio,
            double cooldownRemaining
    ) {
        double x = paddle.x() + paddle.width() / 2 - DASH_BAR_WIDTH / 2;
        double y = paddle.y() - 18;

        if (y < 8) {
            y = paddle.y() + paddle.height() + 10;
        }

        double availableRatio = Math.max(0, Math.min(1, 1.0 - cooldownRatio));

        frame.setX(x);
        frame.setY(y);
        fill.setX(x);
        fill.setY(y);
        fill.setWidth(DASH_BAR_WIDTH * availableRatio);
        text.setText(cooldownRatio <= 0 ? "DASH" : Math.ceil(cooldownRemaining * 10) / 10.0 + "s");
        placeCentered(text, x + DASH_BAR_WIDTH / 2, y - 3);
    }

    private void updateBall(Ball ball, boolean hitFlashActive) {
        double radius = hitFlashActive ? ball.size() / 2 + 4 : ball.size() / 2;
        ballNode.setRadius(radius);
        ballNode.setCenterX(ball.x() + ball.size() / 2);
        ballNode.setCenterY(ball.y() + ball.size() / 2);
    }

    private void updateStateMessage(GameSnapshot snapshot) {
        stateText1.setVisible(snapshot.state() != GameState.PLAYING);
        stateText2.setVisible(snapshot.state() != GameState.PLAYING);
        stateText3.setVisible(snapshot.state() != GameState.PLAYING);

        if (snapshot.state() == GameState.START) {
            stateText1.setFont(TITLE_FONT);
            stateText2.setFont(MAIN_FONT);
            stateText3.setFont(DETAIL_FONT);
            stateText1.setText("Pong");
            stateText2.setText("SPACE para empezar");
            stateText3.setText(
                    "Jugador 1: W/S    Jugador 2: flechas\n"
                            + "Doble toque en direccion = dash vertical\n"
                            + "Cada golpe achica tu paleta y acelera la pelota\n"
                            + "El rebote depende de la zona de impacto\n"
                            + "Primero a 10 gana"
            );
            placeCentered(stateText1, GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2 - 120);
            placeCentered(stateText2, GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2 - 75);
            placeCentered(stateText3, GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2 - 35);
            return;
        }

        if (snapshot.state() == GameState.FINISHED) {
            PlayerSide winner = snapshot.score().winner();
            String message = winner == PlayerSide.LEFT ? "Gano el Jugador 1" : "Gano el Jugador 2";

            stateText1.setFont(TITLE_FONT);
            stateText2.setFont(MAIN_FONT);
            stateText3.setFont(DETAIL_FONT);
            stateText1.setText(message);
            stateText2.setText("Resultado final: " + snapshot.score().left() + " - " + snapshot.score().right());
            stateText3.setText("R para reiniciar");
            placeCentered(stateText1, GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2 - 50);
            placeCentered(stateText2, GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2 - 10);
            placeCentered(stateText3, GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2 + 35);
        }
    }

    private void placeCentered(Text text, double centerX, double baselineY) {
        text.setX(centerX - text.getLayoutBounds().getWidth() / 2);
        text.setY(baselineY);
    }
}
