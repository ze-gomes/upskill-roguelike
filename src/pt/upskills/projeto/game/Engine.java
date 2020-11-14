package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.gui.MapReader;
import pt.upskills.projeto.objects.Hero;

import java.util.List;

public class Engine {


    public void init(){
        // Read all maps to LevelManager
        MapReader mapReader  = new MapReader();
        mapReader.readMaps();
        // Create Level Manager
        LevelManager levelManager = LevelManager.getInstance();
        // Set start level
        levelManager.setCurrentRoom("room0.txt");
        // Init gui and populate level
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        List<ImageTile> tiles = levelManager.getCurrentRoom().getRoomImages();
        Hero hero = new Hero(mapReader.getStartHeroPos());
        levelManager.getCurrentRoom().setHero(hero);
        levelManager.setHeroInstance(hero);
        tiles.add(hero);
        gui.addObserver(hero);
        hero.updateStatus();
        gui.newImages(tiles);
        gui.go();
        while (!levelManager.getGameOver()){
            gui.update();
        }
        gui.update();
    }

    public static void main(String[] args){
        Engine engine = new Engine();
        engine.init();
    }
}
