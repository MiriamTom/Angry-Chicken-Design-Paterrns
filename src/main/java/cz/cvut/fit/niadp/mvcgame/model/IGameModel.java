package cz.cvut.fit.niadp.mvcgame.model;

import cz.cvut.fit.niadp.mvcgame.command.AbstractGameCommand;
import cz.cvut.fit.niadp.mvcgame.decorator.WeatherSystem;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.*;
import cz.cvut.fit.niadp.mvcgame.observer.IObservable;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;

import java.util.List;

public interface IGameModel extends IObservable {
    public void update( );
    public Position getCannonPosition( );
    public void moveCannonUp( );
    public void moveCannonDown( );
    public void moveCannonLeft( );
    public void moveCannonRight( );
    public void aimCannonUp( );
    public void aimCannonDown( );
    public void cannonPowerUp( );
    public void cannonPowerDown( );
    public void cannonShoot( ) ;
    public void cannonReload( ) ;
    public List<AbsMissile> getMissiles( );
    public List<AbsEnemy> getEnemies();
    public List<AbsCollision> getCollisions();
    public List<GameObject> getGameObjects( );
    public IMovingStrategy getMovingStrategy( );
    public AbsGameInfo getGameInfo();
    public AbsBound getBound();

    AbsCannon getCannon();

    public int getLevel();
    public int getCannonAvailableMissiles();
    public void toggleMovingStrategy( );
    public void toggleShootingMode( );
    public Object createMemento( );
    public void setMemento( Object memento );

    public void registerCommand( AbstractGameCommand cmd );
    public void undoLastCommand( );
    public void toggleWeather();
    WeatherSystem getWeatherSystem();
    public int getScore();

    public void restartGame();
}
