package cz.cvut.fit.niadp.mvcgame.visitor;

import cz.cvut.fit.niadp.mvcgame.Composite.Composite;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.*;

public interface IVisitor {

    public void visitCannon( AbsCannon cannon );
    public void visitMissile( AbsMissile missile );

    public void visitCollision( AbsCollision collision );
    public void visitEnemy( AbsEnemy enemy );
    public void visitGameInfo( AbsGameInfo gameInfo );

    public void visitBound(AbsBound bound);

    public void visitEndScene(AbsEndScene endScene);

    public void visitComposite(Composite composite);
}
