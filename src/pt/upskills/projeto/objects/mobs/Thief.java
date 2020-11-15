package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.game.LevelManager;
import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.rogue.utils.Vector2D;

import java.util.Random;

public class Thief extends Enemy implements ImageTile {
    Vector2D[] diagonalMovements = {new Vector2D(1, 1), new Vector2D(-1, 1), new Vector2D(1, -1), new Vector2D(-1, -1)};
    private Random random = new Random();


    public Thief(Position position) {
        super(position, 3, 8, 50);
    }

    @Override
    public String getName() {
        return "Thief";
    }


    // Generate random 2D vector from array of diagonal movements
    public Vector2D getRandomDiagonalMovement() {
        return diagonalMovements[random.nextInt(diagonalMovements.length)];

    }

    // Get random vector in the direction of hero
    public Vector2D getDirectionHero() {
        LevelManager levelManager = LevelManager.getInstance();
        Position heroPos = levelManager.getCurrentRoom().getHero().getPosition();
        Position enemyPos = this.getPosition();
        // Check the coordinates in which the hero is closer and checks for collisions as well
        if (heroPos.getY() > enemyPos.getY()) { // Movements on the diagonal in the Down direction
            if (heroPos.getX() > enemyPos.getX() && !(checkCollision(enemyPos.plus(diagonalMovements[0])))) {
                return diagonalMovements[0];
            } else if (heroPos.getX() < enemyPos.getX() && !(checkCollision(enemyPos.plus(diagonalMovements[1])))) {
                return diagonalMovements[1];
            }
        } else if (heroPos.getY() < enemyPos.getY()) { // Movements on the diagonal in the Up direction
            if (heroPos.getX() > enemyPos.getX() && !(checkCollision(enemyPos.plus(diagonalMovements[2])))) {
                return diagonalMovements[2];
            } else if (heroPos.getX() < enemyPos.getX() && !(checkCollision(enemyPos.plus(diagonalMovements[3])))) {
                return diagonalMovements[3];
            }
        }
        return getRandomDiagonalMovement();
    }

    // Thief has special movement different from other Enemies (moves in diagonal)
    @Override
    public void movement() {
        // Creates random new position from random Vector2D
        Position newRandomPos = this.getPosition().plus(getRandomDiagonalMovement());
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // On a distance > 4.5 from the hero , If there is not collision and within map bounds, move to that pos
        if (distanceToHero() > 4.5) {
            if (!checkCollision(newRandomPos) && (checkInsideMapBounds(newRandomPos))) {
                this.setPosition(newRandomPos);
            }
            // From this distance (4.5-2.0 range) the Thief moves in the direction of the hero
        } else if (distanceToHero() >=   2.0) {
            Position posDirectionHero = getPosition().plus(getDirectionHero());
            if (!checkCollision(posDirectionHero) && (checkInsideMapBounds(posDirectionHero))) {
                this.setPosition(posDirectionHero);
            }
        } else { // Thief attacks in distance < 2.0 (can attack in diagonal)
            LevelManager levelManager = LevelManager.getInstance();
            Hero hero = levelManager.getCurrentRoom().getHero();
            hero.changeHP(-getDamage());
            hero.updateStatus();
        }

    }
}