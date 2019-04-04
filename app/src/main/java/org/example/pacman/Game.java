package org.example.pacman;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.view.View;
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
    private int highScore = 0;

    //bitmap of the pacman
    private Bitmap pacBitmap;
    private Bitmap coinBitmap;
    private Bitmap enemyBitmap;

    private TextView pointsView; //textview reference to points
    private TextView highScoreView;
    private TextView timeView;

    private int pacx, pacy;
    private int up = 0;
    private int left = 1;
    private int down = 2;
    private int right = 3;
    private int curDir;

    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private GameView gameView; //a reference to the gameview
    private int h,w; //height and width of screen

    private Random random = new Random();

    private int amountCoins;
    private boolean coinsInit = false;

    private boolean enemiesInit = false;

    private int time;

    private boolean running = false;
    private boolean isPaused = false;
    private boolean gameOver;

    public Game(Context context, TextView pointsView, TextView highScoreView, TextView timeView) {
        this.context = context;
        this.pointsView = pointsView;
        this.highScoreView = highScoreView;
        this.timeView = timeView;
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
        time = 0;

        coinsInit = false;
        amountCoins = 10;
        enemiesInit = false;

        running = false;
        isPaused = false;
        gameOver = false;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+ points);
        highScoreView.setText(context.getResources().getString(R.string.high_score)+" "+ highScore);
        timeView.setText(context.getResources().getString(R.string.time) + " " + time);

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

    public void movePacmanRight()
    {
        curDir = right;
    }
    public void movePacmanLeft()
    {
        curDir = left;
    }
    public void movePacmanUp()
    {
        curDir = up;
    }
    public void movePacmanDown() {curDir = down;}

    public void doCollisionCheck() {
        for (GoldCoin coin : coins) {
            int lx = coin.getCoinX() - pacx;
            int ly = coin.getCoinY() - pacy;
            double l = Math.sqrt((lx * lx) + (ly * ly));
            int r1 = pacBitmap.getHeight() + pacBitmap.getWidth() - 180;
            int r2 = coinBitmap.getHeight() + coinBitmap.getWidth() - 150;
            if (l <= r1 + r2 && !coin.isTaken() )
            {
                coin.SetIsTaken(true);
                amountCoins -= 1;
                points += coin.getValue();
                pointsView.setText(context.getText(R.string.points) +" "+ points);
            }
        }

        if (amountCoins == 5) {
            amountCoins += 1;
            coins.add(new GoldCoin(
                    random.nextInt(gameView.w - coinBitmap.getWidth()),
                    random.nextInt(gameView.h - coinBitmap.getHeight())));
        }

        for (Enemy enemy : enemies)
        {
            int lx = enemy.getEnX() - pacx;
            int ly = enemy.getEnY() - pacy;
            double l = Math.sqrt((lx * lx) + (ly * ly));
            int r1 = pacBitmap.getHeight() + pacBitmap.getWidth() - 220;
            int r2 = enemyBitmap.getHeight() + enemyBitmap.getWidth() - 220;
            if (l <= r1 + r2 && enemy.isAlive()) {
                gameOver = true;
                GameOver(false);
            }
        }

        Enemy addNewEne = new Enemy(
                random.nextInt(gameView.w - enemyBitmap.getWidth()),
                random.nextInt(gameView.h - enemyBitmap.getHeight()));

        if (points == 100) {
            points = 25 + points;
            enemies.add(addNewEne);
            pointsView.setText(context.getText(R.string.points) +" "+ points);
        } else if (points == 225) {
            points = 25 + points;
            enemies.add(addNewEne);
            pointsView.setText(context.getText(R.string.points) +" "+ points);
        } else if (points == 350) {
            points = 25 + points;
            enemies.add(addNewEne);
            pointsView.setText(context.getText(R.string.points) +" "+ points);
        } else if (points == 475) {
            points = 25 + points;
            enemies.add(addNewEne);
            pointsView.setText(context.getText(R.string.points) +" "+ points);
        } else if (points == 600) {
            points = 25 + points;
            enemies.add(addNewEne);
            pointsView.setText(context.getText(R.string.points) +" "+ points);
        }
    }

    public void GameOver(boolean gameOver) {
        Toast toast;
        if (gameOver) {
            toast = Toast.makeText(context, "You survived for: " + time + " seconds. Points: " + points, Toast.LENGTH_LONG);
            toast.show();
        }

        if (points > highScore) {
            setHighscore(points);
        }
    }

    public int getPacx() {
        return pacx;
    }
    public int setPacx(int value){return pacx = value;}
    public int getPacy()
    {
        return pacy;
    }
    public int setPacy(int value){return pacy = value;}

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

    public int getPoints() {
        return points;
    }

    public int getHighscore() {
        return highScore;
    }

    public int setHighscore(int value){return highScore = value;}

    public int getCurDir(){return curDir;}
    public int getTime(){return time;}
    public int setTime(int value){return time = value;}

    public boolean isRunning(){return running;}
    public boolean setRunning(boolean value) {return running = value;}
    public boolean getIsPaused(){return isPaused;}
    public boolean setIsPaused(boolean value) {return isPaused = value;}
    public boolean getGameOver() {
        return gameOver;
    }
}