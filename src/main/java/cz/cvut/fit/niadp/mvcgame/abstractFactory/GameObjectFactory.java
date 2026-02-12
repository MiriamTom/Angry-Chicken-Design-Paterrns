package cz.cvut.fit.niadp.mvcgame.abstractFactory;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.*;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_A.*;
import cz.cvut.fit.niadp.mvcgame.config.CannonMissileConfig;
import cz.cvut.fit.niadp.mvcgame.config.EnemyScoreConfig;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;

import java.util.Random;

public class GameObjectFactory implements IGameObjectFactory {

    private IGameModel model;

    public GameObjectFactory(IGameModel model ){
        this.model = model;
    }

    @Override
    public Cannon_A createCannon( ) {
        return new Cannon_A( new Position( CannonMissileConfig.CANNON_POS_X, CannonMissileConfig.CANNON_POS_Y ), this );
    }

    @Override
    public Enemy_A createEnemy( int lives ) {
        Random random = new Random();
            int randomPosX = random.nextInt((GameWindowConfig.MAX_X - 100) - 200) + 200;
            int randomPosY = random.nextInt((GameWindowConfig.MAX_Y - 100) - 50) + 50;

            int speed = 1;
            int movementRange = random.nextInt(5) + 5;

            return new Enemy_A(
                    new Position(randomPosX, randomPosY),
                    this,
                    lives,
                    speed,
                    movementRange
            );
        }

    @Override
    public AbsCollision createCollision(Position pos) {
        return new Collision_A(pos, CannonMissileConfig.COLLISION_DELETE_TIME);
    }

    @Override
    public AbsGameInfo createGameInfo(AbsCannon cannon) {
        return new GameInfo_A(new Position(GameWindowConfig.GAME_INFO_POS_X, GameWindowConfig.GAME_INFO_POS_Y),
                cannon, this.model);
    }

    @Override
    public AbsBound createBound() {
        Random random = new Random();
        int posY = 0;
        if(random.nextInt(2) == 1)
            posY = random.nextInt((GameWindowConfig.MAX_Y - GameWindowConfig.BOUND_SIZE)
                        - (GameWindowConfig.BOUND_Y_UPPER_BORDER)) + GameWindowConfig.BOUND_Y_UPPER_BORDER;
        else
            posY = random.nextInt((GameWindowConfig.BOUND_Y_LOWER_BORDER - GameWindowConfig.BOUND_SIZE));
        return new Bound_A(
                new Position(
                        GameWindowConfig.BOUND_POS_X,
                        posY
                ));
    }

    @Override
    public AbsEndScene createEndScene() {
        return new EndScene_A(new Position(
                GameWindowConfig.MAX_X/2 - 40, GameWindowConfig.END_SCENE_POS_Y),
                this.model.getScore());
    }

    @Override
    public Missile_A createMissile( double initAngle, int initVelocity ) {
        return new Missile_A( 
            new Position(
                model.getCannonPosition( ).getX( ), 
                model.getCannonPosition( ).getY( )
            ),
            initAngle, 
            initVelocity, 
            this.model.getMovingStrategy( )
            );
    }
    
}
