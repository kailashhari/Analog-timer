package com.example.analogtimer;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.os.Build;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.Timer;
import java.util.TimerTask;

public class MyView extends View {

    Canvas canvas;
    Paint paint;

    private int mBackgroundColor = Color.WHITE;
    private int mDrawColor = Color.BLACK;
    boolean timerRunning = false;

    String curTime = "0 : 0";
    boolean hasTimerEnded = false;

    private int[] seconds = {30,2,4,6,8,10,12,14,16,18,20,22, 24, 26, 28};
    private int[] minutes = {15, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
    Timer timer;
    boolean isTimerRunning = false;

    private Rect rect = new Rect();
    float curMin = 0, curSec =0;


    public MyView(Context context){

        super(context);
        paint = new Paint();
        paint.setColor(mDrawColor);
        paint.setStyle(Paint.Style.STROKE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvas = new Canvas();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        drawSecCircle(canvas);
        drawSecCentre(canvas);
        drawSecNumerals(canvas);
        drawSecHand(canvas);
        drawMinCircle(canvas);
        drawMinCentre(canvas);
        drawMinNumerals(canvas);
        drawMinHand(canvas);

        paint.reset();
        paint.setTextSize(100);
        paint.setColor(Color.BLACK);
        paint.getTextBounds(curTime, 0, curTime.length(), rect);
        int x =(int) (getWidth()/2)-rect.width()/2;
        int y =(int) (getHeight()/2)+rect.height()/2+ 100;
        canvas.drawText(curTime, x, y, paint);

        paint.reset();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawRect(getWidth()/2-200, getHeight()/2+700, getWidth()/2+200, getHeight()/2+850, paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        paint.setTextSize(100);
        paint.getTextBounds("RESET", 0, "RESET".length(), rect);
        x =(int) (getWidth()/2)-rect.width()/2;
        y =(int) (getHeight()/2)+rect.height()/2+ 775;
        canvas.drawText("RESET", x, y, paint);
        paint.getTextBounds("Tap to start and stop", 0, "Tap to start and stop".length(), rect);
        x =(int) (getWidth()/2)-rect.width()/2;
        y =(int) (getHeight()/2)+rect.height()/2- 730;
        canvas.drawText("Tap to start and stop", x, y, paint);
    }



    private void drawMinCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth()/2, getHeight()/2-215, 180, paint);
    }

    private void drawMinCentre(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth()/2, getHeight()/2-215, 10, paint);
    }

    private void drawMinNumerals(Canvas canvas){

        paint.setTextSize(30);
        for(int minute : minutes){
            String tmp = String.valueOf(minute);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = (2 * Math.PI/15) * minute;
            int x =(int) (getWidth()/2 + (Math.sin(angle) * 150)-rect.width()/2);
            int y =(int) (getHeight()/2 - (Math.cos(angle) * 150)+rect.height()/2);
            canvas.drawText(tmp, x, y-215, paint);
        }
    }
    private void drawMinHand(Canvas canvas){

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        float curCurMin = curMin;
        double angle = (curCurMin * 2 * Math.PI)/15;
        int x = (int) (getWidth()/2 + 120 * Math.sin(angle));
        int y = (int) (getHeight()/2 - 120 * Math.cos(angle));
        canvas.drawLine(getWidth()/2, getHeight()/2-215, x, y-215, paint);
    }


    private void drawSecCircle(Canvas canvas){

        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth()/2, getHeight()/2, 500, paint);

    }

    private void drawSecCentre(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth()/2, getHeight()/2, 30, paint);

    }

    private void drawSecNumerals(Canvas canvas){

        paint.setTextSize(80);
        for(int second : seconds){
            String tmp = String.valueOf(second);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = (Math.PI/15) * second;
            int x =(int) (getWidth()/2 + (Math.sin(angle) * 430)-rect.width()/2);
            int y =(int) (getHeight()/2 - (Math.cos(angle) * 430)+rect.height()/2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    private void drawSecHand(Canvas canvas){

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        float curCurSec = curSec;
        if(curSec>30){
            curCurSec = curSec-30;

        }
        double angle = (curCurSec * Math.PI)/15;
        int x = (int) (getWidth()/2 + 300 * Math.sin(angle));
        int y = (int) (getHeight()/2 - 300 * Math.cos(angle));
        canvas.drawLine(getWidth()/2, getHeight()/2, x, y, paint);

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(isReset(event.getX(), event.getY())){
                timer.cancel();
                isTimerRunning = false;
                hasTimerEnded = true;
                curMin = 0;
                curSec = 0;
                curTime = "0 : 0";

            }
            else
                if(isTimerRunning == false){
                    timer = new Timer();
                    timer.schedule(new MYTask(), 0, 100);
                    isTimerRunning = true;
                    hasTimerEnded = false;
                }
                else{
                    isTimerRunning = false;
                    hasTimerEnded = true;
                    timer.cancel();
                }



        }

        invalidate();

        return true;
    }

    class MYTask extends TimerTask{


        @Override
        public void run() {
            if (curSec == 30)
                curSec = (float) 0.1;
            else {
                curSec = curSec + (float)0.1;
            curMin = curMin+(float)((0.1)/60);
            if (curMin == 15)
                curMin = (float)((0.1)/60);
            }
            curTime = (int) curMin + " : " + (int) curSec%60;
            invalidate();
        }
    }
    private boolean isReset(float x, float y){

        return ((x > (getWidth() / 2 - 220)) && (x < (getWidth() / 2 + 220))) && ((y > (getHeight() / 2 + 680)) && (y < (getHeight() / 2 + 870)));
    }

}
