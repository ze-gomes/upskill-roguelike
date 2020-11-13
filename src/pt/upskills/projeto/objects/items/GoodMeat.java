package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class GoodMeat extends FloorInteractables implements ImageTile {

    public GoodMeat(Position position) {
        super (position, 10, 4);
    }

    @Override
    public String getName() {
        return "GoodMeat";
    }

}