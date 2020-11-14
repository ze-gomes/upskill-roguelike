package pt.upskills.projeto.objects.other;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class GameOver1 implements ImageTile {

    private Position position;

    public GameOver1(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "GameOver1";
    }

    @Override
    public Position getPosition() {
        return position;
    }

}
