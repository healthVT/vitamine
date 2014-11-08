package util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import healthVT.vitamine.*;

/**
 * Created by Jay on 10/6/2014.
 */
public class NavigationActivityListener {
    private Activity thisActivity;
    private LinearLayout bottomItemLayout;
    public void listener(Activity theActivity, String tagName){
        ImageView navigateDaily = (ImageView) theActivity.findViewById(R.id.navigateDaily);
        ImageView navigateChart = (ImageView) theActivity.findViewById(R.id.navigateChart);
        ImageView navigateProfile = (ImageView) theActivity.findViewById(R.id.navigateProfile);
        bottomItemLayout = (LinearLayout) theActivity.findViewById(R.id.bottomItemLayout);

        this.thisActivity = theActivity;
        updateAllIcon(tagName);


        navigateDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachNavigatorListener(DailyActivity.class);
            }
        });

        navigateChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachNavigatorListener(VitaminChartActivity.class);
            }
        });

        navigateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachNavigatorListener(SelfInfoActivity.class);
            }
        });
    }

    public void attachNavigatorListener(Class toActivity){
        if(thisActivity.getClass() != toActivity){

            Intent intent = new Intent(thisActivity, toActivity);
            thisActivity.startActivity(intent);
            thisActivity.finish();
        }

    }

    public void updateAllIcon(String tagName){
        int count = bottomItemLayout.getChildCount();
        for(int i=0;i<count;i++){
            ImageView view = (ImageView) bottomItemLayout.getChildAt(i);
            String viewIconName = view.getTag().toString();
            if(view.getTag().toString().equals(tagName)){
                viewIconName += "_selected";
            }

            view.setImageResource(thisActivity.getResources().getIdentifier(viewIconName, "drawable", thisActivity.getPackageName()));
        }

    }
}
