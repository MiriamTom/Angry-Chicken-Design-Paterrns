import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsEnemy;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

public class EnemyHitMockedTest {

    @Mocked
    private AbsEnemy enemy;

    @Mocked
    private AbsMissile missile;

    @Test
    public void enemyLivesDecrease() {
        new Expectations() {{
            enemy.getLives();
            returns(3, 2);
        }};

        int before = enemy.getLives();
        int after = enemy.getLives();

        Assert.assertTrue(after < before);
    }

}
