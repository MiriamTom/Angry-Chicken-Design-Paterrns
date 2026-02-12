package cz.cvut.fit.niadp.mvcgame.state;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsCannon;

public class DoubleShootingMode implements IShootingMode {

    @Override
    public void shoot( AbsCannon cannon ) {
        cannon.aimUp( );
        cannon.primitiveShoot( );
        cannon.aimDown( );
        cannon.aimDown( );
        cannon.primitiveShoot( );
        cannon.aimUp( );
    }

    @Override
    public String getName( ) {
        return "DoubleShootingMode";
    }
    
}
