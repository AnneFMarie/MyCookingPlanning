package com.annef.mycookingplanning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.annef.mycookingplanning.dao.DAOUtils;
import com.annef.mycookingplanning.model.Week;

import java.util.Calendar;

public class CookingGrid extends AppCompatActivity {



    public static final int SETTINGS_REQUEST_CODE = 11;
    public static final int EDIT_MEAL_REQUEST_CODE = 10;
    private Context context = this;

    private TextView[][] cells = new TextView[3][7];
    Calendar today;

    private Week week;

    private int weekNumberClipboard = 0;

    private DAOUtils db;
    private View.OnClickListener onCellClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // On lance l'activite de modification d'un exercice
            Intent editMealActivite = new Intent(CookingGrid.this, SetMeal.class);
            int row = getRow(view.getId());
            int column = getColumn(view.getId());
            Log.d("annef", "editing cell, column = "+column+" row= "+row);
            if (column < 3 && column >= 1 && row < 7 && row >= 0 ) {
                editMealActivite.putExtra("com.annef.mycookingplanning.meal.desc", cells[column][row].getText());
                editMealActivite.putExtra("com.annef.mycookingplanning.meal.row", row);
                editMealActivite.putExtra("com.annef.mycookingplanning.meal.column", column);
                // Puis on lance l'intent !
                startActivityForResult(editMealActivite, EDIT_MEAL_REQUEST_CODE);
            }
        }

    };
    private int firstDay;

    private int getRow(int id){
        String resourceName = context.getResources().getResourceName(id);
        try {
            int row = Integer.parseInt(resourceName.substring(resourceName.length() - 1));
            return row-1;
        } catch (Exception e){
            return -1;

        }
    }

    private int getColumn(int id){
        String resourceName = context.getResources().getResourceName(id);
        if (resourceName.contains("Midi")){
            return 1;
        } else if (resourceName.contains("Soir")){
            return 2;
        } else {
            return -1;
        }

    }

    private TextView weekNumberText;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case (EDIT_MEAL_REQUEST_CODE): {
                String newMeal = data.getStringExtra("com.annef.mycookingplanning.meal.desc");
                int row = data.getIntExtra("com.annef.mycookingplanning.meal.row", 0);
                int column = data.getIntExtra("com.annef.mycookingplanning.meal.column", 0);
                Log.d("annef", "desc = " + newMeal + " row = " + row + "+ column " + column);
                Toast.makeText(this, "Saving Meal " + newMeal, Toast.LENGTH_SHORT).show();
                cells[column][row].setText(newMeal);
                week.setMeal(row, column, newMeal);
                db.updateWeek(week);
            }
            break;

            case (SETTINGS_REQUEST_CODE): {
                updateFirstDayWithPrefs();
            }
            break;

        }
    }


    private int weekNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_grid);

        db = new DAOUtils(this);

        cells[0][0] = (TextView) findViewById(R.id.textJour1);
        cells[1][0] = (TextView) findViewById(R.id.textMidi1);
        cells[2][0] = (TextView) findViewById(R.id.textSoir1);
        cells[0][1] = (TextView) findViewById(R.id.textJour2);
        cells[1][1] = (TextView) findViewById(R.id.textMidi2);
        cells[2][1] = (TextView) findViewById(R.id.textSoir2);
        cells[0][2] = (TextView) findViewById(R.id.textJour3);
        cells[1][2] = (TextView) findViewById(R.id.textMidi3);
        cells[2][2] = (TextView) findViewById(R.id.textSoir3);
        cells[0][3] = (TextView) findViewById(R.id.textJour4);
        cells[1][3] = (TextView) findViewById(R.id.textMidi4);
        cells[2][3] = (TextView) findViewById(R.id.textSoir4);
        cells[0][4] = (TextView) findViewById(R.id.textJour5);
        cells[1][4] = (TextView) findViewById(R.id.textMidi5);
        cells[2][4] = (TextView) findViewById(R.id.textSoir5);
        cells[0][5] = (TextView) findViewById(R.id.textJour6);
        cells[1][5] = (TextView) findViewById(R.id.textMidi6);
        cells[2][5] = (TextView) findViewById(R.id.textSoir6);
        cells[0][6] = (TextView) findViewById(R.id.textJour7);
        cells[1][6] = (TextView) findViewById(R.id.textMidi7);
        cells[2][6] = (TextView) findViewById(R.id.textSoir7);

        for (int i = 1; i < 3; i++)
            for (int day = 0; day < 7; day++) {
                cells[i][day].setOnClickListener(onCellClickListener);
            }

        today = Calendar.getInstance();

        int weekNumberToPaste = 0;
        if (getIntent().getAction().equals("android.intent.action.MAIN")){
            // 1st call, as Main Activity
            weekNumber = today.get(Calendar.WEEK_OF_YEAR);
        }
        else {
            weekNumber = getIntent().getExtras().getInt("com.annef.workoutchrono.weeknumber");
            weekNumberToPaste = getIntent().getExtras().getInt("com.annef.workoutchrono.weeknumbertopaste");
        }

        //grey paste action if nothing to paste
        if (weekNumberToPaste == 0)
        {
            MenuItem paste = (MenuItem) findViewById(R.id.action_paste);
//            paste.setVisible(false);
        }

        ImageButton previous = (ImageButton) findViewById(R.id.buttonWeekPrev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weekNumber --;
                updateWeek();
            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.buttonWeekNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weekNumber ++;
                updateWeek();
            }
        });

        weekNumberText = (TextView) findViewById(R.id.weeknumber);

        updateFirstDayWithPrefs();
    }

    private void updateFirstDayWithPrefs(){
        //getting start day in preferences
        // try to use getPreferences()
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        firstDay = 0;
        try {
            String firstDayString = prefs.getString("day_list", "0");
            Log.d("annef", "getting prefs day_list String = "+firstDayString);
            firstDay = Integer.valueOf(firstDayString);
        }catch (NumberFormatException e){

        }

        Log.d("annef", "Frist Day from settings = " + firstDay);
        setCalendarFirstDay(firstDay, today);
        Log.d("annef", "Frist Day set to  = " + today.getFirstDayOfWeek());
        updateDayColumn(firstDay);
        updateWeek();
    }

    private void setCalendarFirstDay(int firstDay, Calendar today) {

        switch (firstDay) {
            case 0:
                today.setFirstDayOfWeek(Calendar.MONDAY);
            case 1:
                today.setFirstDayOfWeek(Calendar.TUESDAY);
            case 2:
                today.setFirstDayOfWeek(Calendar.WEDNESDAY);
            case 3:
                today.setFirstDayOfWeek(Calendar.THURSDAY);
            case 4:
                today.setFirstDayOfWeek(Calendar.FRIDAY);
            case 5:
                today.setFirstDayOfWeek(Calendar.SATURDAY);
            case 6:
                today.setFirstDayOfWeek(Calendar.SUNDAY);
            default:
                today.setFirstDayOfWeek(Calendar.MONDAY);
        }
    }

    private void updateWeek() {
        week = db.readWeek(weekNumber);
        Log.d("annef", "reading week week n" + weekNumber);
        if (week == null){
            Log.d("annef", "can't find week n"+weekNumber+" in db, creating one ...");
            week = new Week(weekNumber, context.getResources().getString(R.string.empty_meal));
            db.create(week);
        }
        updateGrid(week);
    }

    private void updateGrid(Week week) {
        int daycell = 0;
        for (int day=firstDay; day<7; day++){
            cells[1][daycell].setText(week.getDayText(1, day));
            cells[2][daycell].setText(week.getDayText(2, day));
            daycell ++;
        }

        for (int day=0; day<firstDay; day++){
            cells[1][daycell].setText(week.getDayText(1, day));
            cells[2][daycell].setText(week.getDayText(2, day));
            daycell ++;
        }

      /*  for (int i = 1; i < 3; i++)
            for (int day = 0; day < 7; day++) {
                cells[i][day].setText(week.getDayText(i, day));
                Log.d("annef", "meal : "+week.getDayText(i, day));
            }
        weekNumberText.setText("Week : " + week.getNumber());*/
    }


    private void setLayoutAndBorder(View view, GridLayout.LayoutParams params,
                                    boolean borderBottom, boolean borderTop, boolean borderLeft, boolean borderRight) {
        params.bottomMargin = borderBottom ? 1 : 0;
        params.leftMargin = borderLeft ? 1 : 0;
        params.rightMargin = borderRight ? 1 : 0;
        params.topMargin = borderTop ? 1 : 0;
        view.setLayoutParams(params);
    }


    private void updateDayColumn(int firstDay) {
        int daycell = 0;
        for (int day=firstDay; day<7; day++){
            Log.d("annef", "setting cell n°"+daycell+" to "+getResources().getStringArray(R.array.Jours)[day]);
            cells[0][daycell].setText(getResources().getStringArray(R.array.Jours)[day]);
            daycell ++;
        }

        for (int day=0; day<firstDay; day++){
            cells[0][daycell].setText(getResources().getStringArray(R.array.Jours)[day]);
            Log.d("annef", "setting cell n°" + daycell + " to " + getResources().getStringArray(R.array.Jours)[day]);
            daycell ++;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cooking_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(CookingGrid.this,
                    SettingsActivity.class);
            startActivityForResult(settingsActivity, SETTINGS_REQUEST_CODE);
        }
        else if (id == R.id.action_paste){
            // get week number to paste
            if (weekNumberClipboard != 0) {
                week = db.readWeek(weekNumberClipboard);

                if (week != null) {
                    week.setWeekNumber(weekNumber);
                    db.updateWeek(week);
                    updateGrid(week);
                }
            }
            weekNumberClipboard = 0;
        }
        else if (id == R.id.action_copy){
            weekNumberClipboard = weekNumber;

        }
        else if (id == R.id.action_empty) {
            week = new Week(weekNumber, context.getResources().getString(R.string.empty_meal));
            updateGrid(week);
        }
        return super.onOptionsItemSelected(item);
    }
}
