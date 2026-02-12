package cz.cvut.fit.niadp.mvcgame.view;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.controller.GameController;
import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsGameInfo;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.GameObject;
import cz.cvut.fit.niadp.mvcgame.observer.IObserver;
import cz.cvut.fit.niadp.mvcgame.visitor.GameDrawer;

import java.util.List;

public class GameView implements IObserver {
    private GameController controller;
    private IGameModel model;
    private IGameGraphics gr;
    private GameDrawer renderer;
    private boolean graphicsReady = false;

    public GameView(IGameModel model, GameDrawer renderer) {
        System.out.println("Creating GameView with custom renderer...");
        this.model = model;
        this.controller = new GameController(model);
        this.gr = null;
        this.renderer = renderer;
        this.graphicsReady = false;
    }

    public GameView(IGameModel model) {
        this(model, new GameDrawer());
    }

    public GameController getController() {
        return this.controller;
    }

    public void render() {
        if (!graphicsReady || this.gr == null) {
            System.err.println("Graphics not ready in GameView.render()");
            return;
        }

        if (this.renderer == null) {
            System.err.println("Renderer is null in GameView.render()!");
            return;
        }

        // DÔLEŽITÉ: Aktualizuj dáta v rendereri PRED vykreslením
        updateRendererData();

        this.gr.clear();

        List<GameObject> gameObjects = this.model.getGameObjects();

        // Vykresli všetky herné objekty
        for (GameObject go : gameObjects) {
            go.acceptVisitor(this.renderer);
        }

        // Vykresli GameInfo
        AbsGameInfo gameInfo = this.model.getGameInfo();
        if (gameInfo != null) {
            gameInfo.acceptVisitor(this.renderer);
        }
    }

    private void updateRendererData() {
        if (this.model != null && this.renderer != null) {
            // Aktualizuj všetky potrebné dáta v rendereri
            this.renderer.updateGameData(
                    this.model.getScore(),
                    this.model.getLevel(),
                    this.model.getCannonAvailableMissiles(),
                    this.model.getWeatherSystem().getName()
            );

            // Nastav GameInfo
            this.renderer.setGameInfo(this.model.getGameInfo());
        }
    }

    public void setGraphicContext(IGameGraphics gr) {
        System.out.println("Setting graphics context in GameView...");
        this.gr = gr;

        if (this.renderer != null) {
            this.renderer.setGraphicContext(gr);
            System.out.println("Graphics context set in renderer.");
        } else {
            System.err.println("Renderer is null when setting graphics context!");
        }

        if (!graphicsReady) {
            this.model.registerObserver(this);
            this.graphicsReady = true;
            System.out.println("GameView registered as observer.");
        }

        this.update();
    }

    public GameDrawer getGameRenderer() {
        return this.renderer;
    }

    @Override
    public void update() {
        this.render();
    }

    public void printStatus() {
        System.out.println("=== GameView Status ===");
        System.out.println("Model: " + (model != null ? "OK" : "NULL"));
        System.out.println("Graphics: " + (gr != null ? "OK" : "NULL"));
        System.out.println("Renderer: " + (renderer != null ? "OK" : "NULL"));
        System.out.println("Graphics ready: " + graphicsReady);
        System.out.println("Current score: " + (model != null ? model.getScore() : "N/A"));
        System.out.println("=========================");
    }
}