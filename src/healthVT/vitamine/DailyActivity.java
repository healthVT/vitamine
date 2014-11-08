package healthVT.vitamine;

import android.app.Activity;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;
import beans.VitaminBean;
import org.json.JSONException;
import org.json.JSONObject;
import sqlite.Database;
import util.NavigationActivityListener;
import util.VitaminRow;
import java.util.*;

import util.popupWindow;
import util.vitamineServer;

/**
 * Created by Jay on 6/15/14.
 */
public class DailyActivity extends TitleBarActivity {

    public static int vitaminRowCount = 0;


    private LinearLayout tempDataLayout, calculateLayout;
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
    private Database db;
    private AutoCompleteTextView foodInput;
    private TextView vitaminEditButton, calculateButton;
    boolean tempListOn = false, editOn = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily);

        demiFont = Typeface.createFromAsset(getAssets(), "demi.ttf");

        foodInput = (AutoCompleteTextView) findViewById(R.id.foodInput);

        vitamineServer server = new vitamineServer(this);

        //call util to get foodList
        try{
            JSONObject jsonResult = server.execute("food/getFoodList").get();
            Log.d("RESULT", jsonResult.toString());
            String[] foodList = jsonResult.get("foodList").toString().split(",");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drop_down_item, foodList);
            foodInput.setAdapter(adapter);

        }catch(Exception e){
            Log.e("Project VT Server exception ", "Exception", e);
        }


        tempDataLayout = (LinearLayout) findViewById(R.id.tempData);

        //Get Resource
        paddingTop = getResources().getDimensionPixelSize(R.dimen.paddingTop);
        paddingLeft = getResources().getDimensionPixelSize(R.dimen.paddingLeft);
        paddingRight = getResources().getDimensionPixelSize(R.dimen.paddingRight);
        paddingBottom = getResources().getDimensionPixelSize(R.dimen.paddingBottom);

        vitaminEditButton = (TextView) findViewById(R.id.vitaminEditButton);
        calculateLayout = (LinearLayout) findViewById(R.id.nextLayout);
        calculateButton = (TextView) findViewById(R.id.nextButton);

        //Create existing vitamin rows from database
        db = new Database(this);

        List<VitaminBean> vitaminRows = db. getVitaminData();
        if(vitaminRows.size() > 0){

            for(VitaminBean bean : vitaminRows){
                createVitaminRow(bean);
            }
        }else{
            addTempFood();
        }
        attachEvent();
        new NavigationActivityListener().listener(this, "icon_today");


    }

    private void attachEvent(){
        if(tempListOn){
            for(int i=0;i<tempDataLayout.getChildCount();i++){
                LinearLayout view = (LinearLayout) tempDataLayout.getChildAt(i);
                if(view != null){
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LinearLayout layout = (LinearLayout) view;
                            TextView textView = (TextView) layout.getChildAt(0);
                            String foodName = textView.getText().toString();

                            getFoodVitamin(foodName);
                        }
                    });
                }

            }
        }


        foodInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view;
                getFoodVitamin(String.valueOf(text.getText()));
                foodInput.setText("");

                editOn = false;
                turnOnEdit();

            }
        });

        vitaminEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editOn){
                    editOn = false;
                }else{
                    editOn = true;
                }
                turnOnEdit();
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {calculateButtonClicked();
            }
        });

        ImageView nextButton = (ImageView) findViewById(R.id.titleRightButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {calculateButtonClicked();
            }
        });
/*
        ImageView navigateDaily = (ImageView) findViewById(R.id.navigateDaily);
        ImageView navigateTrack = (ImageView) findViewById(R.id.navigateTrack);

        navigateDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Botton Navigater", "clicked");
                Intent dailyIntent = new Intent(DailyActivity.this, DailyActivity.class);
                startActivity(dailyIntent);
                finish();
            }
        });*/
    }

    public void calculateButtonClicked(){
        try{

            String foodName = db.getAllFoodName();
            vitamineServer server = new vitamineServer(DailyActivity.this);
            JSONObject resultJSON = server.execute("food/getVitaminDailyResult/?foodList=" + foodName + "&gender=" + "MALE").get();

            VitaminBean vitaminBean = new VitaminBean(null, resultJSON.getDouble("a"), resultJSON.getDouble("c"), resultJSON.getDouble("d"), resultJSON.getDouble("e"), resultJSON.getDouble("k"), resultJSON.getDouble("b1"), resultJSON.getDouble("b2"), resultJSON.getDouble("b3"), resultJSON.getDouble("b6"), resultJSON.getDouble("b12"), 1, "Food");

            Intent intent = new Intent(DailyActivity.this, SummaryActivity.class);

            intent.putExtra("vitaminResult", vitaminBean);
            startActivity(intent);



        }catch(Exception e){
            Log.e("Vitamin server error", "error", e);
        }
    }

    private void getFoodVitamin(final String foodName){

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try{
                    vitamineServer server = new vitamineServer(DailyActivity.this);
                    JSONObject resultJSON = server.execute("food/getVitaminByFood/?foodName=" + foodName).get();
                    Log.d("result", resultJSON.get("success").toString());

                    if (resultJSON.get("success").toString().equals("true")) {
                        Log.d("Vitamin", resultJSON.get("vitamin").toString());
                        createVitaminRow(resultJSON);


                    } else {

                    }

                }catch(Exception e){}
            }
        });
    }

    private void createVitaminRow(JSONObject vitaminResult){

        try {
            JSONObject vitaminList = vitaminResult.getJSONObject("vitamin");
            VitaminBean vitaminBean = new VitaminBean(vitaminResult.getString("foodName"), vitaminList.getDouble("a"), vitaminList.getDouble("c"), vitaminList.getDouble("d"), vitaminList.getDouble("e"), vitaminList.getDouble("k"), vitaminList.getDouble("b1"), vitaminList.getDouble("b2"), vitaminList.getDouble("b3"), vitaminList.getDouble("b6"), vitaminList.getDouble("b12"), 1, "Food");
            createVitaminRow(vitaminBean);

            //Put in database
            db.updateVitaminData(vitaminBean, "Food");

        } catch (JSONException e) {
            Log.e("JSONException", "Exception", e);
        }
    }


    private void createVitaminRow(VitaminBean vitaminBean){
        if(tempListOn){
            tempDataLayout.removeAllViewsInLayout();
            tempListOn = false;
        }
        vitaminRowCount++;

        FrameLayout dailyFrameLayout = (FrameLayout) findViewById(R.id.dailyFrameLayout);
        VitaminRow vitaminLayout = new VitaminRow(DailyActivity.this, tempDataLayout, vitaminBean, vitaminRowCount, dailyFrameLayout);

        HorizontalScrollView scroll = new HorizontalScrollView(getApplication());

        scroll.addView(vitaminLayout);

        tempDataLayout.addView(scroll, 0);
        vitaminLayout.redrawBackground();
        //enable calculator button
        calculateLayout.setVisibility(View.VISIBLE);
    }




    private void addTempFood(){

        for(String foodName : tempFoodArray){
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

            //foodText.setLayoutParams(params);
            foodText.setPadding(0, 0, 0, paddingBottom);
            foodText.setGravity(Gravity.CENTER_HORIZONTAL);
            foodText.setText(foodName);
            foodText.setTextColor(Color.BLACK);


            tempLayout.addView(foodText);
            tempLayout.addView(getDividedLine());
            tempDataLayout.addView(tempLayout);

            tempListOn = true;
        }
    }

    public void turnOnEdit(){
        String[] changeTag = {"numberView", "RemoveVitaminRow"};
        ViewGroup root = tempDataLayout;

        for(String tag : changeTag){
            final int childCount = root.getChildCount();
            for (int j = 0; j < childCount; j++) {
                final View child = root.getChildAt(j);

                if (child instanceof ViewGroup) {
                    ViewGroup v = (ViewGroup) child;
                    View tagV = v.findViewWithTag(tag);
                    if(tagV!=null){
                        if(editOn){
                            if(tag.equals("RemoveVitaminRow")){
                                tagV.setVisibility(View.VISIBLE);
                            }else{
                                tagV.setVisibility(View.GONE);
                            }

                        }else{
                            if(tag.equals("RemoveVitaminRow")){
                                tagV.setVisibility(View.GONE);
                            }else{
                                tagV.setVisibility(View.VISIBLE);
                            }

                        }

                    }

                }
            }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, e);
    }

}