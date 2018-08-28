package nickvenis.sherbert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nick on 12/12/2017.
 */

public class sqlhelperdeadlift extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "deadlift.db";
    private static final int DATABASE_VERSION = 1;

    public static final String DL_TABLE_NAME = "deadlift";
    public static final String DL_COLUMN_ID = "_id";
    public static final String DL_COLUMN_DATE = "date";
    public static final String DL_COLUMN_WEIGHT = "weight";
    public static final String DL_COLUMN_REPS = "reps";

    public sqlhelperdeadlift(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + DL_TABLE_NAME +
                        "(" + DL_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        DL_COLUMN_DATE + " STRING, " +
                        DL_COLUMN_WEIGHT + " INTEGER, " +
                        DL_COLUMN_REPS + " INTEGER)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DL_TABLE_NAME);
        onCreate(db);
    }


    public int getHighestVal(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT MAX("+DL_COLUMN_WEIGHT+") FROM "+DL_TABLE_NAME, null);
        c.moveToFirst();
        return c.getInt(0);
    }
    public Cursor getallinfo(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+DL_TABLE_NAME,null);
        return res;
    }
    public boolean addrecord(String date, int weight, int reps){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DL_COLUMN_DATE, date);
        cv.put(DL_COLUMN_WEIGHT, weight);
        cv.put(DL_COLUMN_REPS, reps);
        long result  = db.insert(DL_TABLE_NAME,null,cv);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

}