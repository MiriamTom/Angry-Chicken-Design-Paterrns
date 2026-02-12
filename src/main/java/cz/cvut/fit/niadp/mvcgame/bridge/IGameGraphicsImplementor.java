package cz.cvut.fit.niadp.mvcgame.bridge;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.Vector;

import java.util.List;

public interface IGameGraphicsImplementor {
    void drawImage(String path, Position pos);
    void drawText(String text, Position pos);
    void drawText(String text, Position pos, String color, int fontSize);
    void drawLine(Position beginPosition, Position endPosition);
    void drawLine(double x1, double y1, double x2, double y2, String color);
    void drawRectangle(double x, double y, double width, double height, String color);
    void clear();
    void drawTrajectory(List<Vector> points);
}