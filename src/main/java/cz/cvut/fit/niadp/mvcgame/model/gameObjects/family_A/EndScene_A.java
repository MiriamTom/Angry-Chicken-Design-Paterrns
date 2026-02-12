package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_A;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsEndScene;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

public class EndScene_A extends AbsEndScene {
    public EndScene_A(Position pos, int score) {
        super(pos, score);
    }

    @Override
        public String getText() {
        return "Vyhrál jsi, pro restart stiskni ENTER";
    }
    @Override
    public void acceptVisitor(IVisitor visitor) {
        visitor.visitEndScene(this);
    }
}
