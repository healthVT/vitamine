package healthVT.vitamine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import beans.VitaminBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.CircleAnimView;
import util.circleAnim.CircleSurface;
import util.vitamineServer;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jay on 7/26/14.
 */
public class SummaryActivity extends Activity  {

    CircleSurface surfaceView;
    LinearLayout trackLayout;
    TextView trackButton;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.summary);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        TextView titleText = (TextView) findViewById(R.id.titleBar);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Lobster.ttf");
        Typeface demiFont = Typeface.createFromAsset(getAssets(), "demi.ttf");
        titleText.setTypeface(titleFont);

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
    }

    public void attachEvent(){
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnTrackButton();
            }
        });
    }

    public void clickOnTrackButton() {
        Intent chartActivity = new Intent(SummaryActivity.this, VitaminChartActivity.class);
        startActivity(chartActivity);
    }
}
