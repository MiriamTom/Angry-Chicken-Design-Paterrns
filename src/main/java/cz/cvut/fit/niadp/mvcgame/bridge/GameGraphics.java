package cz.cvut.fit.niadp.mvcgame.bridge;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.Vector;

import java.util.List;

public class GameGraphics implements IGameGraphics {

    private IGameGraphicsImplementor implementor;

    public GameGraphics(IGameGraphicsImplementor implementor) {
        this.implementor = implementor;
    }

    @Override
    public void drawImage(String path, Position pos) {
        this.implementor.drawImage(path, pos);
    }

    @Override
    public void drawText(String text, Position pos) {
        this.implementor.drawText(text, pos, "white", 12);
    }

    @Override
    public void drawText(String text, Position pos, String color, int fontSize) {
        this.implementor.drawText(text, pos, color, fontSize);
    }

    @Override
    public void drawRectangle(Position leftTop, Position rightBottom) {
        // Starý spôsob - 4 čiary
        this.implementor.drawLine(leftTop, new Position(rightBottom.getX(), leftTop.getY()));
        this.implementor.drawLine(new Position(rightBottom.getX(), leftTop.getY()), rightBottom);
        this.implementor.drawLine(rightBottom, new Position(leftTop.getX(), rightBottom.getY()));
        this.implementor.drawLine(new Position(leftTop.getX(), rightBottom.getY()), leftTop);
    }

    @Override
    public void drawRectangle(double x, double y, double width, double height, String color) {
        // Nový spôsob - vyplnený obdĺžnik
        this.implementor.drawRectangle(x, y, width, height, color);
    }

    @Override
    public void clear() {
        this.implementor.clear();
    }

    @Override
    public void drawTrajectory(List<Vector> trajectory) {
        this.implementor.drawTrajectory(trajectory);
    }
}