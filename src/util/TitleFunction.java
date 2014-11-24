package util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import healthVT.vitamine.R;

/**
 * Created by Jay on 11/22/2014.
 */
public class TitleFunction {
    public TitleFunction(final Activity activity, boolean hasNext){
        //LinearLayout windowTitle = (LinearLayout) activity.findViewById(R.id.windowTitle);
       // Log.d("wind", windowTitle.toString());
        TextView titleText = (TextView) activity.findViewById(R.id.titleBar);
        Typeface titleFont = Typeface.createFromAsset(activity.getAssets(), "Lobster.ttf");
        titleText.setTypeface(titleFont);

        //Button functions
        ImageView leftButton = (ImageView) activity.findViewById(R.id.titleLeftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });

        if(!hasNext){
            ImageView nextButton = (ImageView) activity.findViewById(R.id.titleRightButton);
            nextButton.setVisibility(View.INVISIBLE);
        }
    }
}
