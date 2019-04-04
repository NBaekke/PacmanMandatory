package org.example.pacman;

public class GoldCoin {
    private int coinx, coiny;
    private boolean isTaken = false;
    private final int value = 10;

    public GoldCoin(int coinx, int coiny)
    {
        this.coinx = coinx;
        this.coiny = coiny;
    }

    public int getCoinX()
    {
        return coinx;
    }
    public int getCoinY()
    {
        return coiny;
    }

    public boolean isTaken()
    {
        return isTaken;
    }
    public boolean SetIsTaken(boolean value)
    {
        return isTaken = value;
    }

    public int getValue()
    {
        return value;
    }
}
