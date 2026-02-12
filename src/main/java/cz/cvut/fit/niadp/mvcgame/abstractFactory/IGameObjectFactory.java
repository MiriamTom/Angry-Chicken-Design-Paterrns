package cz.cvut.fit.niadp.mvcgame.abstractFactory;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.*;

public interface IGameObjectFactory {
    public AbsCannon createCannon( );
    public AbsMissile createMissile( double initAngle, int initVelocity );
    public AbsEnemy createEnemy(int lives);
    public AbsCollision createCollision(Position pos);
    public AbsGameInfo createGameInfo(AbsCannon cannon);
    public AbsBound createBound();
    public AbsEndScene createEndScene();
}
