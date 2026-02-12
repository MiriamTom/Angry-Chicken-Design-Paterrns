package cz.cvut.fit.niadp.mvcgame.command;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

public abstract class AbstractGameCommand {
    protected IGameModel model;
    protected Object memento;
    protected abstract void execute();

    public AbstractGameCommand doExecute(){
        memento = model.createMemento();
        execute();
        return this;
    }
    public void unExecute(){
        model.setMemento(memento);
    }
}
