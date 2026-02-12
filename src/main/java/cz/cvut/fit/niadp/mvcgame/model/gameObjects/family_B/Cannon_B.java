package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_B;

import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectFactory;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsCannon;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import cz.cvut.fit.niadp.mvcgame.config.CannonMissileConfig;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_A.Cannon_A;

import java.util.ArrayList;
import java.util.List;

public class Cannon_B extends AbsCannon {
    private IGameObjectFactory goFact;
    private double angle;
    private int power;
    private List<AbsMissile> shootingBatch;
    private int heat; // Teplo dela

    public Cannon_B(Position initialPosition, IGameObjectFactory goFact) {
        this.position = initialPosition;
        this.goFact = goFact;
        this.power = CannonMissileConfig.INIT_POWER;
        this.angle = CannonMissileConfig.INIT_ANGLE;
        this.shootingMode = AbsCannon.SINGLE_SHOOTING_MODE;
        this.shootingBatch = new ArrayList<>();
        this.missilesAvailable = CannonMissileConfig.MISSILE_CAPACITY;
        this.heat = 0;
    }

    @Override
    public void primitiveShoot() {
        if (missilesAvailable > 0 && heat < 100) {
            // Strela s navádzaním pre rodinu B
            this.shootingBatch.add(this.goFact.createMissile(this.angle, this.power));
            missilesAvailable--;
            heat += 10; // Zvýšenie teploty
        }
    }


    public int getHeat() {
        return heat;
    }

    public boolean isOverheated() {
        return heat >= 100;
    }

    public void moveUp( ) {
        this.move( new Vector( 0, -1 * CannonMissileConfig.MOVE_STEP ) );
    }

    public void moveDown( ) {
        this.move( new Vector( 0, CannonMissileConfig.MOVE_STEP ) );
    }

    public void moveLeft( ) {
        this.move( new Vector( -1 * CannonMissileConfig.MOVE_STEP, 0 ) );
    }
    public void moveRight( ) {
        this.move( new Vector( CannonMissileConfig.MOVE_STEP, 0 ) );
    }
    @Override
    public List<AbsMissile> shoot( ) {
        this.shootingBatch.clear( );
        this.shootingMode.shoot( this );
        return this.shootingBatch;
    }

    @Override
    public void aimUp() {
        this.angle -= CannonMissileConfig.ANGLE_STEP;
    }

    @Override
    public void aimDown() {
        this.angle += CannonMissileConfig.ANGLE_STEP;
    }

    @Override
    public void powerUp() {
        this.power += CannonMissileConfig.POWER_STEP;
    }

    @Override
    public void powerDown() {
        if ( this.power - CannonMissileConfig.POWER_STEP > 0 ){
            this.power -= CannonMissileConfig.POWER_STEP;
        }
    }



    @Override
    public double getAngle() {
        return this.angle;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    @Override
    public int getAvailableMissiles() {
        return missilesAvailable;
    }

    @Override
    public void setAvailableMissiles(int missiles) {
        this.missilesAvailable = missiles;
    }

    @Override
    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public void setPower(int power) {
        this.power = power;
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