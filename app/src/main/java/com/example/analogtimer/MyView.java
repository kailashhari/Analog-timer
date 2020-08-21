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

    float secNumRad, secCirRad, secHandRad, secCenRad, minNumRad, minHandRad, minCirRad, minCenRad, minElevation, recL, recB, recTP, textP, curTimeSize, textSize;


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
        secCirRad = (float)(getWidth()/2.3);
        secCenRad = (float)(getWidth()/50);
        secNumRad = (float)(secCirRad*0.89);
        secHandRad = (float)(getWidth()/3.4);
        minCirRad = (float)(getWidth()/6.5);
        minCenRad = (float)(getWidth()/70);
        minNumRad = (float)(minCirRad*0.85);
        minHandRad = (float)(minCirRad * 0.7);
        minElevation = (float)(secCirRad/2-20);
        recL = (float)(getWidth()/3);
        recB = (float)(getHeight()/11);
        recTP = (float)(getHeight()/3);
        textP = (float)(getHeight()/2.7);
        curTimeSize = (float)(getWidth()/15);
        textSize = (float)(getHeight()/18);
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
        paint.setTextSize(curTimeSize);
        paint.setColor(Color.BLACK);
        paint.getTextBounds(curTime, 0, curTime.length(), rect);
        int x =(int) (getWidth()/2)-rect.width()/2;
        int y =(int) (getHeight()/2)+rect.height()/2+ 100;
        canvas.drawText(curTime, x, y, paint);

        paint.reset();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawRect(getWidth()/2-200, getHeight()/2+recTP, getWidth()/2+200, getHeight()/2+recTP+recB, paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        paint.setTextSize(textSize);
        paint.getTextBounds("RESET", 0, "RESET".length(), rect);
        x =(int) (getWidth()/2)-rect.width()/2;
        y =(int) ((getHeight()/2)+rect.height()/2+ recTP+recB/2.3);
        canvas.drawText("RESET", x, y, paint);
        paint.getTextBounds("Tap to start and stop", 0, "Tap to start and stop".length(), rect);
        x =(int) (getWidth()/2)-rect.width()/2;
        y =(int) ((getHeight()/2)+rect.height()/2- textP);
        canvas.drawText("Tap to start and stop", x, y, paint);
    }



    private void drawMinCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth()/2, getHeight()/2-minElevation, minCirRad, paint);
    }

    private void drawMinCentre(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth()/2, getHeight()/2-minElevation, minCenRad, paint);
    }

    private void drawMinNumerals(Canvas canvas){

        paint.setTextSize(curTimeSize/2);
        for(int minute : minutes){
            String tmp = String.valueOf(minute);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = (2 * Math.PI/15) * minute;
            int x =(int) (getWidth()/2 + (Math.sin(angle) * minNumRad)-rect.width()/2);
            int y =(int) (getHeight()/2 - (Math.cos(angle) * minNumRad)+rect.height()/2);
            canvas.drawText(tmp, x, y-minElevation, paint);
        }
    }
    private void drawMinHand(Canvas canvas){

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        float curCurMin = curMin;
        double angle = (curCurMin * 2 * Math.PI)/15;
        int x = (int) (getWidth()/2 + minHandRad * Math.sin(angle));
        int y = (int) (getHeight()/2 - minHandRad * Math.cos(angle));
        canvas.drawLine(getWidth()/2, getHeight()/2-minElevation, x, y-minElevation, paint);
    }


    private void drawSecCircle(Canvas canvas){

        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(getWidth()/2, getHeight()/2, secCirRad, paint);

    }

    private void drawSecCentre(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth()/2, getHeight()/2, secCenRad, paint);

    }

    private void drawSecNumerals(Canvas canvas){

        paint.setTextSize(curTimeSize);
        for(int second : seconds){
            String tmp = String.valueOf(second);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = (Math.PI/15) * second;
            int x =(int) (getWidth()/2 + (Math.sin(angle) * secNumRad)-rect.width()/2);
            int y =(int) (getHeight()/2 - (Math.cos(angle) * secNumRad)+rect.height()/2);
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
        int x = (int) (getWidth()/2 + secHandRad * Math.sin(angle));
        int y = (int) (getHeight()/2 - secHandRad * Math.cos(angle));
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

        return ((x > (getWidth() / 2 - recL/2-10)) && (x < (getWidth() / 2 + recL/2+10))) && ((y > (getHeight() / 2 + recTP-10)) && (y < (getHeight() / 2 + recTP+ recB+10)));
    }

}
