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
        // Check the coordinates in which the hero is closer and if moving that coordinate will
        if (heroPos.getY() > enemyPos.getY()) { // Movimentos na Diagonal para baixo
            if (heroPos.getX() > enemyPos.getX() && !(checkCollision(enemyPos.plus(diagonalMovements[0])))) {
                return diagonalMovements[0];
            } else if (heroPos.getX() < enemyPos.getX() && !(checkCollision(enemyPos.plus(diagonalMovements[1])))) {
                return diagonalMovements[1];
            }
        } else if (heroPos.getY() < enemyPos.getY()) { // Movimentos na Diagonal para cima
            if (heroPos.getX() > enemyPos.getX() && !(checkCollision(enemyPos.plus(diagonalMovements[2])))) {
                return diagonalMovements[2];
            } else if (heroPos.getX() < enemyPos.getX() && !(checkCollision(enemyPos.plus(diagonalMovements[3])))) {
                return diagonalMovements[3];
            }
        }
        return getRandomDiagonalMovement();
    }


    @Override
    public void movement() {
        // Cria random new pos from random Vector2D
        Position newRandomPos = this.getPosition().plus(getRandomDiagonalMovement());
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // Se nao colidiu com nada na nova posiçao e esta dentro dos map bounds,
        // entao move para a nova posiçao
        if (distanceToHero() > 4.5) {
            if (!checkCollision(newRandomPos) && (checkInsideMapBounds(newRandomPos))) {
                this.setPosition(newRandomPos);
            }
        } else if (distanceToHero() >=   2.0) {
            Position posDirectionHero = getPosition().plus(getDirectionHero());
            if (!checkCollision(posDirectionHero) && (checkInsideMapBounds(posDirectionHero))) {
                this.setPosition(posDirectionHero);
            }
        } else { // Thief can attack in diagonal (distance < 2)
            LevelManager levelManager = LevelManager.getInstance();
            Hero hero = levelManager.getCurrentRoom().getHero();
            hero.changeHP(-getDamage());
            hero.updateStatus();
        }

    }
}