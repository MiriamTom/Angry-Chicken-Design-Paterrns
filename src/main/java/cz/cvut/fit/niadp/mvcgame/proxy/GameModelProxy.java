package cz.cvut.fit.niadp.mvcgame.proxy;

import cz.cvut.fit.niadp.mvcgame.command.AbstractGameCommand;
import cz.cvut.fit.niadp.mvcgame.decorator.WeatherSystem;
import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.*;
import cz.cvut.fit.niadp.mvcgame.observer.IObserver;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;

import java.util.List;

public class GameModelProxy implements IGameModel {

    private IGameModel subject;

    public GameModelProxy( IGameModel model ){
        this.subject = model;
    }

    @Override
    public void registerObserver( IObserver obs ) {
        this.subject.registerObserver( obs );
    }

    @Override
    public void unregisterObserver( IObserver obs ) {
        this.subject.unregisterObserver( obs );
    }

    @Override
    public void notifyObservers( ) {
        this.subject.notifyObservers( );
    }

    @Override
    public void update( ) {
        this.subject.update( );
    }

    @Override
    public Position getCannonPosition( ) {
        return this.subject.getCannonPosition( );
    }

    @Override
    public void moveCannonUp( ) {
        this.subject.moveCannonUp( );
    }

    @Override
    public void moveCannonDown( ) {
        this.subject.moveCannonDown( );
    }
    @Override
    public void moveCannonLeft( ) {
        this.subject.moveCannonLeft( );
    }
    @Override
    public void moveCannonRight( ) {
        this.subject.moveCannonRight( );
    }

    @Override
    public void aimCannonUp( ) {
        this.subject.aimCannonUp( );
    }

    @Override
    public void aimCannonDown( ) {
        this.subject.aimCannonDown( );
    }

    @Override
    public void cannonPowerUp( ) {
        this.subject.cannonPowerUp( );
    }

    @Override
    public void cannonPowerDown( ) {
        this.subject.cannonPowerDown( );
    }

    @Override
    public void cannonShoot( ) {
        this.subject.cannonShoot( );
    }

    @Override
    public void cannonReload() {
        this.subject.cannonReload();
    }

    @Override
    public List<AbsMissile> getMissiles( ) {
        return this.subject.getMissiles( );
    }

    @Override
    public List<AbsEnemy> getEnemies() {
        return this.subject.getEnemies();
    }

    @Override
    public List<AbsCollision> getCollisions() {
        return this.subject.getCollisions();
    }

    @Override
    public List<GameObject> getGameObjects( ) {
        return this.subject.getGameObjects( );
    }

    @Override
    public IMovingStrategy getMovingStrategy( ) {
        return this.subject.getMovingStrategy( );
    }

    @Override
    public AbsGameInfo getGameInfo() {
        return this.subject.getGameInfo();
    }

    @Override
    public AbsBound getBound() {
        return this.subject.getBound();
    }

    @Override
    public AbsCannon getCannon() {
        return  this.subject.getCannon();
    }



    @Override
    public int getLevel() {
        return this.subject.getLevel();
    }

    @Override
    public int getCannonAvailableMissiles() {
        return this.subject.getCannonAvailableMissiles();
    }

    @Override
    public void toggleMovingStrategy( ) {
        this.subject.toggleMovingStrategy( );
    }

    @Override
    public void toggleShootingMode( ) {
        this.subject.toggleShootingMode( );
    }

    @Override
    public Object createMemento( ) {
        return this.subject.createMemento( );
    }

    @Override
    public void setMemento( Object memento ) {
        this.subject.setMemento( memento );
    }

    @Override
    public void registerCommand( AbstractGameCommand cmd ) {
        this.subject.registerCommand( cmd );
    }

    @Override
    public void undoLastCommand( ) {
        this.subject.undoLastCommand( );
    }

    @Override
    public void toggleWeather() {
        this.subject.toggleWeather();
    }

    @Override
    public int getScore() {
        return this.subject.getScore();
    }

    @Override
    public void restartGame() {
        this.subject.restartGame();
    }
    @Override
    public WeatherSystem getWeatherSystem() {
        return this.subject.getWeatherSystem();
    }


}
