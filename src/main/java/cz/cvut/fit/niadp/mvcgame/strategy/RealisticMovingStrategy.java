package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import cz.cvut.fit.niadp.mvcgame.config.CannonMissileConfig;
import cz.cvut.fit.niadp.mvcgame.config.EnemyScoreConfig;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;

public class RealisticMovingStrategy implements IMovingStrategy {

    @Override
    public void updatePosition(AbsMissile missile) {
        double initAngle = missile.getInitAngle( );
        int initVelocity = missile.getInitVelocity( );
        long time = missile.getAge( ) / 100;

        int dX = ( int )( initVelocity * time * Math.cos( initAngle ) );
        int dY = ( int )( initVelocity * time * Math.sin( initAngle ) + ( 0.5 * CannonMissileConfig.GRAVITY * time * time) );

        missile.move( new Vector( dX, dY ) );
    }

    @Override
    public String getName() {
        return "RealisticMovingStrategy";
    }

}
