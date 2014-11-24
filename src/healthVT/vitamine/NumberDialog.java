package healthVT.vitamine;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jay on 11/23/2014.
 */
public class NumberDialog extends Dialog implements View.OnClickListener {

    TextView confirmButton, updateView;
    LinearLayout listViewLinear;
    List<String> pickerList;

    public NumberDialog(Context context, List<String> pickerList, int count, TextView updateView){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_number_picker);

        this.pickerList = pickerList;
        this.updateView = updateView;
        confirmButton = (TextView)findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);

        createNumberPicker(count);
    }

    public void createNumberPicker(int pickerCount){
        listViewLinear = (LinearLayout) findViewById(R.id.listViewLinear);

        for(int i=0;i<pickerCount;i++){
            VitaminListView list = new VitaminListView(getContext(), pickerList);
            listViewLinear.addView(list);

        }
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.confirmButton:
                updateView.setText(getSelectedNumber());
                dismiss();
                break;
        }
    }

    public String getSelectedNumber(){
        int count = listViewLinear.getChildCount();
        String number = "";
        for(int i=0;i<count;i++){
            VitaminListView view = (VitaminListView) listViewLinear.getChildAt(i);
            number += view.getSelected();
        }

        return number;
    }
}
