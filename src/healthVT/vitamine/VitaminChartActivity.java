package healthVT.vitamine;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import util.vitaminCircle;
import util.vitamineServer;

import java.util.ArrayList;

/**
 * Created by Jay on 8/13/14.
 */
public class VitaminChartActivity extends Activity {

    private LineChart vitaminChart;
    private TextView periodSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        vitaminChart.setGridWidth(5f);
        vitaminChart.setPinchZoom(false);
        vitaminChart.setGridColor(getResources().getColor(R.color.backgroundMainColor));
        vitaminChart.setStartAtZero(false);
        vitaminChart.setDrawUnitsInChart(false);
        vitaminChart.setDrawYValues(false);

//        vitaminChart.setDrawHorizontalGrid(false);
//        vitaminChart.setDrawVerticalGrid(false);

        setData(7, 100);
        vitaminChart.setDrawLegend(false);
        vitaminChart.invalidate();


        periodSpinner = (TextView) findViewById(R.id.periodText);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.period, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        periodSpinner.setAdapter(adapter);

    }

    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i+1) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 3;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        DataSet set1 = new DataSet(yVals, "DataSet 1");

        ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        ChartData data = new ChartData(xVals, dataSets);

        // set data
        vitaminChart.setData(data);
    }
}
