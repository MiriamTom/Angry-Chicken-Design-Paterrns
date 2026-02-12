package cz.cvut.fit.niadp.mvcgame.model.gameObjects.family_B;

import cz.cvut.fit.niadp.mvcgame.model.Position;
import cz.cvut.fit.niadp.mvcgame.model.gameObjects.AbsEndScene;

public class EndScene_B extends AbsEndScene {
    private int difficultyLevel;
    private int timePlayed; // Čas hry v sekundách

    public EndScene_B(Position pos, int score, int difficultyLevel) {
        super(pos, score);
        this.difficultyLevel = difficultyLevel;
        this.timePlayed = 0;
    }

    public EndScene_B(Position pos, int score, int difficultyLevel, int timePlayed) {
        super(pos, score);
        this.difficultyLevel = difficultyLevel;
        this.timePlayed = timePlayed;
    }

    @Override
    public String getText() {
        // Rozšírený text pre rodinu B
        StringBuilder sb = new StringBuilder();
        sb.append("=== KONIEC HRY ===\n");
        sb.append("Skóre: ").append(score).append(" bodov\n");
        sb.append("Úroveň obtiažnosti: ").append(difficultyLevel).append("\n");

        if (timePlayed > 0) {
            sb.append("Čas hry: ").append(timePlayed).append(" sekúnd\n");
        }

        sb.append("Celkové hodnotenie: ");
        if (score > 1000) sb.append("Výborné!");
        else if (score > 500) sb.append("Dobré!");
        else sb.append("Skús to znova!");

        sb.append("\nPre reštart stlač ENTER");

        return sb.toString();
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }
}