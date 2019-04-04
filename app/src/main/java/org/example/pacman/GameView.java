package org.example.pacman;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GameView extends View {

	Game game;
    int h,w; //used for storing our height and width of the view

	public void setGame(Game game)
	{
		this.game = game;
	}

	public GameView(Context context) {
		super(context);
	}

	public GameView(Context context, AttributeSet attrs) {
	    super(context,attrs);
	}

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context,attrs,defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//Here we get the height and weight
		h = canvas.getHeight();
		w = canvas.getWidth();

        if (!game.CoinsInit()) {
            game.initCoins();
            game.setCoinsInit(true);
        }

        if (!game.EnemiesInit()) {
            game.initEnemies();
            game.setEnemiesInit(true);
        }

		game.setSize(h,w);
		Log.d("GAMEVIEW","h = "+h+", w = "+w);

		Paint paint = new Paint();
		canvas.drawColor(Color.WHITE);

		canvas.drawBitmap(game.getPacBitmap(), game.getPacx(),game.getPacy(), paint);

        for (GoldCoin coin : game.getCoins()) {
            if (!coin.isTaken()) {
                canvas.drawBitmap(game.getCoinBitmap(), coin.getCoinX(), coin.getCoinY(), paint);
            }
        }

        for (Enemy enemy : game.getEnemies()) {
            if (enemy.isAlive()) {
                canvas.drawBitmap(game.getEnemyBitmap(), enemy.getEnx(), enemy.getEny(), paint);
            }
        }

        if(game.getGameOver()) {
            game.newGame();
        }

		super.onDraw(canvas);
	}

}
