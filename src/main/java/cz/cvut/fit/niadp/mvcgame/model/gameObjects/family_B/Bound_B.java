package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_B;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsBound;

public class Bound_B extends AbsBound {
    private int size; // Dynamická veľkosť prekážky
    private boolean isBreakable; // Môže byť zničená

    public Bound_B(Position pos, int size) {
        this.position = pos;
        this.size = size;
        this.isBreakable = false;
    }

    public Bound_B(Position pos, int size, boolean breakable) {
        this.position = pos;
        this.size = size;
        this.isBreakable = breakable;
    }

    public int getSize() {
        return size;
    }

    public boolean isBreakable() {
        return isBreakable;
    }

    public void setBreakable(boolean breakable) {
        this.isBreakable = breakable;
    }

    // Špecifická metóda pre rodinu B
    public void reduceSize() {
        if (isBreakable && size > 0) {
            size -= 10;
        }
    }
}