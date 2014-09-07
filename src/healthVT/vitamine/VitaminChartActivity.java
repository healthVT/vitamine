package healthVT.vitamine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.*;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.vitaminCircle;
import util.vitaminOptionCircleLayout;
import util.vitamineServer;

import java.util.ArrayList;

/**
 * Created by Jay on 8/13/14.
 */
public class VitaminChartActivity extends Activity {

    private LineChart vitaminChart;
    private TextView periodSpinner;
    private LinearLayout vitaminOptionLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data
        Intent intent = getIntent();
        String vitaminHistory = intent.getStringExtra("vitaminHistory");

        //convert to json Array
        JSONArray historyArray = null;
        try{
            historyArray = new JSONArray(vitaminHistory);
        }catch(JSONException e){
            Log.e("Error", "Converting JSON Array", e);
        }

        if(historyArray == null){
            finish();
            return;
        }

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.vitamin_chart);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        TextView titleText = (TextView) findViewById(R.id.titleBar);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Lobster.ttf");
        Typeface demiFont = Typeface.createFromAsset(getAssets(), "demi.ttf");
        titleText.setTypeface(titleFont);

        vitaminChart = (LineChart) findViewById(R.id.vitaminChart);

        ColorTemplate ct = new ColorTemplate();
        ct.addDataSetColors(ColorTemplate.VITAMIN_A_COLOR, this);
        vitaminChart.setColorTemplate(ct);
        vitaminChart.setDrawBorder(true);
        vitaminChart.setBorderStyles(new BarLineChartBase.BorderStyle[]{BarLineChartBase.BorderStyle.BOTTOM, BarLineChartBase.BorderStyle.LEFT, BarLineChartBase.BorderStyle.RIGHT, BarLineChartBase.BorderStyle.TOP});
        vitaminChart.setDescription("");
        vitaminChart.setYLabelCount(4);
        vitaminChart.setLineWidth(7f);
        vitaminChart.setCircleSize(9f);
        vitaminChart.setGridWidth(2f);
        vitaminChart.setPinchZoom(false);
        vitaminChart.setGridColor(getResources().getColor(R.color.backgroundMainColor));
        vitaminChart.setStartAtZero(false);
        vitaminChart.setDrawUnitsInChart(false);
        vitaminChart.setDrawYValues(false);
        vitaminChart.setUnit("%");
        vitaminChart.setStartAtZero(false);

        vitaminChart.setDrawHorizontalGrid(false);
        vitaminChart.setDrawVerticalGrid(false);

        setData(historyArray);
        vitaminChart.setDrawLegend(false);
        vitaminChart.invalidate();


        periodSpinner = (TextView) findViewById(R.id.periodText);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.period, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        periodSpinner.setAdapter(adapter);

        vitaminOptionLayout = (LinearLayout) findViewById(R.id.vitaminOption);
        LinearLayout vitaminOptionRow = new vitaminOptionCircleLayout(this);

        HorizontalScrollView scroll = new HorizontalScrollView(getApplication());
        util.tools.init(this.getResources());
        int vitaminOptionHeight = Math.round(util.tools.convertDpToPixel(90));
        scroll.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                vitaminOptionHeight
        ));
        scroll.addView(vitaminOptionRow);
        vitaminOptionLayout.addView(scroll);


    }

    private void setData(JSONArray history) {
        try {
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            int count = history.length();

            for (int i = 0; i < count; i++) {
                JSONObject object = history.getJSONObject(i);
                if(object != null){
                    xVals.add((object.getString("date")));
                    yVals.add(new Entry(object.getInt("vitaminA"), i));
                }
            }

            // create a dataset and give it a type
            DataSet set1 = new DataSet(yVals, "DataSet 1");

            ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            ChartData data = new ChartData(xVals, dataSets);

            // set data
            vitaminChart.setData(data);

        } catch (Exception e) {
            Log.e("Error", "Setting Value", e);
        }
    }
}
