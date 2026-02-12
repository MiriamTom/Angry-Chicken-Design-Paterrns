package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public interface IDifficulty {

    int getEnemyCountMultiplier();   // koľko enemy
    int getEnemyLivesBonus();        // bonus životov
    double getEnemySpeedMultiplier(); // prípadne neskôr
    void applyEnvironment(IGameModel model); // počasie atď.
    // Nové metódy pre spawnovanie
    int getRecommendedEnemyCount(int level);
    int getRecommendedEnemyLives(int level);
}

