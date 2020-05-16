package com.example.timetablin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Lecture> saveData = new ArrayList<>();
    private LectureStringify ts = new LectureStringify();
    private String fileName = "events";

    /*
     * Deals with the launch of the Activity. In this case it loads saved entries, if the relevant
     * file exists and converts the ByteArray into a String, then converts the String into an
     * ArrayList using custom class LectureStringify. Shows the welcome screen for 5 seconds
     * before displaying the user entries. Sets up functionality for addEvent button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks for saved theme settings
        SharedPreferences pref;
        pref = getSharedPreferences("TimetablinUPref", MODE_PRIVATE);
        if (pref.contains("dark")) {
            boolean dCheck = pref.getBoolean("dark",false);
            if (dCheck) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }

        //load input
        String toLoad = "";
        try { //handle FileNotFound
            FileInputStream fis = getApplicationContext().openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder sB = new StringBuilder();
            try (BufferedReader read = new BufferedReader(isr)) { //handle IO
                String line = read.readLine();
                while(line!=null) {
                    sB.append(line).append('\n');
                    line = read.readLine();
                }
            } catch(IOException e) {
                System.out.println("IOException in opening file for reading");
            } finally {
                toLoad = sB.toString();
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException in opening file");
        }
        saveData = ts.stringToArray(toLoad);

        saveData.removeIf(Lecture::isOutOfDate);

        if (!toLoad.equals("")) {
            for(Lecture count : saveData) {
                populateList(count);
            }
        }

        final ImageButton btnAdd = findViewById(R.id.addEntry);
        final ImageButton btnPref = findViewById(R.id.pref);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventAddition.class);
                startActivityForResult(intent, 1);
            }
        });
        btnPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PrefActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
     * On activity loss of focus, it makes sure to save all current entries to the save file
     * using custom class LectureStringify to convert all entries into a string
     */
    @Override
    public void onPause() {
        super.onPause();
        String toSave = ts.arrayToString(saveData);
        try (FileOutputStream fos = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE)) { //handle IO
            fos.write(toSave.getBytes());
        } catch (IOException e) {
            System.out.println("IOException in writing to file");
        }
    }

    /*
     * Handles results of called activities. requestCode 1 handles the creation of events,
     * requestCode 2 handles the editing of current events
     * @param requestCode - context for activity
     * @param resultCode - result condition
     * @param data - intent with any extras returned
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        saveData.removeIf(Lecture::isOutOfDate);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle retrieve = data.getExtras();
                Lecture entry = retrieve.getParcelable("data");

                populateList(entry); //add to View
                saveData.add(entry); //save to current entries collection
            }
        }
        if (requestCode == 2) {
            Bundle retrieve = data.getExtras();
            Lecture entry = retrieve.getParcelable("data");
            if(resultCode == RESULT_OK) {
                removeEntryFromView(entry);

                populateList(entry);
                saveData.add(entry); //add to current entries collection
            } else if (resultCode == 2) { //handles the removal of an entry
                removeEntryFromView(entry);
            }
        }
    }

    /*
     * Removes a specified event from the activity view and then removes it from the entry array saveData.
     * If no other events present in day then change visibility of the placeholder view.
     * @param entry - specified Lecture entry
     */
    public void removeEntryFromView(Lecture entry) {
        View oldEntry = findViewById(entry.getId());
        ViewGroup parent = (ViewGroup) oldEntry.getParent();
        parent.removeView(oldEntry); //remove the entry marked for deletion from View

        if (parent.getChildCount() == 1) {
            parent.getChildAt(0).setVisibility(View.VISIBLE); //If relevant, replaces "Nothing Scheduled" Text View
        }

        saveData.removeIf(event -> (event.getId() == entry.getId()));
    }

    /*
     * Handles View population, called from onActivityResult() and onCreate(). Inflates view from
     * event.xml adjusting child content based on entry get methods. Sets up functionality to edit
     * entry onClick. Sorts entries based on day and then start time.
     * @param entry - Lecture event to be added to View
     */
    public void populateList(final Lecture entry) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View eventList = inflater.inflate(R.layout.event, null, false); //inflates event.xml to add data and insert into view
        eventList.setId(entry.getId());

        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        margin.setMargins(0,10,0,0);
        eventList.setLayoutParams(margin);

        TextView title = eventList.findViewById(R.id.lecTitle);
        TextView location = eventList.findViewById(R.id.lecLocation);
        TextView timeStart = eventList.findViewById(R.id.lecTime);
        TextView timeEnd = eventList.findViewById(R.id.lecTimeEnd);
        TextView category = eventList.findViewById(R.id.catText);

        title.setText(entry.getTitle());

        String strLocation = entry.getCampus() + ", " + entry.getBuilding() + " " + entry.getRoom();
        location.setText(strLocation); //concatenating in setText produces warning hence strLocation

        timeStart.setText(entry.getTime(false));
        timeEnd.setText(entry.getTime(true));

        switch(entry.getCategory()) { //switchCase to set background colour based on category
            case 0 :
                eventList.setBackgroundColor(getColor(R.color.colorGuestBack));
                category.setText("G");
                break;
            case 1 :
                eventList.setBackgroundColor(getColor(R.color.colorLectureback));
                category.setText("L");
                break;
            case 2 :
                eventList.setBackgroundColor(getColor(R.color.colorSocietyBack));
                category.setText("S");
                break;
            default :
                System.out.println("Invalid Category detected");
                eventList.setBackgroundColor(Color.WHITE);
                category.setText("!");
        }

        eventList.setOnClickListener(new View.OnClickListener() { //edit event functionality
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventEdit.class);
                intent.putExtra("olddata", entry); //so we can remove/edit the correct entry on result
                startActivityForResult(intent, 2);
            }
        });

        LinearLayout listDay;
        TextView nothing;
        switch(entry.getStartDay()) { //switch to determine which weekday to show event under
            case Calendar.SUNDAY :
                listDay = findViewById(R.id.sundayList);
                nothing = findViewById(R.id.nothingBoxSu);
                if (nothing.getVisibility() == View.VISIBLE) { //removes "Nothing Scheduled" TextView from screen
                    nothing.setVisibility(View.GONE);
                }
                break;
            case Calendar.MONDAY :
                listDay = findViewById(R.id.mondayList);
                nothing = findViewById(R.id.nothingBoxMo);
                if (nothing.getVisibility() == View.VISIBLE) {
                    nothing.setVisibility(View.GONE);
                }
                break;
            case Calendar.TUESDAY :
                listDay = findViewById(R.id.tuesdayList);
                nothing = findViewById(R.id.nothingBoxTu);
                if (nothing.getVisibility() == View.VISIBLE) {
                    nothing.setVisibility(View.GONE);
                }
                break;
            case Calendar.WEDNESDAY :
                listDay = findViewById(R.id.wednesdayList);
                nothing = findViewById(R.id.nothingBoxWe);
                if (nothing.getVisibility() == View.VISIBLE) {
                    nothing.setVisibility(View.GONE);
                }
                break;
            case Calendar.THURSDAY :
                listDay = findViewById(R.id.thursdayList);
                nothing = findViewById(R.id.nothingBoxTh);
                if (nothing.getVisibility() == View.VISIBLE) {
                    nothing.setVisibility(View.GONE);
                }
                break;
            case Calendar.FRIDAY :
                listDay = findViewById(R.id.fridayList);
                nothing = findViewById(R.id.nothingBoxFr);
                if (nothing.getVisibility() == View.VISIBLE) {
                    nothing.setVisibility(View.GONE);
                }
                break;
            case Calendar.SATURDAY :
                listDay = findViewById(R.id.saturdayList);
                nothing = findViewById(R.id.nothingBoxSa);
                if (nothing.getVisibility() == View.VISIBLE) {
                    nothing.setVisibility(View.GONE);
                }
                break;
            default :
                System.out.println("Invalid Day detected");
                listDay = findViewById(R.id.mondayList);
        }

        if (!(listDay.getChildAt(0) instanceof LinearLayout)) { //initial add to view
            listDay.addView(eventList, 0);
        } else {
            for (int i = 0; i <= listDay.getChildCount()-2; i++) { //check if earlier and add accordingly
                TextView child = listDay.getChildAt(i).findViewById(R.id.lecTime);
                if (entry.earlierTime(child.getText().toString())) {
                    listDay.addView(eventList, i);
                    break;
                }
                if (i == (listDay.getChildCount()-2)) { //if this is the latest one, add to end of list
                    listDay.addView(eventList, i+1);
                    break;
                }
            }
        }
    }
}