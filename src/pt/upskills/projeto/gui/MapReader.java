package pt.upskills.projeto.gui;

import pt.upskills.projeto.objects.*;
import pt.upskills.projeto.rogue.utils.Position;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapReader {

    private List<ImageTile> readMapImages = new ArrayList<ImageTile>();
    private Position heroPos;

    public static void main(String[] args) {
        MapReader map = new MapReader();
        map.readMap("room0.txt");
    }

    public List<ImageTile> readMap(String nomeFicheiro) {
        // Obtem a directoria do project para aceder ao ficheiro
        String localDir = System.getProperty("user.dir");
        // Correct path using localDir + relative path to the project
        File f = new File(localDir + "\\rooms\\" + nomeFicheiro);
        try {
            Scanner fileScanner = new Scanner(f);
            // Contador para obter as coordenadas do y
            int numLinha = 0;
            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();
                readLine(linha, numLinha);
                numLinha++;
            } // Fecha o fileScanner
            fileScanner.close();
        } catch (FileNotFoundException e) {
            // Apanha e trata o erro
            System.out.println("Ficheiro não encontrado");
            e.printStackTrace();
        } finally {
            // List of all the objects created for a map file
            return readMapImages;
        }
    }

    public Position getHeroPos() {
        return heroPos;
    }

    // Create the corresponding objects for each line of the map read and add them to the list
    public void readLine(String line, int coordY) {
        MapCodes[] mapCodes = MapCodes.values();
        Position pos;
        for (int i = 0; i < line.length(); i++) {
            char elementToConvert = line.charAt(i);
            switch (elementToConvert) {
                case 'w':
                    pos = new Position(i, coordY);
                    ImageTile w = new Wall(pos);
                    readMapImages.add(w);
                    System.out.println("Wall HERE");
                    break;
                case 'h':
                    System.out.println("HERO HERE");
                    heroPos = new Position(i, coordY);
                    ImageTile f = new Floor(heroPos);
                    ImageTile h = new Hero(heroPos);
                    readMapImages.add(f);
                    readMapImages.add(h);
                    break;
                case 'S':
                    pos = new Position(i, coordY);
                    ImageTile s = new Skeleton(pos);
                    f = new Floor(pos);
                    System.out.println("Skelet HERE");
                    readMapImages.add(f);
                    readMapImages.add(s);
                    break;
                case '0':
                    pos = new Position(i, coordY);
                    ImageTile doorO = new DoorOpen(pos);
                    readMapImages.add(doorO);
                    break;
                default:
                    pos = new Position(i, coordY);
                    f = new Floor(pos);
                    readMapImages.add(f);
                    break;
            }
        }
    }


}
