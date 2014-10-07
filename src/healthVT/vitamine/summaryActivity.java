package healthVT.vitamine;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import beans.VitaminBean;
import util.NavigationActivityListener;
import util.circleAnim.CircleSurface;

/**
 * Created by Jay on 7/26/14.
 */
public class SummaryActivity extends TitleBarActivity  {

    CircleSurface surfaceView;
    LinearLayout trackLayout;
    TextView trackButton;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        TextView summaryTitle = (TextView) findViewById(R.id.summaryTitle);
        summaryTitle.setTypeface(null, Typeface.BOLD);

        trackLayout = (LinearLayout) findViewById(R.id.nextLayout);
        trackButton = (TextView) findViewById(R.id.nextButton);
        trackLayout.setVisibility(View.VISIBLE);
        trackButton.setText("Track");
        trackButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);


        VitaminBean vitaminResult = (VitaminBean) getIntent().getSerializableExtra("vitaminResult");

        //surfaceView = new CircleSurface(this, vitaminResult);
        surfaceView = (CircleSurface) findViewById(R.id.vitaminCircleSurface);
        surfaceView.start(vitaminResult);

        attachEvent();
        new NavigationActivityListener().listener(this);
    }

    public void attachEvent(){
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnTrackButton();
            }
        });
        ImageView nextButton = (ImageView) findViewById(R.id.titleRightButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {clickOnTrackButton();
            }
        });
    }

    public void clickOnTrackButton() {
        Intent chartActivity = new Intent(SummaryActivity.this, VitaminChartActivity.class);
        startActivity(chartActivity);
    }
}
