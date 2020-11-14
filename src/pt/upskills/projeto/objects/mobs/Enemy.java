package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.GameCharacter;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.game.LevelManager;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.rogue.utils.Vector2D;

import java.util.Random;

public abstract class Enemy extends GameCharacter implements ImageTile {
    private Random random = new Random();
    private int damage;
    private int mobHP;
    private int score;

    public Enemy(Position position, int damage, int hp, int score) {
        super(position);
        this.damage = damage;
        this.mobHP = hp;
        this.score = score;
    }

    public abstract String getName();

    public void takeDamage(int damage) {
        LevelManager levelManager = LevelManager.getInstance();
        this.mobHP -= damage;
        if (mobHP<= 0){
            levelManager.getCurrentRoom().removeObject(this);
        }
    }

    public int getMobHP() {
        return mobHP;
    }

    public int getDamage() {
        return damage;
    }

    // Generate random 2D vector from array
    public Vector2D getRandomVectorMovement() {
        Vector2D[] vectorArray = new Vector2D[4];
        vectorArray[0] = Direction.DOWN.asVector();
        vectorArray[1] = Direction.UP.asVector();
        vectorArray[2] = Direction.LEFT.asVector();
        vectorArray[3] = Direction.RIGHT.asVector();
        return vectorArray[random.nextInt(vectorArray.length)];
    }

    // Get random vector in the direction of hero
    public Vector2D getDirectionHero(){
        LevelManager levelManager = LevelManager.getInstance();
        Position heroPos = levelManager.getCurrentRoom().getHero().getPosition();
        Position enemyPos = this.getPosition();
        // Check the coordinates in which the hero is closer and if moving that coordinate will
        if (heroPos.getY() < enemyPos.getY() && !(checkCollision(enemyPos.plus(Direction.UP.asVector())))){
            return Direction.UP.asVector();
        } else if (heroPos.getX() < enemyPos.getX() && !(checkCollision(enemyPos.plus(Direction.LEFT.asVector())))){
            return Direction.LEFT.asVector();
        } else if (heroPos.getY() > enemyPos.getY() && !(checkCollision(enemyPos.plus(Direction.DOWN.asVector())))){
            return Direction.DOWN.asVector();
        } else if (heroPos.getX() > enemyPos.getX() && !(checkCollision(enemyPos.plus(Direction.RIGHT.asVector())))){
            return Direction.RIGHT.asVector();
        }
        return getRandomVectorMovement();
    }


    //Get distance to hero
    public double distanceToHero() {
        LevelManager levelManager = LevelManager.getInstance();
        Position heroPos = levelManager.getCurrentRoom().getHero().getPosition();
        Position enemyPos = this.getPosition();
        return heroPos.calculateDistance(enemyPos);
    }

    public void movement() {
        // Cria random new pos from random Vector2D
        Position newRandomPos = this.getPosition().plus(getRandomVectorMovement());
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // Se nao colidiu com nada na nova posiçao e esta dentro dos map bounds,
        // entao move para a nova posiçao
        if (distanceToHero() > 4.0) {
            if (!checkCollision(newRandomPos) && (checkInsideMapBounds(newRandomPos))) {
                this.setPosition(newRandomPos);
            }
        } else if (distanceToHero() > 1.0) {
            Position posDirectionHero = getPosition().plus(getDirectionHero());
            if (!checkCollision(posDirectionHero) && (checkInsideMapBounds(posDirectionHero))) {
                this.setPosition(posDirectionHero);
            }
        }
        else {
            LevelManager levelManager = LevelManager.getInstance();
            Hero hero = levelManager.getCurrentRoom().getHero();
            hero.changeHP(-damage);
            hero.updateStatus();

        }
    }

    public int getScore() {
        return score;
    }
}
