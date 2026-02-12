package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class ToggleShootingModeCmd extends AbstractGameCommand{

    public ToggleShootingModeCmd( IGameModel model ){
        this.model = model;
    }

    @Override
    protected void execute() {
        this.model.toggleShootingMode();
    }
}
