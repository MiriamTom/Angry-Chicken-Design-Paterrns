package cz.cvut.fit.niadp.mvcgame.controller;

import cz.cvut.fit.niadp.mvcgame.command.*;
import cz.cvut.fit.niadp.mvcgame.memento.CareTaker;
import cz.cvut.fit.niadp.mvcgame.model.IGameModel;
import cz.cvut.fit.niadp.mvcgame.config.MvcGameKeys;

import java.util.List;

public class GameController {

    private IGameModel model;

    public GameController(IGameModel model) {
        this.model = model;
    }



    public void processPressedKeys(List<String> pressedKeysCodes) {
        for (String code : pressedKeysCodes) {
            switch (code) {
                case MvcGameKeys.UP_KEY:
                    this.model.registerCommand(new MoveCannonUpCmd(this.model));
                    break;
                case MvcGameKeys.DOWN_KEY:
                    this.model.registerCommand(new MoveCannonDownCmd(this.model));
                    break;
                case MvcGameKeys.LEFT_KEY:
                    this.model.registerCommand(new MoveCannonLeftCmd(this.model));
                    break;
                case MvcGameKeys.RIGHT_KEY:
                    this.model.registerCommand(new MoveCannonRightCmd(this.model));
                    break;
                case MvcGameKeys.SHOOT_KEY:
                    this.model.registerCommand(new CannonShootCmd(this.model));
                    break;
                case MvcGameKeys.AIM_UP_KEY:
                    this.model.registerCommand(new AimCannonUpCmd(this.model));
                    break;
                case MvcGameKeys.AIM_DOWN_KEY:
                    this.model.registerCommand(new AimCannonDownCmd(this.model));
                    break;
                case MvcGameKeys.POWER_UP_KEY:
                    this.model.registerCommand(new CannonPowerUpCmd(this.model));
                    break;
                case MvcGameKeys.POWER_DOWN_KEY:
                    this.model.registerCommand(new CannonPowerDownCmd(this.model));
                    break;
                case MvcGameKeys.TOGGLE_MOVING_STRATEGY_KEY:
                    this.model.registerCommand(new ToggleMovingStrategyCmd(this.model));
                    break;
                case MvcGameKeys.TOGGLE_SHOOTING_MODE_KEY:
                    this.model.registerCommand(new ToggleShootingModeCmd(this.model));
                    break;
                case MvcGameKeys.STORE_GAME_SNAPSHOT_KEY:
                    CareTaker.getInstance().createMemento();
                    break;
                case MvcGameKeys.RESTORE_GAME_SNAPSHOT_KEY:
                    CareTaker.getInstance().restoreMemento();
                    break;
                case MvcGameKeys.UNDO_LAST_COMMAND_KEY:
                    model.undoLastCommand();
                    break;
                case MvcGameKeys.RELOAD_KEY:
                    this.model.registerCommand(new CannonReloadCmd(this.model));
                    break;
                case MvcGameKeys.TOGGLE_WEATHER_KEY: // Pridané
                    this.model.registerCommand(new ChangeWeatherCmd(this.model));
                    break;
                case MvcGameKeys.EXIT_KEY:
                    System.exit(0);
                    break;
                default:
                    // nič
            }
        }
    }
}
