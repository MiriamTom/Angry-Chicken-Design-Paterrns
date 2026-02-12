package cz.cvut.fit.niadp.mvcgame.decorator;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import cz.cvut.fit.niadp.mvcgame.observer.IObservable;

public class FogDecorator extends WeatherDecorator {
    private double fogDensity; // Hustota hmly 0.0 - 1.0
    private double fogHeight; // Výška hmly

    public FogDecorator(IWeatherEffect decoratedEffect, IObservable weatherSubject) {
        super(decoratedEffect, weatherSubject);
        this.fogDensity = 0.5;
        this.fogHeight = 300.0;
    }

    public FogDecorator(IWeatherEffect decoratedEffect, IObservable weatherSubject,
                        double fogDensity, double fogHeight) {
        super(decoratedEffect, weatherSubject);
        this.fogDensity = fogDensity;
        this.fogHeight = fogHeight;
    }

    @Override
    public void applyEffect(AbsMissile missile) {
        super.applyEffect(missile);

        // Hmla ovplyvňuje iba viditeľnosť, nie fyziku strely
        // Môžeme pridať malý náhodný efekt ak je strela v hmle
        if (missile.getPosition().getY() < fogHeight && Math.random() < fogDensity * 0.05) {
            // Náhodná malá odchýlka v hmle
            double randomDeviation = (Math.random() - 0.5) * fogDensity * 5;
            missile.getPosition().setX(missile.getPosition().getX() + (int)randomDeviation);
        }
    }

    @Override
    public String getName() {
        String density;
        if (fogDensity < 0.3) density = "Light";
        else if (fogDensity < 0.7) density = "Moderate";
        else density = "Dense";

        return density + " Fog";
    }
    @Override
    public String getDescription() {
        String density;
        if (fogDensity < 0.3) density = "Light";
        else if (fogDensity < 0.7) density = "Moderate";
        else density = "Dense";

        return super.getName() + " + " + density + " Fog";
    }
    @Override
    public double getAccuracyModifier() {
        // Hmla mierne znižuje presnosť (ťažšie mieriť)
        return super.getAccuracyModifier() * (1.0 - (fogDensity * 0.2));
    }

    @Override
    public double getVisibilityModifier() {
        // Hmla výrazne znižuje viditeľnosť
        return super.getVisibilityModifier() * (1.0 - (fogDensity * 0.7));
    }


    public double getFogDensity() {
        return fogDensity;
    }

    public double getFogHeight() {
        return fogHeight;
    }

    public void setFogDensity(double density) {
        this.fogDensity = Math.max(0.0, Math.min(1.0, density));
        notifyWeatherChange();
    }

    public void setFogHeight(double height) {
        this.fogHeight = height;
        notifyWeatherChange();
    }

    private void notifyWeatherChange() {
        if (weatherSubject != null) {
            weatherSubject.notifyObservers();
        }
    }
}