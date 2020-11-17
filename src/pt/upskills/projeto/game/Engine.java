package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Hero;

import java.util.List;

public class Engine {


    public void init() {
        // Read all maps to LevelManager
        MapReader mapReader = new MapReader();
        mapReader.readMaps();
        // Get Level Manager Singleton
        LevelManager levelManager = LevelManager.getInstance();
        // Set start level
        levelManager.setCurrentRoom("room0.txt");
        levelManager.savedLevel = "room0.txt";
        levelManager.getHighScores();
        // Init gui and populate level
        List<ImageTile> tiles = levelManager.getCurrentRoom().getRoomImages();
        Hero hero = new Hero(mapReader.getStartHeroPos());
        levelManager.getCurrentRoom().setHero(hero);
        levelManager.setHeroInstance(hero);
        tiles.add(hero);
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.addObserver(hero);
        hero.updateStatus();
        gui.newImages(tiles);
        gui.go();
        while (true) {
            gui.update();
        }
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.init();
    }
}
