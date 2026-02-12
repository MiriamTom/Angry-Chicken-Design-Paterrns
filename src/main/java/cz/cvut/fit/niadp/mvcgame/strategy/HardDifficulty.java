package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class HardDifficulty implements IDifficulty {

    @Override
    public int getEnemyCountMultiplier() {
        return 3;
    }

    @Override
    public int getEnemyLivesBonus() {
        return 2;
    }

    @Override
    public double getEnemySpeedMultiplier() {
        return 1.3;
    }

    @Override
    public void applyEnvironment(IGameModel model) {
        model.getWeatherSystem().setStormy();
    }
    public int getRecommendedEnemyCount(int level) {
        return Math.min(10 + level * 3, 20);
    }

    public int getRecommendedEnemyLives(int level) {
        return Math.min(2 + level / 2, 5);
    }
}
