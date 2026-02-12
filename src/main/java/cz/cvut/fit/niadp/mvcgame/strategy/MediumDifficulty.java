package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class MediumDifficulty implements IDifficulty {

    @Override
    public int getEnemyCountMultiplier() {
        return 2;
    }

    @Override
    public int getEnemyLivesBonus() {
        return 1;
    }

    @Override
    public double getEnemySpeedMultiplier() {
        return 1.0;
    }

    @Override
    public void applyEnvironment(IGameModel model) {
        model.getWeatherSystem().setWindy(0.1, 45.0);
        model.getWeatherSystem().setRainy(0.2);
    }
    public int getRecommendedEnemyCount(int level) {
        return Math.min(8 + level * 2, 15);
    }

    public int getRecommendedEnemyLives(int level) {
        return Math.min(1 + level / 3, 3);
    }

}

