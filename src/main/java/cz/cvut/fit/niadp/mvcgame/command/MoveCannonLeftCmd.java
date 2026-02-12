package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class MoveCannonLeftCmd extends AbstractGameCommand {
    public MoveCannonLeftCmd( IGameModel model ){
        this.model = model;
    }

    @Override
    protected void execute( ) {
        this.model.moveCannonLeft( );
    }

}