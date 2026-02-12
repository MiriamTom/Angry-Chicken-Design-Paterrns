package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_B;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsCannon;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsGameInfo;

import java.util.ArrayList;
import java.util.List;

public class GameInfo_B extends AbsGameInfo {
    private int framesPerSecond; // FPS counter

    public GameInfo_B(Position pos, AbsCannon cannon, IGameModel model) {
        super(pos, cannon, model);
        this.framesPerSecond = 0;
    }

    @Override
    public List<String> getTexts() {
        List<String> gameInfoTexts = new ArrayList<>();

        // Štýl rodiny B - kompaktnejší layout
        gameInfoTexts.add("=== HERNE INFO (B) ===");

        if (model != null) {
            gameInfoTexts.add("Úroveň: " + model.getLevel() +
                    " | Nepriatelia: " + model.getEnemies().size());
            gameInfoTexts.add("Stratégia: " + model.getMovingStrategy().getName());
            gameInfoTexts.add("FPS: " + framesPerSecond);
        }

        if (cannon != null) {
            gameInfoTexts.add("--- DELO ---");
            gameInfoTexts.add("Mód: " + cannon.getShootingMode().getName());
            gameInfoTexts.add("Uhol: " + String.format("%.1f°", -(cannon.getAngle())));
            gameInfoTexts.add("Sila: " + cannon.getPower() + "J");
            gameInfoTexts.add("Náboje: " +
                    (model.getCannonAvailableMissiles() > 0 ?
                            model.getCannonAvailableMissiles() + "/" + model.getMissiles() :
                            "PRÁZDNO!"));

            // Progress bar pre nabíjanie
            if (model.getCannonAvailableMissiles() <5) {
                int percent = (model.getCannonAvailableMissiles() * 100) /5;
                gameInfoTexts.add("Nabíjanie: [" + getProgressBar(percent, 10) + "] " + percent + "%");
            }
        }

        return gameInfoTexts;
    }

    @Override
    public List<String> getCommandTexts() {
        List<String> commandTexts = new ArrayList<>();
        commandTexts.add("=== OVLÁDANIE ===");
        commandTexts.add("Pohyb: Šípky ↑↓←→");
        commandTexts.add("Mierenie: W/S");
        commandTexts.add("Sila: E/D");
        commandTexts.add("Stratégia: T");
        commandTexts.add("Režim streľby: R");
        commandTexts.add("Undo: U");
        commandTexts.add("Reštart: ENTER");

        return commandTexts;
    }

    // Pomocná metóda pre progress bar
    private String getProgressBar(int percent, int length) {
        int filled = (percent * length) / 100;
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < length; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        return bar.toString();
    }

    public void setFramesPerSecond(int fps) {
        this.framesPerSecond = fps;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }
}