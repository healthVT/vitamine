package util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import healthVT.vitamine.R;

/**
 * Created by Jay on 10/13/2014.
 */
public class popupWindow extends LinearLayout {
    Context context;
    FrameLayout frameLayout;


    public popupWindow(Context context, AttributeSet attrs, FrameLayout frameLayout, String title, String body) {
        super(context, attrs);

        this.context = context;
        this.frameLayout = frameLayout;

        setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        setBackgroundColor(getResources().getColor(R.color.popupBackground));
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        int popWidth = getResources().getDimensionPixelSize(R.dimen.popupWidth);
        int popHeight = getResources().getDimensionPixelSize(R.dimen.popupHeight);
        int padding = getResources().getDimensionPixelSize(R.dimen.popPadding);
        //Popup Layout
        LinearLayout container = new LinearLayout(this.getContext());
        container.setLayoutParams(new LinearLayout.LayoutParams(
                popWidth,
                popHeight
        ));
        container.setPadding(padding, padding, padding, padding);
        container.setGravity(Gravity.CENTER_HORIZONTAL);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackgroundColor(Color.WHITE);

        //Popup Views

        //TitleText
        TextView titleText = new TextView(this.getContext());
        titleText.setTextColor(Color.BLACK);
        titleText.setGravity(Gravity.CENTER_HORIZONTAL);

        //Separate Red Line
        TextView redLine = new TextView(this.getContext());
        redLine.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        redLine.setHeight(getResources().getDimensionPixelSize(R.dimen.line));
        redLine.setBackgroundColor(Color.parseColor("#fe5a5a"));

        //Body Text
        TextView bodyText = new TextView(this.getContext());
        bodyText.setTextColor(Color.BLACK);
        int paddingTop = getResources().getDimensionPixelSize(R.dimen.popBodyPaddingTop);
        bodyText.setPadding(0, paddingTop, 0, 0);
        bodyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        ScrollView bodyScroll = new ScrollView(context);
        bodyScroll.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        bodyScroll.addView(bodyText);

        Typeface titleFont = Typeface.createFromAsset(context.getAssets(), "Lobster.ttf");
        titleText.setTypeface(titleFont);
        titleText.setText(title);
        bodyText.setText(body);

        //closeView.setImageResource(R.drawable.);

        container.setBackgroundResource(R.drawable.round_corner_full_white);
        container.addView(titleText);
        container.addView(redLine);
        container.addView(bodyScroll);

        addView(container);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

    }

    public void fadeIn(){
        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        frameLayout.addView(this);
        startAnimation(fadeInAnimation);

    }

    public void close(){
        //Popup window
        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        startAnimation(fadeInAnimation);
        setVisibility(LinearLayout.GONE);
    }
}
