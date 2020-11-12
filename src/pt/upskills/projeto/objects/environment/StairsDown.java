package pt.upskills.projeto.objects.environment;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class StairsDown implements ImageTile {

    private Position position;

    public StairsDown(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "StairsDown";
    }

    @Override
    public Position getPosition() {
        return position;
    }

}
