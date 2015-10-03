package com.annef.mycookingplanning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.annef.mycookingplanning.dao.DAOUtils;
import com.annef.mycookingplanning.model.Meal;
import com.annef.mycookingplanning.model.Season;

public class EditMeal extends AppCompatActivity {

    private DAOUtils db;
    private Meal meal;
    private EditText name;
    private AdapterView.OnItemSelectedListener onSpinnerItemListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (id == 0) {
                season = Season.All;
            }
            if (id == 1) {
                season = Season.Printemps;
            }
            if (id == 2) {
                season = Season.Ete;
            }
            if (id == 3) {
                season = Season.Automne;
            }
            if (id == 4) {
                season = Season.Hiver;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }

    };

    private Season season;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);

        db = new DAOUtils(this);

        Spinner spinner = (Spinner) findViewById(R.id.seasonSpinner);
        //  Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.seasons, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onSpinnerItemListener);


        String mealName= getIntent().getExtras().getString(
                "com.annef.mycookingplanning.meal.desc");
        // Search meal in db
        meal = db.getMealByName(mealName);

        name = (EditText) findViewById(R.id.mealName);
        name.setText(mealName);

        // Bouton Annuler
        Button dismiss = (Button) findViewById(R.id.buttonCancel);
        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();

            }
        });
        // Bouton Sauver
        Button save = (Button) findViewById(R.id.buttonSaveMeal);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (meal == null) {
                    meal = new Meal(0, name.getText().toString(),season.toString(), 0, 0, 0, 0 );

                    meal.setId(db.create(meal));
                }
                else
                if (updateMealData()) {
                    db.update(meal);

                    Intent result = new Intent();
                    setResult(RESULT_OK, result);
                    result.putExtra("com.annef.workoutchrono.meal", meal.getName());
                    finish();
                }

            }
        });

    }

    public boolean updateMealData() {

        meal.setName(name.getText().toString());
        //meal.setType(season.getText().toString());
        meal.setSeason(Season.valueOf(season.toString()));
        return true;

    }

    @Override
    public void onBackPressed() {
        if (updateMealData()) {

            db.update(meal);

            Intent result = new Intent();
            setResult(RESULT_OK, result);
            result.putExtra("com.annef.workoutchrono.meal", meal.getName());
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_meal, menu);
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
        if (id == R.id.action_delete) {
            db.delete(meal);

            Intent result = new Intent();
            setResult(RESULT_OK, result);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
