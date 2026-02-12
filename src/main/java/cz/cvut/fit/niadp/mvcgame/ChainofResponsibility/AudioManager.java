package cz.cvut.fit.niadp.mvcgame.ChainofResponsibility;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioManager {
    private static AudioManager instance;
    private Clip backgroundMusic;
    private Clip rainSound;
    private Clip windSound;
    private Clip thunderSound;
    private Map<String, Clip> soundEffects;
    private ExecutorService soundExecutor;

    // Chain of Responsibility pre zvuky počasia
    private WeatherSoundHandler weatherSoundChain;
    private String currentWeather;

    private AudioManager() {
        soundEffects = new HashMap<>();
        soundExecutor = Executors.newFixedThreadPool(3); // Pool pre zvuky
        currentWeather = "Clear";
        initializeSounds();
        setupWeatherSoundChain();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
    public void checkAndFixBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            System.out.println("Background music stopped! Restarting...");
            playBackgroundMusic();
        }
    }
    private void initializeSounds() {
        try {
            System.out.println("Initializing sounds...");

            // Načítanie všetkých zvukov
            loadAllSounds();

            // Štartujeme background music
            playBackgroundMusic();

        } catch (Exception e) {
            System.err.println("Error initializing sounds: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllSounds() {
        // Background music
        backgroundMusic = loadClip("/sounds/soundtrack.wav");
        System.out.println("Background music loaded: " + (backgroundMusic != null));

        // Weather sounds
        rainSound = loadClip("/sounds/rain_loop.wav");
        windSound = loadClip("/sounds/wind_loop.wav");
       // thunderSound = loadClip("/sounds/thunder.wav");

        // Game effect sounds
        soundEffects.put("shoot", loadClip("/sounds/shoot.wav"));
        soundEffects.put("hit", loadClip("/sounds/hit.wav"));
        soundEffects.put("reload", loadClip("/sounds/reload.wav"));
        soundEffects.put("explosion", loadClip("/sounds/explosion.wav"));

        // Debug výpis
        System.out.println("Sound effects loaded: " + soundEffects.size());
    }

    private Clip loadClip(String resourcePath) {
        try {
            System.out.println("Loading sound: " + resourcePath);

            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("Sound resource not found: " + resourcePath);
                System.err.println("Classpath: " + System.getProperty("java.class.path"));
                return null;
            }

            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bis);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            System.out.println("Sound loaded successfully: " + resourcePath +
                    " (duration: " + clip.getMicrosecondLength()/1000000.0 + "s)");
            return clip;

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio format: " + resourcePath + " - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO error loading: " + resourcePath + " - " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + resourcePath + " - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error loading: " + resourcePath + " - " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void setupWeatherSoundChain() {
        System.out.println("Setting up weather sound chain...");

        // Vytvorenie reťaze handlerov
        ClearWeatherHandler clearHandler = new ClearWeatherHandler();
        WindyWeatherHandler windyHandler = new WindyWeatherHandler();
        RainyWeatherHandler rainyHandler = new RainyWeatherHandler();
        StormyWeatherHandler stormyHandler = new StormyWeatherHandler();
        FoggyWeatherHandler foggyHandler = new FoggyWeatherHandler();

        // Nastavenie poradia
        clearHandler.setNext(windyHandler);
        windyHandler.setNext(rainyHandler);
        rainyHandler.setNext(stormyHandler);
        stormyHandler.setNext(foggyHandler);

        weatherSoundChain = clearHandler;
        System.out.println("Weather sound chain setup complete.");
    }

    public void updateWeatherSounds(String weatherType) {
        System.out.println("=== UPDATING WEATHER SOUNDS ===");
        System.out.println("From: " + currentWeather + " to: " + weatherType);

        if (weatherSoundChain == null) {
            System.err.println("Weather sound chain is null!");
            setupWeatherSoundChain();
        }
        stopAllWeatherSounds();

        if (!weatherType.equals(currentWeather)) {
            try {
                weatherSoundChain.handle(weatherType, this);
                currentWeather = weatherType;
                System.out.println("Weather sounds updated successfully");
            } catch (Exception e) {
                System.err.println("Error in weather sound chain: " + e.getMessage());
                e.printStackTrace();
            }
        }

        printSoundStatus();
    }

    private void stopAllWeatherSounds() {
        stopRainSound();
        stopWindSound();
        stopThunderSound();
    }

    public void playBackgroundMusic() {
        if (backgroundMusic == null) {
            System.err.println("Background music is null!");
            return;
        }

        soundExecutor.submit(() -> {
            try {
                System.out.println("Starting background music...");

                if (backgroundMusic.isRunning()) {
                    backgroundMusic.stop();
                }
                backgroundMusic.setFramePosition(0);

                setVolume(backgroundMusic, 0.5f);

                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusic.start();

                System.out.println("Background music started.");

            } catch (Exception e) {
                System.err.println("Error playing background music: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void stopBackgroundMusic() {
        stopSound(backgroundMusic);
    }

    public void playRainSound(float intensity) {
        System.out.println("Playing rain sound with intensity: " + intensity);
        playLoopingSound(rainSound, intensity * 0.5f);
    }

    public void stopRainSound() {
        stopSound(rainSound);
    }

    public void playWindSound(float intensity) {
        System.out.println("Playing wind sound with intensity: " + intensity);
        playLoopingSound(windSound, intensity * 0.4f);
    }

    public void stopWindSound() {
        stopSound(windSound);
    }

    public void playThunderSound() {
        System.out.println("Playing thunder sound");
        playOneShotSound(thunderSound, 0.7f);

        // Naplánuj ďalší blesk
        soundExecutor.submit(() -> {
            try {
                Thread.sleep((long)(Math.random() * 20000 + 10000));
                if (currentWeather.contains("Storm")) {
                    playThunderSound();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void stopThunderSound() {
        stopSound(thunderSound);
    }

    private void playLoopingSound(Clip clip, float volume) {
        if (clip == null) {
            System.err.println("Cannot play null clip!");
            return;
        }

        soundExecutor.submit(() -> {
            try {
                // Ak už hrá, zastav
                if (clip.isRunning()) {
                    clip.stop();
                }

                // Reset na začiatok
                clip.setFramePosition(0);

                // Nastav hlasitosť
                setVolume(clip, volume);

                // Spusti v slučke
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();

                System.out.println("Looping sound started.");

            } catch (Exception e) {
                System.err.println("Error playing looping sound: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void playOneShotSound(Clip clip, float volume) {
        if (clip == null) {
            System.err.println("Cannot play null clip!");
            return;
        }

        soundExecutor.submit(() -> {
            try {

                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.setFramePosition(0);

                setVolume(clip, volume);

                // Spusti
                clip.start();

                System.out.println("One-shot sound played.");

            } catch (Exception e) {
                System.err.println("Error playing one-shot sound: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void setVolume(Clip clip, float volume) {
        if (clip == null) return;

        try {
            // Obmedz volume na 0-1
            volume = Math.max(0, Math.min(1, volume));

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                // Prevod z linear (0-1) na decibely (-80 to 0 typicky)
                float min = gainControl.getMinimum();
                float max = gainControl.getMaximum();
                float gain = min + (volume * (max - min));

                gainControl.setValue(gain);
                System.out.println("Volume set to: " + volume + " (gain: " + gain + " dB)");
            } else {
                System.out.println("Volume control not supported for this clip");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Volume control error: " + e.getMessage());
        }
    }

    private void stopSound(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
            System.out.println("Sound stopped.");
        }
    }

    // Metódy pre hracie efekty
    public void playSoundEffect(String effectName) {
        System.out.println("Playing sound effect: " + effectName);

        Clip clip = soundEffects.get(effectName);
        if (clip != null) {
            playOneShotSound(clip, 1.0f);
        } else {
            System.err.println("Sound effect not found: " + effectName);
        }
    }

    public void playShootSound() {
        playSoundEffect("shoot");
    }

    public void playHitSound() {
        playSoundEffect("hit");
    }

    public void playExplosionSound() {
        playSoundEffect("explosion");
    }

    public void playClickSound() {
        playSoundEffect("click");
    }

    public void cleanup() {
        System.out.println("Cleaning up audio...");

        stopBackgroundMusic();
        stopRainSound();
        stopWindSound();
        stopThunderSound();

        // Zastav všetky efekty
        soundEffects.values().forEach(this::stopSound);

        // Zatvor executor
        if (soundExecutor != null) {
            soundExecutor.shutdownNow();
        }

        System.out.println("Audio cleanup complete.");
    }

    // Debug metóda
    public void printSoundStatus() {
        System.out.println("=== Audio Manager Status ===");
        System.out.println("Current weather: " + currentWeather);
        System.out.println("Background music: " + (backgroundMusic != null ? "Loaded" : "Null"));
        System.out.println("Rain sound: " + (rainSound != null ? "Loaded" : "Null"));
        System.out.println("Wind sound: " + (windSound != null ? "Loaded" : "Null"));
        System.out.println("Sound effects count: " + soundEffects.size());

        if (backgroundMusic != null) {
            System.out.println("Background music running: " + backgroundMusic.isRunning());
        }
    }
}