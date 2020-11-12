package pt.upskills.projeto.objects.environment;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Grass implements ImageTile {

    private Position position;

    public Grass(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Grass";
    }

    @Override
    public Position getPosition() {
        return position;
    }

}
