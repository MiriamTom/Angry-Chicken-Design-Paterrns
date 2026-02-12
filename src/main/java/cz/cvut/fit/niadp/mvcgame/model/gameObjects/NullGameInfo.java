package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Position;

import java.util.List;

public class NullGameInfo extends AbsGameInfo {
    public NullGameInfo() {
        super(new Position(0,0), null, null);
    }

    @Override
    public List<String> getTexts() {
        return null;
    }

    @Override
    public List<String> getCommandTexts() {
        return List.of();
    }
}
