package healthVT.vitamine;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private TextView continueText;
    private RelativeLayout informationExtensionLayout;
    private LinearLayout startButtonLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        TextView titleText = (TextView) findViewById(R.id.titleBar);
        TextView loginText = (TextView) findViewById(R.id.loginText);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Lobster.ttf");
        Typeface demiFont = Typeface.createFromAsset(getAssets(), "demi.ttf");
        titleText.setTypeface(titleFont);
        loginText.setTypeface(demiFont);


        continueText = (TextView) findViewById(R.id.continueLnk);
        informationExtensionLayout = (RelativeLayout) findViewById(R.id.informationExtensionLayout);
        startButtonLayout = (LinearLayout) findViewById(R.id.startButtonLayout);
        attachListener();

    }

    private void attachListener(){
        continueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueText.setVisibility(View.GONE);
                informationExtensionLayout.setVisibility(RelativeLayout.VISIBLE);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) startButtonLayout.getLayoutParams();
                params.topMargin = 10;
                params.bottomMargin = 70;
            }
        });

    }
}
