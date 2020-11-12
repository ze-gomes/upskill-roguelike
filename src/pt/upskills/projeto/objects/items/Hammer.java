package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Hammer extends FloorInteractables implements ImageTile {
    private int damage = 4;

    public Hammer(Position position) {
        super (position, 10);
    }

    @Override
    public String getName() {
        return "Hammer";
    }

    public int getDamage() {
        return damage;
    }
}