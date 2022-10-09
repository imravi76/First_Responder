package com.firstresponder.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.firstresponder.app.Models.Contact;
import com.firstresponder.app.Models.Rule;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "appdb";
    private static final int DB_VERSION = 1;

    //tables
    //private static final String TABLE_CONTACT = "contacts";
    private static final String TABLE_RULES = "rules";
    private static final String TABLE_APPS = "apps";

    //columns
    private static final String ID_COL = "id";
    //private static final String NAME_COL = "name";
    //private static final String CONTACT_COL = "contact";

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

        /*String query1 = "CREATE TABLE " + TABLE_CONTACT + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + CONTACT_COL + " TEXT)";*/

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

        //sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
        sqLiteDatabase.execSQL(query3);

    }

    /*public void addNewContact(String contactName, String contactNumber) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_COL, contactName);
        values.put(CONTACT_COL, contactNumber);

        db.insert(TABLE_CONTACT, null, values);
        //db.close();
    }*/

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

    /*public List<Contact> search(String keyword) {
        List<Contact> contacts = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_CONTACT + " where " + NAME_COL + " like ?", new String[] { "%" + keyword + "%" });
            if (cursor.moveToFirst()) {
                contacts = new ArrayList<Contact>();
                do {
                    Contact contact = new Contact();
                    contact.setId(cursor.getInt(0));
                    contact.setName(cursor.getString(1));
                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            contacts = null;
        }
        return contacts;
    }*/

    // we have created a new method for reading all the courses.
    public ArrayList<Rule> readRules() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorRules = db.rawQuery("SELECT * FROM " + TABLE_RULES, null);

        // on below line we are creating a new array list.
        ArrayList<Rule> ruleModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorRules.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                ruleModalArrayList.add(new Rule(cursorRules.getInt(0),
                        cursorRules.getString(1),
                        cursorRules.getString(2),
                        cursorRules.getString(3),
                        cursorRules.getString(4),
                        cursorRules.getString(5),
                        cursorRules.getString(6)));
            } while (cursorRules.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorRules.close();
        return ruleModalArrayList;
    }

}
