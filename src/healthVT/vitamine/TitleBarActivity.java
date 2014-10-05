package healthVT.vitamine;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Jay on 10/5/2014.
 */
public abstract class TitleBarActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.daily);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        TextView titleText = (TextView) findViewById(R.id.titleBar);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Lobster.ttf");
        titleText.setTypeface(titleFont);

        //Button functions
        TextView leftButton = (TextView) findViewById(R.id.titleLeftButton);
        TextView rightButton = (TextView) findViewById(R.id.titleRightButton);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
