package cz.cvut.fit.niadp.mvcgame;

import cz.cvut.fit.niadp.mvcgame.bridge.IGameGraphics;
import cz.cvut.fit.niadp.mvcgame.config.GameWindowConfig;
import cz.cvut.fit.niadp.mvcgame.controller.GameController;
import cz.cvut.fit.niadp.mvcgame.memento.CareTaker;
import cz.cvut.fit.niadp.mvcgame.model.GameModel;
import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsGameInfo;
import cz.cvut.fit.niadp.mvcgame.proxy.GameModelProxy;
import cz.cvut.fit.niadp.mvcgame.visitor.GameDrawer;
import cz.cvut.fit.niadp.mvcgame.view.GameView;

import java.util.List;

public class MvcGame {
    private IGameModel model;
    private GameView view;
    private GameController controller;
    private GameDrawer gameDrawer;

    public void init() {
        // 1. Vytvor model
        this.model = new GameModelProxy(new GameModel());

        // 2. Vytvor GameDrawer
        this.gameDrawer = new GameDrawer();

        // 3. Vytvor GameView a predaj mu GameDrawer
        this.view = new GameView(model, gameDrawer);

        // 4. Vytvor controller
        this.controller = this.view.getController();

        // 5. Nastav memento
        CareTaker.getInstance().setModel(model);

        // 6. Nastav GameInfo do GameDrawer
        if (model != null && gameDrawer != null) {
            gameDrawer.setGameInfo(model.getGameInfo());
        }

        System.out.println("MvcGame initialized with GameDrawer: " + (gameDrawer != null));
    }

    public void processPressedKeys(List<String> pressedKeysCodes) {
        if (controller != null) {
            this.controller.processPressedKeys(pressedKeysCodes);
        }
    }

    public void update() {
        if (model != null) {
            this.model.update();
        }
    }

    public void render() {
        if (this.view != null) {
            this.view.render();
        } else {
            System.err.println("GameView is null in render()!");
        }
    }

    public void render(IGameGraphics gr) {
        if (this.view != null && gr != null) {
            this.view.setGraphicContext(gr);
            this.view.render();
        } else {
            System.err.println("View or Graphics is null: view=" + (view != null) + ", gr=" + (gr != null));
        }
    }

    public String getWindowTitle() {
        return "ANGRY CHICKEN";
    }

    public int getWindowWidth() {
        return GameWindowConfig.MAX_X;
    }

    public int getWindowHeight() {
        return GameWindowConfig.MAX_Y;
    }

    public int getScore() {
        return model != null ? model.getScore() : 0;
    }

    public int getLevel() {
        return model != null ? model.getLevel() : 1;
    }

    public int getCannonAvailableMissiles() {
        return model != null ? model.getCannonAvailableMissiles() : 0;
    }

    public String getCurrentWeatherName() {
        return model != null ? model.getWeatherSystem().getName() : "Clear";
    }

    public GameDrawer getGameRenderer() {
        return gameDrawer;
    }

    public GameView getView() {
        return view;
    }

    // Debug metóda
    public void printStatus() {
        System.out.println("=== MvcGame Status ===");
        System.out.println("Model: " + (model != null ? "OK" : "NULL"));
        System.out.println("View: " + (view != null ? "OK" : "NULL"));
        System.out.println("Controller: " + (controller != null ? "OK" : "NULL"));
        System.out.println("GameDrawer: " + (gameDrawer != null ? "OK" : "NULL"));
        System.out.println("GameInfo: " + (model != null && model.getGameInfo() != null ? "OK" : "NULL"));
        System.out.println("======================");
    }

    public int getObjectCount() {
        if (model != null) {
            int count = model.getMissiles().size() +
                    model.getEnemies().size() +
                    model.getCollisions().size() + 2;

            // Debug výpis ak je veľa objektov
            if (count > 50) {
                System.out.println("HIGH OBJECT COUNT: " + count +
                        " (M:" + model.getMissiles().size() +
                        " E:" + model.getEnemies().size() +
                        " C:" + model.getCollisions().size() + ")");
            }

            return count;
        }
        return 0;
    }

    // Nová metóda pre získanie GameInfo
    public AbsGameInfo getGameInfo() {
        return model != null ? model.getGameInfo() : null;
    }


}