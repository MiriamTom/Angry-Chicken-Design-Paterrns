package cz.cvut.fit.niadp.mvcgame.memento;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CareTaker {
    private IGameModel model;
    private final Stack<Object> mementos = new Stack<Object>();
    private CareTaker(){}
    private static class SingletonHolder {
        private static final CareTaker INSTANCE = new CareTaker();
    }
    public static CareTaker getInstance(){
        return SingletonHolder.INSTANCE;
    }
    public void setModel(IGameModel model){
        this.model = model;
    }
    public void createMemento(){
        if(model != null)
            mementos.add(model.createMemento());
    }
    public void restoreMemento(){
        if (model != null && !mementos.isEmpty()) {
            model.setMemento(mementos.pop());
        }
    }
}

