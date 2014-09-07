package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import healthVT.vitamine.R;

/**
 * Created by Jay on 9/6/2014.
 */

public class vitaminOptionCircle extends View {
    private String vitaminString;
    private Paint paint;
    private int textSize, marginLeft, stroke1, stroke2, stroke3, stroke4, width, height, radius, vitaminTextSize, stroke6;
    private boolean selected;
    public vitaminOptionCircle(Context context, String vitaminString, boolean selected){
        super(context);
        this.selected = selected;
        this.vitaminString = vitaminString;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);

        this.textSize = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleOptionText);
        this.vitaminTextSize = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleOptionVitaminText);
        this.marginLeft = context.getResources().getDimensionPixelSize(R.dimen.vitaminMarginLeft);
        this.stroke1 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke1);
        this.stroke2 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke2);
        this.stroke3 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke3);
        this.stroke4 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke4);
        this.stroke6 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke6);

        this.width = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleWidth);
        this.height = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleHeight);
        this.radius = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleOptionRadius);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selected();
            }
        });
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

        paint.setStrokeWidth(stroke4);
        paint.setTextSize(textSize);
        if(selected){
            paint.setColor(util.tools.getVitaminColor(vitaminString));
            paint.setStyle(Paint.Style.FILL);
        }else{
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
        }

        canvas.drawCircle((height/2), height/2, radius, paint);

        paint.setColor(Color.WHITE);
        Rect bounds = new Rect();
        paint.setStrokeWidth(stroke1);
        paint.setStyle(Paint.Style.FILL);
        String vitaminText = "Vitamin";
        float vitaminWidth = paint.measureText(vitaminText);
        paint.getTextBounds(vitaminText, 0, 1, bounds);
        canvas.drawText(vitaminText, ((height/2)-(vitaminWidth/2)), ((radius)), paint);

        paint.setTextSize(vitaminTextSize);
        paint.setStrokeWidth(stroke3);
        vitaminWidth = paint.measureText(vitaminString);
        paint.getTextBounds(vitaminText, 0, 1, bounds);
        canvas.drawText(vitaminString, ((height/2)-(vitaminWidth/2)), ((radius)+bounds.height() + stroke6), paint);

        //canvas.drawText(number, (height/2)-(numberWidth/2), (heightText*2), paint);
    }

    public void selected(){
        selected = true;
        invalidate();
    }
}