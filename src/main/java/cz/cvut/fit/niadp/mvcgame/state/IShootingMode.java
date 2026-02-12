package cz.cvut.fit.niadp.mvcgame.state;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsCannon;

public interface IShootingMode {

    public void shoot( AbsCannon cannon );
    public String getName( );
    
}
