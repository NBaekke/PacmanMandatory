package org.example.pacman;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    //reference to the main view
    GameView gameView;
    //reference to the game class.
    Game game;

    int speed = 5;
    int enemySpeed = 7;

    private Timer pacTimer;
    private Timer enemyTimer;
    private Timer realTimer;

    private int delay = 15;
    private TextView timeView;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        gameView =  findViewById(R.id.gameView);
        TextView pointsView = findViewById(R.id.points);
        TextView highscoreView = findViewById(R.id.high_score);
        timeView = findViewById(R.id.time);

        game = new Game(this, pointsView, highscoreView, timeView);
        game.setGameView(gameView);
        gameView.setGame(game);

        pacTimer = new Timer();
        enemyTimer = new Timer();
        realTimer = new Timer();

        game.newGame();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        checkSaved();

        editor.putInt("high_score", game.getHighscore());
        editor.apply();

        pacTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Move();
            }
        }, 0, delay);

        realTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                RealTime();
            }
        }, 0, 1000);

        enemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                RandomDir();
            }
        }, 250, 500);

        Button pauseBtn = findViewById(R.id.pauseButton);
        pauseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                game.setRunning(false);
                game.setIsPaused(true);
            }
        });

        Button contBtn = findViewById(R.id.continueButton);
        contBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(game.getIsPaused())
                {
                    game.setRunning(true);
                    game.setIsPaused(false);
                }

            }
        });

        Button buttonRight = findViewById(R.id.moveRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!game.getIsPaused())
                {
                    game.movePacmanRight();
                    game.setRunning(true);
                }
            }
        });


        Button buttonUp = findViewById(R.id.moveUp);
        buttonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!game.getIsPaused())
                {
                    game.movePacmanUp();
                    game.setRunning(true);
                }

            }
        });

        Button buttonLeft = findViewById(R.id.moveLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!game.getIsPaused())
                {
                    game.movePacmanLeft();
                    game.setRunning(true);
                }
            }
        });

        Button buttonDown = findViewById(R.id.moveDown);
        buttonDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!game.getIsPaused())
                {
                    game.movePacmanDown();
                    game.setRunning(true);
                }
            }
        });
    }

    private void checkSaved() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int highScore = sharedPref.getInt("high_score", 0);
        game.setHighscore(highScore);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        pacTimer.cancel();
        realTimer.cancel();
        enemyTimer.cancel();
    }

    private void Move()
    {
        this.runOnUiThread(MoveTicker);
    }

    private void RandomDir()
    {
        this.runOnUiThread(EnemyTicker);
    }

    private void RealTime()
    {
        this.runOnUiThread(TimeTicker);
    }

    private Runnable TimeTicker = new Runnable() {
        @Override
        public void run() {
            if (game.isRunning()) {
                game.setTime(game.getTime() + 1);
                timeView.setText(getText(R.string.time) + " " + game.getTime() + " sec");
            } else {
                game.setRunning(false);
                game.setIsPaused(true);
            }
        }
    };

    private Runnable EnemyTicker = new Runnable() {
        @Override
        public void run() {
            if (game.isRunning()) {
                for (Enemy enemy : game.getEnemies()) {
                    enemy.setDir(random.nextInt(5));
                }

            }
        }
    };

    private Runnable MoveTicker = new Runnable()
    {
        @Override
        public void run()
        {
            for (Enemy enemy : game.getEnemies()) {
                switch (enemy.getDir()) {
                    case 0:
                        if (game.isRunning()) {
                            if (enemy.getEnY() + enemySpeed > 0) {
                                enemy.setEnY(enemy.getEnY() - enemySpeed);
                                game.doCollisionCheck();
                                gameView.invalidate();
                            }
                        }
                        break;
                    case 1:
                        if (game.isRunning()) {
                            if (enemy.getEnX() + enemySpeed > 0) {
                                enemy.setEnX(enemy.getEnX() - enemySpeed);
                                game.doCollisionCheck();
                                gameView.invalidate();
                            }
                        }
                        break;
                    case 2:
                        if (game.isRunning()) {
                            if (enemy.getEnY() - enemySpeed + game.getEnemyBitmap().getWidth() < gameView.w) {
                                enemy.setEnY(enemy.getEnY() + enemySpeed);
                                game.doCollisionCheck();
                                gameView.invalidate();
                            }
                        }
                        break;
                    case 3:
                        if (game.isRunning()) {
                            if (enemy.getEnX() - enemySpeed + game.getEnemyBitmap().getWidth() < gameView.w) {
                                enemy.setEnX(enemy.getEnX() + enemySpeed);
                                game.doCollisionCheck();
                                gameView.invalidate();
                            }
                        }
                        break;
                }
            }
                switch (game.getCurDir()) {
                    case 0:
                        if (game.getPacy() + speed > 0 && game.isRunning()) {
                            game.setPacy(game.getPacy() - speed);
                            game.doCollisionCheck();
                            gameView.invalidate();
                        }
                        break;
                    case 1:
                        if (game.getPacx() + speed > 0 && game.isRunning()) {
                            game.setPacx(game.getPacx() - speed);
                            game.doCollisionCheck();
                            gameView.invalidate();
                        }
                        break;
                    case 2:
                        if (game.getPacy() + speed + game.getPacBitmap().getHeight() < gameView.h && game.isRunning()) {
                            game.setPacy(game.getPacy() + speed);
                            game.doCollisionCheck();
                            gameView.invalidate();
                        }
                        break;
                    case 3:
                        if (game.getPacx() + speed + game.getPacBitmap().getWidth() < gameView.w && game.isRunning()) {
                            game.setPacx(game.getPacx() + speed);
                            game.doCollisionCheck();
                            gameView.invalidate();
                        }
                        break;
                }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_newGame) {

            if(game.getPoints() > game.getHighscore()) {
                game.setHighscore(game.getPoints());
            }
            game.newGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
