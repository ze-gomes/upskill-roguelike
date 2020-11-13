package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Key extends FloorInteractables implements ImageTile {
    private String code;

    public Key(Position position, String nome) {
        super (position,  20, 0);
        this.code = nome;
    }

    @Override
    public String getName() {
        return "Key";
    }

    public String getCode() {
        return code;
    }
}
