package org.example.pacman;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Random;
/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private int points; //how points do we have

    //bitmap of the pacman
    private Bitmap pacBitmap;
    private Bitmap coinBitmap;
    private Bitmap enemyBitmap;

    private TextView pointsView; //textview reference to points

    private int pacx, pacy;

    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private GameView gameView; //a reference to the gameview
    private int h,w; //height and width of screen

    private Random random = new Random();

    private int amountCoins = 10;
    private boolean coinsInit = false;

    private boolean enemiesInit = false;

    private boolean gameOver = false;

    public Game(Context context, TextView view) {
        this.context = context;
        this.pointsView = view;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    //TODO initialize goldcoins also here
    public void newGame() {
        pacx = 500;
        pacy = 400; //just some starting coordinates
        //reset the points
        points = 0;

        coinsInit = false;
        enemiesInit = false;

        gameOver = false;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);

        for (GoldCoin coin : coins) {
            coin.SetIsTaken(true);
        }

        for (Enemy enemy : enemies) {
            enemy.SetIsAlive(false);
        }



        gameView.invalidate(); //redraw screen
    }

    public void initCoins() {
        for (int i = 0; i < amountCoins; i++)
        {
            coins.add(new GoldCoin(
                    random.nextInt(gameView.w - coinBitmap.getWidth()),
                    random.nextInt(gameView.h - coinBitmap.getHeight())));
        }
    }

    public void initEnemies() {
        for (int i = 0; i < 2; i++)
        {
            enemies.add(new Enemy(
                    random.nextInt(gameView.w - enemyBitmap.getWidth()),
                    random.nextInt(gameView.h - enemyBitmap.getHeight())));
        }
    }

    public void setSize(int h, int w) {
        this.h = h;
        this.w = w;
    }

    public void movePacmanRight(int pixels) {
        //still within our boundaries?
        if (pacx+pixels+pacBitmap.getWidth()<w) {
            pacx = pacx + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanUp(int pixels) {
        //still within our boundaries?
        if (pacy-pixels>0) {
            pacy = pacy - pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanLeft(int pixels) {
        //still within our boundaries?
        if (pacx-pixels>0) {
            pacx = pacx - pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanDown(int pixels) {
        //still within our boundaries?
        if (pacy+pixels+pacBitmap.getHeight()<h) {
            pacy = pacy + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public void doCollisionCheck() {
        for (GoldCoin coin : coins) {
            int lx = coin.getCoinx()-pacx;
            int ly = coin.getCoiny()-pacy;
            double l = Math.sqrt((lx*lx)+(ly*ly));
            int r1 = pacBitmap.getHeight()+pacBitmap.getWidth()-180;
            int r2 = coinBitmap.getHeight()+coinBitmap.getWidth()-180;
            if (l <= r1+r2 && !coin.isTaken() )
            {
                coin.SetIsTaken(true);
                points += coin.getValue();
                pointsView.setText(context.getText(R.string.points) +" "+ points);
                if (points == 100)
                {
                    gameOver = true;
                    GameOver(true);
                }
            }
        }

        for (Enemy enemy : enemies)
        {
            int lx = enemy.getEnemyX()-pacx;
            int ly = enemy.getEnemyY()-pacy;
            double l = Math.sqrt((lx*lx)+(ly*ly));
            int r1 = pacBitmap.getHeight()+pacBitmap.getWidth()-220;
            int r2 = enemyBitmap.getHeight()+enemyBitmap.getWidth()-220;

            if (l <= r1+r2 && enemy.isAlive()) {
                gameOver = true;
                GameOver(false);
            }
        }
    }

    public void GameOver(boolean victory) {
        Toast toast;
        if (victory) {
            toast = Toast.makeText(context, "You win!" + " Points: " + points, Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            toast = Toast.makeText(context, "Game over!" +  " Points: " + points, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public int getPacx() {
        return pacx;
    }
    public int getPacy() {
        return pacy;
    }

    public ArrayList<GoldCoin> getCoins() {
        return coins;
    }
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public Bitmap getPacBitmap() {
        return pacBitmap;
    }
    public Bitmap getCoinBitmap() {
        return coinBitmap;
    }
    public Bitmap getEnemyBitmap(){
        return enemyBitmap;
    }

    public boolean CoinsInit() {
        return coinsInit;
    }
    public boolean setCoinsInit(boolean value) {
        return coinsInit = value;
    }

    public boolean EnemiesInit() {
        return enemiesInit;
    }
    public boolean setEnemiesInit(boolean value) {
        return enemiesInit = value;
    }

    public boolean getGameOver() {
        return gameOver;
    }
}