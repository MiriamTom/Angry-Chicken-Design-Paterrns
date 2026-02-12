package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class CannonPowerDownCmd extends AbstractGameCommand{

    public CannonPowerDownCmd( IGameModel model ){
        this.model = model;
    }
    @Override
    protected void execute() {
        this.model.cannonPowerDown();
    }
}
