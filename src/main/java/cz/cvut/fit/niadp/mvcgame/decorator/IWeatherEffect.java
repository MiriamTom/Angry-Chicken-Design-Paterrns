package cz.cvut.fit.niadp.mvcgame.decorator;



import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;

public interface IWeatherEffect {
    void applyEffect(AbsMissile missile);
    String getName();
    double getAccuracyModifier();
    double getVisibilityModifier();
    double getWindStrength();
    String getDescription();
}