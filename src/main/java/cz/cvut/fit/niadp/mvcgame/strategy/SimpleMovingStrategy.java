package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;

public class SimpleMovingStrategy implements IMovingStrategy {

    @Override
    public void updatePosition( AbsMissile missile ) {
        double initAngle = missile.getInitAngle( );
        int initVelocity = missile.getInitVelocity( );
        long time = missile.getAge( ) / 100;

        int dX = ( int )( initVelocity * time * Math.cos( initAngle ) );
        int dY = ( int )( initVelocity * time * Math.sin( initAngle ) );

        missile.move( new Vector( dX, dY ) );

    }

    @Override
    public String getName() {
        return "SimpleMovingStrategy";
    }

}
