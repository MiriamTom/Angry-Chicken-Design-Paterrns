package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class ChangeWeatherCmd extends AbstractGameCommand {

    public ChangeWeatherCmd(IGameModel model) {
        this.model = model;
    }

    @Override
    public void execute() {
        this.model.toggleWeather();
    }
}
