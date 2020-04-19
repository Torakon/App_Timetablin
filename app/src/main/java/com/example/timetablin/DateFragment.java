package com.example.timetablin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int input = 0;

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle argue = getArguments();
        input = argue.getInt("input");
        final Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, y, m, d);
    }

    /*
     * Uses variable set as argument on creation to determine which View to change on dialog close.
     * @param view - DatePickerDialog
     * @param y - year
     * @param m - month
     * @param d - day
     */
    @Override
    public void onDateSet(DatePicker view, int y, int m, int d) {
        String formatD = String.format(Locale.UK, "%02d", d);
        String formatM = String.format(Locale.UK, "%02d", m + 1); //month was returning 1 less than initially expected
        boolean isThere = true;
        EditText selection = null;
        switch(input) {
            case 1 :
                selection = getActivity().findViewById(R.id.enterDate);
                break;
            case 2:
                selection = getActivity().findViewById(R.id.endDate);
                break;
            default :
                isThere = false;
                System.out.print("Invalid field");
        }
        if (isThere) {
            String placeholder = formatD + "/" + formatM + "/" + y;
            selection.setText(placeholder); //concatenating in setText produces warning hence placeholder
        }
    }
}
