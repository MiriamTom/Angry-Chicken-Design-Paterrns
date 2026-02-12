import cz.cvut.fit.niadp.mvcgame.decorator.WeatherSystem;
import cz.cvut.fit.niadp.mvcgame.decorator.RainDecorator;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsMissile;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

public class WeatherSystemMockedTest {

    @Mocked
    AbsMissile missile;

    @Test
    public void rainEffectIsApplied() {
        WeatherSystem weatherSystem = new WeatherSystem();

        // Použijeme skutočný dekorátor, nie mock
        RainDecorator rain = new RainDecorator(weatherSystem.getCurrentWeather(), weatherSystem, 0.5);

        weatherSystem.setWeather(rain);

        // Aplikujeme efekt na mockovanú raketu
        weatherSystem.applyEffect(missile);

        // Overenie, že sa počasie nastavilo
        Assert.assertEquals("Moderate Rain", weatherSystem.getCurrentWeather().getName());
    }
}
