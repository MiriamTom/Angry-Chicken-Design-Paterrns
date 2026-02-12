package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;

public interface IMovingStrategy {

    public void updatePosition( AbsMissile missile );

    public abstract String getName();
}
