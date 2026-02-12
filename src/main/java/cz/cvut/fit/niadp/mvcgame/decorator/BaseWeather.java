package cz.cvut.fit.niadp.mvcgame.decorator;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;

public class BaseWeather implements IWeatherEffect {

    @Override
    public void applyEffect(AbsMissile missile) {
        // Žiadny efekt - normálne počasie
    }

    @Override
    public String getName() {
        return "Clear";
    }

    @Override
    public double getAccuracyModifier() {
        return 1.0; // Žiadna zmena
    }

    @Override
    public double getVisibilityModifier() {
        return 1.0; // Plná viditeľnosť
    }

    @Override
    public double getWindStrength() {
        return 0.0; // Žiadny vietor
    }

    @Override
    public String getDescription() {
        return "";
    }
}