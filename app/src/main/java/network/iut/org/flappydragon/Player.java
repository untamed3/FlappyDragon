package network.iut.org.flappydragon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;

public class Player {
    /** Static bitmap to reduce memory usage. */
    public static Bitmap globalBitmap;
    private final Bitmap bitmap;
    private final byte frameTime;
    private SurfaceHolder holder;
    private int frameTimeCounter;
    private final int width;
    private final int height;
    private int x;
    private int y;
    private float speedX;
    private float speedY;
    private GameView view;
    private Context context;

    public Player(Context context, GameView view) {

        this.context = context;

        int height = context.getResources().getDisplayMetrics().heightPixels;
        int width = context.getResources().getDisplayMetrics().widthPixels;

        if(globalBitmap == null) {
            Log.e("TEST", "Height : " + height + ", width : " + width);
            globalBitmap = Util.decodeSampledBitmapFromResource(context.getResources(), R.drawable.frame1, Float.valueOf(height / 10f).intValue(), Float.valueOf(width / 10f).intValue());
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
        this.frameTime = 1;		// the frame will change every 3 runs
        this.y = context.getResources().getDisplayMetrics().heightPixels - 300;	// Startposition in the middle of the screen

        this.view = view;
        this.x = this.width / 6;
        this.speedX = 0;
    }

    public void onTap() {

        this.speedY = getTabSpeed();
        this.y += getPosTabIncrease();
    }

    private float getPosTabIncrease() {
        return - view.getHeight() / 100;
    }

    private float getTabSpeed() {
        return -view.getHeight() / 16f;
    }

    public void move() {
        changeToNextFrame();

        this.speedX = 0;
        if(speedY < 0){
            // The character is moving up
            Log.i("Move", "Moving up");
            speedY = speedY * 2 / 3 + getSpeedTimeDecrease() / 2;
        }else{
            // the character is moving down
            if(this.y < context.getResources().getDisplayMetrics().heightPixels - 300){
                this.speedY += getSpeedTimeDecrease();
                Log.i("Move", "Moving down");
            }
            else{
                this.speedY = 0;
                this.speedX = 0;
            }
        }
        if(this.speedY > getMaxSpeed()){
            // speed limit
            this.speedY = getMaxSpeed();
        }

        // manage frames
/*        if(row != 3){
            // not dead
            if(speedY > getTabSpeed() / 3 && speedY < getMaxSpeed() * 1/3){
                row = 0;
            }else if(speedY > 0){
                row = 1;
            }else{
                row = 2;
            }
        }
*/
        this.x += speedX;
        this.y += speedY;
    }

    protected void changeToNextFrame(){
        this.frameTimeCounter++;
        if(this.frameTimeCounter >= this.frameTime){
            //TODO Change frame
            this.frameTimeCounter = 0;
        }
    }

    private float getSpeedTimeDecrease() {
        return view.getHeight() / 320;
    }

    public int getY(){
        return this.y;
    }

    private float getMaxSpeed() {
        return view.getHeight() / 51.2f;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y , null);
    }
}
