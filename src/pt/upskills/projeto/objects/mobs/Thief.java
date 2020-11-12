package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.objects.LevelManager;
import pt.upskills.projeto.rogue.utils.Position;

public class Thief extends Enemy implements ImageTile {

    public Thief(Position position) {
        super(position, 2, 6, 50);
    }

    @Override
    public String getName() {
        return "Thief";
    }

    @Override
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
            hero.takeDamage(getDamage());
            hero.updateStatus();

        }

    }
}