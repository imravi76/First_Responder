package com.firstresponder.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.firstresponder.app.Models.Rule;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "appdb";
    private static final int DB_VERSION = 1;

    //tables
    private static final String TABLE_RULES = "rules";
    private static final String TABLE_APPS = "apps";

    //columns
    private static final String ID_COL = "id";

    private static final String PACKAGE_COL = "package";

    //Responder Columns
    private static final String STATUS_COL = "status";
    private static final String MSG_COL = "received_msg";
    //private static final String MATCH_COL = "pattern_matching";
    private static final String REPLY_COL = "reply_message";
    private static final String MSGCOUNT_COL = "reply_number";
    private static final String CONTACTS_COL = "contacts";
    private static final String IGNORED_COL = "ignored_contacts";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query2 = "CREATE TABLE " + TABLE_APPS + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PACKAGE_COL + " TEXT)";

        String query3 = "CREATE TABLE " + TABLE_RULES + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STATUS_COL + " TEXT,"
                + MSG_COL + " TEXT,"
                //+ MATCH_COL + " TEXT,"
                + REPLY_COL + " TEXT,"
                + MSGCOUNT_COL + " TEXT,"
                + CONTACTS_COL + " TEXT,"
                + IGNORED_COL + " TEXT)";

        sqLiteDatabase.execSQL(query2);
        sqLiteDatabase.execSQL(query3);

    }

    public void addNewPackage(String packageName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PACKAGE_COL, packageName);

        db.insert(TABLE_APPS, null, values);
        //db.close();
    }

    public void addNewRule(String ruleStatus, String ruleMsg, String ruleReply, String ruleCount, String ruleContacts, String ruleIgnored){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STATUS_COL, ruleStatus);
        values.put(MSG_COL, ruleMsg);
        values.put(REPLY_COL, ruleReply);
        values.put(MSGCOUNT_COL, ruleCount);
        values.put(CONTACTS_COL, ruleContacts);
        values.put(IGNORED_COL, ruleIgnored);

        db.insert(TABLE_RULES, null, values);
        //db.close();
    }

    public void deleteRule(Integer ruleId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_RULES, "id=?", new String[]{String.valueOf(ruleId)});
        //db.close();
    }

    public void updateRule(Integer ruleId, String ruleStatus) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STATUS_COL, ruleStatus);

        db.update(TABLE_RULES, values, "id=?", new String[]{String.valueOf(ruleId)});
        //db.close();
    }

    public void updateFullRule(Integer ruleId, String ruleStatus, String ruleMsg, String ruleReply, String ruleCount, String ruleContacts, String ruleIgnored) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(STATUS_COL, ruleStatus);
        values.put(MSG_COL, ruleMsg);
        values.put(REPLY_COL, ruleReply);
        values.put(MSGCOUNT_COL, ruleCount);
        values.put(CONTACTS_COL, ruleContacts);
        values.put(IGNORED_COL, ruleIgnored);

        db.update(TABLE_RULES, values, "id=?", new String[]{String.valueOf(ruleId)});
        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RULES);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Rule> readRules() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorRules = db.rawQuery("SELECT * FROM " + TABLE_RULES, null);
        ArrayList<Rule> ruleModalArrayList = new ArrayList<>();

        if (cursorRules.moveToFirst()) {
            do {

                ruleModalArrayList.add(new Rule(cursorRules.getInt(0),
                        cursorRules.getString(1),
                        cursorRules.getString(2),
                        cursorRules.getString(3),
                        cursorRules.getString(4),
                        cursorRules.getString(5),
                        cursorRules.getString(6)));
            } while (cursorRules.moveToNext());
        }

        cursorRules.close();
        return ruleModalArrayList;
    }

    public List<Rule> nonContact(String contact){
        ArrayList<Rule> rules = new ArrayList<>();

            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_RULES + " where status = '1' and received_msg = '*welcome*' and ? not in (ignored_contacts)", new String[]{contact});

            if(cursor.moveToFirst()){

                do {
                    rules.add(new Rule(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)));
                } while (cursor.moveToNext());
            }
        cursor.close();
        return rules;
    }

    public List<Rule> Contact(String contact){
        List<Rule> rules = null;
        try{
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_RULES + " where status = '1' and received_msg = '*' and ignored_contacts != ?", new String[]{contact});
            if(cursor.moveToFirst()){
                rules = new ArrayList<>();
                do {
                    Rule rule = new Rule();
                    rule.setId(cursor.getInt(0));
                    rule.setStatus(cursor.getString(1));
                    rule.setMsg(cursor.getString(2));
                    rule.setReply(cursor.getString(3));
                    rule.setMsgCount(cursor.getString(4));
                    rule.setSpecificContact(cursor.getString(5));
                    rule.setIgnoredContact(cursor.getString(6));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rules;
    }

    public List<Rule> Match(String msg, String contact){
        List<Rule> rules = null;
        try{
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_RULES + " where status = '1' and received_msg = ? and ignored_contacts != ?", new String[]{msg, contact});
            if(cursor.moveToFirst()){
                rules = new ArrayList<>();
                do {
                    Rule rule = new Rule();
                    rule.setId(cursor.getInt(0));
                    rule.setStatus(cursor.getString(1));
                    rule.setMsg(cursor.getString(2));
                    rule.setReply(cursor.getString(3));
                    rule.setMsgCount(cursor.getString(4));
                    rule.setSpecificContact(cursor.getString(5));
                    rule.setIgnoredContact(cursor.getString(6));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rules;
    }

}
