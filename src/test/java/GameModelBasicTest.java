
import cz.cvut.fit.niadp.mvcgame.MvcGame;
import cz.cvut.fit.niadp.mvcgame.command.MoveCannonUpCmd;
import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import org.junit.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;


public class GameModelBasicTest {

    @Test
    public void undoLastCommandTest() {
        IGameModel gameModel = new GameModel();
        int beginCannonPositionX = gameModel.getCannonPosition().getX();
        int beginCannonPositionY = gameModel.getCannonPosition().getY();

        gameModel.registerCommand(new MoveCannonUpCmd(gameModel));
        gameModel.update(); // Vykoná command
        gameModel.undoLastCommand(); // Undo

        assertEquals(beginCannonPositionX, gameModel.getCannonPosition().getX());
        assertEquals(beginCannonPositionY, gameModel.getCannonPosition().getY());
    }

    @Test
    public void testGameInitialization() {
        MvcGame game = new MvcGame();
        game.init();
       // assertNotNull(game.getModel());
        assertNotNull(game.getGameRenderer());
    }

    @Test
    public void testCannonAimingUp() {
        GameModel model = new GameModel();
        double initialAngle = model.getCannon().getAngle();

        model.aimCannonUp();
        model.update();

        assertTrue( model.getCannon().getAngle() > initialAngle);
    }



    @Test
    public void testCannonShooting() {
        GameModel model = new GameModel();
        int initialMissileCount = model.getMissiles().size();

        model.cannonShoot();

        assertTrue("Shooting should add missiles",
                model.getMissiles().size() > initialMissileCount);
    }
}