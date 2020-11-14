package pt.upskills.projeto.gui;

import pt.upskills.projeto.game.LevelManager;
import pt.upskills.projeto.objects.*;
import pt.upskills.projeto.objects.environment.*;
import pt.upskills.projeto.objects.items.*;
import pt.upskills.projeto.objects.mobs.*;
import pt.upskills.projeto.rogue.utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MapReader {
    private List<ImageTile> readMapObjects;
    private Position heroPos;
    private HashMap<Integer, Door> listaPortas;
    private String doorKey;
    private List<ImageTile> readMapImages;


    public void readMaps() {
        // Obtem a directoria do project para aceder ao ficheiro
        String localDir = System.getProperty("user.dir");
        // Correct path using localDir + relative path to the project
        File[] fileArray = new File(localDir + "\\rooms\\").listFiles();
        // Obtem o levelManager para carregar os niveis no jogo
        LevelManager levelManager = LevelManager.getInstance();
        try {// carrega todos os ficheiros
            for (File f : fileArray) {
                // Para cada ficheiro cria e armazena uma lista de objectos ImageTile e uma Lista de Portas
                readMapImages = new ArrayList<ImageTile>();
                readMapObjects = new ArrayList<ImageTile>();
                listaPortas = new HashMap<Integer, Door>();
                Scanner fileScanner = new Scanner(f);
                addFloor();
                // Contador para obter as coordenadas do y
                int numLinha = 0;
                while (fileScanner.hasNextLine()) {
                    String linha = fileScanner.nextLine();
                    // Le e trata info de portas de cada mapa
                    if (linha.charAt(0) == '#') {
                        String[] porta = linha.split(" ");
                        lePortas(porta);
                    } else { // Le e trata info do mapa
                        readLine(linha, numLinha);
                        numLinha++;
                    }
                }
                // Create room for each file and add to levelManager singleton
                readMapImages.addAll(readMapObjects);
                levelManager.addGameLevel(f.getName(), new Room(f.getName(), readMapImages, listaPortas));
                // Fecha o fileScanner
                fileScanner.close();
            }
            // Sets first level
            levelManager.setCurrentRoom("room0.txt");
        } catch (FileNotFoundException e) {
            // Apanha e trata o erro
            System.out.println("Ficheiro não encontrado");
            e.printStackTrace();
        }
    }

    public Position getStartHeroPos() {
        return heroPos;
    }

    public void lePortas(String[] porta) {
        // Se a 3º posicao da linha é vazia, entao nao tem info de porta
        // Simplesmente é o separador inicial ou final das info de portas
        // Nesse caso nao faz nada
        if (porta.length > 1) {
            int numDoor = Integer.parseInt(porta[1]);
            String doorType = porta[2];
            String destRoom = porta[3];
            int destDoor = Integer.parseInt(porta[4]);
            if (doorType.equals("D")) {
                // Só verifica se a porta tiver 1 key associada
                // Só se a porta tiver chave cria uma porta fechada
                if (porta.length == 6) {
                    doorKey = porta[5];
                    // Crio portas com posiçoes (0,0) e depois quando percorro o mapa actualizo as posiçoes com o setPosicao
                    Door portaFechada = new DoorClosed(new Position(0, 0), numDoor, destRoom, destDoor, doorKey);
                    listaPortas.put(numDoor, portaFechada);
                } else {
                    Door portaAberta = new DoorOpen(new Position(0, 0), numDoor, destRoom, destDoor);
                    listaPortas.put(numDoor, portaAberta);
                }
            } else if (doorType.equals("E")) {
                Door semPorta = new DoorWay(new Position(0, 0), numDoor, destRoom, destDoor);
                listaPortas.put(numDoor, semPorta);
            }
        }
    }

    //Adds floor below things
    public void addFloor() {
        Position pos;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                pos = new Position(x, y);
                ImageTile f = new Floor(pos);
                readMapImages.add(f);
            }
        }
    }


    // Create the corresponding objects for each line of the map read and add them to the list
    public void readLine(String line, int coordY) {
        Position pos;
        for (int i = 0; i < line.length(); i++) {
            char elementToConvert = line.charAt(i);
            switch (elementToConvert) {
                case 'W':
                    pos = new Position(i, coordY);
                    ImageTile w = new Wall(pos);
                    readMapObjects.add(w);
                    break;
                case 'H':
                    heroPos = new Position(i, coordY);
                    break;
                case 'S':
                    pos = new Position(i, coordY);
                    Enemy skeleton = new Skeleton(pos);
                    readMapObjects.add(skeleton);
                    break;
                case 'G':
                    pos = new Position(i, coordY);
                    Enemy badGuy = new BadGuy(pos);
                    readMapObjects.add(badGuy);
                    break;
                case 'B':
                    pos = new Position(i, coordY);
                    Enemy bat = new Bat(pos);
                    readMapObjects.add(bat);
                    break;
                case 'T':
                    pos = new Position(i, coordY);
                    Enemy thief = new Thief(pos);
                    readMapObjects.add(thief);
                    break;
                case 's':
                    pos = new Position(i, coordY);
                    FloorInteractables sword = new Sword(pos);
                    readMapObjects.add(sword);
                    break;
                case 'h':
                    pos = new Position(i, coordY);
                    FloorInteractables hammer = new Hammer(pos);
                    readMapObjects.add(hammer);
                    break;
                case 'g':
                    pos = new Position(i, coordY);
                    ImageTile grass = new Grass(pos);
                    readMapImages.add(grass);
                    break;
                case 'k':
                    pos = new Position(i, coordY);
                    FloorInteractables key = new Key(pos, doorKey);
                    readMapObjects.add(key);
                    break;
                case 't':
                    pos = new Position(i, coordY);
                    FloorInteractables trap = new Trap(pos);
                    readMapObjects.add(trap);
                    break;
                case 'm':
                    pos = new Position(i, coordY);
                    FloorInteractables goodMeat = new GoodMeat(pos);
                    readMapObjects.add(goodMeat);
                    break;
                case '0':
                    pos = new Position(i, coordY);
                    Door porta0 = listaPortas.get(0);
                    porta0.setPosition(pos);
                    readMapObjects.add(porta0);
                    break;
                case '1':
                    pos = new Position(i, coordY);
                    Door porta1 = listaPortas.get(1);
                    porta1.setPosition(pos);
                    readMapObjects.add(porta1);
                    break;
                case '2':
                    pos = new Position(i, coordY);
                    Door porta2 = listaPortas.get(2);
                    porta2.setPosition(pos);
                    readMapObjects.add(porta2);
                    break;
                default:
                    break;
            }
        }
    }


}
