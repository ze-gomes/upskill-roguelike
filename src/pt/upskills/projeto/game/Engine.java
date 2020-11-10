package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.gui.MapReader;
import pt.upskills.projeto.objects.Floor;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.objects.LevelManager;
import pt.upskills.projeto.objects.Room;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Engine {


    public void init(){
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        MapReader mapReader  = new MapReader();
        LevelManager levelManager = LevelManager.getInstance();
        Room addedRoom = mapReader.readMap("room0");
        levelManager.setCurrentRoom(addedRoom);
        levelManager.addGameLevel(addedRoom);

        List<ImageTile> tiles = levelManager.getCurrentRoom().getRoomImages();
//       List<ImageTile> tiles = new ArrayList<>();
//        for(int i=0; i<10; i++){
//            for(int j=0; j<10; j++){
//                tiles.add(new Floor(new Position(i, j)));
//            }
//        }
//        Hero hero = new Hero(new Position(4, 3));
//        tiles.add(hero);
        Hero hero = new Hero(levelManager.getCurrentRoom().getHeroPos());
        tiles.add(hero);
        gui.addObserver(hero);
        hero.updateStatus();
        gui.newImages(tiles);
        gui.go();

        while (true){
            gui.update();
        }
    }

    public static void main(String[] args){
        Engine engine = new Engine();
        engine.init();
    }
}
