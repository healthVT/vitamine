package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import healthVT.vitamine.R;

/**
 * Created by Jay on 7/19/14.
 */
public class NumberView extends TextView {

    private int height, width, stroke, color, radius;
    private Paint paint = new Paint();

    public NumberView(Context context, int color, String text){
        super(context);

        height = getResources().getDimensionPixelSize(R.dimen.numberViewHeight);
        width = getResources().getDimensionPixelSize(R.dimen.numberViewWidth);
        stroke = getResources().getDimensionPixelSize(R.dimen.numberViewStroke);
        radius = getResources().getDimensionPixelSize(R.dimen.numberViewRadius);
        int margin = getResources().getDimensionPixelSize(R.dimen.numberViewMargin);

        this.color = color;

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, margin, 0, 0);
        setLayoutParams(llp);

        setPadding(0, 0, 0, 0);
        setText(text);
        setTextColor(color);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
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
