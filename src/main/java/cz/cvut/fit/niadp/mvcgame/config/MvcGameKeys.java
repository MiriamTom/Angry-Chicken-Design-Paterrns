package cz.cvut.fit.niadp.mvcgame.config;

public class MvcGameKeys {
    private MvcGameKeys() { }

    public static final String UP_KEY = "UP";
    public static final String DOWN_KEY = "DOWN";
    public static final String LEFT_KEY = "LEFT";
    public static final String RIGHT_KEY = "RIGHT";
    public static final String SHOOT_KEY = "SPACE";
    public static final String AIM_UP_KEY = "S";
    public static final String AIM_DOWN_KEY = "W";
    public static final String POWER_UP_KEY = "E";
    public static final String POWER_DOWN_KEY = "Q";
    public static final String TOGGLE_MOVING_STRATEGY_KEY = "M";
    public static final String TOGGLE_SHOOTING_MODE_KEY = "N";
    public static final String RELOAD_KEY = "R";
    public static final String STORE_GAME_SNAPSHOT_KEY = "U";
    public static final String RESTORE_GAME_SNAPSHOT_KEY = "I";

    public static final String UNDO_LAST_COMMAND_KEY = "Y";

    public static final String EXIT_KEY = "ESCAPE";
    public static final String TOGGLE_WEATHER_KEY  = "P";
    public static String getNameofKey(String keyCode) {
        switch (keyCode) {
            case UP_KEY:
                return "Up Arrow";
            case DOWN_KEY:
                return "Down Arrow";
            case LEFT_KEY:
                return "Left Arrow";
            case RIGHT_KEY:
                return "Right Arrow";
            case SHOOT_KEY:
                return "Space";
            case AIM_UP_KEY:
                return "S";
            case AIM_DOWN_KEY:
                return "W";
            case POWER_UP_KEY:
                return "E";
            case POWER_DOWN_KEY:
                return "Q";
            case TOGGLE_MOVING_STRATEGY_KEY:
                return "M";
            case TOGGLE_SHOOTING_MODE_KEY:
                return "N";
            case RELOAD_KEY:
                return "R";
            case STORE_GAME_SNAPSHOT_KEY:
                return "U";
            case RESTORE_GAME_SNAPSHOT_KEY:
                return "I";
            case UNDO_LAST_COMMAND_KEY:
                return "Y";
            case EXIT_KEY:
                return "Escape";
            case TOGGLE_WEATHER_KEY :
                return "P";
            default:
                return "Unknown Key";
        }
    }
}
