package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;

public class SinusoidMovingStrategy implements IMovingStrategy {

    @Override
    public void updatePosition( AbsMissile missile ) {
        double initAngle = missile.getInitAngle( );
        int initVelocity = missile.getInitVelocity( );
        long time = missile.getAge( ) / 100;

        int dX = ( int )( initVelocity * time * Math.cos( initAngle ) );
        int dY = ( int )(initVelocity * Math.sin(initAngle + Math.sin(time * initVelocity)) );

        missile.move( new Vector( dX, dY ) );
    }

    @Override
    public String getName() {
        return "SinusoidMovingStrategy";
    }

}
