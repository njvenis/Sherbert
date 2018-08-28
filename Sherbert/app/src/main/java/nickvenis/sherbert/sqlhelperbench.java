package nickvenis.sherbert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nick on 12/12/2017.
 */

public class sqlhelperbench extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bench.db";
    private static final int DATABASE_VERSION = 1;

    public static final String BENCH_TABLE_NAME = "bench";
    public static final String BENCH_COLUMN_ID = "_id";
    public static final String BENCH_COLUMN_DATE = "date";
    public static final String BENCH_COLUMN_WEIGHT = "weight";
    public static final String BENCH_COLUMN_REPS = "reps";

    public sqlhelperbench(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + BENCH_TABLE_NAME +
                        "(" + BENCH_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        BENCH_COLUMN_DATE + " STRING, " +
                        BENCH_COLUMN_WEIGHT + " INTEGER, " +
                        BENCH_COLUMN_REPS + " INTEGER)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BENCH_TABLE_NAME);
        onCreate(db);
    }


    public int getHighestVal(){
        SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT MAX("+BENCH_COLUMN_WEIGHT+") FROM "+BENCH_TABLE_NAME, null);
            c.moveToFirst();
            return c.getInt(0);
    }
    public Cursor getallinfo(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+BENCH_TABLE_NAME,null);
        return res;
    }
    public boolean addrecord(String date, int weight, int reps){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BENCH_COLUMN_DATE, date);
        cv.put(BENCH_COLUMN_WEIGHT, weight);
        cv.put(BENCH_COLUMN_REPS, reps);
        long result  = db.insert(BENCH_TABLE_NAME,null,cv);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

}
