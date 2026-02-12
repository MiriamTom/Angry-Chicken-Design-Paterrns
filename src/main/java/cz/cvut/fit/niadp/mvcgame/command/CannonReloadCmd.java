package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class CannonReloadCmd extends AbstractGameCommand {

    public CannonReloadCmd( IGameModel model ){
        this.model = model;
    }

    @Override
    protected void execute() {

        this.model.cannonReload();

    }
}
