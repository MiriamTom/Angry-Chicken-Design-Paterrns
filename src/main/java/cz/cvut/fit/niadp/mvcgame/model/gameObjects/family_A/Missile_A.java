package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_A;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;
import cz.cvut.fit.niadp.mvcgame.config.CannonMissileConfig;
import cz.cvut.fit.niadp.mvcgame.config.EnemyScoreConfig;

import java.util.ArrayList;
import java.util.List;

public class Missile_A extends AbsMissile {
    private IMovingStrategy movingStrategy;
    private List<AbsEnemy> enemiesHit;
    private long age; // Vek strely v ms

    public Missile_A(Position initialPosition, double initAngle, int initVelocity,
                     IMovingStrategy movingStrategy) {
        super(initialPosition, initAngle, initVelocity);
        this.position = initialPosition;
        this.movingStrategy = movingStrategy;
        this.enemiesHit = new ArrayList<>();
        this.age = 0;
    }

    @Override
    public void move() {
        this.movingStrategy.updatePosition(this);
        this.age += 16; // Predpoklad 60 FPS
    }

    @Override
    public boolean hit(AbsEnemy enemy) {
        if (enemy == null) return false;

        // Vypočítaj vzdialenosť medzi raketou a nepriateľom
        double distance = Math.sqrt(
                Math.pow(this.getPosition().getX() - enemy.getPosition().getX(), 2) +
                        Math.pow(this.getPosition().getY() - enemy.getPosition().getY(), 2)
        );

        // Debug výpis
        if (distance < 50) { // Ak sú blízko
            System.out.println("Hit check: missile at " + this.getPosition() +
                    ", enemy at " + enemy.getPosition() + ", distance: " + distance);
        }

        // Zásah ak sú dostatočne blízko
        return distance < 30; // Zväčšite hit radius z 30 na 50
    }

    // Nová metóda pre počasie
    public long getAge() {
        return age;
    }
}