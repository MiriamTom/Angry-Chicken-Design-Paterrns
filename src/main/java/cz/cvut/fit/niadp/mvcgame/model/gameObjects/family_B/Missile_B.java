package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_B;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;

import java.util.ArrayList;
import java.util.List;

public class Missile_B extends AbsMissile {
    private IMovingStrategy movingStrategy;
    private List<AbsEnemy> enemiesHit;
    private boolean isHoming; // Navádzaná strela
    private double rotation; // Rotácia pre vizuálny efekt

    public Missile_B(Position initialPosition, double initAngle, int initVelocity,
                     IMovingStrategy movingStrategy, boolean isHoming) {
        super(initialPosition, initAngle, initVelocity);
        this.position = initialPosition;
        this.movingStrategy = movingStrategy;
        this.enemiesHit = new ArrayList<>();
        this.isHoming = isHoming;
        this.rotation = 0;
    }

    @Override
    public void move() {
        if (isHoming && !enemiesHit.isEmpty()) {
            // Navádzanie na posledného zasiahnutého nepriateľa
            AbsEnemy lastHit = enemiesHit.get(enemiesHit.size() - 1);
            adjustTrajectory(lastHit.getPosition());
        }
        this.movingStrategy.updatePosition(this);
        this.rotation += 5; // Rotácia pre efekt
    }

    private void adjustTrajectory(Position target) {
        // Jednoduché navádzanie
        double dx = target.getX() - this.position.getX();
        double dy = target.getY() - this.position.getY();
        double targetAngle = Math.atan2(dy, dx);

        // Pomalá korekcia uhla
    }

    public void setAngle(double v) {
        // Nastavenie uhla strely

    }

    @Override
    public boolean hit(AbsEnemy enemy) {
        if (enemiesHit.contains(enemy)) return false;

        // Väčšia detekčná oblasť pre rodinu B
        int hitRadius = 30; // Väčší radius
        int differenceX = this.position.getX() - enemy.getPosition().getX();
        int differenceY = this.position.getY() - enemy.getPosition().getY();

        double distance = differenceX * differenceX + differenceY * differenceY;

        if (distance <= Math.pow(hitRadius, 2)) {
            enemiesHit.add(enemy);

            // Chain reaction - strela môže zasiahnuť viac nepriateľov
            if (isHoming && enemiesHit.size() < 3) {
                // Strela pokračuje ďalej
                return true;
            }
            return true;
        }
        return false;
    }

    public boolean isHoming() {
        return isHoming;
    }

    public double getRotation() {
        return rotation;
    }

    public int getChainHits() {
        return enemiesHit.size();
    }
}