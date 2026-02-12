package cz.cvut.fit.niadp.mvcgame.Composite;

import cz.cvut.fit.niadp.mvcgame.model.gameObjects.GameObject;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;

import java.util.ArrayList;
import java.util.List;

public class Composite extends GameObject {
    private List<GameObject> children;

    public Composite () {
        children = new ArrayList<>();
    }

    public void addChild (GameObject child) {
        children.add(child);
    }

    @Override
    public void acceptVisitor(IVisitor visitor) {
        for (GameObject child : children) {
            child.acceptVisitor(visitor);
        }
        visitor.visitComposite(this);
    }
}
