package cz.cvut.fit.niadp.mvcgame.model;

import cz.cvut.fit.niadp.mvcgame.ChainofResponsibility.AudioManager;
import cz.cvut.fit.niadp.mvcgame.Composite.Composite;
import cz.cvut.fit.niadp.mvcgame.MvcGame;
import cz.cvut.fit.niadp.mvcgame.abstractFactory.GameObjectFactory;
import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectFactory;
import cz.cvut.fit.niadp.mvcgame.command.AbstractGameCommand;
import cz.cvut.fit.niadp.mvcgame.decorator.IWeatherEffect;
import cz.cvut.fit.niadp.mvcgame.decorator.WeatherSystem;
import cz.cvut.fit.niadp.mvcgame.decorator.WindDecorator;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.*;
import cz.cvut.fit.niadp.mvcgame.observer.IObserver;
import cz.cvut.fit.niadp.mvcgame.state.IShootingMode;
import cz.cvut.fit.niadp.mvcgame.strategy.*;
import cz.cvut.fit.niadp.mvcgame.config.EnemyScoreConfig;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class GameModel implements IGameModel {
    private AbsCannon cannon;
    private List<AbsMissile> missiles;
    private List<AbsEnemy> enemies;
    private List<IObserver> observers;
    private List<AbsCollision> collisions;
    private IGameObjectFactory goFact;
    private IMovingStrategy movingStrategy;
    private Queue<AbstractGameCommand> unexecutedCommands;
    private Stack<AbstractGameCommand> executedCommands;
    private Composite composite;
    private AbsGameInfo gameInfo;
    private AbsBound bound;
    private AbsEndScene endScene;
    private int level;
    private final int maxLevel;
    private int score;
    private IDifficulty difficulty;
    private WeatherSystem weatherSystem;
    private int currentWeatherIndex;
    private List<String> weatherTypes;
    private long lastSpawnTime = 0;
    private static final long SPAWN_COOLDOWN = 1000; // 1 sekunda medzi spawnmi
    private int enemiesToSpawn = 0;
    private boolean isSpawning = false;
    private boolean levelSwitchPending = false;

    private static final int GRID_SIZE = 100; // Veľkosť bunky gridu
    private Map<String, List<AbsMissile>> missileGrid;
    private Map<String, List<AbsEnemy>> enemyGrid;
    private int hitChecksPerFrame = 0;
    private long lastHitDebug = 0;


    public GameModel() {
        this.observers = new ArrayList<IObserver>();
        this.goFact = new GameObjectFactory(this);
        // this.goFact = new GameObjectFactory_B(this);
        this.maxLevel = EnemyScoreConfig.MAX_LEVEL;
        this.missileGrid = new HashMap<>();
        this.enemyGrid = new HashMap<>();
        // Inicializácia systému počasia
        this.weatherSystem = new WeatherSystem();
        this.weatherTypes = new ArrayList<>();
        initializeWeatherTypes();
        this.currentWeatherIndex = 0;

        init();
        this.difficulty = new EasyDifficulty();
        spawnEnemies();
    }

    private void initializeWeatherTypes() {
        weatherTypes.add("Clear");
        weatherTypes.add("Windy");
        weatherTypes.add("Rainy");
        weatherTypes.add("Foggy");
        weatherTypes.add("Stormy");
    }

    private void init(){
        this.bound = this.goFact.createBound();
        this.cannon = this.goFact.createCannon( );
        this.missiles = new ArrayList<AbsMissile>();
        this.movingStrategy = new SimpleMovingStrategy( );
        this.score = 0;
        this.unexecutedCommands = new LinkedBlockingQueue<AbstractGameCommand>( );
        this.executedCommands = new Stack<AbstractGameCommand>();
        this.enemies = new ArrayList<AbsEnemy>( );
        this.collisions = new ArrayList<AbsCollision>();
        this.gameInfo = this.goFact.createGameInfo(this.cannon);
        this.level = 1;
        this.endScene = new NullEndScene();
        this.composite = new Composite();
        this.composite.addChild(this.bound);

        this.weatherSystem.registerObserver(new IObserver() {
            @Override
            public void update() {
                GameModel.this.notifyObservers();

                // Voliteľné: Aktualizovať GameInfo
                if (gameInfo != null) {
                    // Aktualizovať informácie o počasí v GameInfo
                }
            }
        });
    }
    public void update() {
        if (this.endScene != null && !(this.endScene instanceof NullEndScene)) {
            System.out.println("Game Over - no more updates");
            return;
        }

        this.executedCommands();
        this.moveMissiles();
        this.checkHits();

        this.checkLevelSwitch();
        this.updateDifficulty();

        this.destroyCollisions();
        this.moveEnemies();
        this.destroyMissiles();
        this.updateWeatherEffects();

        this.notifyObservers();
    }
    private void moveEnemies() {
        if (enemies.isEmpty()) {
            return;
        }

        // Limit na počet nepriateľov, ktorí sa hýbu v jednom frame
        int maxEnemiesToMove = Math.min(enemies.size(), 30); // Znížené z 50 na 30
        int movedCount = 0;



        for (AbsEnemy enemy : enemies) {
            if (movedCount >= maxEnemiesToMove) {
                break;
            }
            enemy.move();
            movedCount++;
        }


        notifyObservers();
    }
    private boolean difficultyChanged = false;
    private int lastDifficultyScore = 0;

    private void updateDifficulty() {
        int score = gameInfo.getScore();

        IDifficulty newDifficulty;
        if (score < 200) {
            newDifficulty = new EasyDifficulty();
        } else if (score < 400) {
            newDifficulty = new MediumDifficulty();
        } else {
            newDifficulty = new HardDifficulty();
        }
        if (!newDifficulty.getClass().equals(difficulty.getClass())) {
            System.out.println("Difficulty changing from " +
                    difficulty.getClass().getSimpleName() + " to " +
                    newDifficulty.getClass().getSimpleName());

            IDifficulty oldDifficulty = difficulty;
            difficulty = newDifficulty;
            difficulty.applyEnvironment(this);
            difficultyChanged = true;
            lastDifficultyScore = score;
        }
    }

    private void spawnEnemies() {
        System.out.println("=== SPAWNING ENEMIES FOR LEVEL " + level + " ===");

        int baseEnemyCount = EnemyScoreConfig.BASE_ENEMY_COUNT +
                (level * EnemyScoreConfig.ENEMY_COUNT_STEP);

        baseEnemyCount = Math.min(baseEnemyCount, 10);

        int baseEnemyLives = Math.min(
                EnemyScoreConfig.BASE_ENEMY_LIVES + (level * EnemyScoreConfig.ENEMY_LIVES_STEP),
                3
        );

        int finalEnemyCount = Math.min(
                baseEnemyCount * difficulty.getEnemyCountMultiplier(),
                15
        );

        int finalEnemyLives = Math.min(
                baseEnemyLives + difficulty.getEnemyLivesBonus(),
                5
        );
        enemies.clear();

        int screenWidth = GameWindowConfig.MAX_X - 30;
        int screenHeight = GameWindowConfig.MAX_Y - 30;

        int enemiesPerRow = Math.max(1, (int) Math.ceil(Math.sqrt(finalEnemyCount)));
        int spacingX = 100;
        int spacingY = 80;

        for (int i = 0; i < finalEnemyCount; i++) {
            int row = i / enemiesPerRow;
            int col = i % enemiesPerRow;

            int x = screenWidth - 300 - (col * spacingX);
            int y = 100 + (row * spacingY);

            x = Math.max(50, Math.min(x, screenWidth - 100));
            y = Math.max(50, Math.min(y, screenHeight - 100));

            Position pos = new Position(x, y);
            AbsEnemy enemy = goFact.createEnemy(finalEnemyLives);
            enemy.getPosition().setX(pos.getX());
            enemy.getPosition().setY(pos.getY());

            double speedMultiplier = Math.min(1.5, difficulty.getEnemySpeedMultiplier());
            enemy.setSpeedMultiplier(speedMultiplier);

            enemies.add(enemy);
        }
    }

    private void updateWeatherEffects() {
        // Tu môžeme pridať dynamické zmeny počasia
        // Napríklad: vietor sa môže meniť v čase
        if (Math.random() < 0.01) { // 1% šanca na zmenu vetra
            IWeatherEffect current = weatherSystem.getCurrentWeather();
            if (current instanceof WindDecorator) {
                double newStrength = ((WindDecorator) current).getWindStrength() +
                        (Math.random() - 0.5) * 2.0;
                ((WindDecorator) current).setWindStrength(Math.max(0, newStrength));
            }
        }
    }
    private void executedCommands( ) {
        while( !this.unexecutedCommands.isEmpty( ) ){
            executedCommands.push(unexecutedCommands.poll().doExecute());
        }
    }
    private void moveMissiles() {
        if (missiles.isEmpty()) {
            return;
        }

        List<AbsMissile> missilesToRemove = new ArrayList<>();
        int maxMissilesToProcess = Math.min(missiles.size(), 30); // Max 30 na frame
        int processed = 0;

        for (AbsMissile missile : missiles) {
            if (processed >= maxMissilesToProcess) {
                break;
            }

            // Aplikuj efekt počasia na strelu
            weatherSystem.applyEffect(missile);

            // Pôvodný pohyb
            missile.move();

            // Kontrola, či raketa opustila obrazovku
            if (missile.getPosition().getX() > GameWindowConfig.MAX_X ||
                    missile.getPosition().getY() < 0 ||
                    missile.getPosition().getY() > GameWindowConfig.MAX_Y) {
                missilesToRemove.add(missile);
            }

            processed++;
        }

        this.missiles.removeAll(missilesToRemove);
        this.notifyObservers();
    }

    private void checkHits() {
        System.out.println("=== CHECK HITS ===");
        System.out.println("Current score before check: " + this.gameInfo.getScore());

        if (missiles.isEmpty() || enemies.isEmpty()) {
            return;
        }

        List<AbsEnemy> enemiesToRemove = new ArrayList<>();
        List<AbsMissile> missilesToRemove = new ArrayList<>();
        int hitCount = 0;
        int scoreToAdd = 0;

        // Pre každú strelu
        for (AbsMissile missile : missiles) {
            // Pre každého nepriateľa
            for (AbsEnemy enemy : enemies) {
                if (enemiesToRemove.contains(enemy)) continue;

                // Vypočítaj vzdialenosť
                double dx = missile.getPosition().getX() - enemy.getPosition().getX();
                double dy = missile.getPosition().getY() - enemy.getPosition().getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                // DEBUG: Vypíš vzdialenosti
                if (distance < 100) {
                    System.out.println("Distance missile-enemy: " + distance +
                            " (missile: " + missile.getPosition() +
                            ", enemy: " + enemy.getPosition() + ")");
                }

                // Kolízia ak sú dostatočne blízko
                if (distance < 40) { // Zvýšené z 30 na 40
                    hitCount++;

                    // Zvuk zásahu
                    AudioManager.getInstance().playHitSound();

                    if (enemy.getLives() > 1) {
                        // Zníž životy
                        enemy.setLives(enemy.getLives() - 1);
                        System.out.println("Enemy hit! Lives left: " + enemy.getLives());
                    } else {
                        // Znič nepriateľa
                        enemy.setLives(0);

                        // Vytvor explóziu
                        this.collisions.add(goFact.createCollision(enemy.getPosition()));

                        // Zvuk explózie
                        AudioManager.getInstance().playExplosionSound();

                        // Pridaj skóre
                        scoreToAdd += EnemyScoreConfig.KILL_SCORE;
                        System.out.println("Enemy destroyed! Adding " + EnemyScoreConfig.KILL_SCORE + " points");

                        enemiesToRemove.add(enemy);
                    }

                    // Označ strelu na odstránenie
                    missilesToRemove.add(missile);
                    break; // Každá strela môže zničiť len jedného nepriateľa
                }
            }
        }

        // Aktualizuj skóre
        if (scoreToAdd > 0) {
            int oldScore = this.gameInfo.getScore();
            int newScore = oldScore + scoreToAdd;
            this.gameInfo.updateScore(newScore);
            System.out.println("Score updated: " + oldScore + " -> " + newScore);
        }

        // Odstráň zničené objekty
        this.missiles.removeAll(missilesToRemove);
        this.enemies.removeAll(enemiesToRemove);

        // Skontroluj level
        if (!enemiesToRemove.isEmpty()) {
            this.checkLevelSwitch();
        }

        // Debug info
        if (hitCount > 0) {
            System.out.println("Total hits: " + hitCount + ", Score added: " + scoreToAdd);
        }

        System.out.println("Score after check: " + this.gameInfo.getScore());
        System.out.println("=== END CHECK HITS ===");
    }
    private void checkLevelSwitch() {
        if (enemies.isEmpty()) {
            System.out.println("=== LEVEL COMPLETE ===");
            System.out.println("Current level: " + level + ", Max level: " + maxLevel);

            if (difficultyChanged) {
                System.out.println("Applying difficulty change...");
                difficultyChanged = false;
            }
            if (level >= maxLevel) {
                System.out.println("=== GAME COMPLETE ===");
                System.out.println("Final score: " + gameInfo.getScore());

                this.endScene = this.goFact.createEndScene();

                notifyObservers();
                // this.gameInfo = new NullGameInfo();
            } else {
                level++;
                System.out.println("Switching to level: " + level);

                clearOldLevelObjects();
                spawnEnemiesForCurrentDifficulty();
            }
        }
    }
    private void spawnEnemiesForCurrentDifficulty() {
        System.out.println("=== SPAWNING ENEMIES FOR LEVEL " + level +
                " (Difficulty: " + difficulty.getClass().getSimpleName() + ") ===");

        // Získaj odporúčané hodnoty z obtiažnosti
        int enemyCount = difficulty.getRecommendedEnemyCount(level);
        int enemyLives = difficulty.getRecommendedEnemyLives(level);

        System.out.println("Spawning " + enemyCount + " enemies with " + enemyLives + " lives each");

        // Vyčisti starých nepriateľov
        enemies.clear();

        // Rozmery obrazovky
        int screenWidth = GameWindowConfig.MAX_X - 30;
        int screenHeight = GameWindowConfig.MAX_Y - 30;

        // Rozloženie nepriateľov
        int enemiesPerRow = Math.max(1, (int) Math.ceil(Math.sqrt(enemyCount)));
        int spacingX = 100;
        int spacingY = 80;

        for (int i = 0; i < enemyCount; i++) {
            int row = i / enemiesPerRow;
            int col = i % enemiesPerRow;

            // Výpočet pozície
            int x = screenWidth - 300 - (col * spacingX); // Pravá strana
            int y = 100 + (row * spacingY); // Rozlož vertikálne

            // Omedzenie na obrazovku
            x = Math.max(50, Math.min(x, screenWidth - 100));
            y = Math.max(50, Math.min(y, screenHeight - 100));

            Position pos = new Position(x, y);

            // Vytvor nepriateľa
            AbsEnemy enemy = goFact.createEnemy(enemyLives);
            enemy.getPosition().setX(pos.getX());
            enemy.getPosition().setY(pos.getY());

            // Nastav rýchlosť podľa obtiažnosti
            enemy.setSpeedMultiplier(difficulty.getEnemySpeedMultiplier());

            // Pridaj do zoznamu
            enemies.add(enemy);

            // Debug výpis pre prvých nepriateľov
            if (i < 3) {
                System.out.println("Spawned enemy " + (i+1) + " at " + pos +
                        " with " + enemyLives + " lives");
            }
        }

        System.out.println("Total enemies spawned: " + enemies.size());
    }
    private void clearOldLevelObjects() {
        // DÔLEŽITÉ: Vyčisti všetky staré objekty
        this.missiles.clear(); // Odstráň všetky staré strely
        this.collisions.clear(); // Odstráň všetky staré kolízie

        // Resetuj pozíciu dela (voliteľné)
        // this.cannon.getPosition().setX(100);
        // this.cannon.getPosition().setY(GameWindowConfig.MAX_Y / 2);

        System.out.println("Cleared old level objects");
    }
    private void destroyCollisions() {
        // Použi iterator namiesto vytvárania nového ArrayListu
        Iterator<AbsCollision> iter = collisions.iterator();
        while (iter.hasNext()) {
            AbsCollision collision = iter.next();
            if (collision.destroy()) {
                iter.remove();
            }
        }

    }


    private void destroyMissiles( ) {
        List<AbsMissile> missilesToRemove = new ArrayList<AbsMissile>();
        for ( AbsMissile missile : this.missiles ) {
            if( missile.getPosition( ).getX( ) > GameWindowConfig.MAX_X ) {
                missilesToRemove.add( missile );
            }
        }
        this.missiles.removeAll(missilesToRemove);
    }

    public Position getCannonPosition( ) {
        return this.cannon.getPosition( );
    }

    public void moveCannonUp( ) {
        if(!this.cannon.touchBound(this.bound) || (this.cannon.getPosition().getY() < this.bound.getPosition().getY())) {
            this.cannon.moveUp();
            // this.notifyObservers();
        }
    }

    public void moveCannonDown( ) {
        if(!this.cannon.touchBound(this.bound) || (this.cannon.getPosition().getY() > this.bound.getPosition().getY())) {
            this.cannon.moveDown();
            // this.notifyObservers();
        }
    }

    @Override
    public void moveCannonLeft() {
        if(!this.cannon.touchBound(this.bound) || (this.cannon.getPosition().getX() > this.bound.getPosition().getX())) {
            this.cannon.moveLeft();
            //  this.notifyObservers();
        }

    }

    @Override
    public void moveCannonRight() {
        if(!this.cannon.touchBound(this.bound) || (this.cannon.getPosition().getX() < this.bound.getPosition().getX())) {
            this.cannon.moveRight();
            // this.notifyObservers();
        }

    }

    public void aimCannonUp( ) {
        this.cannon.aimUp( );
        //   this.notifyObservers( );
    }

    public void aimCannonDown( ) {
        this.cannon.aimDown( );
        //   this.notifyObservers( );
    }

    public void cannonPowerUp( ) {
        this.cannon.powerUp( );
        // this.notifyObservers( );
    }

    public void cannonPowerDown( ) {
        this.cannon.powerDown( );
        //  this.notifyObservers( );
    }

    @Override
    public void registerObserver( IObserver obs ) {
        if( !this.observers.contains( obs ) ) {
            this.observers.add( obs );
        }
    }

    @Override
    public void unregisterObserver( IObserver obs ) {
        if( this.observers.contains( obs ) ) {
            this.observers.remove( obs );
        }
    }

    @Override
    public void notifyObservers( )  {
        for( IObserver obs : this.observers ){
            obs.update( );
        }
    }

    public void cannonShoot() {
        AudioManager.getInstance().playShootSound();
        // Počasie ovplyvňuje streľbu
        double accuracy = weatherSystem.getAccuracyModifier();
        if (Math.random() > accuracy * 0.9) {
            // Šanca na chybu pri streľbe
            System.out.println("Weather caused shooting error!");
        }

        this.missiles.addAll(cannon.shoot());
        // this.notifyObservers();
    }

    @Override
    public void cannonReload() {
        this.cannon.reload();
    }

    public List<AbsMissile> getMissiles( ) {
        return this.missiles;
    }


    @Override
    public List<AbsEnemy> getEnemies() {
        return this.enemies;
    }

    @Override
    public List<AbsCollision> getCollisions() {
        return this.collisions;
    }

    public List<GameObject> getGameObjects( ) {
        List<GameObject> go = new ArrayList<GameObject>();
        go.add(this.cannon);
        go.add(this.composite);
        go.addAll(this.missiles);
        go.addAll(this.enemies);
        go.addAll(this.collisions);
        if (this.endScene != null && !(this.endScene instanceof NullEndScene)) {
            System.out.println("Adding endScene to game objects");
            go.add(this.endScene);
        }
        return go;
    }

    public IMovingStrategy getMovingStrategy( ){
        return this.movingStrategy;
    }

    @Override
    public AbsGameInfo getGameInfo() {
        return this.gameInfo;
    }

    @Override
    public AbsBound getBound() {
        return this.bound;
    }

    @Override
    public AbsCannon getCannon() {
        return this.cannon;
    }


    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getCannonAvailableMissiles() {
        return cannon.getAvailableMissiles();
    }

    public void toggleMovingStrategy() {
        if (this.movingStrategy instanceof SimpleMovingStrategy) {
            this.movingStrategy = new RealisticMovingStrategy();
        }
        else if (this.movingStrategy instanceof RealisticMovingStrategy) {
            this.movingStrategy = new SinusoidMovingStrategy();
        }
        else if (this.movingStrategy instanceof SinusoidMovingStrategy) {
            this.movingStrategy = new RandomMovingStrategy();
        }
        else if (this.movingStrategy instanceof RandomMovingStrategy) {
            this.movingStrategy = new SimpleMovingStrategy();
        }
    }


    public void toggleShootingMode( ){
        this.cannon.toggleShootingMode( );
    }

    private static class Memento {
        private int score;
        private Position cannonPosition;
        private List<AbsEnemy> enemies;
        private List<AbsCollision> collisions;
        private IMovingStrategy movingStrategy;
        private IShootingMode shootingMode;
        private double angle;
        private int power;
        private int availableMissiles;
        private int level;
    }

    public Object createMemento( ) {
        Memento m = new Memento( );
        m.score = this.gameInfo.getScore();
        m.cannonPosition = new Position(this.getCannonPosition().getX(), this.getCannonPosition().getY());
        m.enemies = new ArrayList<>(this.enemies);
        m.collisions = new ArrayList<>(this.collisions);
        m.movingStrategy = this.movingStrategy;
        m.shootingMode = this.cannon.getShootingMode();
        m.angle = this.cannon.getAngle();
        m.power = this.cannon.getPower();
        m.availableMissiles = this.cannon.getAvailableMissiles();
        m.level = this.level;
        return m;
    }

    public void setMemento( Object memento ) {
        Memento m = ( Memento ) memento;
        this.gameInfo.updateScore(m.score);
        this.cannon.getPosition( ).setX( m.cannonPosition.getX() );
        this.cannon.getPosition( ).setY( m.cannonPosition.getY() );
        this.cannon.setAngle(m.angle);
        this.cannon.setPower(m.power);
        this.enemies = new ArrayList<>(m.enemies);
        this.collisions = new ArrayList<>(m.collisions);
        this.movingStrategy = m.movingStrategy;
        this.cannon.setShootingMode(m.shootingMode);
        this.cannon.setAvailableMissiles(m.availableMissiles);
        this.level = m.level;
    }

    @Override
    public void registerCommand( AbstractGameCommand cmd ) {
        this.unexecutedCommands.add( cmd );
    }

    @Override
    public void undoLastCommand( ) {
        if(!executedCommands.isEmpty()){
            executedCommands.pop().unExecute();
            notifyObservers();
        }
    }
    @Override
    public void toggleWeather() {
        currentWeatherIndex = (currentWeatherIndex + 1) % weatherTypes.size();
        String weatherType = weatherTypes.get(currentWeatherIndex);

        switch (weatherType) {
            case "Clear":
                weatherSystem.clearWeather();
                break;
            case "Windy":
                weatherSystem.setWindy(8.0, 90.0); // Severovýchodný vietor
                break;
            case "Rainy":
                weatherSystem.setRainy(0.6);
                break;
            case "Foggy":
                weatherSystem.setFoggy(0.7, 400.0);
                break;
            case "Stormy":
                weatherSystem.setStormy();
                break;
        }



        System.out.println("Weather changed to: " + weatherSystem.getName());
        notifyObservers();

    }

    @Override
    public WeatherSystem getWeatherSystem() {
        return this.weatherSystem;
    }
    @Override
    public int getScore() {
        return this.gameInfo.getScore();
    }

    @Override
    public void restartGame() {
        init();
        spawnEnemies();
    }

}