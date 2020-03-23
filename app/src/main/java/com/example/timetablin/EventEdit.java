package com.example.timetablin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventEdit extends AppCompatActivity {
    private EditText titleView, sDayView, eDayView, roomView;
    private TextView sTimeView, eTimeView;
    private Spinner campusView, buildingView, catView;
    private Lecture entry;

    /*
     * Deals with the launch of the Activity. In this case it sets up View variables by id and
     * enables functionality of each interactive asset. Dynamically changes buildingSpin content
     * dependant on campusSpin selection. Uses same xml as .EventAddition
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_addition);

        titleView = findViewById(R.id.eventTitle);
        sDayView = findViewById(R.id.enterDate);
        eDayView = findViewById(R.id.endDate);
        sTimeView = findViewById(R.id.startTime);
        eTimeView = findViewById(R.id.endTime);
        campusView = findViewById(R.id.campusSpin);
        buildingView = findViewById(R.id.buildingSpin);
        roomView = findViewById(R.id.enterRoom);
        catView = findViewById(R.id.catSpin);

        ImageButton btnStart = findViewById(R.id.pickStartDate);
        ImageButton btnEnd = findViewById(R.id.pickEndDate);
        ImageButton toHide = findViewById(R.id.finalEvent);
        ImageButton btnSave = findViewById(R.id.save);
        ImageButton btnDelete = findViewById(R.id.delete);

        Bundle retrieve = getIntent().getExtras();
        try {
            entry = retrieve.getParcelable("olddata");
        } catch (NullPointerException e) {
            System.out.println("NullPointerException fetching 'old' entry for edit.");
        }

        toHide.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(1);
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(2);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    returnReply(RESULT_OK);
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnReply(2);
            }
        });

        sDayView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String edit = sDayView.getText().toString();
                if (edit.length() == 10 && before != 10) {
                    Calendar c = Calendar.getInstance();

                    int day = Integer.parseInt(edit.substring(0, 2));
                    int mon = Integer.parseInt(edit.substring(3, 5));
                    int year = Integer.parseInt(edit.substring(6, 10));

                    if (mon > 12) { mon = 12; }
                    c.set(Calendar.MONTH, mon - 1);
                    if (year < c.get(Calendar.YEAR)) { year = c.get(Calendar.YEAR); }
                    else if (year > c.get(Calendar.YEAR) + 1) { year = c.get(Calendar.YEAR) + 1; }
                    c.set(Calendar.YEAR, year);
                    if (day > c.getActualMaximum(Calendar.DATE)) { day = c.getActualMaximum(Calendar.DATE); }
                    String placeholder = String.format(Locale.UK, "%02d%02d%02d", day, mon, year);
                    placeholder = String.format("%s/%s/%s", placeholder.substring(0, 2),
                            placeholder.substring(2, 4),
                            placeholder.substring(4, 8));
                    sDayView.setText(placeholder);
                }
                System.out.println("back" + start + " " + before + " " + count);
                if ((edit.length() == 2 || edit.length() == 5) && (before != 1)) {
                    System.out.println("auto" + start + " " + before + " " + count);
                    sDayView.append("/");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });
        eDayView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String edit = eDayView.getText().toString();
                if (edit.length() == 10 && before != 10) {
                    Calendar c = Calendar.getInstance();

                    int day = Integer.parseInt(edit.substring(0, 2));
                    int mon = Integer.parseInt(edit.substring(3, 5));
                    int year = Integer.parseInt(edit.substring(6, 10));

                    if (mon > 12) { mon = 12; }
                    c.set(Calendar.MONTH, mon - 1);
                    if (year < c.get(Calendar.YEAR)) { year = c.get(Calendar.YEAR); }
                    else if (year > c.get(Calendar.YEAR) + 1) { year = c.get(Calendar.YEAR) + 1; }
                    c.set(Calendar.YEAR, year);
                    if (day > c.getActualMaximum(Calendar.DATE)) { day = c.getActualMaximum(Calendar.DATE); }
                    String placeholder = String.format(Locale.UK, "%02d%02d%02d", day, mon, year);
                    placeholder = String.format("%s/%s/%s", placeholder.substring(0, 2),
                            placeholder.substring(2, 4),
                            placeholder.substring(4, 8));
                    eDayView.setText(placeholder);
                }
                System.out.println("back" + start + " " + before + " " + count);
                if ((edit.length() == 2 || edit.length() == 5) && (before != 1)) {
                    System.out.println("auto" + start + " " + before + " " + count);
                    eDayView.append("/");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }
        });

        sTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(1);
            }
        });
        eTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(2);
            }
        });

        campusView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                buildingView = findViewById(R.id.buildingSpin);
                ArrayAdapter<CharSequence> newSpin;
                switch(pos){
                    case 0 :
                        newSpin = ArrayAdapter.createFromResource(v.getContext(), R.array.granBuilding, android.R.layout.simple_spinner_item);
                        break;
                    case 1 :
                        newSpin = ArrayAdapter.createFromResource(v.getContext(), R.array.eastBuilding, android.R.layout.simple_spinner_item);
                        break;
                    case 2 :
                        newSpin = ArrayAdapter.createFromResource(v.getContext(), R.array.falmBuilding, android.R.layout.simple_spinner_item);
                        break;
                    case 3 :
                        newSpin = ArrayAdapter.createFromResource(v.getContext(), R.array.moulBuilding, android.R.layout.simple_spinner_item);
                        break;
                    default :
                        newSpin = ArrayAdapter.createFromResource(v.getContext(), R.array.granBuilding, android.R.layout.simple_spinner_item);
                        System.out.println("Invalid Campus selected.");
                }
                buildingView.setAdapter(newSpin);
                if(campusView.getSelectedItem().toString().equals(entry.getCampus())){
                    buildingView.setSelection(newSpin.getPosition(entry.getBuilding()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }
        });
        populateScreen(entry);
    }

    /*
     * Edits the Lecture entry using it's various setter methods and adds it as an extra to intent
     * @param resultCode - context for returned data
     */
    public void returnReply(int resultCode) {
        Intent returnIntent = new Intent();

        entry.setTitle(titleView.getText().toString());
        entry.setDate(false, sDayView.getText().toString());
        entry.setDate(true, eDayView.getText().toString());
        entry.setTime(false, sTimeView.getText().toString());
        entry.setTime(true, eTimeView.getText().toString());
        entry.setCampus(campusView.getSelectedItem().toString());
        entry.setBuilding(buildingView.getSelectedItem().toString());
        entry.setRoom(roomView.getText().toString());
        entry.setCategory(catView.getSelectedItemPosition());

        returnIntent.putExtra("data", entry); //as id is only set on creation, no need to alter based on resultCode
        setResult(resultCode, returnIntent);
        finish();
    }

    /*
     * Handles View population, called from onCreate(). Uses getter methods to populate the fields
     * in this activity.
     * @param entry - Lecture event to be added to Views
     */
    public void populateScreen(Lecture entry) {
        titleView.setText(entry.getTitle());
        sDayView.setText(entry.getDate(false));
        eDayView.setText(entry.getDate(true));
        sTimeView.setText(entry.getTime(false));
        eTimeView.setText(entry.getTime(true));

        ArrayAdapter<CharSequence> newSpin;
        switch (entry.getCampus()) {
            case "City" :
                campusView.setSelection(0);
                newSpin = ArrayAdapter.createFromResource(getApplicationContext(), R.array.granBuilding, android.R.layout.simple_spinner_item);
                break;
            case "Eastbourne" :
                campusView.setSelection(1);
                newSpin = ArrayAdapter.createFromResource(getApplicationContext(), R.array.eastBuilding, android.R.layout.simple_spinner_item);
                break;
            case "Falmer" :
                campusView.setSelection(2);
                newSpin = ArrayAdapter.createFromResource(getApplicationContext(), R.array.falmBuilding, android.R.layout.simple_spinner_item);
                break;
            case "Moulsecoomb" :
                campusView.setSelection(3);
                newSpin = ArrayAdapter.createFromResource(getApplicationContext(), R.array.moulBuilding, android.R.layout.simple_spinner_item);
                break;
            default :
                campusView.setSelection(0);
                System.out.println("Error reading Campus Data");
                newSpin = ArrayAdapter.createFromResource(getApplicationContext(), R.array.granBuilding, android.R.layout.simple_spinner_item);
        }
        buildingView.setAdapter(newSpin);
        buildingView.setSelection(newSpin.getPosition(entry.getBuilding()));
        roomView.setText(entry.getRoom());
        catView.setSelection(entry.getCategory());
    }

    /*
     * Uses Dialog Fragment to show a date selection 'pop-up'
     * @param input - variable to be set as argument and passed to fragment
     */
    public void showDatePickerDialog(int input) {
        Bundle bundle = new Bundle();
        bundle.putInt("input", input);
        DialogFragment newFragment = new DateFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /*
     * Uses Dialog Fragment to show a time selection 'pop-up'
     * @param input - variable to be set as argument and passed to fragment
     */
    public void showTimePickerDialog(int input) {
        Bundle bundle = new Bundle();
        bundle.putInt("input", input);
        DialogFragment newFragment = new TimeFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /*
     * Data validation for user input, ensures all input is usable by subsequent methods.
     * Creates a toast informing the user of things that should be changed if needed.
     * @return boolean true if all field data meets input constraints
     */
    public boolean validate() {
        String sDayCheck = sDayView.getText().toString();
        String eDayCheck = eDayView.getText().toString();
        String sTimeCheck = sTimeView.getText().toString();
        String eTimeCheck = eTimeView.getText().toString();

        if ((titleView.getText().toString().matches("")) || (roomView.getText().toString().matches(""))) {
            Toast.makeText(getApplicationContext(),"Please fill in all fields.",Toast.LENGTH_SHORT).show();
            return false;
        }

        if ((sDayCheck.length() != 10) ||
                (eDayCheck.length() != 10) ||
                (sTimeCheck.length() != 5) ||
                (eTimeCheck.length() != 5)) {
            Toast.makeText(getApplicationContext(),"Please check your Date and Time fields.",Toast.LENGTH_SHORT).show();
            return false;
        } else if ((sDayCheck.charAt(2) != '/') || (sDayCheck.charAt(5) != '/') ||
                (eDayCheck.charAt(2) != '/') || (eDayCheck.charAt(5) != '/')) {
            Toast.makeText(getApplicationContext(), "Please check your Date fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else if ((sTimeCheck.charAt(2) != ':') || (eTimeCheck.charAt(2) != ':')) {
            Toast.makeText(getApplicationContext(), "Please check your Time fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!endDateAfter(sDayCheck, eDayCheck)) {
            Toast.makeText(getApplicationContext(), "End date must be on or after the Start date.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!endTimeAfter(sTimeCheck, eTimeCheck)) {
            Toast.makeText(getApplicationContext(), "End time must be on or after the Start time.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    boolean endDateAfter(String start, String end) {
        SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        Calendar c = Calendar.getInstance();
        try {
            Date startDate = ddMMyyyy.parse(start);
            Date endDate = ddMMyyyy.parse(end);
            c.setTime(startDate);
            return startDate.compareTo(endDate) <= 0;
        } catch (ParseException e) {
            System.out.println("Error occurred parsing Date");
        }
        return true;
    }

    boolean endTimeAfter(String start, String end) {
        SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm", Locale.UK);
        Calendar c = Calendar.getInstance();
        try {
            Date startTime = HHmm.parse(start);
            Date endTime = HHmm.parse(end);
            c.setTime(startTime);
            return startTime.compareTo(endTime) <= 0;
        } catch (ParseException e) {
            System.out.println("Error occurred parsing Time");
        }
        return true;
    }
}