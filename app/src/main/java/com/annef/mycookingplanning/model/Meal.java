package com.annef.mycookingplanning.model;

/**
 * Created by annefrancoisemarie on 30/09/15.
 */
public class Meal {

    private long id;
    private String name;
    private Season season;
    private int rank; // from 1 to 5 - 5 is most used and loved
    private boolean isVegetable;
    private boolean isMeat;
    private int calories;

    public Meal(long id, String name, Season season, int rank, boolean isVegetable, boolean isMeat, int calories){
        this.id = id;
        this.name = name;
        this.season = season;
        this.rank = rank;
        this.isVegetable = isVegetable;
        this.isMeat = isMeat;
        this.calories = calories;
    }
    public Meal(long id, String name, String season, int rank, int isVegetable, int isMeat, int calories){
        this.id = id;
        this.name = name;
        this.season = Season.valueOf(season);
        this.rank = rank;
        this.isVegetable = (isVegetable == 1);
        this.isMeat = (isMeat == 1);
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public Season getSeason() {
        return season;
    }

    public int getRank() {
        return rank;
    }

    public boolean isVegetable() {
        return isVegetable;
    }

    public boolean isMeat() {
        return isMeat;
    }

    public int getCalories() {
        return calories;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void setId(long id) {
        this.id = id;
    }

}
