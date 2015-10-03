package com.annef.mycookingplanning.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.annef.mycookingplanning.model.Meal;
import com.annef.mycookingplanning.model.Week;

/**
 * Created by annefrancoisemarie on 30/09/15.
 */
public class DAOUtils extends SQLiteOpenHelper {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 5;
    // Le nom du fichier qui représente ma base
    protected final static String NOM = "database.db";

    public static final String TABLE_WEEK = "week";
    public static final String TABLE_MEAL = "meal";
    public static final String ID = "id";
    public static final String J1MATIN = "j1Matin";
    public static final String J2MATIN = "j2Matin";
    public static final String J3MATIN = "j3Matin";
    public static final String J4MATIN = "j4Matin";
    public static final String J5MATIN = "j5Matin";
    public static final String J6MATIN = "j6Matin";
    public static final String J7MATIN = "j7Matin";
    public static final String J1SOIR = "j1Soir";
    public static final String J2SOIR = "j2Soir";
    public static final String J3SOIR = "j3Soir";
    public static final String J4SOIR = "j4Soir";
    public static final String J5SOIR = "j5Soir";
    public static final String J6SOIR = "j6Soir";
    public static final String J7SOIR = "j7Soir";
    public static final String[] mealList = { J1MATIN, J2MATIN, J3MATIN, J4MATIN, J5MATIN, J6MATIN, J7MATIN, J1SOIR, J2SOIR, J3SOIR, J4SOIR, J5SOIR, J6SOIR, J7SOIR};
    public static final String WEEKNUMBER = "number";

    public static final String TABLE_FAVORITE = "favorite";
    public static final String NAME = "name";
    public static final String SEASON = "season";
    public static final String RANK = "rank"; // from 1 to 5 - 5 is most used and loved
    public static final String ISVEGETABLE = "isVegetable";
    public static final String ISMEAT = "isMeat";
    public static final String CALORIES = "calories";

    public static final String CREATE_WEEK = "CREATE TABLE " + TABLE_WEEK
            + " (" + WEEKNUMBER + " INTEGER PRIMARY KEY, " +
            J1MATIN + " TEXT," +
            J2MATIN + " TEXT," +
            J3MATIN + " TEXT," +
            J4MATIN + " TEXT," +
            J5MATIN + " TEXT," +
            J6MATIN + " TEXT," +
            J7MATIN + " TEXT," +
            J1SOIR + " TEXT," +
            J2SOIR + " TEXT," +
            J3SOIR + " TEXT," +
            J4SOIR + " TEXT," +
            J5SOIR + " TEXT," +
            J6SOIR + " TEXT," +
            J7SOIR + " TEXT );";

    public static final String CREATE_TABLE_MEAL = "CREATE TABLE " + TABLE_FAVORITE +
            "("  + ID + " INTEGER PRIMARY KEY, "+
            NAME + " TEXT," +
            SEASON + " TEXT," +
            RANK + " TEXT," +
            ISVEGETABLE + " TEXT," +
            ISMEAT + " TEXT," +
            CALORIES+ " TEXT);";

    public static final String DROP_TABLE_WEEK = "DROP TABLE IF EXISTS "
            + TABLE_WEEK + ";";

    public static final String DROP_TABLE_MEAL = "DROP TABLE IF EXISTS "
            + TABLE_FAVORITE + ";";

    public SQLiteDatabase mDb;

    public DAOUtils(Context pContext) {
        super(pContext, NOM, null, VERSION);
        // DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase
        // s'en charge
        mDb = this.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_WEEK);
        sqLiteDatabase.execSQL(CREATE_TABLE_MEAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL(DROP_TABLE_WEEK);
        sqLiteDatabase.execSQL(DROP_TABLE_MEAL);
        onCreate(sqLiteDatabase);
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    public Week readWeek(int weekNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WEEK, new String[]{WEEKNUMBER, J1MATIN, J2MATIN, J3MATIN,
                        J4MATIN, J5MATIN, J6MATIN, J7MATIN, J1SOIR, J2SOIR, J3SOIR, J4SOIR,
                        J5SOIR, J6SOIR, J7SOIR}, WEEKNUMBER
                        + "=?", new String[]{String.valueOf(weekNumber)}, null, null, null,
                null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String[][] meal = new String[3][7];
            for (int column = 1; column < 3; column++)
                for (int day = 0; day < 7; day++) {
                    meal[column][day] = cursor.getString(day + ((column - 1) * 7) + 1);
                    Log.d("annef", "In readWeek DAO method : column = " + column + " day =" + day + " meal =" + meal[column][day]);
                }

            //Week week = new Week(cursor.getLong(0), meal, cursor.getInt(15));
            Week week = new Week(meal, cursor.getInt(0));
            // return contact

            cursor.moveToNext();
            return week;



        }
        return null;
    }

    public Meal readMeal(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITE, new String[]{ID, NAME, SEASON, RANK, ISVEGETABLE, ISMEAT, CALORIES}, ID
                        + "=?", new String[]{String.valueOf(id)}, null, null, null,
                null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            Meal meal = new Meal(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                    cursor.getInt(4), cursor.getInt(5), cursor.getInt(6));

            cursor.moveToNext();
            return meal;



        }
        return null;
    }


    public int updateWeek(Week week) {

        ContentValues values = new ContentValues();
        for (int column = 1; column < 3; column++)
            for (int day = 0; day < 7; day++) {
                values.put(mealList[day + (7 * (column - 1))], week.getDayText(column, day));
            }

        int number = week.getNumber();
        // updating row
        Log.d("com.annef.workoutchrono", "update week " + number);

        if (mDb == null){
            mDb = this.getWritableDatabase();
        }
        int update = mDb.update(TABLE_WEEK, values,
                WEEKNUMBER + " = " + number, null);

        return update;
    }

    public long create(Week week) {
        ContentValues values = new ContentValues();
        for (int column = 1; column < 3; column++)
            for (int day = 0; day < 7; day++) {
                values.put(mealList[day + (7 * (column - 1))], week.getDayText(column, day));
            }

        int number = week.getNumber();
        values.put(WEEKNUMBER, number);
        // updating row
        Log.d("com.annef.workoutchrono", "create week " +number);

        if (mDb == null){
            mDb = this.getWritableDatabase();
        }
        long create = mDb.insert(TABLE_WEEK, null, values);

        return create;
    }


    public long create(Meal meal) {

        ContentValues values = new ContentValues();
        values.put(NAME, meal.getName());
        values.put(SEASON, meal.getSeason().toString());

        long create = mDb.insert(TABLE_FAVORITE, null, values);

        return create;
    }

    public Cursor getReadAllMealCursor() {
        mDb = getReadableDatabase();

        Cursor cursor = mDb.rawQuery("Select id as _id, name from "
                + TABLE_FAVORITE, null);
        //close();
        return cursor;
    }

    public SimpleCursorAdapter getMealAdapter(Context context) {

        Cursor cursor = getReadAllMealCursor();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                android.R.layout.simple_list_item_1, cursor, new String[] { NAME },
                new int[] {android.R.id.text1 }, 0);

        return adapter;
    }

    public int update(Meal meal) {

        ContentValues values = new ContentValues();

        Log.d("annef.workout", "updating exercise ");

        values.clear();
        values.put(NAME, meal.getName());
        values.put(SEASON, meal.getSeason().toString());
        values.put(RANK, meal.getRank());
        values.put(ISVEGETABLE, meal.isVegetable() ? "1" : "0");
        values.put(ISMEAT, meal.isMeat() ? "1" : "0");
        values.put(CALORIES, String.valueOf(meal.getCalories()));

        // updating row
        Log.d("com.annef.workoutchrono", "update meal " + meal.getName());

        // updating row

        return mDb.update(TABLE_FAVORITE, values, ID + " = ?",
                new String[] { String.valueOf(meal.getId()) });
    }

    public void delete(Meal meal) {
        mDb.delete(TABLE_FAVORITE, ID + " = ?",
                new String[]{String.valueOf(meal.getId())});
    }

    public boolean isPresent(String mealName){
        mDb = getReadableDatabase();

        Cursor cursor = mDb
                .rawQuery(
                        "Select "+ID+" as _id, "+NAME+", "+SEASON+", "+RANK+", "+ISVEGETABLE+", "+ISMEAT+", "+CALORIES+" from "
                                + TABLE_FAVORITE + " where "+NAME+" = ?",
                        new String[] { String.valueOf(mealName) });

        return  (cursor.getCount() > 0);
    }

    public Meal getMealByName(String mealName) {
        Meal meal = null;

        mDb = getReadableDatabase();

        Cursor cursor = mDb
                .rawQuery(
                        "Select "+ID+" as _id, "+NAME+", "+SEASON+", "+RANK+", "+ISVEGETABLE+", "+ISMEAT+", "+CALORIES+" from "
                                + TABLE_FAVORITE + " where "+NAME+" = ?",
                        new String[] { String.valueOf(mealName) });

        if (cursor.getCount() > 0){
            meal = new Meal(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4),
                    cursor.getInt(5), cursor.getInt(6));
        }

        return meal;
    }


}
