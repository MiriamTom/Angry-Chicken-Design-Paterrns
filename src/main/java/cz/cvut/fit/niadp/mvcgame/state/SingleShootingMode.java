package cz.cvut.fit.niadp.mvcgame.state;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsCannon;

public class SingleShootingMode implements IShootingMode {

    @Override
    public void shoot( AbsCannon cannon ) {
        cannon.primitiveShoot( );
    }

    @Override
    public String getName( ) {
        return "SingleShootingMode";
    }

    
}
