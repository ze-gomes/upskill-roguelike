package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Key extends FloorInteractables implements ImageTile {
    private String code;

    public Key(Position position, String code) {
        // For the sake of grouping all items on FloorInteractables,
        // we pass the damage attribute as 0 for the key, it's never used on this class.
        super (position,  20, 0);
        this.code = code;
    }

    @Override
    public String getName() {
        return "Key";
    }

    public String getCode() {
        return code;
    }
}
