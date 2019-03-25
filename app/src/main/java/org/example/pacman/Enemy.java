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

    public int getEnemyX() {
        return enX;
    }

    public int getEnemyY(){
        return enY;
    }

    public int setEnemyX(int value){
        return enX = value;
    }

    public int setEnemyY(int value){
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
