package util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import beans.VitaminBean;
import healthVT.vitamine.DailyActivity;
import healthVT.vitamine.R;
import org.json.JSONException;
import org.json.JSONObject;
import sqlite.Database;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jay on 7/10/14.
 */
public class VitaminRow extends LinearLayout {

    Context context;
    int paddingTop, paddingLeft, paddingRight, paddingBottom;
    private VitaminBean vitaminBean;
    private Database db;
    NumberView numberView;
    Spinner numberSpinner;
    //setup Circle Size
    int vitaminCircleWidth;
    int vitaminCircleHeight;
    int vitaminCircleRadius;
    private LinearLayout vitaminCircleLayout;
    boolean initialized = false;
    LinearLayout foodLayout;
    ViewGroup root;

    public VitaminRow(Context context, ViewGroup root, VitaminBean vitaminBean, int count){
        super(context);
        this.context = context;
        this.root = root;
        this.vitaminBean = vitaminBean;

        //Default padding
        paddingTop = getResources().getDimensionPixelSize(R.dimen.paddingTop);
        paddingLeft = getResources().getDimensionPixelSize(R.dimen.paddingLeft);
        paddingRight = getResources().getDimensionPixelSize(R.dimen.paddingRight);
        paddingBottom = getResources().getDimensionPixelSize(R.dimen.paddingBottom);

        vitaminCircleWidth = getResources().getDimensionPixelSize(R.dimen.vitaminCircleWidth);
        vitaminCircleHeight = getResources().getDimensionPixelSize(R.dimen.vitaminCircleHeight);
        vitaminCircleRadius = getResources().getDimensionPixelSize(R.dimen.vitaminCircleRadius);

        //Default
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
        setPadding(0, paddingTop, paddingRight, paddingBottom);
        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        vitaminCircleLayout = new LinearLayout(getContext());
        vitaminCircleLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        vitaminCircleLayout.setOrientation(HORIZONTAL);


        //food info (left panel)
        int vitaminFoodNameWidth = getResources().getDimensionPixelSize(R.dimen.vitaminFoodNameWidth);
        foodLayout = new LinearLayout(getContext());
        foodLayout.setOrientation(LinearLayout.VERTICAL);
        foodLayout.setGravity(Gravity.CENTER);
        foodLayout.setLayoutParams(new LinearLayout.LayoutParams(
                vitaminFoodNameWidth,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        foodLayout.setPadding(0,0,0,0);
        foodLayout.setTag("foodLayout");

        db = new Database(getContext());

        createView(vitaminBean, count);

    }

    public void createView(VitaminBean vitaminBean, int count){
        int color = Color.parseColor("#464646");
        if (count % 2 == 0) {
            color = (Color.WHITE);
        }

        //Food name
        TextView foodText = new TextView(getContext());
        foodText.setText(vitaminBean.getFoodName());
        foodText.setTextColor(color);
        foodText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        foodText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        //Number Spinner
        numberSpinner = new Spinner(getContext());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.numbers, R.layout.drop_down_item);
        numberSpinner.setAdapter(adapter);
        numberSpinner.setVisibility(View.INVISIBLE);
        numberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(initialized){
                    TextView numberTextView = (TextView) view;
                    String amount = String.valueOf(numberTextView.getText());
                    Log.d("Item selected", String.valueOf(amount));
                    numberView.setText(amount);

                    updateVitaminAmount(Integer.parseInt(amount));
                }else{
                    initialized = true;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Number text view
        numberView = new NumberView(getContext(), color, String.valueOf(vitaminBean.getAmount()));
        numberView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                numberSpinner.performClick();
            }
        });

        //Remove on vitamin row
        RemoveIcon removeView = new RemoveIcon(getContext(), count % 2);
        removeView.setVisibility(View.GONE);

        removeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView();
            }
        });
        foodLayout.addView(removeView);
        foodLayout.addView(foodText);
        foodLayout.addView(numberView);
        foodLayout.addView(numberSpinner);


        addView(foodLayout);

        //Line
        TextView line = new TextView(getContext());
        line.setBackgroundColor(color);

        int stroke1 = this.getResources().getDimensionPixelSize(R.dimen.vitaminCircleStroke1);
        line.setWidth(stroke1);
        line.setHeight(vitaminCircleHeight);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, paddingRight, 0);
        line.setLayoutParams(llp);
        addView(line);

        vitaminSection();

        addView(vitaminCircleLayout);
    }

    public void vitaminSection(){
        vitaminCircleLayout.removeAllViewsInLayout();

        Map vitaminMap = vitaminBean.getVitaminMap();
        int amount = vitaminBean.getAmount();
        DecimalFormat df = new DecimalFormat("#.##");
        Iterator it = vitaminMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Double> each = (Map.Entry<String, Double>) it.next();
            if (each.getValue() > 0.01) {
                each.setValue(each.getValue() * amount);
                vitaminCircle circleView = new vitaminCircle(getContext(), vitaminCircleWidth, vitaminCircleHeight, vitaminCircleRadius, tools.getVitaminColor(each.getKey()), each.getKey().toUpperCase(), df.format(each.getValue()));
                vitaminCircleLayout.addView(circleView);
            }

            it.remove();
        }

        db.close();
    }

    public void updateVitaminAmount(int amount){
        Log.d("update food name", vitaminBean.getFoodName());
        Log.d("update amount", String.valueOf(amount));

        db.updateAmount(vitaminBean.getFoodName(), amount);
        vitaminBean.updateAmount(amount);
        vitaminSection();
    }

    public void removeView(){

        //((HorizontalScrollView)this.getParent()).;
        root.removeView((HorizontalScrollView)this.getParent());
        DailyActivity.vitaminRowCount--;
        db.deleteByFoodName(vitaminBean.getFoodName());
        redrawBackground();
    }

    public void redrawBackground(){
        final int childCount = root.getChildCount();
        for (int j = 0; j < childCount; j++) {
            HorizontalScrollView scroll = (HorizontalScrollView) root.getChildAt(j);
            if(scroll != null){

                LinearLayout foodLayout = (LinearLayout) scroll.findViewWithTag("foodLayout");
                if(foodLayout!=null){
                    for(int i=0;i<foodLayout.getChildCount();i++){
                        if(foodLayout.getChildAt(i) instanceof TextView){
                            if (j % 2 != 0) {
                                ((TextView) foodLayout.getChildAt(i)).setTextColor(Color.parseColor("#fffbf3"));
                            }else{
                                ((TextView) foodLayout.getChildAt(i)).setTextColor(Color.parseColor("#000000"));
                            }
                        }

                        if(foodLayout.getChildAt(i) instanceof  RemoveIcon){
                            if (j % 2 != 0) {
                                ((RemoveIcon) foodLayout.getChildAt(i)).updateColor(Color.parseColor("#fffbf3"));
                            }else{
                                ((RemoveIcon) foodLayout.getChildAt(i)).updateColor(Color.parseColor("#fe5a5a"));
                            }

                        }

                        if(foodLayout.getChildAt(i) instanceof  NumberView){
                            if (j % 2 != 0) {
                                ((NumberView) foodLayout.getChildAt(i)).updateColor(Color.parseColor("#fffbf3"));
                            }else{
                                ((NumberView) foodLayout.getChildAt(i)).updateColor(Color.parseColor("#000000"));
                            }
                        }
                    }
                }


                if (j % 2 != 0) {
                    scroll.setBackgroundColor(Color.parseColor("#fe5a5a"));
                }else{
                    scroll.setBackgroundColor(Color.parseColor("#fffbf3"));
                }

            }
        }
    }

}