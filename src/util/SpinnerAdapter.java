package util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import healthVT.vitamine.R;

import java.util.List;

/**
 * Created by Jay on 11/16/2014.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    int resource;

    public SpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(resource, parent, false);
        TextView view = (TextView) row.findViewById(R.id.spinnerItem);
        view.setTextColor(Color.BLACK);
        view.setText(getItem(position));
        return row;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(resource, parent, false);
        TextView view = (TextView) row.findViewById(R.id.spinnerItem);
        view.setTextColor(Color.BLACK);
        view.setText(getItem(position));

        return row;
    }
}
