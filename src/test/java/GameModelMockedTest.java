import cz.cvut.fit.niadp.mvcgame.abstractFactory.GameObjectFactory;
import cz.cvut.fit.niadp.mvcgame.abstractFactory.IGameObjectFactory;
import cz.cvut.fit.niadp.mvcgame.controller.GameController;
import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_A.Missile_A;
import cz.cvut.fit.niadp.mvcgame.strategy.IMovingStrategy;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

public class GameModelMockedTest {

    private static final double INIT_ANGLE = 45.0;
    private static final int INIT_POWER = 5;
    private static final int INIT_POS_X = 100;
    private static final int INIT_POS_Y = 100;


    @Test
    public void createMissileTest() {
        GameModel realModel = new GameModel();

        new MockUp<GameModel>() {
            @Mock
            public Position getCannonPosition() {
                return new Position(INIT_POS_X, INIT_POS_Y);
            }
        };

        IGameObjectFactory factory = new GameObjectFactory(realModel);
        AbsMissile missile = factory.createMissile(INIT_ANGLE, INIT_POWER);

        Assert.assertNotNull(missile);
        Assert.assertEquals(INIT_POS_X, missile.getPosition().getX());
        Assert.assertEquals(INIT_POS_Y, missile.getPosition().getY());
    }

    private void generalMocksSetup() {
        new MockUp<GameModel>() {
            @mockit.Mock
            public Position getCommonPossition() {
                return new Position(INIT_POS_X, INIT_POS_Y);
            }
        };
    }
}
