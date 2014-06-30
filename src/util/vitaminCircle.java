package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import healthVT.vitamine.R;

import java.util.Map;

/**
 * Created by Jay on 6/28/14.
 */

public class vitaminCircle extends View {
    private int width, height, radius, color, left, top;
    private String vitamin;
    private double number;
    private Paint paint = new Paint();
    private int textSize, stroke1, stroke2, stroke3, stroke4;
    public vitaminCircle(Context context, int width, int height, int radius, int color, String vitamin, double number){
        super(context);
        this.textSize = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleText);
        this.stroke1 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke1);
        this.stroke2 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke2);
        this.stroke3 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke3);
        this.stroke4 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke4);

        this.width = width;
        this.height = height;
        this.radius = radius;
        this.color = color;

        this.vitamin = vitamin;
        this.number = number;


    }

    @Override
    protected void onMeasure(int height, int width){

        height = this.height;
        width = this.width;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int margin = 5;
        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke4);
        paint.setColor(this.color);

        canvas.drawCircle(radius + margin, radius + margin, radius, paint);
        canvas.drawLine(margin, radius+margin, (radius)*2+margin, radius+margin, paint);

        float vitaminWidth = paint.measureText(vitamin);

        paint.setStrokeWidth(stroke2);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textSize);

        Rect bounds = new Rect();

        paint.getTextBounds(vitamin, 0, 1, bounds);
        canvas.drawText(vitamin, radius-(vitaminWidth/2), ((radius*2)+bounds.height())/3, paint);

        float numberWidth = paint.measureText(String.valueOf(number));
        paint.getTextBounds(String.valueOf(number), 0, 1, bounds);

        float heightText = ((radius*2)+bounds.height())/3;
        canvas.drawText(String.valueOf(number), radius-(numberWidth/2)+margin, (heightText*2)+margin, paint);

    }

}
