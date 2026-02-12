package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_A;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsCollision;

public class Collision_A extends AbsCollision {
    public Collision_A(Position pos, int deleteTime){
        super(pos);
        this.deleteTime = deleteTime;
    }

    @Override
    public boolean destroy() {
        return getAge() >= deleteTime;
    }
}
