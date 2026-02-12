package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class EasyDifficulty implements IDifficulty {

    @Override
    public int getEnemyCountMultiplier() {
        return 1;
    }

    @Override
    public int getEnemyLivesBonus() {
        return 0;
    }

    @Override
    public double getEnemySpeedMultiplier() {
        return 0.8;
    }

    @Override
    public void applyEnvironment(IGameModel model) {
        model.getWeatherSystem().clearWeather();
    }


    public int getRecommendedEnemyCount(int level) {
        return Math.min(5 + level, 10);
    }

    public int getRecommendedEnemyLives(int level) {
        return 1;
    }

}
