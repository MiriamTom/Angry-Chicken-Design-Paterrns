package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_A;

import cz.cvut.fit.niadp.mvcgame.config.MvcGameKeys;
import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsCannon;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsGameInfo;

import java.util.ArrayList;
import java.util.List;

public class GameInfo_A extends AbsGameInfo {

    private int score = 0; // Pridaj toto pole

    public GameInfo_A(Position pos, AbsCannon cannon, IGameModel model) {
        super(pos, cannon, model);
    }

    @Override
    public void updateScore(int newScore) {
        this.score = newScore;
        System.out.println("GameInfo_A: Score updated to " + this.score);
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public List<String> getTexts() {
        List<String> gameInfoTexts = new ArrayList<>();

        if (model != null) {
            gameInfoTexts.add("SCORE: " + this.score); // Použi lokálne skóre
            gameInfoTexts.add("LEVEL: " + model.getLevel());
            gameInfoTexts.add("STRATEGY: " + model.getMovingStrategy().getName());
            gameInfoTexts.add("ENEMIES: " + model.getEnemies().size());
            gameInfoTexts.add("COLLISIONS: " + model.getCollisions().size());
            gameInfoTexts.add("MISSILES: " + model.getMissiles().size());
            gameInfoTexts.add("WEATHER: " + model.getWeatherSystem().getName());
        }

        if (cannon != null) {
            gameInfoTexts.add("SHOOT MODE: " + cannon.getShootingMode().getName());
            gameInfoTexts.add("ANGLE: " + String.format("%.2f", cannon.getAngle()));
            gameInfoTexts.add("POWER: " + cannon.getPower());
            gameInfoTexts.add("AVAILABLE MISSILES: " +
                    (model.getCannonAvailableMissiles() > 0 ?
                            model.getCannonAvailableMissiles() : "RELOAD NEEDED"));
        }

        return gameInfoTexts;
    }

    @Override
    public List<String> getCommandTexts() {
        List<String> commandTexts = new ArrayList<>();
        commandTexts.add("MOVEMENT: ↑↓←→ ARROWS");
        commandTexts.add("AIM UP: " + MvcGameKeys.getNameofKey(MvcGameKeys.AIM_UP_KEY));
        commandTexts.add("AIM DOWN: " + MvcGameKeys.getNameofKey(MvcGameKeys.AIM_DOWN_KEY));
        commandTexts.add("INCREASE POWER: " + MvcGameKeys.getNameofKey(MvcGameKeys.POWER_UP_KEY));
        commandTexts.add("DECREASE POWER: " + MvcGameKeys.getNameofKey(MvcGameKeys.POWER_DOWN_KEY));
        commandTexts.add("CHANGE STRATEGY: " + MvcGameKeys.getNameofKey(MvcGameKeys.TOGGLE_MOVING_STRATEGY_KEY));
        commandTexts.add("CHANGE SHOOT MODE: " + MvcGameKeys.getNameofKey(MvcGameKeys.TOGGLE_SHOOTING_MODE_KEY));
        commandTexts.add("UNDO: " + MvcGameKeys.getNameofKey(MvcGameKeys.UNDO_LAST_COMMAND_KEY));
        commandTexts.add("CHANGE WEATHER: " + MvcGameKeys.getNameofKey(MvcGameKeys.TOGGLE_WEATHER_KEY));
        commandTexts.add("RELOAD: " + MvcGameKeys.getNameofKey(MvcGameKeys.RELOAD_KEY));
        return commandTexts;
    }
}