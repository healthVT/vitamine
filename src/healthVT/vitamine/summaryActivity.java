package healthVT.vitamine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import beans.VitaminBean;
import util.circleAnim.CircleSurface;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jay on 7/26/14.
 */
public class SummaryActivity extends Activity {

    LinearLayout mainLayout;
    CircleSurface surfaceView;

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

        mainLayout = (LinearLayout) findViewById(R.id.circleAnimLayout);

        VitaminBean vitaminResult = (VitaminBean) getIntent().getSerializableExtra("vitaminResult");

        surfaceView = new CircleSurface(this, vitaminResult);
        surfaceView.setZOrderOnTop(true);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);

        mainLayout.addView(surfaceView);

    }
}
