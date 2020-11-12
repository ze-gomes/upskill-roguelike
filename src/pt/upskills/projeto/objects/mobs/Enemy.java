package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.GameCharacter;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.objects.LevelManager;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.DoorClosed;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;
import pt.upskills.projeto.rogue.utils.Vector2D;

import java.util.Random;

public abstract class Enemy extends GameCharacter implements ImageTile {
    private Random random = new Random();
    private int damage;
    private int mobHP;

    public Enemy(Position position, int damage, int hp) {
        super(position);
        this.damage = damage;
        this.mobHP = hp;
    }

    public abstract String getName();

    public void takeDamage(int damage) {
        this.mobHP -= damage;
    }

    public int getMobHP() {
        return mobHP;
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

    public void movement() {
        // Cria random new pos from random Vector2D
        Position newRandomPos = this.getPosition().plus(getRandomVectorMovement());
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // Se nao colidiu com nada na nova posiçao e esta dentro dos map bounds,
        // entao move para a nova posiçao
        if (!checkCollision(newRandomPos) && (checkInsideMapBounds(newRandomPos))) {
            gui.removeImage(this);
            this.setPosition(newRandomPos);
            gui.addImage(this);
        } else if (getCollisionItem() instanceof Hero){
            Hero hero = (Hero) getCollisionItem();
            hero.takeDamage(damage);
        }
    }


}
