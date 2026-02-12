package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public abstract class AbsMissile extends LifetimeLimitedGameObject {

    private double initAngle;
    private int initVelocity;
    private double velocity;

    protected AbsMissile( Position initialPosition, double initAngle, int initVelocity ) {
        super( initialPosition );
        this.initAngle = initAngle;
        this.initVelocity = initVelocity;
        this.velocity = initVelocity; //
    }

    @Override
    public void acceptVisitor( IVisitor visitor ) {
        visitor.visitMissile( this );
    }

    public int getInitVelocity( ){
        return this.initVelocity;
    }

    public double getInitAngle( ) {
        return this.initAngle;
    }

    public abstract void move( );

    public abstract boolean hit(AbsEnemy enemy);

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

}
