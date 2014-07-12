package util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
    TextView numberView;
    Spinner numberSpinner;
    //setup Circle Size
    int vitaminCircleWidth;
    int vitaminCircleHeight;
    int vitaminCircleRadius;
    private LinearLayout vitaminCircleLayout;

    public VitaminRow(Context context, VitaminBean vitaminBean, int count){
        super(context);
        this.context = context;
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

        db = new Database(getContext());


        createView(vitaminBean, count);
    }

    public void createView(VitaminBean vitaminBean, int count){

        int vitaminFoodNameWidth = getResources().getDimensionPixelSize(R.dimen.vitaminFoodNameWidth);

        //food info (left panel)
        LinearLayout foodLayout = new LinearLayout(getContext());
        foodLayout.setOrientation(LinearLayout.VERTICAL);
        foodLayout.setGravity(Gravity.CENTER);
        foodLayout.setLayoutParams(new LinearLayout.LayoutParams(
                vitaminFoodNameWidth,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));


        //Food name
        TextView foodText = new TextView(getContext());
        foodText.setText(vitaminBean.getFoodName());
        foodText.setTextColor(Color.BLACK);

        foodText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));


        //Remove on vitamin row
        TextView removeView = new TextView(getContext());
        removeView.setBackgroundResource(R.drawable.remove_image);
        removeView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        removeView.setTag("RemoveVitaminRow");
        removeView.setVisibility(View.GONE);

        removeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView();
            }
        });

        //Number Spinner
        numberSpinner = new Spinner(getContext());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.numbers, R.layout.drop_down_item);
        numberSpinner.setAdapter(adapter);
        numberSpinner.setVisibility(View.INVISIBLE);
        numberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView numberTextView = (TextView) view;
                String amount = String.valueOf(numberTextView.getText());
                numberView.setText(amount);
                updateVitaminAmount(Integer.parseInt(amount));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Number text view
        numberView = new TextView(getContext());
        numberView.setTextColor(Color.BLACK);
        numberView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        numberView.setText("1");

        numberView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                numberSpinner.performClick();
            }
        });


        foodLayout.addView(removeView);
        foodLayout.addView(foodText);
        foodLayout.addView(numberView);
        foodLayout.addView(numberSpinner);


        addView(foodLayout);

        //Line
        TextView line = new TextView(getContext());
        if (count % 2 != 0) {
            line.setBackgroundColor(Color.parseColor("#464646"));
        } else {
            line.setBackgroundColor(Color.WHITE);
        }

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
                each.setValue(each.getValue()*amount);
                vitaminCircle circleView = new vitaminCircle(getContext(), vitaminCircleWidth, vitaminCircleHeight, vitaminCircleRadius, tools.getVitaminColor(each.getKey()), each.getKey().toUpperCase(), df.format(each.getValue()));
                vitaminCircleLayout.addView(circleView);
            }

            it.remove();
        }
    }

    public void updateVitaminAmount(int amount){
        vitaminBean.updateAmount(amount);
        vitaminSection();
    }

    public void removeView(){
        ((HorizontalScrollView)this.getParent()).removeView(this);
        DailyActivity.vitaminRowCount--;
        db.deleteByFoodName(vitaminBean.getFoodName());
    }

}