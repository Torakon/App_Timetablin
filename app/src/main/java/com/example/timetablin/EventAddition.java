package com.example.timetablin;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class EventAddition extends AppCompatActivity {
    private EditText titleView, sDayView, eDayView, roomView;
    private TextView sTimeView, eTimeView;
    private Spinner campusView, buildingView, catView;

    /*
     * Deals with the launch of the Activity. In this case it sets up View variables by id and
     * enables functionality of each interactive asset. Dynamically changes buildingSpin content
     * dependant on campusSpin selection.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_addition);

        ImageButton btnStart = findViewById(R.id.pickStartDate);
        ImageButton btnEnd = findViewById(R.id.pickEndDate);
        ImageButton btnFinal = findViewById(R.id.finalEvent);

        titleView = findViewById(R.id.eventTitle);
        sDayView = findViewById(R.id.enterDate);
        eDayView = findViewById(R.id.endDate);
        sTimeView = findViewById(R.id.startTime);
        eTimeView = findViewById(R.id.endTime);
        campusView = findViewById(R.id.campusSpin);
        buildingView = findViewById(R.id.buildingSpin);
        roomView = findViewById(R.id.enterRoom);
        catView = findViewById(R.id.catSpin);
        //TODO: think about adding notes
        //TODO: look into implementing map functionality -- no user interaction, just display location based on location input?

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
        btnFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    returnReply();
                }
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

        Spinner campusView = findViewById(R.id.campusSpin);
        campusView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                Spinner buildingView = findViewById(R.id.buildingSpin);
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //do nothing
            }
        });
    }

    /*
     * Gathers user input and creates a new object of type Lecture before adding it to the return
     * intent as an extra
     */
    public void returnReply(){
        String title = titleView.getText().toString();
        String sDay = sDayView.getText().toString();
        String eDay = eDayView.getText().toString();
        String sTime = sTimeView.getText().toString();
        String eTime = eTimeView.getText().toString();
        String campus = campusView.getSelectedItem().toString();
        String building = buildingView.getSelectedItem().toString();
        String room = roomView.getText().toString();
        int cat = catView.getSelectedItemPosition();
        int id;
        Random gen = new Random();
        id = gen.nextInt(1000); //TODO: Look into making this id more unique than just a random int. add bound to make it unique? in result handling .Main?

        Intent returnIntent = new Intent();
        returnIntent.putExtra("data", new Lecture(title, sDay, eDay, sTime, eTime, campus, building, room, cat, id));
        setResult(RESULT_OK, returnIntent);
        finish();
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
        return true;
    }
}
//https://developer.android.com/guide/components/fragments
//https://developer.android.com/guide/topics/ui/controls/pickers