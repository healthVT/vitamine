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
    private boolean allFinished = false, pause = false;
    private int x;
    private Circle circleArray;
    private int startSize;


    public CircleAnimThread(SurfaceHolder holder, Resources resources){
        this.holder = holder;

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        startSize = resources.getDimensionPixelSize(R.dimen.dpFront);

        Canvas canvas = holder.lockCanvas();
        if(canvas != null){
            drawStartCircle(canvas);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    public void drawStartCircle(Canvas canvas){
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        paint.setColor(loadingColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(canvas.getWidth() >> 1, canvas.getWidth() >> 1, (canvas.getWidth() - 4) >> 1, paint);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
        paint.setTextSize(startSize);
        Rect bounds = new Rect();
        paint.getTextBounds("Start", 0, 1, bounds);
        float startWidth = paint.measureText("Start");
        canvas.drawText("Start", (canvas.getWidth()>>1) - (startWidth/2) - 2, (canvas.getWidth() + bounds.height())>>1, paint);
    }

    @Override
    public void run(){

        Canvas canvas = null;
        x = -90;

        circleArray = new Circle(3, 3, mjCanvasWidth-4, 0, loadingColor);

        while(!allFinished){
            if (!pause) {
                try {

                    canvas = holder.lockCanvas();

                    if (canvas == null) {
                        return;
                    }
                    synchronized (canvas) {
                        //canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                        //canvas.drawBitmap(picture, matrix, paint);
                        draw(canvas);
                    }

                } catch (Exception e) {
                    Log.e("Synchronized exception", "Exception", e);
                    this.interrupt();
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }

                try {
                    Thread.sleep(refreshRate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{

            }

//        canvas = holder.lockCanvas();
//        if(canvas != null){
//            drawStartCircle(canvas);
//        }
//        holder.unlockCanvasAndPost(canvas);
//
//        this.interrupt();
        }
    }

    private void draw(Canvas canvas){


            if(circleArray == null){
                return;
            }
            //canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            paint.setColor(circleArray.getColor());
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            RectF rec = new RectF(circleArray.x, circleArray.y, circleArray.radius, circleArray.radius);
            canvas.drawOval(rec, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(circleArray.getReverseColor());
            //RectF rec;
            if(circleArray.reverse){
                drawStartCircle(canvas);

                rec = new RectF(circleArray.x+2, circleArray.y+2, circleArray.radius-2, circleArray.radius-2);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            }else{
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                rec = new RectF(circleArray.x, circleArray.y, circleArray.radius, circleArray.radius);
            }

            canvas.drawArc(rec, x, circleArray.tempY, true, paint);

            if(circleArray.angle == 0){
                if(circleArray.tempY < 370){
                    circleArray.tempY += 5;
                }else{
                    if(circleArray.reverse){
                        circleArray.reverse = false;
                    }else{
                        circleArray.reverse = true;
                    }

                    circleArray.tempY = 0;
                }
            }else{
                if(circleArray.angle >= circleArray.tempY){
                    circleArray.tempY+=5;
                }
            }

            if(circleArray.angle != 0 && (circleArray.angle <= circleArray.tempY)){
                circleArray.finished = true;
            }

            if(!circleArray.finished){

                allFinished = false;
            }else{
                allFinished = true;
            }


    }

    public boolean isFinished(){
        return allFinished;
    }

    public void setFinish() { allFinished = true;}

    protected void onPause(){
        pause = true;
        Canvas canvas = holder.lockCanvas();
        drawStartCircle(canvas);
        holder.unlockCanvasAndPost(canvas);

        circleArray.tempY = 0;
    }

    protected void onResume(){
        pause = false;
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
        private boolean reverse = true;


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
