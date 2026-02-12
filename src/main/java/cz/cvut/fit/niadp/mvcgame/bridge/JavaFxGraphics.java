package cz.cvut.fit.niadp.mvcgame.bridge;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.Vector;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class JavaFxGraphics implements IGameGraphicsImplementor {

    private final GraphicsContext gr;
    private final Map<String, Image> imageCache;

    public JavaFxGraphics(GraphicsContext gr) {
        this.gr = gr;
        this.imageCache = new HashMap<>();
    }

    @Override
    public void drawImage(String path, Position pos) {
        Image image = imageCache.get(path);
        if (image == null) {
            URL resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("Resource not found: " + path);
                return;
            }
            image = new Image(resource.toExternalForm());
            imageCache.put(path, image);
        }
        this.gr.drawImage(image, pos.getX(), pos.getY());
    }

    @Override
    public void drawText(String text, Position pos) {
        drawText(text, pos, "white", 12);
    }

    @Override
    public void drawText(String text, Position pos, String color, int fontSize) {
        gr.setFill(Color.web(color));
        gr.setFont(Font.font("Arial", fontSize));
        gr.fillText(text, pos.getX(), pos.getY());
    }

    @Override
    public void drawLine(Position beginPosition, Position endPosition) {
        gr.setStroke(Color.WHITE);
        gr.setLineWidth(1);
        gr.strokeLine(beginPosition.getX(), beginPosition.getY(),
                endPosition.getX(), endPosition.getY());
    }

    @Override
    public void drawLine(double x1, double y1, double x2, double y2, String color) {
        gr.setStroke(Color.web(color));
        gr.setLineWidth(1);
        gr.strokeLine(x1, y1, x2, y2);
    }

    @Override
    public void drawRectangle(double x, double y, double width, double height, String color) {
        gr.setFill(Color.web(color));
        gr.fillRect(x, y, width, height);
    }

    @Override
    public void clear() {
        this.gr.clearRect(0, 0, GameWindowConfig.MAX_X, GameWindowConfig.MAX_Y);
    }

    @Override
    public void drawTrajectory(List<Vector> points) {
        gr.setStroke(Color.GRAY);
        gr.setLineWidth(1);
        gr.setLineDashes(4);

        for (int i = 0; i < points.size() - 1; i++) {
            Vector p1 = points.get(i);
            Vector p2 = points.get(i + 1);
            gr.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }

        gr.setLineDashes(); // Reset
    }
}