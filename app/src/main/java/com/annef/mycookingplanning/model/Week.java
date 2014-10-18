package com.annef.mycookingplanning.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LAHI8322 on 18/09/2014.
 */
public class Week implements Parcelable {

    //private long id;
    private String[][] days;
    private int weekNumber;


    public static final Parcelable.Creator<Week> CREATOR = new Parcelable.Creator<Week>(){
        @Override
        public Week createFromParcel(Parcel source)
        {
            return new Week(source);
        }

        @Override
        public Week[] newArray(int size)
        {
            return new Week[size];
        }
    };

    public Week(int weekNumber){
        days = new String[3][7];
        for (int i=1; i<3; i++)
            for (int day=0; day<7; day++){
                days[i][day]="empty";
            }
        this.weekNumber = weekNumber;
    }

    public Week(Parcel in){
        days = new String[3][7];
        for (int i=1; i<3; i++)
            for (int day=0; day<7; day++){
                days[i][day]=in.readString();
            }
        weekNumber = in.readInt();
    }

    //public Week(long id, String[][] days, int weekNumber){
    public Week(String[][] days, int weekNumber){
        this.days = new String[3][7];

        this.days = days;
        this.weekNumber = weekNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //parcel.writeLong(id);
        for (int column=1; column<3; column++)
            for (int day=0; day<7; day++){
                parcel.writeString(days[column][day]);

            }

        parcel.writeInt(weekNumber);
    }

    public String getDayText(int column, int day){
        return days[column][day];
    }

    public void setMeal(int row, int column, String newMeal) {
        days[column][row] = newMeal;
    }

    public int getNumber() {
        return weekNumber;
    }
}
