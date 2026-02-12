package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public abstract class AbsEndScene extends GameObject {
    protected int score;

    public AbsEndScene(Position pos, int score) {
        this.position = pos;
        this.score = score;
    }

    @Override
    public void acceptVisitor(IVisitor visitor) {
        visitor.visitEndScene(this);
    }

    public abstract String getText();
}
