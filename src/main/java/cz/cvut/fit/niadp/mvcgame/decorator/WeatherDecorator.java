package cz.cvut.fit.niadp.mvcgame.decorator;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import cz.cvut.fit.niadp.mvcgame.observer.IObservable;
import cz.cvut.fit.niadp.mvcgame.observer.IObserver;

public abstract class WeatherDecorator implements IWeatherEffect, IObserver {
    protected IWeatherEffect decoratedEffect;
    protected IObservable weatherSubject;

    public WeatherDecorator(IWeatherEffect decoratedEffect, IObservable weatherSubject) {
        this.decoratedEffect = decoratedEffect;
        this.weatherSubject = weatherSubject;
        if (weatherSubject != null) {
            weatherSubject.registerObserver(this);
        }
    }

    @Override
    public void update() {
        // Reaguje na zmeny v počasí
        if (decoratedEffect instanceof IObserver) {
            ((IObserver) decoratedEffect).update();
        }
    }

    @Override
    public void applyEffect(AbsMissile missile) {
        if (decoratedEffect != null) {
            decoratedEffect.applyEffect(missile);
        }
    }

    @Override
    public String getName() {
        return decoratedEffect != null ? decoratedEffect.getName() : "Base Weather";
    }

    @Override
    public double getAccuracyModifier() {
        return decoratedEffect != null ? decoratedEffect.getAccuracyModifier() : 1.0;
    }

    @Override
    public double getVisibilityModifier() {
        return decoratedEffect != null ? decoratedEffect.getVisibilityModifier() : 1.0;
    }

    @Override
    public double getWindStrength() {
        return decoratedEffect != null ? decoratedEffect.getWindStrength() : 0.0;
    }
}