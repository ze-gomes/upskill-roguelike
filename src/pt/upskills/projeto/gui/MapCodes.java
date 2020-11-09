package pt.upskills.projeto.gui;

public enum MapCodes {
    // Crio o enumerador para cada carta com os respectivos pontos
    Wall('W'), DoorOpen('0'), Hero('h');
    private char codigo;


    //enum tem sempre constructor privado
    private MapCodes(char codigo) {
        this.codigo  = codigo;
    }

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }


    //Getter
    public char getCodigo() {
        return codigo;
    }

}
