package pt.upskills.projeto.objects.mobs;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.GameCharacter;
import pt.upskills.projeto.rogue.utils.Position;

public abstract class Enemy extends GameCharacter implements ImageTile {

    public Enemy(Position position){
        super(position);
    }

    public abstract String getName();
}
