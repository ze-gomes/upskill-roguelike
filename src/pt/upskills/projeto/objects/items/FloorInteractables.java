package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public abstract class FloorInteractables implements ImageTile {

    private Position position;
    private int score;

    public FloorInteractables(Position position, int score) {
        this.position = position;
        this.score = score;
    }

    @Override
    public String getName() {
        return "FloorInteractables";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public int getScore() {
        return score;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}