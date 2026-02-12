package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public abstract class AbsEnemy extends GameObject {
    protected int lives;

    @Override
    public void acceptVisitor(IVisitor visitor) {
        visitor.visitEnemy(this);
    }

    public abstract void move();

    public int getLives() { return lives;}

    public void setLives(int newLives) {this.lives = newLives;}


    public void setSpeedMultiplier(double enemySpeedMultiplier) {

    }
}
