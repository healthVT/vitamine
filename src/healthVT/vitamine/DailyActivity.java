package healthVT.vitamine;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Jay on 6/15/14.
 */
public class DailyActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.daily);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        TextView titleText = (TextView) findViewById(R.id.titleBar);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Lobster.ttf");
        titleText.setTypeface(titleFont);

    }
}