package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public abstract class AbsCollision extends LifetimeLimitedGameObject {
    protected int deleteTime;

    protected AbsCollision(Position position) {
        super(position);
    }

    @Override
    public void acceptVisitor(IVisitor visitor) {
        visitor.visitCollision(this);
    }

    public abstract boolean destroy();
}
