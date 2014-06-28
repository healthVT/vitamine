package util;

import android.content.res.Resources;
import android.graphics.*;
import android.util.Log;
import android.view.SurfaceHolder;
import healthVT.vitamine.R;

/**
 * Created by Jay on 6/18/14.
 */
public class CircleAnimThread extends Thread {
    private int mCanvasHeight, mjCanvasWidth;
    private SurfaceHolder holder;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int refreshRate = 50;
    private int loadingColor = Color.parseColor("#E83F3F");
    private boolean allFinished = false;
    int x;
    private Bitmap picture;
    private Circle[] circleArray;
    private Matrix matrix;

    public CircleAnimThread(SurfaceHolder holder, Resources resources){
        this.holder = holder;

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
    }

    @Override
    public void run(){

        Canvas canvas = null;
        x = -90;
        circleArray = new Circle[2];

        circleArray[0] = new Circle(3, 3, mjCanvasWidth-4, 0, loadingColor);

        while(!allFinished){
            try{
                canvas = holder.lockCanvas();
                if(canvas == null){
                    return;
                }
                synchronized (canvas) {
                    //canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                    //canvas.drawBitmap(picture, matrix, paint);
                    draw(canvas);
                }

            }catch(Exception e){
                Log.e("Synchronized exception", "Exception", e);
            }finally{
                if(canvas != null){
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            try{
                Thread.sleep(refreshRate);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void draw(Canvas canvas){


        for(int i=0;i<circleArray.length;i++){
            if(circleArray[i] == null){
                break;
            }
            //canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            paint.setColor(circleArray[i].getColor());
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            RectF rec = new RectF(circleArray[i].x, circleArray[i].y, circleArray[i].radius, circleArray[i].radius);
            canvas.drawOval(rec, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(circleArray[i].getReverseColor());
            //RectF rec;
            if(circleArray[i].reverse){
                rec = new RectF(circleArray[i].x+2, circleArray[i].y+2, circleArray[i].radius-2, circleArray[i].radius-2);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            }else{
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                rec = new RectF(circleArray[i].x, circleArray[i].y, circleArray[i].radius, circleArray[i].radius);
            }

            canvas.drawArc(rec, x, circleArray[i].tempY, true, paint);

            if(circleArray[i].angle == 0){
                if(circleArray[i].tempY < 370){
                    circleArray[i].tempY += 5;
                }else{
                    if(circleArray[i].reverse){
                        circleArray[i].reverse = false;
                    }else{
                        circleArray[i].reverse = true;
                    }

                    circleArray[i].tempY = 0;
                }
            }else{
                if(circleArray[i].angle >= circleArray[i].tempY){
                    circleArray[i].tempY+=5;
                }
            }

            if(circleArray[i].angle != 0 && (circleArray[i].angle <= circleArray[i].tempY)){
                circleArray[i].finished = true;
            }
        }

        for(int i=0;i<circleArray.length-1;i++){
            if(!circleArray[i].finished){

                allFinished = false;
                break;
            }else{
                allFinished = true;
            }
        }


    }

    public boolean isFinished(){
        return allFinished;
    }

    public void setSurfaceSize(int width, int height){
        synchronized (holder){
            mCanvasHeight = height;
            mjCanvasWidth = width;
        }
    }

    private class Circle{
        private int x,y,radius,color, angle, tempY = 0;
        private int transparent = Color.TRANSPARENT;
        private boolean finished = false;
        private boolean reverse = false;


        Circle(int x, int y, int radius, int angle, int color){
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.angle = angle;
            this.color = color;
        }

        public int getColor(){
            return color;
        }

        public int getReverseColor(){
            if(reverse){
                return this.transparent;
            }else{
                return this.color;
            }
        }

    }
}
