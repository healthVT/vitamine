package healthVT.vitamine;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import org.json.JSONArray;
import org.json.JSONObject;
import util.*;

import java.util.ArrayList;

/**
 * Created by Jay on 8/13/14.
 */
public class VitaminChartActivity extends TitleBarActivity {

    private LineChart vitaminChart;
    private TextView periodText;
    private Spinner periodSpinner;
    private vitaminOptionCircleLayout vitaminOptionRow;
    private JSONArray historyArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vitamin_chart);

        int mainColor = getResources().getColor(R.color.backgroundMainColor);

        //get data
        historyArray = getVitaminChartFromServer("1%20WEEK");

        if(historyArray == null){
            finish();
            return;
        }

        vitaminChart = (LineChart) findViewById(R.id.vitaminChart);

        vitaminChart.setDrawBorder(true);
        vitaminChart.setDescription("");
        vitaminChart.setBorderWidth(5);
        vitaminChart.setBorderColor(mainColor);
        vitaminChart.setPinchZoom(false);
        vitaminChart.setDoubleTapToZoomEnabled(false);
        vitaminChart.setDrawUnitsInChart(false);
        vitaminChart.setDrawYValues(false);
        vitaminChart.setUnit("%");
        vitaminChart.setStartAtZero(true);

        vitaminChart.setDrawHorizontalGrid(false);
        vitaminChart.setDrawVerticalGrid(false);

        setData(historyArray, "A");
        vitaminChart.setDrawLegend(false);
        vitaminChart.invalidate();

        //set xlabel and ylabel
        XLabels xl = vitaminChart.getXLabels();
        xl.setPosition(XLabels.XLabelPosition.BOTTOM);
        xl.setTextColor(mainColor);

        YLabels yl = vitaminChart.getYLabels();
        yl.setLabelCount(5);
        yl.setTextColor(mainColor);


        /**
         * Setup period selector
         */
        periodText = (TextView) findViewById(R.id.periodText);

        periodSpinner = (Spinner) findViewById(R.id.periodSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.period, R.layout.drop_down_item);
        periodSpinner.setAdapter(adapter);
        periodSpinner.setVisibility(View.INVISIBLE);


        LinearLayout vitaminOptionLayout = (LinearLayout) findViewById(R.id.vitaminOption);
        vitaminOptionRow = new vitaminOptionCircleLayout(this);

        HorizontalScrollView scroll = new HorizontalScrollView(getApplication());
        util.tools.init(this.getResources());
        int vitaminOptionHeight = Math.round(util.tools.convertDpToPixel(90));
        scroll.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                vitaminOptionHeight
        ));
        scroll.addView(vitaminOptionRow);
        vitaminOptionLayout.addView(scroll);

        attachEvent();
        new NavigationActivityListener().listener(this, "icon_track");
        new TitleFunction(this, false);
    }
 
    private JSONArray getVitaminChartFromServer(String period){
        vitamineServer server = new vitamineServer(this);
        JSONArray recordList = null;
        //call util to get foodList
        try{
            JSONObject jsonResult = server.execute("food/getVitaminRecord?period=" + period).get();

            if(jsonResult.getBoolean("success")){
                recordList = jsonResult.getJSONArray("vitaminRecordList");

            }else{

            }


        }catch(Exception e){
            Log.e("Project VT Server exception ", "Exception", e);
        }
        return recordList;
    }

    private void attachEvent(){
        int count = vitaminOptionRow.getChildCount();

        for(int i=0;i<count;i++){
            final int j = i;
            final vitaminOptionCircle vitaminCircle = (vitaminOptionCircle) vitaminOptionRow.getChildAt(i);
            vitaminCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickVitaminOptionEvent(vitaminCircle);
                }
            });
        }

        periodText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                periodSpinner.performClick();
            }
        });


        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selected = (TextView) view;
                updatePeriod(selected.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Selected Period", "Nothing");
            }
        });
    }

    public void onClickVitaminOptionEvent(vitaminOptionCircle clickedCircle){
        updateVitaminOptionColor(clickedCircle.getVitaminName());
        setData(historyArray, clickedCircle.getVitaminName());

    }

    public void updatePeriod(String period){
        period = period.replace(" ", "%20");
        historyArray = getVitaminChartFromServer(period);
        updateVitaminOptionColor("A");
        setData(historyArray, "A");
    }

    public void updateVitaminOptionColor(String vitaminName){
        for(int i=0;i<vitaminOptionRow.getChildCount();i++){
            vitaminOptionCircle vitaminCircle = (vitaminOptionCircle) vitaminOptionRow.getChildAt(i);

            if(vitaminCircle.getVitaminName().equals(vitaminName)){
                vitaminCircle.selected(true);
            }else{
                vitaminCircle.selected(false);
            }

        }
    }

    private void setData(JSONArray history, String selectedVitamin) {
        try {
            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            int count = history.length();

            for (int i = 0; i < count; i++) {
                JSONObject object = history.getJSONObject(i);
                if(object != null){
                    xVals.add((object.getString("date")));
                    yVals.add(new Entry(object.getInt(selectedVitamin), i));
                }
            }

            //Set line color
            LineDataSet lineData = new LineDataSet(yVals, "DataSet 1");
            lineData.setLineWidth(8f);
            lineData.setCircleSize(10f);


            int color = util.tools.getVitaminColor(selectedVitamin);

            lineData.setColor(color);
            lineData.setCircleColor(color);

            // create a dataset and give it a type
            ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

            dataSets.add(lineData); // add the datasets

            LineData data = new LineData(xVals, dataSets);

            // set data
            vitaminChart.setData(data);
            vitaminChart.invalidate();

        } catch (Exception e) {
            Log.e("Error", "Setting Value", e);
        }
    }
}