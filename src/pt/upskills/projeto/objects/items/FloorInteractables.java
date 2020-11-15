package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

// Class of Items that are interactable when you move over them
public abstract class FloorInteractables implements ImageTile {

    private Position position;
    private int score;
    private int damage; // Works for both weapon damage and also GoodMeat and Potion HP.

    public FloorInteractables(Position position, int score, int damage) {
        this.position = position;
        this.score = score;
        this.damage = damage;
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

    public int getDamage() {
        return damage;
    }
}