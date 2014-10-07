package util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import healthVT.vitamine.DailyActivity;
import healthVT.vitamine.R;
import healthVT.vitamine.SummaryActivity;
import healthVT.vitamine.VitaminChartActivity;

/**
 * Created by Jay on 10/6/2014.
 */
public class NavigationActivityListener {
    private Activity theActivity;
    public void listener(Activity theActivity){
        ImageView navigateDaily = (ImageView) theActivity.findViewById(R.id.navigateDaily);
        ImageView navigateTrack = (ImageView) theActivity.findViewById(R.id.navigateTrack);
        this.theActivity = theActivity;

        navigateDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachNavigatorListener(DailyActivity.class);
            }
        });

        navigateTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachNavigatorListener(VitaminChartActivity.class);
            }
        });
    }

    public void attachNavigatorListener(Class toActivity){
        if(theActivity.getClass() != toActivity){
            Intent intent = new Intent(theActivity, toActivity);
            theActivity.startActivity(intent);
            theActivity.finish();
        }

    }
}
