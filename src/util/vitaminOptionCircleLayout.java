package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import beans.VitaminBean;
import healthVT.vitamine.R;

import java.util.List;

/**
 * Created by Jay on 8/23/14.
 */
public class vitaminOptionCircleLayout extends LinearLayout{


    public vitaminOptionCircleLayout(Context context){
        super(context);
        String[] vitaminList = {"A", "B1", "B2", "B3", "B6", "B12", "C", "D", "E", "K"};
        setBackgroundColor(Color.parseColor("#fe5a5a"));
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        int stroke4 = context.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke4);
        setPadding(stroke4, 0, stroke4, 0);
        for(int i=0;i<vitaminList.length;i++){
            boolean selected = false;
            if(i==0){
                selected =true;
            }
            View vitaminCircle = new vitaminOptionCircle(context, vitaminList[i], selected);
            addView(vitaminCircle);
        }
    }

    public vitaminOptionCircleLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

    }

    public vitaminOptionCircleLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }
}
