package com.annef.mycookingplanning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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


public class CookingGrid extends Activity {

    private Context context = this;

    private GridLayout myGridLayout;
    private CookingCell[][] cells = new CookingCell[3][7];

    private Week week;

    private DAOUtils db;
    private View.OnLongClickListener onCellLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
                // On lance l'activite de modification d'un exercice
                Intent editMealActivite = new Intent(CookingGrid.this,
                        EditMeal.class);
            int column = Integer.parseInt(view.getContentDescription().charAt(0) + "");
            int row = Integer.parseInt(view.getContentDescription().charAt(2) + "");
            editMealActivite.putExtra("com.annef.mycookingplanning.meal.desc",cells[column][row].getText());
            editMealActivite.putExtra("com.annef.mycookingplanning.meal.row",row);
            editMealActivite.putExtra("com.annef.mycookingplanning.meal.column",column);
                // Puis on lance l'intent !
                startActivityForResult(editMealActivite, 0);
            return false;
        }

    };
    private TextView weekNumberText;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        String newMeal = data.getStringExtra("com.annef.mycookingplanning.meal.desc");
        int row = data.getIntExtra("com.annef.mycookingplanning.meal.row", 0);
        int column = data.getIntExtra("com.annef.mycookingplanning.meal.column", 0);
         Log.d("annef", "desc = " + newMeal + " row = " + row + "+ column " + column);
        Toast.makeText(this, "Saving Meal "+newMeal, Toast.LENGTH_SHORT).show();
        cells[column][row].setText(newMeal);
        week.setMeal(row, column, newMeal);
        db.updateWeek(week);
    }


    private class CookingCell {
        private TextView textView;

        public CookingCell() {

        }

        /**
         * Cette méthode permet d'ajouter un textView
         *
         * @param label
         * @param row
         * @param column
         * @return
         */
        public TextView createTextView(int column, int row, String label) {
            textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setText(label);
            textView.setBackgroundColor(Color.WHITE);
            textView.setContentDescription(column + "," + row);
            textView.setPadding(5, 5, 5, 5);
            setLayoutAndBorder(textView,
                    new GridLayout.LayoutParams(
                            GridLayout.spec(row, GridLayout.FILL),
                            GridLayout.spec(column, GridLayout.FILL)),
                    false, true, true, false);

            textView.setOnLongClickListener(onCellLongClickListener);
            return textView;
        }

        public void setText(String text) {
            textView.setText(text);
        }

        public String getText() {
        return textView.getText()+"";
        }

    }

    private int weekNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_grid);

         db = new DAOUtils(this);

        myGridLayout = (GridLayout) findViewById(R.id.grid);


        Calendar today = Calendar.getInstance();


        if (getIntent().getAction().equals("android.intent.action.MAIN")){
            // 1st call, as Main Activity
            weekNumber = today.get(Calendar.WEEK_OF_YEAR);
        }
        else {
            weekNumber = getIntent().getExtras().getInt("com.annef.workoutchrono.weeknumber");
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
        buildDayColumn();
        buildTextView();

        updateWeek();
    }

    private void updateWeek() {
        week = db.read(weekNumber);
        Log.d("annef", "reading week week n" + weekNumber);
        if (week == null){
            Log.d("annef", "can't find week n"+weekNumber+" in db, creating one ...");
            week = new Week(weekNumber);
            db.create(week);
        }
        updateGrid(week);
    }

    private void updateGrid(Week week) {

            for (int i = 1; i < 3; i++)
                for (int day = 0; day < 7; day++) {
                    cells[i][day].setText(week.getDayText(i, day));
                    Log.d("annef", "meal : "+week.getDayText(i, day));
                }
            weekNumberText.setText("Week : "+week.getNumber());
    }

    private void buildTextView() {
        for (int i=1; i<3; i++)
            for (int day=0; day<7; day++){
                CookingCell cell = new CookingCell();
                myGridLayout.addView(cell.createTextView(i, day, ""));
                cells[i][day]=cell;
            }
 }


    private void setLayoutAndBorder(View view, GridLayout.LayoutParams params,
                                    boolean borderBottom, boolean borderTop, boolean borderLeft, boolean borderRight) {
        params.bottomMargin = borderBottom ? 1 : 0;
        params.leftMargin = borderLeft ? 1 : 0;
        params.rightMargin = borderRight ? 1 : 0;
        params.topMargin = borderTop ? 1 : 0;
        view.setLayoutParams(params);
    }

    private void buildDayColumn() {
        //create day column
        // for now it starts on Monday
        // but evolution will configure starting day
        for (int day=0; day<7; day++){
            CookingCell cell = new CookingCell();
            myGridLayout.addView(cell.createTextView(0, day, getResources().getStringArray(R.array.Jours)[day]));
            cells[0][day]=cell;

        }
    /*    myGridLayout.addView(createTextView("Lundi", 1, 0, Color.LTGRAY, false, false));
        myGridLayout.addView(createTextView("Mardi", 2, 0, Color.LTGRAY, false, false));
        myGridLayout.addView(createTextView("Mercredi", 3, 0, Color.LTGRAY, false, false));
        myGridLayout.addView(createTextView("Jeudi", 4, 0, Color.LTGRAY, false, false));
        myGridLayout.addView(createTextView("Vendredi", 5, 0, Color.LTGRAY, false, false));
        myGridLayout.addView(createTextView("Samedi", 6, 0, Color.LTGRAY, false, false));
        myGridLayout.addView(createTextView("Dimanche", 7, 0, Color.LTGRAY, false, false));
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cooking_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
