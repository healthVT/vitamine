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
import healthVT.vitamine.NumberDialog;
import healthVT.vitamine.R;
import org.json.JSONObject;
import sqlite.Database;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Jay on 7/10/14.
 */



public class VitaminRow extends LinearLayout implements Callback {

    Context context;
    int paddingTop, paddingLeft, paddingRight, paddingBottom;
    private VitaminBean vitaminBean;
    private Database db;
    NumberView numberView;
    //setup Circle Size
    int vitaminCircleWidth;
    int vitaminCircleHeight;
    int vitaminCircleRadius;
    private LinearLayout vitaminCircleLayout;
    boolean initialized = false;
    LinearLayout foodLayout;
    ViewGroup root;
    FrameLayout dailyFrameLayout;

    @Override
    public void selected(String answer){
        numberView.setText(answer);
        updateVitaminAmount(Integer.parseInt(answer));
    }

    public VitaminRow(Context context, ViewGroup root, VitaminBean vitaminBean, int count, FrameLayout dailyFrameLayout){
        super(context);
        this.context = context;
        this.root = root;
        this.vitaminBean = vitaminBean;
        this.dailyFrameLayout = dailyFrameLayout;

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

        //Number text view
        int cornerResource = 0;
        if(color == Color.WHITE){
            cornerResource = R.drawable.round_corner_whtie;
        }else{
            cornerResource = R.drawable.round_corner_black;
        }
        numberView = new NumberView(getContext(), color, cornerResource, String.valueOf(vitaminBean.getAmount()));


        final List<String> column = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        numberView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberDialog dialog = new NumberDialog(context, column, 2, VitaminRow.this);
                dialog.show();
            }
        });



        //Remove on vitamin row
        RemoveIcon removeView = new RemoveIcon(getContext(), count % 2);
        removeView.setVisibility(View.GONE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, paddingTop, 0, 0);
        removeView.setLayoutParams(lp);

        removeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView();
            }
        });

        foodLayout.addView(foodText);
        foodLayout.addView(removeView);
        foodLayout.addView(numberView);


        addView(foodLayout);

        //Line
        TextView line = new TextView(getContext());
        line.setTag("line");
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
                vitaminCircle circleView = new vitaminCircle(getContext(), vitaminCircleWidth,  vitaminCircleHeight, vitaminCircleRadius, tools.getVitaminColor(each.getKey()), each.getKey().toUpperCase(), df.format(each.getValue()));
                circleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            vitaminCircle vView = (vitaminCircle) view;
                            popupWindow(vView.getVitamin());
                        }
                });
                vitaminCircleLayout.addView(circleView);
            }

            it.remove();
        }

        db.close();
    }

    private void popupWindow(String vitamin){

        try{
            vitamineServer server = new vitamineServer(context);
            JSONObject jsonResult = server.execute("vitamin/description?vitaminName=" + vitamin).get();
            Log.d("descpriont", jsonResult.toString());

            //Popup window
            util.popupWindow popWIndow = new popupWindow(context, null, dailyFrameLayout, "Vitamin " + vitamin, jsonResult.getString("vitaminDescription"));
            popWIndow.fadeIn();
        }catch(Exception e){
            Log.e("Project VT Server exception ", "Exception", e);
        }
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
                                ((NumberView) foodLayout.getChildAt(i)).updateColor(Color.parseColor("#fffbf3"), R.drawable.round_corner_whtie);
                            }else{
                                ((NumberView) foodLayout.getChildAt(i)).updateColor(Color.parseColor("#000000"), R.drawable.round_corner_black);
                            }
                        }
                    }
                }

                TextView line = (TextView) scroll.findViewWithTag("line");

                    if (j % 2 != 0) {
                        line.setBackgroundColor(Color.parseColor("#fffbf3"));
                    }else{
                        line.setBackgroundColor(Color.parseColor("#000000"));
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