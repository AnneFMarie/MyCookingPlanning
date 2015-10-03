package com.annef.mycookingplanning;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.annef.mycookingplanning.dao.DAOUtils;
import com.annef.mycookingplanning.model.Meal;

public class SetMeal extends AppCompatActivity {


    private Context context = this;
    private EditText text;
    private DAOUtils db;


    private SimpleCursorAdapter dbAdapter;

    int row;
    int column;
    String desc;

    private ListView liste;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(this, "Workout Sauvegardé", Toast.LENGTH_SHORT).show();

        Log.d("annef", "on Store Meal Result");

        dbAdapter.notifyDataSetChanged();
        dbAdapter.swapCursor(db.getReadAllMealCursor());

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_meal);

        db = new DAOUtils(this);

        row = getIntent().getExtras().getInt("com.annef.mycookingplanning.meal.row");
        column = getIntent().getExtras().getInt("com.annef.mycookingplanning.meal.column");
        desc = getIntent().getExtras().getString("com.annef.mycookingplanning.meal.desc");

        text = (EditText) findViewById(R.id.editText);
        if (!desc.equals("-") && !desc.equals("empty") && !desc.equals(context.getResources().getString(R.string.empty_meal)))
            text.setText(desc);

        liste = (ListView) findViewById(R.id.mealList);

        dbAdapter = db.getMealAdapter(this);
        liste.setAdapter(dbAdapter);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) liste.getItemAtPosition(i);
                text.setText(cursor.getString(1));
            }
        });
        liste.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //long click = edit meal
                return false;
            }
        });


        // Bouton Annuler
        Button dismiss = (Button) findViewById(R.id.buttonDeleteMeal);
        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();

            }
        });

        // Bouton Annuler
        ImageButton store = (ImageButton) findViewById(R.id.buttonEditMeal);
        store.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String mealName = text.getText().toString();
                if (! mealName.equals("") ) {


                    if (! db.isPresent(mealName)) {
                        Meal meal = new Meal(0, mealName, "None", 0, 0, 0, 0);

                        meal.setId(db.create(meal));


                        Log.d("annef", "meal "+mealName+" stored");

                        dbAdapter.notifyDataSetChanged();
                        dbAdapter.swapCursor(db.getReadAllMealCursor());
                    }
                }

               /* Intent editMealActivite = new Intent(SetMeal.this,
                        EditMeal.class);

                editMealActivite.putExtra("com.annef.mycookingplanning.meal.desc", text.getText());
                // Puis on lance l'intent !
                startActivityForResult(editMealActivite, 0);
*/
            }
        });

        // Bouton Sauver
        Button save = (Button) findViewById(R.id.buttonSaveMeal);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // db.updateMeal(desc, row, column);
                desc = text.getText().toString();

                Intent result = new Intent();
                setResult(RESULT_OK, result);
                result.putExtra("com.annef.mycookingplanning.meal.desc",desc);
                result.putExtra("com.annef.mycookingplanning.meal.row",row);
                result.putExtra("com.annef.mycookingplanning.meal.column",column);
                finish();


            }

        });
    }

    @Override
    public void onBackPressed() {
        desc = text.getText().toString();

        Intent result = new Intent();
        setResult(RESULT_OK, result);
        result.putExtra("com.annef.mycookingplanning.meal.desc",desc);
        result.putExtra("com.annef.mycookingplanning.meal.row",row);
        result.putExtra("com.annef.mycookingplanning.meal.column", column);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_meal, menu);
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
        if (id == R.id.action_edit) {
            Intent editMealActivite = new Intent(SetMeal.this,
                    EditMeal.class);

            editMealActivite.putExtra("com.annef.mycookingplanning.meal.desc", text.getText());
            // Puis on lance l'intent !
            startActivityForResult(editMealActivite, 0);
        }

        return super.onOptionsItemSelected(item);
    }
}
