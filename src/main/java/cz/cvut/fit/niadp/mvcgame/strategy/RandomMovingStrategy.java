package cz.cvut.fit.niadp.mvcgame.strategy;

import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;

import java.util.Random;

public class RandomMovingStrategy implements IMovingStrategy {

    @Override
    public void updatePosition(AbsMissile missile) {
        int offset = 5;
        final Random RANDOM = new Random();
        double initAngle = missile.getInitAngle( );
        int initVelocity = missile.getInitVelocity( );
        long time = missile.getAge( ) / 100;

        int dX = ( int )( initVelocity * time * Math.cos( initAngle ) );
        int dY = (int)(initVelocity * time * (RANDOM.nextInt((offset - (offset * -1))) + (offset * -1)));

        missile.move( new Vector( dX, dY ) );
    }

    @Override
    public String getName() {
        return "RandomMovingStrategy";
    }
}
