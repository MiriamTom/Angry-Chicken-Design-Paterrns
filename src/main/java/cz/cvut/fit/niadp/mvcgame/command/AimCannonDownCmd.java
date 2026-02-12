package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class AimCannonDownCmd extends AbstractGameCommand{

    public AimCannonDownCmd( IGameModel model ){
        this.model = model;
    }

    @Override
    protected void execute() {
        this.model.aimCannonDown();
    }
}
