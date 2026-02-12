package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_A;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsEnemy;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;

public class Enemy_A extends AbsEnemy {
    private IGameObjectFactory goFact;
    private Vector velocity;
    private int movementDirection; // 1 = doprava, -1 = doľava
    private int movementRange; // Rozsah pohybu v pixeloch
    private int originalX; // Pôvodná X pozícia pre osciláciu

    // Cache pre sínusové výpočty
    private long lastSineUpdate = 0;
    private double cachedSineValue = 0;
    private static final long SINE_UPDATE_INTERVAL = 100; // Update každých 100ms

    public Enemy_A(Position pos, IGameObjectFactory goFact, int lives) {
        this.position = pos;
        this.goFact = goFact;
        this.lives = lives;

        // Inicializácia pohybu - OPRAVENÉ!
        this.velocity = new Vector(2, 0); // Začína pohyb doprava, rýchlosť 2
        this.movementDirection = 1;
        this.movementRange = 100; // OPRAVENÉ: pohybuje sa 100 pixelov do každého smeru
        this.originalX = pos.getX();

        System.out.println("Enemy created at " + pos + " with movement range: " + movementRange);
    }

    // Konštruktor s možnosťou nastaviť pohybové parametre
    public Enemy_A(Position pos, IGameObjectFactory goFact, int lives,
                   int speed, int movementRange) {
        this.position = pos;
        this.goFact = goFact;
        this.lives = lives;
        this.velocity = new Vector(speed, 0);
        this.movementDirection = 1;
        this.movementRange = movementRange;
        this.originalX = pos.getX();
    }

    @Override
    public void setSpeedMultiplier(double enemySpeedMultiplier) {
        if (this.velocity != null) {
            // Násobíme pôvodnú rýchlosť hodnotou multiplier
            int baseSpeed = Math.max(1, Math.abs(this.velocity.getDX())); // aby nebola 0
            this.velocity.setX((int) Math.round(baseSpeed * enemySpeedMultiplier));
        }
    }

    public void move() {
        // Horizontálny pohyb s osciláciou
        int newX = this.position.getX() + (velocity.getDX() * movementDirection);

        // Kontrola hraníc pohybu
        if (newX > originalX + movementRange) {
            movementDirection = -1; // Zmena smeru doprava → doľava
            newX = originalX + movementRange;
        } else if (newX < originalX - movementRange) {
            movementDirection = 1; // Zmena smeru doľava → doprava
            newX = originalX - movementRange;
        }

        // Kontrola hraníc obrazovky
        if (newX < 0) {
            newX = 0;
            movementDirection = 1;
        } else if (newX > GameWindowConfig.MAX_X - 50) {
            newX = GameWindowConfig.MAX_X - 50;
            movementDirection = -1;
        }

        this.position.setX(newX);


        int waveOffset = (int)(Math.sin(System.currentTimeMillis() / 500.0));
        int newY = this.position.getY() + waveOffset;

        // Kontrola vertikálnych hraníc
        if (newY < 50) newY = 50;
        else if (newY > GameWindowConfig.MAX_Y - 50) newY = GameWindowConfig.MAX_Y - 50;

        this.position.setY(newY);
    }
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public int getMovementDirection() {
        return movementDirection;
    }

    public void setMovementDirection(int direction) {
        this.movementDirection = direction;
    }

    public int getMovementRange() {
        return movementRange;
    }

    // Pridaná metóda pre debug
    @Override
    public String toString() {
        return "Enemy_A[pos=" + position +
                ", lives=" + lives +
                ", range=" + movementRange +
                ", dir=" + movementDirection + "]";
    }

}