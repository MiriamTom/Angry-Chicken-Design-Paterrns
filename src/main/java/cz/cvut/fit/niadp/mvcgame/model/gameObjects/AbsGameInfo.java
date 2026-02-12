package cz.cvut.fit.niadp.mvcgame.model.gameObjects;

import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.visitor.IVisitor;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;
import java.util.List;

public abstract class AbsGameInfo {
    protected int score;
    protected AbsCannon cannon;
    protected IGameModel model;
    protected int spacing;
    protected Position pos;

    public AbsGameInfo(Position pos, AbsCannon cannon, IGameModel model){
        this.pos = pos;
        this.spacing = GameWindowConfig.GAME_INFO_TEXT_SPACING;
        this.score = 0;
        this.cannon = cannon;
        this.model = model;
    }

    public abstract List<String> getTexts();

    public void acceptVisitor(IVisitor visitor) {
        visitor.visitGameInfo(this);
    }

    public int getScore () { return this.score;}

    public void updateScore (int score) {this.score = score;}

    public int getSpacing () {return this.spacing;}

    public Position getPosition () {return this.pos;}

    public abstract List<String> getCommandTexts();
}
