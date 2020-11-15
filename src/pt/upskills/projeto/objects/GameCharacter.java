package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.LevelManager;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.Wall;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.rogue.utils.Position;

// Super class for all characters of the game, both Hero and Enemies
public abstract class GameCharacter implements ImageTile {
    private Position position;
    private ImageTile collisionObject;

    public GameCharacter(Position position) {
        this.position = position;
    }

    public abstract String getName();

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ImageTile getCollisionItem() {
        return collisionObject;
    }

    //Check map bounds for a given position, return TRUE if inside map dimensions
    public boolean checkInsideMapBounds(Position pos) {
        return !(pos.getY() >= 10 || pos.getX() >= 10 || pos.getX() < 0 || pos.getY() < 0);
    }


    // Check collision for all game characters
    public boolean checkCollision(Position pos) {
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        // Get tile in the room for the position we are doing the collision check
        ImageTile foundImage = currentRoom.checkPosition(pos);
        if (foundImage instanceof Enemy) {
            collisionObject = foundImage;
            return true;
        } else if (foundImage instanceof Hero) {
            collisionObject = foundImage;
            return true;
        } else if (foundImage instanceof Door) {
            collisionObject = foundImage;
            return true;
        } else if (foundImage instanceof Wall) {
            collisionObject = foundImage;
            return true;
            // null means it's the Floor or Grass
        } else if (foundImage == null) {
            collisionObject = null;
            return false;
        }
        collisionObject = null;
        return false;
    }


}
