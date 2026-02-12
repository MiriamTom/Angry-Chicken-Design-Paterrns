package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class MoveCannonUpCmd extends AbstractGameCommand {

    public MoveCannonUpCmd( IGameModel model ){
        this.model = model;
    }

    @Override
    protected void execute( ) {
        this.model.moveCannonUp( );
    }
    
}
