package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jay on 7/19/14.
 */
public class NumberView extends TextView {

    private int height, width, stroke, color, radius;
    private Paint paint = new Paint();

    public NumberView(Context context, int color, String text){
        super(context);

        height = tools.pixelsToSp(context, 50);
        width = tools.pixelsToSp(context, 100);
        stroke = tools.pixelsToSp(context, 5);
        radius = tools.pixelsToSp(context, 15);
        this.color = color;

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, tools.pixelsToSp(context, 10), 0, 0);
        setLayoutParams(llp);

        setPadding(0, 0, 0, 0);
        setText(text);
        setTextColor(color);
        setTextSize(tools.pixelsToSp(context, 20));
        setTag("numberView");
        setGravity(Gravity.CENTER);

//        setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));

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

        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        paint.setColor(color);

        RectF rect = new RectF();
        rect.set(0, 0, getWidth(), getHeight());

        canvas.drawRoundRect(rect, radius, radius, paint);
    }

    public void updateColor(int color){
        this.color = color;
        invalidate();
    }
}
