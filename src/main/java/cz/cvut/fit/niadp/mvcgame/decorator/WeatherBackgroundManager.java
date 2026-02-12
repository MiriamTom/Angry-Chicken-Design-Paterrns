package cz.cvut.fit.niadp.mvcgame.decorator;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.ImagePattern;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WeatherBackgroundManager {
    private static WeatherBackgroundManager instance;
    private Map<String, Image> imageCache; // Cache pre obrázky
    private Map<String, Background> backgroundCache; // Cache pre Background objekty
    private Map<String, ImagePattern> patternCache; // Cache pre ImagePattern
    private String lastWeatherName = "";
    private Background lastBackground = null;

    // Double buffer pre plynulé prepínanie
    private Background currentBackground;
    private Background nextBackground;
    private boolean isLoadingBackground = false;

    // Přednačítané obrázky v pozadí
    private Thread preloadThread;

    private WeatherBackgroundManager() {
        imageCache = new ConcurrentHashMap<>();
        backgroundCache = new ConcurrentHashMap<>();
        patternCache = new ConcurrentHashMap<>();
        preloadAllBackgrounds();
    }

    public static WeatherBackgroundManager getInstance() {
        if (instance == null) {
            instance = new WeatherBackgroundManager();
        }
        return instance;
    }

    private void preloadAllBackgrounds() {
        preloadThread = new Thread(() -> {
            String[] weatherTypes = {"Clear", "Windy", "Rainy", "Foggy", "Stormy"};
            String[] filenames = {"back.jpg", "windy_background.jpg", "rainy_background.jpg",
                    "foggy_background.jpg", "stormy_background.jpg"};

            for (int i = 0; i < weatherTypes.length; i++) {
                loadImageToCache(filenames[i], weatherTypes[i]);
                System.out.println("Preloaded: " + weatherTypes[i]);
                try {
                    Thread.sleep(50); // Malá pauza medzi načítaním
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        preloadThread.setDaemon(true);
        preloadThread.setPriority(Thread.MIN_PRIORITY);
        preloadThread.start();
    }

    private void loadImageToCache(String filename, String weatherType) {
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/" + filename)); // true = načítaj na pozadí

            // Počkaj kým sa obrázok načíta
            while (!image.isBackgroundLoading() && image.getProgress() < 1.0) {
                Thread.sleep(10);
            }

            imageCache.put(weatherType, image);

            // Vytvor Background v pozadí
            Background background = createBackgroundFromImage(image);
            backgroundCache.put(weatherType, background);

            // Vytvor ImagePattern v pozadí
            ImagePattern pattern = new ImagePattern(image);
            patternCache.put(weatherType, pattern);

        } catch (Exception e) {
            System.err.println("Error preloading " + weatherType + ": " + e.getMessage());
            // Fallback na defaultný obrázok
            Image defaultImage = getDefaultImage();
            imageCache.put(weatherType, defaultImage);
            backgroundCache.put(weatherType, createBackgroundFromImage(defaultImage));
            patternCache.put(weatherType, new ImagePattern(defaultImage));
        }
    }

    private Image getDefaultImage() {
        try {
            return new Image(getClass().getResourceAsStream("/images/back.jpg"));
        } catch (Exception e) {
            // Ak ani defaultný obrázok nejde načítať
            return createFallbackImage();
        }
    }

    private Image createFallbackImage() {
        // Vytvor jednoduchý gradient ako fallback
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==");
    }

    private Background createBackgroundFromImage(Image image) {
        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        return new Background(bgImage);
    }

    public Background getBackground(String weatherName) {
        // Kontrola cache
        if (weatherName.equals(lastWeatherName) && lastBackground != null) {
            return lastBackground;
        }

        String key = extractWeatherKey(weatherName);

        // Skús získať z cache
        Background cached = backgroundCache.get(key);
        if (cached != null) {
            lastWeatherName = weatherName;
            lastBackground = cached;
            return cached;
        }

        // Ak nie je v cache, načítaj synchrónne (málokedy)
        return loadBackgroundSync(key);
    }

    private Background loadBackgroundSync(String key) {
        try {
            String filename = getFilenameForWeather(key);
            Image image = new Image(getClass().getResourceAsStream("/images/" + filename));

            Background background = createBackgroundFromImage(image);

            // Ulož do cache pre budúce použitie
            backgroundCache.put(key, background);
            imageCache.put(key, image);

            lastWeatherName = key;
            lastBackground = background;

            return background;
        } catch (Exception e) {
            System.err.println("Error loading background: " + e.getMessage());
            return getDefaultBackground();
        }
    }

    private Background getDefaultBackground() {
        Background defaultBg = backgroundCache.get("Clear");
        if (defaultBg == null) {
            defaultBg = createBackgroundFromImage(getDefaultImage());
            backgroundCache.put("Clear", defaultBg);
        }
        return defaultBg;
    }

    private String getFilenameForWeather(String weather) {
        switch (weather) {
            case "Windy": return "windy_background.jpg";
            case "Rainy": return "rainy_background.jpg";
            case "Foggy": return "foggy_background.jpg";
            case "Stormy": return "stormy_background.jpg";
            default: return "back.jpg";
        }
    }

    public ImagePattern getImagePattern(String weatherName) {
        String key = extractWeatherKey(weatherName);

        ImagePattern cached = patternCache.get(key);
        if (cached != null) {
            return cached;
        }

        // Fallback na default
        ImagePattern defaultPattern = patternCache.get("Clear");
        if (defaultPattern == null) {
            defaultPattern = new ImagePattern(getDefaultImage());
            patternCache.put("Clear", defaultPattern);
        }
        return defaultPattern;
    }

    private String extractWeatherKey(String weatherName) {
        if (weatherName.contains("Clear")) return "Clear";
        if (weatherName.contains("Wind")) return "Windy";
        if (weatherName.contains("Rain")) return "Rainy";
        if (weatherName.contains("Fog")) return "Foggy";
        if (weatherName.contains("Storm")) return "Stormy";
        return "Clear";
    }

    // Double buffer metódy
    public void prepareNextBackground(String weatherName) {
        if (isLoadingBackground) return;

        new Thread(() -> {
            isLoadingBackground = true;
            String key = extractWeatherKey(weatherName);

            Background bg = getBackground(key); // Toto použije cache
            nextBackground = bg;

            isLoadingBackground = false;
        }).start();
    }

    public Background getCurrentBackground() {
        return currentBackground != null ? currentBackground : getDefaultBackground();
    }

    public boolean switchToNextBackground() {
        if (nextBackground != null) {
            currentBackground = nextBackground;
            nextBackground = null;
            return true;
        }
        return false;
    }

    public void clearCache() {
        imageCache.clear();
        backgroundCache.clear();
        patternCache.clear();
        lastWeatherName = "";
        lastBackground = null;
        currentBackground = null;
        nextBackground = null;
    }
}