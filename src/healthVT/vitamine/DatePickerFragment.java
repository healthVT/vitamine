package healthVT.vitamine;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Jay on 11/16/2014.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR) - 10;
        int month = 1;
        int day = 1;

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMaxDate(c.getTime().getTime());
        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String monthString;
        if(month < 10){
            monthString = "0" + String.valueOf(month);
        }else{
            monthString = String.valueOf(month);
        }

        String dayString;
        if(day < 10){
            dayString = "0" + String.valueOf(day);
        }else{
            dayString = String.valueOf(day);
        }
        String date = monthString + "/" + dayString + "/" + String.valueOf(year);
        TextView ageEdit = (TextView) getActivity().findViewById(R.id.ageEdit);
        ageEdit.setText(date);
        ageEdit.setTag(String.valueOf(year) + "-" + monthString+ "-" + dayString);
    }
}