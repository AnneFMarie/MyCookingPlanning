package com.annef.mycookingplanning.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.annef.mycookingplanning.R;

/**
 * Created by annefrancoisemarie on 30/09/15.
 */
public class Week implements Parcelable {

    //private long id;
    private String[][] days;
    private int weekNumber;


    public Week(int weekNumber, String default_meal){
        days = new String[3][7];
        for (int i=1; i<3; i++)
            for (int day=0; day<7; day++){
                days[i][day]= default_meal;
            }
        this.weekNumber = weekNumber;
    }


    //public Week(long id, String[][] days, int weekNumber){
    public Week(String[][] days, int weekNumber){

        this.days = days;
        this.weekNumber = weekNumber;
    }

    protected Week(Parcel in) {

        days = new String[3][7];
        for (int i=1; i<3; i++)
            for (int day=0; day<7; day++){
                days[i][day]=in.readString();
            }
        weekNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //parcel.writeLong(id);
        for (int column=1; column<3; column++)
            for (int day=0; day<7; day++){
                dest.writeString(days[column][day]);

            }

        dest.writeInt(weekNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Week> CREATOR = new Creator<Week>() {
        @Override
        public Week createFromParcel(Parcel in) {
            return new Week(in);
        }

        @Override
        public Week[] newArray(int size) {
            return new Week[size];
        }
    };


    public String getDayText(int column, int day){
        return days[column][day];
    }

    public void setMeal(int row, int column, String newMeal) {
        days[column][row] = newMeal;
    }

    public int getNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

}
