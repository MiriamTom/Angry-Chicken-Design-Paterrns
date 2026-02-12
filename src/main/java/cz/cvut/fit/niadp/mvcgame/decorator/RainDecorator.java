package cz.cvut.fit.niadp.mvcgame.decorator;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import cz.cvut.fit.niadp.mvcgame.observer.IObservable;

public class RainDecorator extends WeatherDecorator {
    private double rainIntensity; // Intenzita dažďa 0.0 - 1.0
    private double windInRain; // Vietor sprevádzajúci dážď

    public RainDecorator(IWeatherEffect decoratedEffect, IObservable weatherSubject) {
        super(decoratedEffect, weatherSubject);
        this.rainIntensity = 0.5;
        this.windInRain = 2.0;
    }

    public RainDecorator(IWeatherEffect decoratedEffect, IObservable weatherSubject,
                         double rainIntensity) {
        super(decoratedEffect, weatherSubject);
        this.rainIntensity = rainIntensity;
        this.windInRain = rainIntensity * 4.0; // Čím silnejší dážď, tým silnejší vietor
    }

    @Override
    public void applyEffect(AbsMissile missile) {
        super.applyEffect(missile);

        // Dážď zvyšuje odpor vzduchu
        double airResistance = 1.0 + (rainIntensity * 0.3);
        missile.setVelocity(missile.getVelocity() / airResistance);

        // Dážď prispieva k poklesu strely
        missile.getPosition().setY(missile.getPosition().getY() + (int)(rainIntensity * 2));

        // Náhodné výchylky od kvapiek dažďa
        if (Math.random() < rainIntensity * 0.1) {
            double randomDeviation = (Math.random() - 0.5) * rainIntensity * 10;
            missile.getPosition().setX(missile.getPosition().getX() + (int)randomDeviation);
        }
    }

    @Override
    public String getName() {
        String intensity;
        if (rainIntensity < 0.3) intensity = "Light";
        else if (rainIntensity < 0.7) intensity = "Moderate";
        else intensity = "Heavy";

        return intensity + " Rain";
    }

    @Override
    public String getDescription() {
        String intensity;
        if (rainIntensity < 0.3) intensity = "Light";
        else if (rainIntensity < 0.7) intensity = "Moderate";
        else intensity = "Heavy";

        return super.getName() + " + " + intensity + " Rain";
    }
    @Override
    public double getAccuracyModifier() {
        // Dážď výrazne znižuje presnosť
        return super.getAccuracyModifier() * (1.0 - (rainIntensity * 0.07));
    }

    @Override
    public double getVisibilityModifier() {
        // Dážď znižuje viditeľnosť
        return super.getVisibilityModifier() * (1.0 - (rainIntensity * 0.3));
    }

    @Override
    public double getWindStrength() {
        // Dážď prináša aj vietor
        return super.getWindStrength() + windInRain;
    }



    public double getRainIntensity() {
        return rainIntensity;
    }

    public void setRainIntensity(double intensity) {
        this.rainIntensity = Math.max(0.0, Math.min(1.0, intensity));
        notifyWeatherChange();
    }

    private void notifyWeatherChange() {
        if (weatherSubject != null) {
            weatherSubject.notifyObservers();
        }
    }
}