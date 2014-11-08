package healthVT.vitamine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import util.NavigationActivityListener;
import util.vitaminOptionCircle;
import util.vitaminOptionCircleLayout;
import util.vitamineServer;

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

        //get data
        historyArray = getVitaminChartFromServer("1%20WEEK");

        if(historyArray == null){
            finish();
            return;
        }

        vitaminChart = (LineChart) findViewById(R.id.vitaminChart);

//        ColorTemplate ct = new ColorTemplate();
//        ct.addDataSetColors(ColorTemplate.VITAMIN_A_COLOR, this);
//        vitaminChart.setColorTemplate(ct);
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

        setData(historyArray, "A");
        vitaminChart.setDrawLegend(false);
        vitaminChart.invalidate();


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
    }
 
    private JSONArray getVitaminChartFromServer(String period){
        vitamineServer server = new vitamineServer(this);
        JSONArray recordList = null;
        //call util to get foodList
        try{
            JSONObject jsonResult = server.execute("food/getVitaminRecord?period=" + period).get();
            Log.d("RESULT", jsonResult.toString());

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
            ColorTemplate ct = new ColorTemplate();
            int[] colors = new int[1];
            colors[0] = util.tools.getVitaminColor(selectedVitamin);
            ct.addDataSetColors(colors, this);
            vitaminChart.setColorTemplate(ct);

            // create a dataset and give it a type
            DataSet set1 = new DataSet(yVals, "DataSet 1");

            ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            ChartData data = new ChartData(xVals, dataSets);

            // set data
            vitaminChart.setData(data);
            vitaminChart.clearAll();

            vitaminChart.invalidate();

        } catch (Exception e) {
            Log.e("Error", "Setting Value", e);
        }
    }
}