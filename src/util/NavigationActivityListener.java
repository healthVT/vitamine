package util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import healthVT.vitamine.DailyActivity;
import healthVT.vitamine.R;
import healthVT.vitamine.SummaryActivity;
import healthVT.vitamine.VitaminChartActivity;

/**
 * Created by Jay on 10/6/2014.
 */
public class NavigationActivityListener {
    private Activity thisActivity;
    private ImageView navigateDaily, navigateChart;
    private LinearLayout bottomItemLayout;
    public void listener(Activity theActivity, String tagName){
        navigateDaily = (ImageView) theActivity.findViewById(R.id.navigateDaily);
        navigateChart = (ImageView) theActivity.findViewById(R.id.navigateChart);
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
