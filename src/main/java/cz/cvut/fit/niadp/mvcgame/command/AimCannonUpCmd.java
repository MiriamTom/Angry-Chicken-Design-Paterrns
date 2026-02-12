package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class AimCannonUpCmd extends AbstractGameCommand {

    public AimCannonUpCmd( IGameModel model ){
        this.model = model;
    }

    @Override
    protected void execute() {
        this.model.aimCannonUp();
    }
}
