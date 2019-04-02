package org.example.pacman;

public class Enemy {
    private int enX, enY;
    private boolean isAlive = true;
    private int dir;

    public Enemy(int enX, int enY)
    {
        this.enX = enX;
        this.enY = enY;
    }

    public int getEnX() {
        return enX;
    }
    public int getEnY(){
        return enY;
    }

    public int setEnX(int value){
        return enX = value;
    }
    public int setEnY(int value){
        return enY = value;
    }

    public boolean isAlive(){
        return isAlive;
    }
    public boolean SetIsAlive(boolean value){
        return isAlive = value;
    }

    public int getDir(){
        return dir;
    }
    public int setDir(int value){
        return dir = value;
    }
}
