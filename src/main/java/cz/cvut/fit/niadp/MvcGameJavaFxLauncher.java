package cz.cvut.fit.niadp;

import cz.cvut.fit.niadp.mvcgame.MvcGame;
import cz.cvut.fit.niadp.mvcgame.bridge.GameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.bridge.JavaFxGraphics;
import cz.cvut.fit.niadp.mvcgame.decorator.WeatherBackgroundManager;
import cz.cvut.fit.niadp.mvcgame.ChainofResponsibility.AudioManager;
import cz.cvut.fit.niadp.mvcgame.visitor.GameDrawer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MvcGameJavaFxLauncher extends Application {

    private static final MvcGame theMvcGame = new MvcGame();
    private WeatherBackgroundManager backgroundManager;
    private AudioManager audioManager;
    private BorderPane root;

    private String lastWeather = "";
    private AtomicBoolean backgroundChangePending = new AtomicBoolean(false);
    private long lastBackgroundChange = 0;

    // FPS tracking
    private long frameCount = 0;
    private long lastFpsTime = 0;
    private double currentFps = 0;

    private static final int PADDING = 10;

    @Override
    public void init() {
        theMvcGame.init();
        theMvcGame.printStatus();

        backgroundManager = WeatherBackgroundManager.getInstance();
        audioManager = AudioManager.getInstance();
        String initialWeather = theMvcGame.getCurrentWeatherName();
        System.out.println("Initial weather from game: " + initialWeather);

        audioManager.updateWeatherSounds(initialWeather);
        lastWeather = initialWeather;

        new Thread(() -> {
            try {
                Thread.sleep(500);
                if (audioManager != null) {
                    audioManager.playBackgroundMusic();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        System.out.println("Initialization complete.");
    }

    @Override
    public void start(Stage stage) {
        System.out.println("\n=== STARTING APPLICATION ===");

        String winTitle = theMvcGame.getWindowTitle();
        int gameWidth = theMvcGame.getWindowWidth();
        int gameHeight = theMvcGame.getWindowHeight();

        System.out.println("Game dimensions: " + gameWidth + "x" + gameHeight);

        int totalWidth = gameWidth + (2 * PADDING);
        int totalHeight = gameHeight + (2 * PADDING);

        stage.setTitle(winTitle);

        root = new BorderPane();
        root.setPadding(new Insets(PADDING));

        Scene scene = new Scene(root, totalWidth, totalHeight);
        stage.setScene(scene);

        Canvas canvas = new Canvas(gameWidth, gameHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        System.out.println("Canvas created: " + canvas.getWidth() + "x" + canvas.getHeight());

        // Vytvor Bridge pattern: GameGraphics -> JavaFxGraphics
        IGameGraphics gr = new GameGraphics(new JavaFxGraphics(gc));

        System.out.println("Graphics bridge created.");

        GameDrawer renderer = theMvcGame.getGameRenderer();
        if (renderer != null) {
            System.out.println("GameDrawer obtained from MvcGame.");
            renderer.setGraphicContext(gr);
            System.out.println("Graphics context set in GameDrawer.");
        } else {
            System.err.println("ERROR: GameDrawer is null from MvcGame!");
        }

        System.out.println("Setting graphics context to GameView...");
        theMvcGame.getView().setGraphicContext(gr);

        initBackground();

        StackPane canvasContainer = new StackPane(canvas);
        canvasContainer.setAlignment(Pos.CENTER);
        root.setCenter(canvasContainer);

        ArrayList<String> pressedKeysCodes = new ArrayList<>();
        setupKeyHandlers(scene, pressedKeysCodes);

        startGameLoop(pressedKeysCodes);

        AudioManager.getInstance().playBackgroundMusic();

        stage.show();
    }

    private void setupKeyHandlers(Scene scene, ArrayList<String> pressedKeysCodes) {
        scene.setOnKeyPressed(e -> {
            String code = e.getCode().toString();
            if (!pressedKeysCodes.contains(code)) {
                pressedKeysCodes.add(code);
            }
        });
        scene.setOnKeyReleased(e -> pressedKeysCodes.remove(e.getCode().toString()));
    }

    private void initBackground() {
        Background defaultBg = backgroundManager.getCurrentBackground();
        root.setBackground(defaultBg);
        System.out.println("Background initialized.");
    }

    private void startGameLoop(ArrayList<String> pressedKeysCodes) {
        new AnimationTimer() {
            private long lastUpdate = 0;
            private long lastWeatherUpdate = 0;
            private int frameCounter = 0;
            private long lastFPSCheck = 0;
            private double fpsSum = 0;
            private int fpsCount = 0;
            private long lastAudioCheck = 0;
            @Override
            public void handle(long now) {
                frameCounter++;
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastUpdate < 16) {
                    return;
                }
                if (currentTime - lastAudioCheck > 2000) { // Každé 2 sekundy
                    AudioManager.getInstance().checkAndFixBackgroundMusic();
                    lastAudioCheck = currentTime;
                }
                lastUpdate = currentTime;
                calculateFPS(now);

                theMvcGame.processPressedKeys(pressedKeysCodes);
                pressedKeysCodes.clear();

                theMvcGame.update();

                updateGameRendererData();

                theMvcGame.render();

                if (currentTime - lastFPSCheck > 1000) {
                    double avgFps = fpsCount > 0 ? fpsSum / fpsCount : 0;
                    System.out.println("=== FPS REPORT ===");
                    System.out.println("Current FPS: " + String.format("%.1f", currentFps));
                    System.out.println("Average FPS: " + String.format("%.1f", avgFps));
                    System.out.println("Game objects: " + theMvcGame.getObjectCount());
                    System.out.println("==================");

                    fpsSum = 0;
                    fpsCount = 0;
                    lastFPSCheck = currentTime;
                }

                fpsSum += currentFps;
                fpsCount++;

                if (currentTime - lastWeatherUpdate > 500) {
                    updateWeatherAndSounds();
                    lastWeatherUpdate = currentTime;
                }
                if (backgroundChangePending.get() &&
                        currentTime - lastBackgroundChange > 100) {
                    if (backgroundManager.switchToNextBackground()) {
                        root.setBackground(backgroundManager.getCurrentBackground());
                        backgroundChangePending.set(false);
                        lastBackgroundChange = currentTime;
                    }
                }
            }

            private void calculateFPS(long now) {
                frameCount++;

                if (lastFpsTime == 0) {
                    lastFpsTime = now;
                    return;
                }

                long elapsedNanos = now - lastFpsTime;
                if (elapsedNanos > 1_000_000_000) {
                    currentFps = frameCount * 1_000_000_000.0 / elapsedNanos;
                    frameCount = 0;
                    lastFpsTime = now;

                    if (currentFps < 30) {
                        System.out.println("WARNING: Low FPS (" + String.format("%.1f", currentFps) +
                                ")! Objects: " + theMvcGame.getObjectCount());
                    }
                }
            }
        }.start();
    }

    private void updateGameRendererData() {
        GameDrawer renderer = theMvcGame.getGameRenderer();
        if (renderer != null) {
            renderer.updateGameData(
                    theMvcGame.getScore(),
                    theMvcGame.getLevel(),
                    theMvcGame.getCannonAvailableMissiles(),
                    theMvcGame.getCurrentWeatherName()
            );

            renderer.updateFPS(currentFps);
            renderer.setGameInfo(theMvcGame.getGameInfo());
        }
    }

    private void updateWeatherAndSounds() {
        try {
            String currentWeather = theMvcGame.getCurrentWeatherName();
            System.out.println("DEBUG: Checking weather - current: '" + currentWeather + "', last: '" + lastWeather + "'");


            if (!currentWeather.equals(lastWeather)) {
                System.out.println("=== WEATHER CHANGE DETECTED ===");
                System.out.println("From: " + lastWeather + " to: " + currentWeather);

                AudioManager audio = AudioManager.getInstance();
                audio.printSoundStatus();

                audio.updateWeatherSounds(currentWeather);

                audio.printSoundStatus();

                backgroundManager.prepareNextBackground(currentWeather);
                backgroundChangePending.set(true);

                lastWeather = currentWeather;
                System.out.println("Weather update complete");
            } else {
                System.out.println("DEBUG: Weather unchanged: " + currentWeather);
            }

        } catch (Exception e) {
            System.err.println("Error updating weather and sounds: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public void stop() {
        System.out.println("\n=== STOPPING APPLICATION ===");

        if (backgroundManager != null) {
            backgroundManager.clearCache();
        }
        if (audioManager != null) {
            audioManager.cleanup();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== MVC GAME LAUNCHER ===");
        System.out.println("Starting JavaFX application...");
        launch(args);
    }
}