package healthVT.vitamine;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.*;
import android.widget.*;
import beans.VitaminBean;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import sqlite.Database;
import util.VitaminRow;
import util.tools;
import util.vitaminCircle;

import java.text.DecimalFormat;
import java.util.*;

import util.vitamineServer;

/**
 * Created by Jay on 6/15/14.
 */
public class DailyActivity extends Activity {

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
    private TextView vitaminEditButton;
    boolean editOn = false;
    boolean tempListOn = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.daily);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        TextView titleText = (TextView) findViewById(R.id.titleBar);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Lobster.ttf");
        demiFont = Typeface.createFromAsset(getAssets(), "demi.ttf");
        titleText.setTypeface(titleFont);

        foodInput = (AutoCompleteTextView) findViewById(R.id.foodInput);
        //call util to get foodList
        try{
            vitamineServer server = new vitamineServer();
            JSONObject jsonResult = server.execute("food/getFoodList").get();
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
        calculateLayout = (LinearLayout) findViewById(R.id.calculateLayout);

        //Create existing vitamin rows from database
        db = new Database(this);

        List<VitaminBean> vitaminRows = db.getVitaminData();
        if(vitaminRows.size() > 0){

            for(VitaminBean bean : vitaminRows){
                createVitaminRow(bean);
            }
        }else{
            addTempFood();
        }

        attachEvent();



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

            }
        });

        vitaminEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnEdit(tempDataLayout);
                Log.d("In", "click");
            }
        });
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
            VitaminBean vitaminBean = new VitaminBean(vitaminResult.getString("foodName"), vitaminList.getDouble("a"), vitaminList.getDouble("c"), vitaminList.getDouble("d"), vitaminList.getDouble("e"), vitaminList.getDouble("k"), vitaminList.getDouble("b1"), vitaminList.getDouble("b2"), vitaminList.getDouble("b3"), vitaminList.getDouble("b6"), vitaminList.getDouble("b12"), 1);
            createVitaminRow(vitaminBean);

            //Put in database
            db.updateVitaminData(vitaminBean);

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
        LinearLayout vitaminLayout = new VitaminRow(DailyActivity.this, vitaminBean, vitaminRowCount);

        HorizontalScrollView scroll = new HorizontalScrollView(getApplication());
        if (vitaminRowCount % 2 != 0) {
            scroll.setBackgroundColor(Color.parseColor("#fffbf3"));
        }
        scroll.addView(vitaminLayout);
        tempDataLayout.addView(scroll);

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

    public void turnOnEdit(ViewGroup root){
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);

            if (child instanceof ViewGroup) {
                ViewGroup v = (ViewGroup) child;
                View tagV = v.findViewWithTag("RemoveVitaminRow");
                if(tagV!=null){
                    if(editOn){
                        tagV.setVisibility(View.GONE);
                    }else{
                        tagV.setVisibility(View.VISIBLE);
                    }

                }

            }
        }

        if(editOn){
            editOn=false;
        }else{
            editOn=true;
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
