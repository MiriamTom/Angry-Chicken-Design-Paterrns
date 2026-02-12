package cz.cvut.fit.niadp.mvcgame.visitor;

import cz.cvut.fit.niadp.mvcgame.Composite.Composite;
import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cz.cvut.fit.niadp.mvcgame.config.MvcGameResources.*;

public class GameDrawer implements IVisitor {

    private IGameGraphics gr;
    private int score = 0;
    private int level = 1;
    private int availableMissiles = 0;
    private String weather = "Clear";
    private String currentTime = "";
    private double fps = 0;
    private AbsGameInfo gameInfo;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void setGraphicContext(IGameGraphics gr) {
        this.gr = gr;
    }

    public IGameGraphics getGraphicContext() {
        return gr;
    }

    public void updateGameData(int score, int level, int availableMissiles, String weather) {
        this.score = score;
        this.level = level;
        this.availableMissiles = availableMissiles;
        this.weather = weather;
        this.currentTime = LocalTime.now().format(timeFormatter);
        System.out.println("GameDrawer: Updated data - Score: " + score + ", Level: " + level);
    }

    public void updateFPS(double fps) {
        this.fps = fps;
    }

    public void setGameInfo(AbsGameInfo gameInfo) {
        this.gameInfo = gameInfo;
        if (gameInfo != null) {
            this.score = gameInfo.getScore(); // Synchronizuj skóre
        }
    }

    public AbsGameInfo getGameInfo() {
        return gameInfo;
    }

    private boolean checkGraphics() {
        if (gr == null) {
            System.err.println("Graphics context not set in GameDrawer!");
            return false;
        }
        return true;
    }

    @Override
    public void visitCannon(AbsCannon cannon) {
        if (!checkGraphics()) return;
        gr.drawImage(CANNON_RESOURCE, cannon.getPosition());
    }

    @Override
    public void visitMissile(AbsMissile missile) {
        if (!checkGraphics()) return;
        gr.drawImage(MISSILE_RESOURCE, missile.getPosition());
    }

    @Override
    public void visitCollision(AbsCollision collision) {
        if (!checkGraphics()) return;
        gr.drawImage(COLLISION_RESOURCE, collision.getPosition());
    }

    @Override
    public void visitEnemy(AbsEnemy enemy) {
        if (!checkGraphics()) return;
        gr.drawImage(ENEMY1_RESOURCE, enemy.getPosition());

        // Zobrazenie životov nad nepriateľom
        if (enemy.getLives() > 1) {
            Position textPos = new Position(
                    enemy.getPosition().getX() + 20,
                    enemy.getPosition().getY() - 10
            );
            gr.drawText(String.valueOf(enemy.getLives()), textPos, "white", 12);
        }
    }

    @Override
    public void visitGameInfo(AbsGameInfo gameInfo) {
        if (!checkGraphics()) return;

        // DÔLEŽITÉ: Aktualizuj dáta z GameInfo
        if (gameInfo != null) {
            this.score = gameInfo.getScore();
            this.gameInfo = gameInfo;
        }

        // Vykresli základné UI elementy
        drawUIElements();

        // Vykresli dodatočné informácie z GameInfo
        drawGameInfoDetails(gameInfo);
    }

    @Override
    public void visitBound(AbsBound bound) {
        if (!checkGraphics()) return;
        gr.drawImage(BOUND_RESOURCE, bound.getPosition());
    }

    @Override
    public void visitEndScene(AbsEndScene endScene) {
        if (!checkGraphics()) return;
        gr.drawRectangle(0, 0, GameWindowConfig.MAX_X, GameWindowConfig.MAX_Y, "rgba(0, 0, 0, 0.7)");

        Position center = new Position(GameWindowConfig.MAX_X / 2, GameWindowConfig.MAX_Y / 2);
        gr.drawText("GAME OVER", center, "#FF0000", 48);

        Position scorePos = new Position(GameWindowConfig.MAX_X / 2, GameWindowConfig.MAX_Y / 2 + 50);
        gr.drawText("FINAL SCORE: " + score, scorePos, "#FFD700", 32);
    }

    @Override
    public void visitComposite(Composite composite) {
        // Nie je potrebné vykresľovať kompozit
    }

    private void drawUIElements() {
        // Horný panel
        gr.drawRectangle(0, 0, GameWindowConfig.MAX_X, 100, "rgba(0, 0, 0, 0.2)");

        // Ľavá strana - základné informácie
        gr.drawText("SCORE: " + score, new Position(20, 30), getScoreColor(), 20);
        gr.drawText("LEVEL: " + level, new Position(20, 60), "#FFFFFF", 18);
        gr.drawText("MISSILES: " + availableMissiles, new Position(20, 90),
                availableMissiles > 0 ? "#00FF00" : "#FF0000", 18);

        // Pravá strana - systémové informácie
        gr.drawText("WEATHER: " + weather,
                new Position(GameWindowConfig.MAX_X - 300, 30),
                getWeatherColor(), 18);
        gr.drawText("TIME: " + currentTime,
                new Position(GameWindowConfig.MAX_X - 300, 60),
                getTimeColor(), 16);
        gr.drawText(String.format("FPS: %.1f", fps),
                new Position(GameWindowConfig.MAX_X - 300, 90),
                getFPSColor(), 16);
    }

    private void drawGameInfoDetails(AbsGameInfo gameInfo) {
        if (gameInfo == null) return;

        int panelWidth = 360;
        int margin = 5;
        int gap = 1200;
        int lineHeight = 20;

        List<String> infoTexts = gameInfo.getTexts();
        List<String> commandTexts = gameInfo.getCommandTexts();

        int infoLines = infoTexts != null ? infoTexts.size() : 0;
        int commandLines = commandTexts != null ? commandTexts.size() : 0;

        int infoHeight = infoLines * lineHeight + 20;
        int commandsHeight = commandLines * lineHeight + 40;
        int totalHeight = Math.max(infoHeight, commandsHeight);

        int startY = GameWindowConfig.MAX_Y - totalHeight - margin;

        int infoX = margin;
        int commandsX = margin + panelWidth + gap;

        /* ================= INFO PANEL ================= */
        if (infoTexts != null && !infoTexts.isEmpty()) {
            gr.drawRectangle(
                    infoX - 5,
                    startY - 5,
                    panelWidth,
                    infoHeight,
                    "rgba(0, 0, 0, 0.6)"
            );

            for (int i = 0; i < infoTexts.size(); i++) {
                gr.drawText(
                        infoTexts.get(i),
                        new Position(infoX, startY + 15 + i * lineHeight),
                        "#FFFFFF",
                        10
                );
            }
        }

    }

    private String getScoreColor() {
        if (score > 1000) return "#FF4500";
        if (score > 500) return "#00FF00";
        if (score > 100) return "#FFFF00";
        return "#FFFFFF";
    }

    private String getWeatherColor() {
        if (weather.contains("Clear")) return "#90EE90";
        if (weather.contains("Wind")) return "#87CEEB";
        if (weather.contains("Rain")) return "#4682B4";
        if (weather.contains("Fog")) return "#D3D3D3";
        if (weather.contains("Storm")) return "#FF4500";
        return "#FFFFFF";
    }

    private String getTimeColor() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        if (hour >= 6 && hour < 12) return "#FFD700";     // ráno
        if (hour >= 12 && hour < 18) return "#87CEEB";    // popoludnie
        if (hour >= 18 && hour < 22) return "#FF8C00";    // večer
        return "#FFFFFF";                                // noc
    }

    private String getFPSColor() {
        if (fps > 55) return "#00FF00";
        if (fps > 30) return "#FFFF00";
        return "#FF0000";
    }
}