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
    int damage = 6;
    private ImageTile collisionObject;
    private Position position;

    public Fire(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Fire";
    }

    @Override
    public Position getPosition() {
        return position;
    }


    public ImageTile getCollisionItem() {
        return collisionObject;
    }


    @Override
    public boolean validateImpact() {
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        ImageTile foundImage = currentRoom.checkPosition(getPosition());
        if (foundImage instanceof Enemy) {
            collisionObject = foundImage;
            Enemy enemy = (Enemy) collisionObject;
            enemy.takeDamage(this.damage);
            return false;
        } else if (foundImage instanceof Hero) {
            collisionObject = foundImage;
            return false;
        } else if (foundImage instanceof Door) {
            collisionObject = foundImage;
            return false;
        } else if (foundImage instanceof Wall) {
            collisionObject = foundImage;
            return false;
            // null means it's the Floor
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
