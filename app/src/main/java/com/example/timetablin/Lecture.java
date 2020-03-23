package com.example.timetablin;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Lecture implements Parcelable {
    private String lecTitle, startDate, endDate, startTime, endTime, lecCampus, lecBuilding, lecRoom;
    private int category, id;

    /*
     * Constructor creates Lecture object
     * @param lecTitle - User input for entry title
     * @param startDate - User input for entry first session date
     * @param endDate - User input for entry last session date
     * @param startTime - User input for entry first session time
     * @param endtime - User input for entry last session time
     * @param lecCampus - User input for entry campus location
     * @param lecBuilding - User input for entry building location
     * @param lecRoom - user input for entry room location
     * @param category - id of user input for entry category
     * @param id - id assigned to this entry
     */
    Lecture(String lecTitle, String startDate, String endDate, String startTime, String endTime, String lecCampus, String lecBuilding, String lecRoom, int category, int id){
        this.lecTitle = lecTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lecCampus = lecCampus;
        this.lecBuilding = lecBuilding;
        this.lecRoom = lecRoom;
        this.category = category;
        this.id = id;
    }

    /*
     * Get & Set methods for all variables
     */
    String getTitle() { return lecTitle; }
    void setTitle(String inputData) { this.lecTitle = inputData; }
    String getDate(boolean startEnd) { return (startEnd) ? endDate : startDate; } //boolean used to determine which date required: false for start, true for end
    void setDate(boolean startEnd, String inputData) {
        if (startEnd) {
            this.endDate = inputData;
        } else {
            this.startDate = inputData;
        }
    }
    String getTime(boolean startEnd) { return (startEnd) ? endTime : startTime; } //boolean used to determine which time required: false for start, true for end
    void setTime(boolean startEnd, String inputData) {
        if (startEnd) {
            this.endTime = inputData;
        } else {
            this.startTime = inputData;
        }
    }
    String getCampus() { return lecCampus; }
    void setCampus(String inputData) { this.lecCampus = inputData; }
    String getBuilding() { return lecBuilding; }
    void setBuilding(String inputData) { this.lecBuilding = inputData; }
    String getRoom() { return lecRoom; }
    void setRoom(String inputData) { this.lecRoom = inputData; }
    int getCategory(){ return category; }
    void setCategory(int inputData){ this.category = inputData; }
    public int getId() { return id; }
    public void setId(int inputData) { this.id = inputData; }

    /*
     * Parses the start day String in to a date format (dd/MM/yyyy) to calculate the day of the week
     * @return the id of the day of the week associated with startDay (0: Sunday)
     */
    int getStartDay() {
        SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        Calendar c = Calendar.getInstance();
        try {
            Date result = ddMMyyyy.parse(startDate);
            c.setTime(result);
        } catch (ParseException e) {
            System.out.println("Error occurred parsing Date");
        }
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /*
     * Parses the startTime to date format (HH:mm) and compares it to the date format version of
     * input variable in order to determine which is earlier
     * @param other - input date from other entry
     * @return boolean is this entry set earlier than other entry?
     */
    boolean earlierTime(String other){
        SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm", Locale.UK);
        try {
            Date thisTime = HHmm.parse(startTime);
            Date otherTime = HHmm.parse(other);
            return thisTime.compareTo(otherTime) <= 0;
        } catch (ParseException e) {
            System.out.println("Error occurred parsing Time");
        }
        return true;
    }

    /*
     * Parcelable implementation
     */
    public static final Parcelable.Creator<Lecture> CREATOR = new Parcelable.Creator<Lecture>() {
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }
        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    private Lecture(Parcel in) {
        lecTitle = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        lecCampus = in.readString();
        lecBuilding = in.readString();
        lecRoom = in.readString();
        category = in.readInt();
        id = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(lecTitle);
        out.writeString(startDate);
        out.writeString(endDate);
        out.writeString(startTime);
        out.writeString(endTime);
        out.writeString(lecCampus);
        out.writeString(lecBuilding);
        out.writeString(lecRoom);
        out.writeInt(category);
        out.writeInt(id);
    }
}