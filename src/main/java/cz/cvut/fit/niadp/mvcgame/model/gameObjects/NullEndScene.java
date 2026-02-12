package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Position;

public class NullEndScene extends AbsEndScene {
    public NullEndScene() {
        super(new Position(0,0), 0);
    }

    @Override
    public String getText() {
        return null;
    }
}
