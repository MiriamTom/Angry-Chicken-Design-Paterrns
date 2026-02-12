package cz.cvut.fit.niadp.mvcgame.decorator;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import cz.cvut.fit.niadp.mvcgame.observer.IObservable;

public class WindDecorator extends WeatherDecorator {
    private double windStrength; // Sila vetra v m/s
    private double windDirection; // Smer vetra v stupňoch (0 = východ, 90 = sever)

    public WindDecorator(IWeatherEffect decoratedEffect, IObservable weatherSubject) {
        super(decoratedEffect, weatherSubject);
        this.windStrength = 5.0; // Predvolená sila
        this.windDirection = 90.0; // Predvolený smer
    }

    public WindDecorator(IWeatherEffect decoratedEffect, IObservable weatherSubject,
                         double windStrength, double windDirection) {
        super(decoratedEffect, weatherSubject);
        this.windStrength = windStrength;
        this.windDirection = windDirection;
    }

    @Override
    public void applyEffect(AbsMissile missile) {
        super.applyEffect(missile);

        // Aplikuj vplyv vetra na strelu
        double windRad = Math.toRadians(windDirection);
        double timeFactor = missile.getAge() / 1000.0; // Čas v sekundách

        // Výpočet posunu od vetra
        double windOffsetX = Math.cos(windRad) * windStrength * timeFactor;
        double windOffsetY = Math.sin(windRad) * windStrength * timeFactor;

        // Aktualizácia pozície strely
        missile.getPosition().setX(missile.getPosition().getX() + (int)windOffsetX);
        missile.getPosition().setY(missile.getPosition().getY() + (int)windOffsetY);

        // Vietor ovplyvňuje aj rýchlosť a smer strely
        missile.setVelocity(missile.getVelocity() - (windStrength * 0.1));
    }

    @Override
    public String getName() {
        String direction = getWindDirectionName();
        return super.getName() + " + Wind (" + direction + " " + String.format("%.1f", windStrength) + " m/s)";
    }
    @Override
    public String getDescription() {

        String direction = getWindDirectionName();
        return super.getName() + " + Wind (" + direction + " " + String.format("%.1f", windStrength) + " m/s)";
    }
    @Override
    public double getAccuracyModifier() {
        // Vietor znižuje presnosť
        return super.getAccuracyModifier() * (1.0 - (windStrength * 0.05));
    }

    @Override
    public double getWindStrength() {
        return windStrength;
    }



    public void setWindStrength(double strength) {
        this.windStrength = strength;
        notifyWeatherChange();
    }

    public void setWindDirection(double direction) {
        this.windDirection = direction;
        notifyWeatherChange();
    }

    public double getWindDirection() {
        return windDirection;
    }

    private String getWindDirectionName() {
        if (windDirection >= 337.5 || windDirection < 22.5) return "E";
        if (windDirection >= 22.5 && windDirection < 67.5) return "NE";
        if (windDirection >= 67.5 && windDirection < 112.5) return "N";
        if (windDirection >= 112.5 && windDirection < 157.5) return "NW";
        if (windDirection >= 157.5 && windDirection < 202.5) return "W";
        if (windDirection >= 202.5 && windDirection < 247.5) return "SW";
        if (windDirection >= 247.5 && windDirection < 292.5) return "S";
        return "SE"; // 292.5 - 337.5
    }

    private void notifyWeatherChange() {
        if (weatherSubject != null) {
            weatherSubject.notifyObservers();
        }
    }
}