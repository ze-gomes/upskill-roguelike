package pt.upskills.projeto.rogue.utils;

import java.util.Random;

public class Vector2D {

    private int x;
    private int y;

    public Vector2D(int i, int j) {
        x = i;
        y = j;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2D plus(Vector2D vector2d) {
        return null;
    }

    public Vector2D minus(Vector2D vector2d) {
        return new Vector2D(getX()-vector2d.getX(), getY()-vector2d.getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector2D other = (Vector2D) obj;
        if (x != other.getX())
            return false;
        if (y != other.getY())
            return false;
        return true;
    }


}
