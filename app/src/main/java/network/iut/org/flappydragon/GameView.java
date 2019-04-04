package network.iut.org.flappydragon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends SurfaceView implements Runnable {
    public static final long UPDATE_INTERVAL = 50; // = 20 FPS
    private SurfaceHolder holder;
    private boolean paused = true;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Player player;
    private Background background;
    private ArrayList<Pizza> pizzas;

    public GameView(Context context) {
        super(context);
        player = new Player(context, this);
        background = new Background(context);
        pizzas = new ArrayList<Pizza>();
        holder = getHolder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GameView.this.run();
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();

        //Log.i("X", event.getX() + "");
        //Log.i("Y", event.getY() + "");
        if (event.getX() < 1850 & event.getX() > 1750 & event.getY() < 950 & event.getY() > 850  & event.getAction() == MotionEvent.ACTION_DOWN) {
            createPizza();
        }

        if (event.getX() < 1850 & event.getX() > 1750 & event.getY() < 150 & event.getY() > 50) {
            paused = true;
            stopTimer();
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(paused) {
                resume();
            } else {
                Log.i("PLAYER", "PLAYER TAPPED");
                this.player.onTap();
            }
        }
        return true;
    }

    private void createPizza(){
        Pizza pizza = new Pizza(getContext(), this.player.getY());
        this.pizzas.add(pizza);
        Log.i("pizzas", pizzas + "");
    }

    private void resume() {
        paused = false;
        startTimer();
    }

    private void startTimer() {
        Log.i("TIMER", "START TIMER");
        setUpTimerTask();
        timer = new Timer();
        timer.schedule(timerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    private void setUpTimerTask() {
        stopTimer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                GameView.this.run();
            }
        };
    }

    @Override
    public void run() {
        player.move();
        if (pizzas != null){
            for (Pizza pizza : this.pizzas){
                pizza.move();
            }
        }
        draw();
    }

    private void draw() {
        while(!holder.getSurface().isValid()){
            /*wait*/
            try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            drawCanvas(canvas);
        }
        try {
            holder.unlockCanvasAndPost(canvas);
        } catch (IllegalStateException e) {
            // Already unlocked
        }
    }

    private void drawCanvas(Canvas canvas) {
        background.draw(canvas);
        player.draw(canvas);

        if (pizzas != null) {
            for (Pizza pizza : this.pizzas){
                pizza.draw(canvas);
            }
        }
        if (paused) {
            canvas.drawText("PAUSED", canvas.getWidth() / 2, canvas.getHeight() / 2, new Paint());
        }
    }

}
