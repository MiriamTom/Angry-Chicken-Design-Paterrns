package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class CannonShootCmd extends AbstractGameCommand{

    public CannonShootCmd( IGameModel model ){
        this.model = model;
    }

    @Override
    protected void execute() {

        this.model.cannonShoot();

    }
}
