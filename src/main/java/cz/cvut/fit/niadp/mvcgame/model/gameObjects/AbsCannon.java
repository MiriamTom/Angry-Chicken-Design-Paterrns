package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.state.DoubleShootingMode;
import cz.cvut.fit.niadp.mvcgame.state.IShootingMode;
import cz.cvut.fit.niadp.mvcgame.state.SingleShootingMode;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;
import cz.cvut.fit.niadp.mvcgame.config.CannonMissileConfig;
import cz.cvut.fit.niadp.mvcgame.config.EnemyScoreConfig;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsCannon extends GameObject {

    protected IShootingMode shootingMode;
    protected static IShootingMode SINGLE_SHOOTING_MODE = new SingleShootingMode( );
    protected static IShootingMode DOUBLE_SHOOTING_MODE = new DoubleShootingMode( );
    protected int missilesAvailable;

    public abstract void moveUp( );
    public abstract void moveDown( );
    public abstract void moveLeft( );
    public abstract void moveRight( );
    public abstract void aimUp( );
    public abstract void aimDown( );
    public abstract void powerUp( );
    public abstract void powerDown( );

    public abstract List<AbsMissile> shoot( );
    public abstract void primitiveShoot( );

    public abstract double getAngle( );
    public abstract int getPower( );

    public abstract int getAvailableMissiles();
    public abstract void setAvailableMissiles( int missiles);

    public abstract void setAngle(double angle);
    public abstract void setPower(int power);

    private List<Vector> cachedTrajectory;
    private double lastAngle = -1;
    private double lastPower = -1;


    public IShootingMode getShootingMode () {return this.shootingMode;}
    public void setShootingMode (IShootingMode mode) {this.shootingMode = mode;}

    public void reload() {
        missilesAvailable = CannonMissileConfig.MISSILE_CAPACITY;
    }

    @Override
    public void acceptVisitor( IVisitor visitor ) {
        visitor.visitCannon( this );
    }



    public void toggleShootingMode( ) {
        if( this.shootingMode instanceof SingleShootingMode ){
            this.shootingMode = DOUBLE_SHOOTING_MODE;
        }
        else if( this.shootingMode instanceof DoubleShootingMode ){
            this.shootingMode = SINGLE_SHOOTING_MODE;
        }
        else{

        }
    }

    public Boolean touchBound (AbsBound bound){
        if (this.getPosition().getY() < bound.getPosition().getY() + GameWindowConfig.BOUND_SIZE
            && CannonMissileConfig.CANNON_SIZE_Y + this.getPosition().getY() > bound.getPosition().getY()) return true;
        return false;
    }

    public List<Vector> getPredictedTrajectory() {
        double currentAngle = this.getAngle();
        double currentPower = this.getPower();

        if (cachedTrajectory == null || currentAngle != lastAngle || currentPower != lastPower) {
            cachedTrajectory = computeTrajectory(currentAngle, currentPower);
            lastAngle = currentAngle;
            lastPower = currentPower;
        }

        return cachedTrajectory;
    }

    private List<Vector> computeTrajectory(double angle, double power) {
        List<Vector> trajectory = new ArrayList<>();
        double angleRad = Math.toRadians(angle);
        double v = power;
        double g = 9.81;
        double tStep = 0.1;

        for (double t = 0; t < 5; t += tStep) {
            double x = v * Math.cos(angleRad) * t;
            double y = v * Math.sin(angleRad) * t - 0.5 * g * t * t;
            if (y < 0) break; // dopad na zem
            trajectory.add(new Vector(this.position.getX() + x, this.position.getY() - y));
        }

        return trajectory;
    }
}
