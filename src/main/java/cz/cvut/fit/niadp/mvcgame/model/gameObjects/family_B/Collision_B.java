package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_B;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsCollision;

public class Collision_B extends AbsCollision {
    private String effectType; // Typ efektu
    private int maxSize; // Maximálna veľkosť

    public Collision_B(Position pos, double deleteTime) {
        super(pos);
        this.deleteTime = (int) deleteTime;
        this.effectType = "default";
        this.maxSize = 50;
    }

    public Collision_B(Position pos, double deleteTime, String effectType, int maxSize) {
        super(pos);
        this.deleteTime = (int) deleteTime;
        this.effectType = effectType;
        this.maxSize = maxSize;
    }

    @Override
    public boolean destroy() {
        // V rodine B kolízia zmizne rýchlejšie, ale má animáciu
        return getAge() >= deleteTime;
    }

    public String getEffectType() {
        return effectType;
    }

    public int getCurrentSize() {
        // Meniaca sa veľkosť podľa času
        double progress = getAge() / deleteTime;
        return (int)(maxSize * (1 - progress));
    }

    public boolean isExpanding() {
        // Prvých 50% času sa efekt zväčšuje
        return (getAge() / deleteTime) < 0.5;
    }
}