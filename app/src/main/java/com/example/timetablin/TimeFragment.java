package com.example.timetablin;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int input = 0;

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle argue = getArguments();
        input = argue.getInt("input");
        final Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, h, m, DateFormat.is24HourFormat(getActivity()));
    }

    /*
     * Uses variable set as argument on creation to determine which View to change on dialog close.
     * @param view - TimePickerDialog
     * @param h - hour
     * @param m - minute
     */
    @Override
    public void onTimeSet(TimePicker view, int h, int m) {
        String formatH = String.format(Locale.UK, "%02d", h);
        String formatM = String.format(Locale.UK,"%02d", m);
        boolean isThere = true;
        TextView selection = null;
        switch(input) {
            case 1 :
                selection = getActivity().findViewById(R.id.startTime);
                break;
            case 2:
                selection = getActivity().findViewById(R.id.endTime);
                break;
            default :
                isThere = false;
                System.out.print("Invalid field");
        }
        if (isThere) {
            String placeholder = formatH + ":" + formatM;
            selection.setText(placeholder); //concatenating in setText produces warning hence placeholder
        }
    }
}
