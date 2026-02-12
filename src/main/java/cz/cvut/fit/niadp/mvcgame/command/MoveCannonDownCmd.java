package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public class MoveCannonDownCmd extends AbstractGameCommand {
    
    public MoveCannonDownCmd( IGameModel model ){
        this.model = model;
    }

    @Override
    protected void execute( ) {
        this.model.moveCannonDown( );
    }

}
