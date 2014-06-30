package healthVT.vitamine;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import util.vitaminCircle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import util.vitamineServer;

/**
 * Created by Jay on 6/15/14.
 */
public class DailyActivity extends Activity {

    private LinearLayout tempDataLayout;
    private Typeface demiFont;
    private String[] tempFoodArray = {
            "Apple",
            "Orange",
            "Banana",
            "Beef",
            "Chicken",
            "Rice",
            "Pork",
            "Potato",
            "Spinach",
            "Tofu",
            "Tomato"
    };
    int paddingTop;
    int paddingLeft;
    int paddingRight;
    int paddingBottom;
    private int vitaminCircleWidth, vitaminCircleHeight, vitaminCircleRadius;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.daily);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        TextView titleText = (TextView) findViewById(R.id.titleBar);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Lobster.ttf");
        demiFont = Typeface.createFromAsset(getAssets(), "demi.ttf");
        titleText.setTypeface(titleFont);

        tempDataLayout = (LinearLayout) findViewById(R.id.tempData);

        //Get Resource
        paddingTop = getResources().getDimensionPixelSize(R.dimen.paddingTop);
        paddingLeft = getResources().getDimensionPixelSize(R.dimen.paddingLeft);
        paddingRight = getResources().getDimensionPixelSize(R.dimen.paddingRight);
        paddingBottom = getResources().getDimensionPixelSize(R.dimen.paddingBottom);

        //setup Circle Size
        vitaminCircleWidth = getResources().getDimensionPixelSize(R.dimen.vitaminCircleWidth);
        vitaminCircleHeight = getResources().getDimensionPixelSize(R.dimen.vitaminCircleHeight);
        vitaminCircleRadius = getResources().getDimensionPixelSize(R.dimen.vitaminCircleRadius);

        addTempFood();
        attachEvent();

    }

    private void attachEvent(){
        for(int i=0;i<tempDataLayout.getChildCount();i++){
            LinearLayout view = (LinearLayout) tempDataLayout.getChildAt(i);
            if(view != null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout layout = (LinearLayout) view;
                        TextView textView = (TextView) layout.getChildAt(0);
                        String foodName = textView.getText().toString();
                        Log.d("Click name", foodName);
                        getFoodVitamin(foodName);
                    }
                });
            }

        }
    }

    private void getFoodVitamin(final String foodName){

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try{
                    vitamineServer server = new vitamineServer();
                    JSONObject resultJSON = server.execute("food/getVitaminByFood/?foodName=" + foodName).get();
                    Log.d("result", resultJSON.get("success").toString());

                    if (resultJSON.get("success").toString().equals("true")) {
                        Log.d("Vitamin", resultJSON.get("vitamin").toString());

                        JSONObject vitamin = resultJSON.getJSONObject("vitamin");

                        try{
                            LinearLayout vitaminLayout = new LinearLayout(DailyActivity.this);
                            vitaminLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));

                            tempDataLayout.removeAllViewsInLayout();

                            HorizontalScrollView scroll = new HorizontalScrollView(getApplication());


                            vitaminLayout.setOrientation(LinearLayout.HORIZONTAL);
                            vitaminLayout.setBackgroundColor(Color.WHITE);
                            vitaminLayout.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

                            Iterator<String> key = vitamin.keys();
                            while(key.hasNext()){
                                String keyName = key.next().toString();
                                if(vitamin.getDouble(keyName) > 0.01){
                                    vitaminCircle circleView = new vitaminCircle(DailyActivity.this, vitaminCircleWidth, vitaminCircleHeight, vitaminCircleRadius, Color.parseColor("#790000"), keyName.toUpperCase(), vitamin.getDouble(keyName));
                                    vitaminLayout.addView(circleView);
                                }
                            }
                            scroll.addView(vitaminLayout);
                            tempDataLayout.addView(scroll);
                            //scroll.addView(tempDataLayout);

                        }catch(JSONException e){
                            Log.e("JSONException", "Exception", e);
                        }

                    } else {

                    }

                }catch(Exception e){}
            }
        });
    }

//    private String vitaminView(JSONObject vitamin, String name){
//        String vitaminString = "";
//
//        try{
//            if(vitamin.get(name) != null && vitamin.getInt(name) != 0 && vitamin.getInt(name) > 0){
//                vitaminString = " " + name.toUpperCase() + ": " + df.format(vitamin.getDouble(name));
//            }
//        }catch(JSONException e){
//            Log.e("JSONException", "Exception", e);
//        }
//        return vitaminString;
//    }

    private void addTempFood(){



        for(String foodName : tempFoodArray){
            Log.d("Array", foodName);
            LinearLayout tempLayout = new LinearLayout(this);
            tempLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            tempLayout.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            tempLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            tempLayout.setBackgroundColor(Color.WHITE);
            tempLayout.setOrientation(LinearLayout.VERTICAL);


            TextView foodText = new TextView(this);
            foodText.setTypeface(demiFont);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            //foodText.setLayoutParams(params);
            foodText.setPadding(0, 0, 0, paddingBottom);
            foodText.setGravity(Gravity.CENTER_HORIZONTAL);
            foodText.setText(foodName);
            foodText.setTextColor(Color.BLACK);


            tempLayout.addView(foodText);
            tempLayout.addView(getDividedLine());
            tempDataLayout.addView(tempLayout);
        }
    }

    private TextView getDividedLine(){
        TextView dividLine = new TextView(this);
        dividLine.setBackgroundColor(Color.BLACK);
        int lineHeight = getResources().getDimensionPixelSize(R.dimen.line);
        dividLine.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        dividLine.setHeight(lineHeight);

        return dividLine;
    }
}