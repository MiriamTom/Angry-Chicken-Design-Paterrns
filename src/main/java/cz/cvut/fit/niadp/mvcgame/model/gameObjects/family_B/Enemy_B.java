package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_B;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsEnemy;

public class Enemy_B extends AbsEnemy {
    private IGameObjectFactory goFact;
    private int scoreValue; // Bonusový skóre pre túto rodinu
    private boolean isMoving; // Nepriatelia sa môžu pohybovať

    public Enemy_B(Position pos, IGameObjectFactory goFact, int lives, int scoreValue) {
        this.position = pos;
        this.goFact = goFact;
        this.lives = lives;
        this.scoreValue = scoreValue;
        this.isMoving = true;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public boolean isMoving() {
        return isMoving;
    }

    // Metóda na pohyb nepriateľa (pre rodinu B)
    public void move() {
        if (isMoving) {
            this.position.setX(this.position.getX() + 5);
        }
    }
}