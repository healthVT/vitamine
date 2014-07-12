package sqlite;

/**
 * Created by Jay on 7/11/14.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import beans.VitaminBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jay on 4/14/14.
 */
public class Database extends SQLiteOpenHelper {
    //Database version
    private static final int DATABASE_VERSION = 3;

    //Database name
    private static final String DATABASE_NAME = "projectVT";

    //Table Name
    private static final String TABLE_NAME = "vitaminData";

    private static final String CREATE_VITAMIN_TABLE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, logDate TEXT, " +
            "foodName TEXT, vitaminA REAL, vitaminC REAL, vitaminD REAL, vitaminE REAL, vitaminK REAL, vitaminB1 REAL, vitaminB2 REAL, " +
            "vitaminB3 REAL, vitaminB6 REAL, vitaminB12 REAL, amount INTEGER)";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_VITAMIN_TABLE);
    }

    public boolean updateVitaminData(VitaminBean vitaminLog){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("foodName", vitaminLog.getFoodName());
        values.put("vitaminA", vitaminLog.getVitaminA());
        values.put("vitaminC", vitaminLog.getVitaminC());
        values.put("vitaminD", vitaminLog.getVitaminD());
        values.put("vitaminE", vitaminLog.getVitaminE());
        values.put("vitaminK", vitaminLog.getVitaminK());
        values.put("vitaminB1", vitaminLog.getVitaminB1());
        values.put("vitaminB2", vitaminLog.getVitaminB2());
        values.put("vitaminB3", vitaminLog.getVitaminB3());
        values.put("vitaminB6", vitaminLog.getVitaminB6());
        values.put("vitaminB12", vitaminLog.getVitaminB12());
        values.put("logDate", getToday());

        long id = db.insert(TABLE_NAME, null, values);
        Log.e("inserted", String.valueOf(id));
        db.close();

        return true;
    }

    public List<VitaminBean> getVitaminData(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<VitaminBean> vitaminDataList = new ArrayList<VitaminBean>();

        String today = getToday();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            vitaminDataList.add(new VitaminBean(c.getString(c.getColumnIndex("foodName")), c.getDouble(c.getColumnIndex("vitaminA")),
                    c.getDouble(c.getColumnIndex("vitaminC")), c.getDouble(c.getColumnIndex("vitaminD")), c.getDouble(c.getColumnIndex("vitaminE")),
                    c.getDouble(c.getColumnIndex("vitaminK")), c.getDouble(c.getColumnIndex("vitaminB1")), c.getDouble(c.getColumnIndex("vitaminB2")),
                    c.getDouble(c.getColumnIndex("vitaminB3")), c.getDouble(c.getColumnIndex("vitaminB6")), c.getDouble(c.getColumnIndex("vitaminB12")), c.getInt(c.getColumnIndex("amount"))
            ));
        }
        db.close();
        return vitaminDataList;
    }

    public VitaminBean getVitaminBeanByFoodName(String foodName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE foodName=" + foodName + " LIMIT 1";
        Cursor c = db.rawQuery(query, null);

        VitaminBean vitaminBean = null;
        if(c != null){
            vitaminBean = new VitaminBean(c.getString(c.getColumnIndex("foodName")), c.getDouble(c.getColumnIndex("vitaminA")),
                    c.getDouble(c.getColumnIndex("vitaminC")), c.getDouble(c.getColumnIndex("vitaminD")), c.getDouble(c.getColumnIndex("vitaminE")),
                    c.getDouble(c.getColumnIndex("vitaminK")), c.getDouble(c.getColumnIndex("vitaminB1")), c.getDouble(c.getColumnIndex("vitaminB2")),
                    c.getDouble(c.getColumnIndex("vitaminB3")), c.getDouble(c.getColumnIndex("vitaminB6")), c.getDouble(c.getColumnIndex("vitaminB12")), c.getInt(c.getColumnIndex("amount"))
            );
        }
        db.close();

        return vitaminBean;
    }

    public void deleteByFoodName(String foodName){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "foodName = '" + foodName + "'";
        db.delete(TABLE_NAME, where, null);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null && db.isOpen()){
            db.close();
        }
    }

    public String getToday(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat dateFormation = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

        return dateFormation.format(cal.getTime());
    }

}
