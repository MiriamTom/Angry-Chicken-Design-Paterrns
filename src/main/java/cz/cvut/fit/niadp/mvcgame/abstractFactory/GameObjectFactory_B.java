package cz.cvut.fit.niadp.mvcgame.abstractFactory;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.*;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_B.*;
import cz.cvut.fit.niadp.mvcgame.config.CannonMissileConfig;
import cz.cvut.fit.niadp.mvcgame.config.EnemyScoreConfig;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;

import java.util.Random;

public class GameObjectFactory_B implements IGameObjectFactory {

    private IGameModel model;

    public GameObjectFactory_B(IGameModel model) {
        this.model = model;
    }

    @Override
    public Cannon_B createCannon() {
        return new Cannon_B(
                new Position(CannonMissileConfig.CANNON_POS_X, CannonMissileConfig.CANNON_POS_Y),
                this
        );
    }

    @Override
    public Enemy_B createEnemy(int lives) {
        Random random = new Random();
        int randomPosX = random.nextInt((GameWindowConfig.MAX_X - 100) - 200) + 200;
        int randomPosY = random.nextInt((GameWindowConfig.MAX_Y - 100) - 50) + 50;

        return new Enemy_B(
                new Position(randomPosX, randomPosY),
                this,
                lives,
                EnemyScoreConfig.ENEMY_LIVES_LEVEL_2 * 2
        );
    }

    @Override
    public Collision_B createCollision(Position pos) {
        // Dlhšia doba zobrazenia kolízie pre rodinu B
        return new Collision_B(pos, CannonMissileConfig.COLLISION_DELETE_TIME * 1.5);
    }

    @Override
    public GameInfo_B createGameInfo(AbsCannon cannon) {
        // Iný štýl zobrazenia herných informácií
        return new GameInfo_B(
                new Position(
                        GameWindowConfig.GAME_INFO_POS_X + 50,
                        GameWindowConfig.GAME_INFO_POS_Y
                ),
                cannon,
                this.model
        );
    }

    @Override
    public Bound_B createBound() {
        Random random = new Random();
        // Iná logika pre umiestnenie prekážok
        int posY = random.nextInt(GameWindowConfig.MAX_Y - 200) + 100;
        int size = GameWindowConfig.BOUND_SIZE + random.nextInt(20);

        return new Bound_B(
                new Position(
                        GameWindowConfig.BOUND_POS_X,
                        posY
                ),
                size // Príklad dodatočného parametra
        );
    }

    @Override
    public EndScene_B createEndScene() {
        // Iný vzhľad koncovej scény
        return new EndScene_B(
                new Position(
                        GameWindowConfig.MAX_X / 2 - 60,
                        GameWindowConfig.END_SCENE_POS_Y
                ),
                this.model.getScore(),
                this.model.getLevel()
        );
    }

    @Override
    public Missile_B createMissile(double initAngle, int initVelocity) {
        // Iný typ strely s odlišným správaním
        return new Missile_B(
                new Position(
                        model.getCannonPosition().getX(),
                        model.getCannonPosition().getY()
                ),
                initAngle,
                (int) (initVelocity * 1.2), // Rýchlejšie strely
                this.model.getMovingStrategy(),
                true // Príklad: strely s navádzaním
        );
    }

}