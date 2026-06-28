package com.taller.pong.application;

import com.taller.pong.domain.constants.GameConfig;
import com.taller.pong.domain.model.Ball;
import com.taller.pong.domain.model.DashDirection;
import com.taller.pong.domain.model.GameState;
import com.taller.pong.domain.model.Paddle;
import com.taller.pong.domain.model.PlayerSide;
import com.taller.pong.domain.model.Score;
import com.taller.pong.domain.rules.BallSpawnService;
import com.taller.pong.domain.rules.CollisionService;
import com.taller.pong.domain.rules.DifficultyService;
import com.taller.pong.domain.rules.ScoringService;

public class GameUseCase {
    private final Ball ball;
    private final Paddle leftPaddle;
    private final Paddle rightPaddle;
    private final Score score;
    private final CollisionService collisionService;
    private final ScoringService scoringService;
    private final DifficultyService difficultyService;
    private final BallSpawnService ballSpawnService;
    private final SoundEffects soundEffects;

    private GameState state;
    private double hitFlashTimer;
    private DashDirection leftDashDirection = DashDirection.NONE;
    private DashDirection rightDashDirection = DashDirection.NONE;
    private double leftDashTimeRemaining;
    private double rightDashTimeRemaining;
    private double leftDashCooldownRemaining;
    private double rightDashCooldownRemaining;
    private double scoreFlashTimer;
    private PlayerSide lastScorer;

    public GameUseCase() {
        this(SoundEffects.none());
    }

    public GameUseCase(SoundEffects soundEffects) {
        this(
                new CollisionService(),
                new ScoringService(),
                new DifficultyService(),
                new BallSpawnService(),
                soundEffects
        );
    }

    public GameUseCase(
            CollisionService collisionService,
            ScoringService scoringService,
            DifficultyService difficultyService,
            BallSpawnService ballSpawnService,
            SoundEffects soundEffects
    ) {
        this.collisionService = collisionService;
        this.scoringService = scoringService;
        this.difficultyService = difficultyService;
        this.ballSpawnService = ballSpawnService;
        this.soundEffects = soundEffects == null ? SoundEffects.none() : soundEffects;

        this.ball = new Ball();

        this.leftPaddle = new Paddle(
                PlayerSide.LEFT,
                40,
                GameConfig.HEIGHT / 2 - GameConfig.PADDLE_INITIAL_HEIGHT / 2
        );

        this.rightPaddle = new Paddle(
                PlayerSide.RIGHT,
                GameConfig.WIDTH - 40 - GameConfig.PADDLE_WIDTH,
                GameConfig.HEIGHT / 2 - GameConfig.PADDLE_INITIAL_HEIGHT / 2
        );

        this.score = new Score();
        this.state = GameState.START;
    }

    public void update(double deltaTime, InputState input) {
        updateHitFlash(deltaTime);
        updateScoreFlash(deltaTime);
        updateDashCooldowns(deltaTime);

        if (input.resetPressed()) {
            resetGame();
            input.setResetPressed(false);
            input.setStartPressed(false);
            input.clearDashRequests();
            return;
        }

        if (state == GameState.START && input.startPressed()) {
            startGame();
            input.setStartPressed(false);
        }

        if (state != GameState.PLAYING) {
            input.clearDashRequests();
            return;
        }

        processDashRequests(input);
        updateActiveDashes(deltaTime);
        updatePaddles(deltaTime, input);

        ball.update(deltaTime);

        boolean hitWall = collisionService.handleWallCollision(ball);

        if (hitWall) {
            soundEffects.wallHit();
        }

        handlePaddleCollision(leftPaddle);
        handlePaddleCollision(rightPaddle);

        PlayerSide scorer = scoringService.detectPoint(ball);
        if (scorer != null) {
            handlePoint(scorer);
        }
    }

    private void updateDashCooldowns(double deltaTime) {
        if (leftDashCooldownRemaining > 0) {
            leftDashCooldownRemaining = Math.max(0, leftDashCooldownRemaining - deltaTime);
        }

        if (rightDashCooldownRemaining > 0) {
            rightDashCooldownRemaining = Math.max(0, rightDashCooldownRemaining - deltaTime);
        }
    }

    private void processDashRequests(InputState input) {
        if (leftDashCooldownRemaining <= 0 && leftDashTimeRemaining <= 0) {
            if (input.leftDashUpRequested()) {
                startLeftDash(DashDirection.UP);
            } else if (input.leftDashDownRequested()) {
                startLeftDash(DashDirection.DOWN);
            }
        }

        if (rightDashCooldownRemaining <= 0 && rightDashTimeRemaining <= 0) {
            if (input.rightDashUpRequested()) {
                startRightDash(DashDirection.UP);
            } else if (input.rightDashDownRequested()) {
                startRightDash(DashDirection.DOWN);
            }
        }

        input.clearDashRequests();
    }

    private void startLeftDash(DashDirection direction) {
        leftDashDirection = direction;
        leftDashTimeRemaining = GameConfig.DASH_DURATION_SECONDS;
        leftDashCooldownRemaining = GameConfig.DASH_COOLDOWN_SECONDS;
    }

    private void startRightDash(DashDirection direction) {
        rightDashDirection = direction;
        rightDashTimeRemaining = GameConfig.DASH_DURATION_SECONDS;
        rightDashCooldownRemaining = GameConfig.DASH_COOLDOWN_SECONDS;
    }

    private void updateActiveDashes(double deltaTime) {
        if (leftDashTimeRemaining > 0) {
            double activeDeltaTime = Math.min(deltaTime, leftDashTimeRemaining);
            leftPaddle.dashMove(leftDashDirection, activeDeltaTime);
            leftDashTimeRemaining -= activeDeltaTime;

            if (leftDashTimeRemaining <= 0) {
                leftDashTimeRemaining = 0;
                leftDashDirection = DashDirection.NONE;
            }
        }

        if (rightDashTimeRemaining > 0) {
            double activeDeltaTime = Math.min(deltaTime, rightDashTimeRemaining);
            rightPaddle.dashMove(rightDashDirection, activeDeltaTime);
            rightDashTimeRemaining -= activeDeltaTime;

            if (rightDashTimeRemaining <= 0) {
                rightDashTimeRemaining = 0;
                rightDashDirection = DashDirection.NONE;
            }
        }
    }

    public void startGame() {
        if (state == GameState.START) {
            ballSpawnService.spawnFromCenterRandomDirection(ball);
            state = GameState.PLAYING;
            soundEffects.start();
        }
    }

    private void updatePaddles(double deltaTime, InputState input) {
        if (input.leftUp()) {
            leftPaddle.moveUp(deltaTime);
        }

        if (input.leftDown()) {
            leftPaddle.moveDown(deltaTime);
        }

        if (input.rightUp()) {
            rightPaddle.moveUp(deltaTime);
        }

        if (input.rightDown()) {
            rightPaddle.moveDown(deltaTime);
        }
    }

    private void handlePaddleCollision(Paddle paddle) {
        if (collisionService.collidesWithPaddle(ball, paddle)) {
            collisionService.resolvePaddleCollision(ball, paddle);
            difficultyService.applyHitDifficulty(ball, paddle);
            hitFlashTimer = 0.12;
            soundEffects.paddleHit();
        }
    }

    private void handlePoint(PlayerSide scorer) {
        lastScorer = scorer;
        scoreFlashTimer = GameConfig.SCORE_FLASH_DURATION_SECONDS;
        score.addPoint(scorer);
        soundEffects.score();

        if (score.hasWinner()) {
            state = GameState.FINISHED;
            ball.center();
            hitFlashTimer = 0;
            resetDashState();
            return;
        }

        resetRound();
    }

    private void resetRound() {
        difficultyService.resetPaddles(leftPaddle, rightPaddle);
        ballSpawnService.spawnFromCenterRandomDirection(ball);
        hitFlashTimer = 0;
        resetDashState();
    }

    private void resetGame() {
        score.reset();
        difficultyService.resetPaddles(leftPaddle, rightPaddle);
        ball.center();
        state = GameState.START;
        hitFlashTimer = 0;
        scoreFlashTimer = 0;
        lastScorer = null;
        resetDashState();
    }

    private void updateHitFlash(double deltaTime) {
        if (hitFlashTimer > 0) {
            hitFlashTimer -= deltaTime;
        }
    }

    private boolean hitFlashActive() {
        return hitFlashTimer > 0;
    }

    private void updateScoreFlash(double deltaTime) {
        if (scoreFlashTimer > 0) {
            scoreFlashTimer = Math.max(0, scoreFlashTimer - deltaTime);
        }
    }

    private boolean scoreFlashActive() {
        return scoreFlashTimer > 0 && lastScorer != null;
    }

    private void resetDashState() {
        leftDashDirection = DashDirection.NONE;
        rightDashDirection = DashDirection.NONE;
        leftDashTimeRemaining = 0;
        rightDashTimeRemaining = 0;
        leftDashCooldownRemaining = 0;
        rightDashCooldownRemaining = 0;
    }

    private double dashCooldownRatio(double remaining) {
        if (remaining <= 0) {
            return 0;
        }

        return remaining / GameConfig.DASH_COOLDOWN_SECONDS;
    }

    public GameSnapshot snapshot() {
        return new GameSnapshot(
                ball,
                leftPaddle,
                rightPaddle,
                score,
                state,
                hitFlashActive(),
                leftDashCooldownRemaining,
                rightDashCooldownRemaining,
                dashCooldownRatio(leftDashCooldownRemaining),
                dashCooldownRatio(rightDashCooldownRemaining),
                lastScorer,
                scoreFlashActive()
        );
    }
}
