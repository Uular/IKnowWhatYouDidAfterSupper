package fi.oulu.cse.iknowwhatyoudidaftersupper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 4);
    }

    // Database Name
    private static final String DATABASE_NAME = "SupperManager";

    // table names
    private static final String TABLE_OPENTASKS = "opentasks";
    private static final String TABLE_MYTASKS = "mytasks";
    private static final String TABLE_GROCERIES = "groceries";
    private static final String TABLE_MEALS = "meals";
    private static final String TABLE_CHAT = "chat";


    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_GROCERY = "item";
    private static final String KEY_MYTASK = "MyTask";
    private static final String KEY_OPENTASK = "openTask";
    private static final String KEY_TIME = "mealTime";
    private static final String KEY_NICK = "nick";
    private static final String KEY_MESSAGE = "msg";
    private static final String KEY_MSG_TIME = "time";

    //Groceries create statement
    private static String CREATE_GROCERIES_TABLE = "CREATE TABLE " + TABLE_GROCERIES + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GROCERY +  ")";

    String CREATE_MYTASKS_TABLE = "CREATE TABLE " + TABLE_MYTASKS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MYTASK +  ")";

    String CREATE_OPENTASKS_TABLE = "CREATE TABLE " + TABLE_OPENTASKS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_OPENTASK +  ")";

    String CREATE_MEAL_TABLE = "CREATE TABLE " + TABLE_MEALS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME +  ")";

    String CREATE_CHAT_TABLE = "CREATE TABLE " + TABLE_CHAT + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_MSG_TIME + " INTEGER,"
            + KEY_MESSAGE + " TEXT,"
            + KEY_NICK + " TEXT)";



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_GROCERIES_TABLE);
        db.execSQL(CREATE_OPENTASKS_TABLE);
        db.execSQL(CREATE_MYTASKS_TABLE);
        db.execSQL(CREATE_MEAL_TABLE);
        db.execSQL(CREATE_CHAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS opentasks");
        db.execSQL("DROP TABLE IF EXISTS mytasks");
        db.execSQL("DROP TABLE IF EXISTS groceries");
        db.execSQL("DROP TABLE IF EXISTS meals");
        db.execSQL("DROP TABLE IF EXISTS chat");
        onCreate(db);
    }

    public boolean insertGroceries  (String grocery)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GROCERY, grocery);
        db.insert(TABLE_GROCERIES, null, contentValues);
        return true;
    }
    public boolean insertMealTime  (String time)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TIME, time);
        db.insert(TABLE_MEALS, null, contentValues);
        return true;
    }
    public boolean insertMyTask  (String MyTask)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MYTASK, MyTask);
        db.insert(TABLE_MYTASKS, null, contentValues);
        return true;
    }
    public boolean insertOpenTask  (String OpenTask)
    {
        isFilled();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_OPENTASK, OpenTask);
        db.insert(TABLE_OPENTASKS, null, contentValues);
        return true;
    }

    public boolean insertMessage(String msg, String nick) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NICK, nick);
        cv.put(KEY_MESSAGE, msg);
        cv.put(KEY_MSG_TIME, Calendar.getInstance().getTimeInMillis());
        db.insert(TABLE_CHAT, null, cv);
        return true;
    }

    public boolean isFilled() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from " + TABLE_OPENTASKS + " where " + KEY_OPENTASK + " = 'Groceries'";
        String query2 = "Select * from " + TABLE_MYTASKS + " where " + KEY_MYTASK + " = 'Groceries'";
        Cursor cursor = db.rawQuery(query, null);
        Cursor cursor2 = db.rawQuery(query2, null);
        if (cursor.getCount() == 0 && cursor2.getCount() == 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        cursor2.close();
        return true;
    }


    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfGroceryRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_GROCERIES);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean deleteGrocery (String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_GROCERIES, KEY_GROCERY+"="+"'"+name+"'", null) > 0;

    }
    public boolean deleteMealTime (String time)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MEALS, KEY_TIME+"="+"'"+time+"'", null) > 0;

    }
    public boolean deleteAllGrocery ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_GROCERIES, null, null) > 0;

    }
    public boolean deleteMyTask(String task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MYTASKS, KEY_MYTASK+"="+"'"+task+"'", null) > 0;
    }
    public boolean deleteOpenTask(String task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OPENTASKS, KEY_OPENTASK+"="+"'"+task+"'", null) > 0;
    }

    public ArrayList<String> getAllGroceries()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from groceries", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(KEY_GROCERY)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getAllMyTasks()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from mytasks", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(KEY_MYTASK)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getAllOpenTasks()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from opentasks", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(KEY_OPENTASK)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getAllMealTimes() {
        ArrayList<String> array_list = new ArrayList<String>();

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.set(Calendar.HOUR_OF_DAY, 23);
        tomorrow.set(Calendar.MINUTE, 59);

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from meals where mealTime > ?", new String[]{
                Long.toString(Calendar.getInstance().getTimeInMillis())});
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(KEY_TIME)));
            res.moveToNext();
        }
        return array_list;
    }

    public List<String> getMessages() {
        ArrayList<String> chat_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery("select time, nick, msg from chat order by time", null);

        while (cur.moveToNext()) {
            final long time = cur.getLong(0);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            final String nick = cur.getString(1);
            final String msg = cur.getString(2);
            final String message = String.format(Locale.US,
                    "%02d:%02d:%02d <%s> %s\n",
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    c.get(Calendar.SECOND),
                    nick,
                    msg);
            chat_list.add(message);
        }
        cur.close();
        return chat_list;
    }
}