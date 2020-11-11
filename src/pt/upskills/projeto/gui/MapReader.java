package pt.upskills.projeto.gui;

import pt.upskills.projeto.objects.*;
import pt.upskills.projeto.objects.environment.*;
import pt.upskills.projeto.objects.items.Sword;
import pt.upskills.projeto.objects.mobs.*;
import pt.upskills.projeto.rogue.utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MapReader {
    private List<ImageTile> readMapImages;
    private List<Enemy> readMapEnemies;
    private Position heroPos;
    private HashMap<Integer, Door> listaPortas;


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
                listaPortas = new HashMap<Integer, Door>();
                readMapEnemies = new ArrayList<Enemy>();
                Scanner fileScanner = new Scanner(f);
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
                levelManager.addGameLevel(f.getName(), new Room(f.getName(), readMapImages, heroPos, listaPortas, readMapEnemies));
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

    public Position getHeroPos() {
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
                    String doorKey = porta[5];
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


    // Create the corresponding objects for each line of the map read and add them to the list
    public void readLine(String line, int coordY) {
        Position pos;
        for (int i = 0; i < line.length(); i++) {
            char elementToConvert = line.charAt(i);
            switch (elementToConvert) {
                case 'W':
                    pos = new Position(i, coordY);
                    ImageTile w = new Wall(pos);
                    readMapImages.add(w);
                    break;
                case 'h':
                    heroPos = new Position(i, coordY);
                    ImageTile f = new Floor(heroPos);
                    Hero h = new Hero(heroPos);
                    readMapImages.add(f);
                    break;
                case 'S':
                    pos = new Position(i, coordY);
                    Enemy skeleton = new Skeleton(pos);
                    f = new Floor(pos);
                    readMapImages.add(f);
                    readMapImages.add(skeleton);
                    readMapEnemies.add(skeleton);
                    break;
                case 'G':
                    pos = new Position(i, coordY);
                    Enemy badGuy = new BadGuy(pos);
                    f = new Floor(pos);
                    readMapImages.add(f);
                    readMapImages.add(badGuy);
                    readMapEnemies.add(badGuy);
                    break;
                case 'B':
                    pos = new Position(i, coordY);
                    Enemy bat = new Bat(pos);
                    f = new Floor(pos);
                    readMapImages.add(f);
                    readMapImages.add(bat);
                    readMapEnemies.add(bat);
                    break;
                case 'T':
                    pos = new Position(i, coordY);
                    Enemy thief = new Thief(pos);
                    f = new Floor(pos);
                    readMapImages.add(f);
                    readMapImages.add(thief);
                    readMapEnemies.add(thief);
                    break;
                case 's':
                    pos = new Position(i, coordY);
                    ImageTile sword = new Sword(pos);
                    f = new Floor(pos);
                    readMapImages.add(f);
                    readMapImages.add(sword);
                    break;
                case '0':
                    pos = new Position(i, coordY);
                    Door porta0 = listaPortas.get(0);
                    porta0.setPosition(pos);
                    f = new Floor(pos);
                    readMapImages.add(f);
                    readMapImages.add(porta0);
                    break;
                case '1':
                    pos = new Position(i, coordY);
                    Door porta1 = listaPortas.get(1);
                    porta1.setPosition(pos);
                    f = new Floor(pos);
                    readMapImages.add(f);
                    readMapImages.add(porta1);
                    break;
                case '2':
                    pos = new Position(i, coordY);
                    Door porta2 = listaPortas.get(2);
                    porta2.setPosition(pos);
                    f = new Floor(pos);
                    readMapImages.add(f);
                    readMapImages.add(porta2);
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
