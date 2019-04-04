package org.example.pacman;

public class Enemy {
    private int enx, eny;
    private boolean isAlive = true;
    private int dir;

    public Enemy(int enx, int eny)
    {
        this.enx = enx;
        this.eny = eny;
    }

    public int getEnx() {
        return enx;
    }
    public int getEny(){
        return eny;
    }

    public int setEnx(int value){
        return enx = value;
    }
    public int setEny(int value){
        return eny = value;
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
