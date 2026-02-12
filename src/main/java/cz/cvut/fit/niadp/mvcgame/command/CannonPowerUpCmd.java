package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class CannonPowerUpCmd extends AbstractGameCommand{

    public CannonPowerUpCmd( IGameModel model ){
        this.model = model;
    }

    @Override
    protected void execute() {
        this.model.cannonPowerUp();
    }
}
