package cz.cvut.fit.niadp.mvcgame.ChainofResponsibility;

// Abstract handler
public abstract class WeatherSoundHandler {
    protected WeatherSoundHandler next;

    public void setNext(WeatherSoundHandler next) {
        this.next = next;
    }

    public abstract void handle(String weatherType, AudioManager audioManager);

    protected void passToNext(String weatherType, AudioManager audioManager) {
        if (next != null) {
            next.handle(weatherType, audioManager);
        }
    }
}

// Clear weather handler
class ClearWeatherHandler extends WeatherSoundHandler {
    @Override
    public void handle(String weatherType, AudioManager audioManager) {
        if (weatherType.contains("Clear")) {
            audioManager.playBackgroundMusic();
            audioManager.stopRainSound();
            audioManager.stopWindSound();
            audioManager.stopThunderSound();
        } else {
            passToNext(weatherType, audioManager);
        }
    }

}

// Windy weather handler
class WindyWeatherHandler extends WeatherSoundHandler {
    @Override
    public void handle(String weatherType, AudioManager audioManager) {
        if (weatherType.contains("Wind")) {
            audioManager.playBackgroundMusic(); // DÔLEŽITÉ
            audioManager.playWindSound(0.5f);
            audioManager.stopRainSound();
            audioManager.stopThunderSound();
        } else {
            passToNext(weatherType, audioManager);
        }
    }
}

// Rainy weather handler
class RainyWeatherHandler extends WeatherSoundHandler {
    @Override
    public void handle(String weatherType, AudioManager audioManager) {
        if (weatherType.contains("Rain")) {
            audioManager.playBackgroundMusic(); // DÔLEŽITÉ
            audioManager.playRainSound(0.7f);
            audioManager.playWindSound(0.3f);
            audioManager.stopThunderSound();
        } else {
            passToNext(weatherType, audioManager);
        }
    }
}

// Stormy weather handler
class StormyWeatherHandler extends WeatherSoundHandler {
    @Override
    public void handle(String weatherType, AudioManager audioManager) {
        if (weatherType.contains("Storm")) {
            audioManager.playBackgroundMusic(); // DÔLEŽITÉ
            audioManager.playRainSound(1.0f);
            audioManager.playWindSound(0.8f);
            audioManager.playThunderSound();
        } else {
            passToNext(weatherType, audioManager);
        }
        }
}

// Foggy weather handler
class FoggyWeatherHandler extends WeatherSoundHandler {
    @Override
    public void handle(String weatherType, AudioManager audioManager) {
        if (weatherType.contains("Fog")) {
            audioManager.playBackgroundMusic(); // DÔLEŽITÉ
            audioManager.playWindSound(0.2f);
            audioManager.stopRainSound();
            audioManager.stopThunderSound();
        } else {
            passToNext(weatherType, audioManager);
        }
    }
}