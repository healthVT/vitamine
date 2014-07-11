package util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import beans.VitaminBean;
import healthVT.vitamine.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jay on 7/10/14.
 */
public class VitaminRow extends LinearLayout {

    Context context;
    int paddingTop, paddingLeft, paddingRight, paddingBottom;

    public VitaminRow(Context context, JSONObject vitaminResult, int count){
        super(context);
        this.context = context;

        //Default padding
        paddingTop = getResources().getDimensionPixelSize(R.dimen.paddingTop);
        paddingLeft = getResources().getDimensionPixelSize(R.dimen.paddingLeft);
        paddingRight = getResources().getDimensionPixelSize(R.dimen.paddingRight);
        paddingBottom = getResources().getDimensionPixelSize(R.dimen.paddingBottom);

        //Default
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
        setPadding(0, paddingTop, paddingRight, paddingBottom);
        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        createView(vitaminResult, count);
    }

    public void createView(JSONObject vitaminResult, int count){

        //setup Circle Size
        int vitaminCircleWidth = getResources().getDimensionPixelSize(R.dimen.vitaminCircleWidth);
        int vitaminCircleHeight = getResources().getDimensionPixelSize(R.dimen.vitaminCircleHeight);
        int vitaminCircleRadius = getResources().getDimensionPixelSize(R.dimen.vitaminCircleRadius);


        try {
            JSONObject vitaminList = vitaminResult.getJSONObject("vitamin");

            VitaminBean vitaminBean = new VitaminBean(vitaminResult.getString("foodName"), vitaminList.getDouble("a"), vitaminList.getDouble("c"), vitaminList.getDouble("d"), vitaminList.getDouble("e"), vitaminList.getDouble("k"), vitaminList.getDouble("b1"), vitaminList.getDouble("b2"), vitaminList.getDouble("b3"), vitaminList.getDouble("b6"), vitaminList.getDouble("b12"));
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


            //Remove image on vitamin row
            TextView removeView = new TextView(getContext());
            removeView.setBackgroundResource(R.drawable.remove_image);
            removeView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            removeView.setVisibility(View.GONE);


            foodLayout.addView(removeView);
            foodLayout.addView(foodText);


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


            Map vitaminMap = vitaminBean.getVitaminMap();
            DecimalFormat df = new DecimalFormat("#.##");
            Iterator it = vitaminMap.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<String, Double> each = (Map.Entry<String, Double>) it.next();
                Log.d("Vitamin", each.getKey());
                if (each.getValue() > 0.01) {
                    vitaminCircle circleView = new vitaminCircle(getContext(), vitaminCircleWidth, vitaminCircleHeight, vitaminCircleRadius, tools.getVitaminColor(each.getKey()), each.getKey().toUpperCase(), df.format(each.getValue()));
                    addView(circleView);
                }

                it.remove();
            }
        } catch (JSONException e) {
            Log.e("JSONException", "Exception", e);
        }
    }
}
