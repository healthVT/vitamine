package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import healthVT.vitamine.R;

/**
 * Created by Jay on 7/19/14.
 */
public class RemoveIcon extends ImageView {
    private Paint paint = new Paint();
    private int stroke;
    private int color, height, width, radius;

    public RemoveIcon(Context context, int count){
        super(context);

        if(count != 0){
            color = Color.parseColor("#fe5a5a");
        }else{
            color = Color.parseColor("#FFFFFF");
        }
        stroke = tools.pixelsToSp(context, 6);
        radius = tools.pixelsToSp(context, 25);
        height = tools.pixelsToSp(context, 60);
        width = tools.pixelsToSp(context, 60);
        this.color = color;
        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        setTag("RemoveVitaminRow");
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

        canvas.drawCircle(height/2, width/2, radius, paint);

        canvas.drawLine((height/2) - radius, height/2, (height/2) + radius, height/2, paint);

    }

    public void updateColor(int color){
        this.color = color;
        invalidate();
    }
}
