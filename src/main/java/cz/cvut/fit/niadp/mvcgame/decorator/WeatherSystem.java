package cz.cvut.fit.niadp.mvcgame.decorator;


import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.observer.IObserver;
import cz.cvut.fit.niadp.mvcgame.observer.IObservable;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class WeatherSystem implements IObservable, IWeatherEffect {
    private List<IObserver> observers;
    private IWeatherEffect currentWeather;
    private String weatherName;

    // Zjednodušená cache - iba na sledovanie času
    private long lastWeatherEffectUpdate = 0;
    private static final long WEATHER_EFFECT_UPDATE_INTERVAL = 100; // 100ms je lepšie

    // Debug
    private int effectApplyCount = 0;
    private long lastDebugOutput = 0;
    public WeatherSystem() {
        this.observers = new ArrayList<>();
        this.currentWeather = new BaseWeather();
        this.weatherName = "Clear";
    }

    public void setWeather(IWeatherEffect weather) {
        this.currentWeather = weather;
        this.weatherName = weather.getName();
        notifyObservers();
    }

    public void addWeatherEffect(IWeatherEffect effect) {
        if (effect instanceof WeatherDecorator) {
            this.currentWeather = effect;
        } else {
            this.currentWeather = effect;
        }
        this.weatherName = this.currentWeather.getName();
        notifyObservers();
    }

    public void clearWeather() {
        this.currentWeather = new BaseWeather();
        this.weatherName = "Clear";
        notifyObservers();
    }

    @Override
    public void applyEffect(AbsMissile missile) {
        if (missile == null) return;

        // Debug: počítaj aplikácie efektov
        effectApplyCount++;
        long currentTime = System.currentTimeMillis();

        // Výpis debug info každú sekundu
        if (currentTime - lastDebugOutput > 1000) {
            System.out.println("Weather effects applied in last second: " + effectApplyCount);
            effectApplyCount = 0;
            lastDebugOutput = currentTime;
        }

        // VŽDY aplikuj efekt - cache odstráňte, lebo spôsobuje problémy
        currentWeather.applyEffect(missile);

        // Debug: vypíš pozíciu strely po efekte
        if (Math.random() < 0.001) { // 0.1% šanca na debug výpis
            System.out.println("Missile after weather effect: " +
                    missile.getPosition() + ", velocity: " + missile.getVelocity());
        }
    }

    @Override
    public String getName() {
        return weatherName;
    }

    @Override
    public double getAccuracyModifier() {
        return currentWeather.getAccuracyModifier();
    }

    @Override
    public double getVisibilityModifier() {
        return currentWeather.getVisibilityModifier();
    }

    @Override
    public double getWindStrength() {
        return currentWeather.getWindStrength();
    }

    @Override
    public String getDescription() {
        return "";
    }

    // Observer pattern methods
    @Override
    public void registerObserver(IObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unregisterObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update();
        }
    }

    public IWeatherEffect getCurrentWeather() {
        return currentWeather;
    }

    // Metódy na zmenu počasia
    public void setWindy(double strength, double direction) {
        WindDecorator wind = new WindDecorator(currentWeather, this, strength, direction);
        setWeather(wind);
    }

    public void setRainy(double intensity) {
        RainDecorator rain = new RainDecorator(currentWeather, this, intensity);
        setWeather(rain);
    }

    public void setFoggy(double density, double height) {
        FogDecorator fog = new FogDecorator(currentWeather, this, density, height);
        setWeather(fog);
    }

    public void setStormy() {
        // Kombinácia silného vetra a dažďa
        RainDecorator rain = new RainDecorator(currentWeather, this, 0.8);
        WindDecorator storm = new WindDecorator(rain, this, 2.0, 90.0);
        setWeather(storm);
    }
}