package cz.cvut.fit.niadp.mvcgame.config;

public class CannonMissileConfig {
    public static final int MOVE_STEP = 10;

    public static final int CANNON_POS_X = 50;
    public static final int CANNON_POS_Y = GameWindowConfig.MAX_Y / 2;
    public static final int CANNON_SIZE_Y = 69;

    public static final int INIT_POWER = 5;
    public static final double INIT_ANGLE = 0;
    public static final double ANGLE_STEP = - Math.PI / 10;
    public static final int POWER_STEP = 1;

    public static final double GRAVITY = 1.8;

    public static final int RELOAD_TIME = 2;
    public static final int MISSILE_CAPACITY = 6;
    public static final int MISSILE_HIT_RADIUS = 14;
    public static final int COLLISION_DELETE_TIME = 3000; // milliseconds
}
