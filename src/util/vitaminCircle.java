package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import healthVT.vitamine.R;

import java.util.Map;

/**
 * Created by Jay on 6/28/14.
 */

public class vitaminCircle extends View {
    private int width, height, radius, color, left, top, marginLeft;
    private String vitamin;
    private String number;
    private Paint paint = new Paint();
    private int textSize, stroke1, stroke2, stroke3, stroke4;
    public vitaminCircle(Context context, int width, int height, int radius, int color, String vitamin, String number){
        super(context);
        this.textSize = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleText);
        this.marginLeft = context.getResources().getDimensionPixelSize(R.dimen.vitaminMarginLeft);
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
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        ViewGroup.MarginLayoutParams margins = ViewGroup.MarginLayoutParams.class.cast(getLayoutParams());
//        //int margin = 15;
//        margins.leftMargin = marginLeft;
//        margins.rightMargin = marginLeft;
//
//        setLayoutParams(margins);
//    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke4);
        paint.setColor(this.color);

        canvas.drawCircle(height/2, height/2, radius, paint);

        paint.setStrokeWidth(stroke2);
        canvas.drawLine((height/2) - radius, height/2, (height/2) + radius, height/2, paint);

        paint.setStrokeWidth(stroke2);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textSize);

        Rect bounds = new Rect();
        float vitaminWidth = paint.measureText(vitamin);
        paint.getTextBounds(vitamin, 0, 1, bounds);
        canvas.drawText(vitamin, (height/2)-(vitaminWidth/2), ((radius*2)+bounds.height())/3, paint);

        float numberWidth = paint.measureText(number);
        paint.getTextBounds(number, 0, 1, bounds);

        float heightText = ((radius*2)+bounds.height())/3;
        canvas.drawText(number, (height/2)-(numberWidth/2), (heightText*2), paint);

    }

}
