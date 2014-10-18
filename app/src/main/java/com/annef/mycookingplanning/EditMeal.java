package com.annef.mycookingplanning;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.annef.mycookingplanning.dao.DAOUtils;

/**
 * Created by LAHI8322 on 21/09/2014.
 */
public class EditMeal extends Activity {


    private Context context = this;
    private EditText text;
    private DAOUtils db;

    int row;
    int column;
    String desc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmeal);

        db = new DAOUtils(this);

        row = getIntent().getExtras().getInt("com.annef.mycookingplanning.meal.row");
        column = getIntent().getExtras().getInt("com.annef.mycookingplanning.meal.column");
        desc = getIntent().getExtras().getString("com.annef.mycookingplanning.meal.desc");

        text = (EditText) findViewById(R.id.editText);
        text.setText(desc);


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
}


