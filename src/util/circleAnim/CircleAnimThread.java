package util.circleAnim;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import beans.VitaminBean;
import healthVT.vitamine.R;
import util.tools;
import util.vitaminCircle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jay on 6/18/14.
 */
public class CircleAnimThread extends Thread {
    private int circleHeight, circleWidth, circleRadius, textSize;
    private SurfaceHolder holder;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int refreshRate = 10, rate = 3;
    private boolean allFinished = false;
    int x, stroke1, stroke2, stroke3, stroke4;
    int screenWidth, screenHeight;
    private int canvasWidth = 0;

    private List<Circle> circleArray;

    public CircleAnimThread(SurfaceHolder holder, Context context, Resources resources, VitaminBean vitamin) {
        this.holder = holder;

        circleHeight = resources.getDimensionPixelSize(R.dimen.circleAnimHeight);
        circleWidth = resources.getDimensionPixelSize(R.dimen.circleAnimWidth);
        circleRadius = resources.getDimensionPixelSize(R.dimen.circleAnimRadius);
        stroke1 = resources.getDimensionPixelSize(R.dimen.vitaminCircleStroke1);
        stroke2 = resources.getDimensionPixelSize(R.dimen.vitaminCircleStroke2);
        stroke3 = resources.getDimensionPixelSize(R.dimen.vitaminCircleStroke3);
        stroke4 = resources.getDimensionPixelSize(R.dimen.vitaminCircleStroke4);
        textSize = resources.getDimensionPixelSize(R.dimen.circleAnimText);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenHeight = size.y;
        screenWidth = size.x;

        circleArray = new ArrayList<Circle>();
        Iterator it = vitamin.getVitaminMap().entrySet().iterator();
        int tempX, tempY, times = 1;
        tempX =circleRadius;
        tempY = circleRadius / 2 ;
        while (it.hasNext()) {
            Map.Entry<String, Double> each = (Map.Entry<String, Double>) it.next();
            circleArray.add(new Circle(tempX, tempY, circleRadius, each.getValue(), tools.getVitaminColor(each.getKey()), each.getKey()));
            tempX += circleRadius*3;
            if((tempX + circleWidth) > screenWidth){
                if(canvasWidth < (tempX + circleRadius + circleWidth)){
                    canvasWidth = tempX;
                }

                times++;
                tempX = circleRadius;
                tempY = tempY + circleRadius*3;

            }
            it.remove();
        }
    }

    @Override
    public void run() {

        Canvas canvas = null;
        x = -90;
        paint.setAntiAlias(true);

        while (!allFinished) {

            try {
                canvas = holder.lockCanvas();
                synchronized (canvas) {
                    draw(canvas);
                }

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
        }
    }

    private void draw(Canvas canvas) {
        for (Circle circle: circleArray) {
            paint.setColor(circle.getColor());
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke2);
            RectF rec = new RectF(circle.x, circle.y, circle.x + circle.radius*2, circle.y + circle.radius*2);
            canvas.drawOval(rec, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(circle.getColor());

            rec = new RectF(circle.x, circle.y, circle.x + circle.radius*2, circle.y + circle.radius*2);
            canvas.drawArc(rec, x, circle.tempY, true, paint);

            float textWidth = paint.measureText(circle.getText());
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            canvas.drawText(circle.getText(), circle.x + circleWidth/2 - textWidth/2, circle.y + circleHeight, paint);

            if (circle.angle >= circle.tempY && !circle.finished) {
                circle.tempY += rate;
            }

            if (circle.angle == 0 || circle.angle <= circle.tempY) {
                circle.finished = true;
            }
        }

        for (Circle circle: circleArray) {
            if (!circle.finished) {
                allFinished = false;
                break;
            } else {
                allFinished = true;
            }
        }


    }

    public boolean isFinished() {
        return allFinished;
    }

    public int getWidth(){
        return canvasWidth;
    }

    private class Circle {
        private int x, y, radius, color, angle, tempY = 0;
        private boolean finished = false;
        private boolean reverse = false;
        private String vitaminName;


        Circle(int x, int y, int radius, Double angle, int color, String vitaminName) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.angle = angle.intValue();
            this.color = color;
            this.vitaminName = vitaminName;
        }

        public String getText(){
            return vitaminName + " " + angle + "%";
        }

        public int getColor() {
            return color;
        }

        public String getVitaminName(){
            return vitaminName;
        }

    }
}
