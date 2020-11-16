package pt.upskills.projeto.objects.status;

import pt.upskills.projeto.gui.FireTile;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.game.LevelManager;
import pt.upskills.projeto.objects.Room;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.Wall;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.rogue.utils.Position;

public class Fire implements FireTile {
    String name;
    int damage = 6;
    private ImageTile collisionObject;
    private Position position;

    public Fire(Position position) {
        this.position = position;
        this.name = "Fire";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public boolean validateImpact() {
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        ImageTile foundImage = currentRoom.checkPosition(getPosition());
        // If it collides with a enemy, damage him.
        if (foundImage instanceof Enemy) {
            collisionObject = foundImage;
            Enemy enemy = (Enemy) collisionObject;
            enemy.takeDamage(this.damage);
            setName("FireExplosion");
            return false;
        } else if (foundImage instanceof Hero) {
            collisionObject = foundImage;
            setName("FireExplosion");
            return false;
        } else if (foundImage instanceof Door) {
            collisionObject = foundImage;
            setName("FireExplosion");
            return false;
        } else if (foundImage instanceof Wall) {
            collisionObject = foundImage;
            setName("FireExplosion");
            return false;
            // null means it's the Floor or Grass
        } else if (foundImage == null) {
            return true;
        }
        collisionObject = null;
        return true;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
