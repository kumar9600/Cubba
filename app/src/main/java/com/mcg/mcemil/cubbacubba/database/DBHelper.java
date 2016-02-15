package com.mcg.mcemil.cubbacubba.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mcg.mcemil.cubbacubba.ui.Components.Inbox;

import org.jivesoftware.smack.packet.Message;


import java.util.ArrayList;

/**
 * Created by mcemil on 26.11.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME   = "zibamDB";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // User table
    private static final String TABLE_USERS = "users";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";

    //Inbox table
    private static final String TABLE_INBOX = "inbox";
    private static final String INBOX_ID = "inbox_id";
    private static final String INBOX_TO = "inbox_to";
    private static final String INBOX_THREAD_ID = "inbox_thread_id";

    //Message table
    private static final String TABLE_MESSAGE = "messages";
    private static final String MESSAGE_ID = "message_id";
    private static final String MESSAGE_BODY = "message_body";
    private static final String MESSAGE_DATE = "message_date";
    private static final String MESSAGE_FROM = "message_from";
    private static final String MESSAGE_TO = "message_to";
    private static final String MESSAGE_THREAD_ID = "message_thread_id";

    //Table create statements
    //user table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + USER_ID + " INTEGER PRIMARY KEY," + USER_NAME
            + " TEXT" +");";

    //Inbox table
    private static final String CREATE_TABLE_INBOX = "CREATE TABLE "
            + TABLE_INBOX + "(" + INBOX_ID + " INTEGER PRIMARY KEY," + INBOX_TO
            + " TEXT," + INBOX_THREAD_ID + " TEXT" +");";

    //Messages table
    private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE "
            + TABLE_MESSAGE + "(" + MESSAGE_ID + " INTEGER PRIMARY KEY," + MESSAGE_BODY
            + " TEXT," + MESSAGE_DATE + " DATETIME," + MESSAGE_FROM + " TEXT," + MESSAGE_TO + " TEXT,"
            + MESSAGE_THREAD_ID + " TEXT" +");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_INBOX);
        db.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INBOX);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);

        //create new tables
        onCreate(db);
    }

    /**
     * Create a user
     * @param username
     * @return
     */
    public long createUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, username);

        //insert row
        long user_id = db.insert(TABLE_USERS, null, values);

        return user_id;
    }

    /**
     * get single user
     * @param user_id
     * @return
     */
    public String getUser(long user_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + USER_ID + " = " + user_id;

        Log.e(DATABASE_NAME, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return  c.getString(c.getColumnIndex(USER_NAME));
    }


    /**
     * get all users
     * @return
     */
    public ArrayList<String> getAllUsers(){
        ArrayList<String> users = new ArrayList<String >();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        Log.e(DATABASE_NAME, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String username = new String();
                username = c.getString((c.getColumnIndex(USER_NAME)));

                // adding to user list
                users.add(username);
            } while (c.moveToNext());
        }

        return users;
    }

    /**
     * Deleting a user
     */
    public void deleteUser(long user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, USER_ID + " = ?",
                new String[]{String.valueOf(user_id)});
    }

    /**
     * Create a inbox
     */
    public long createInbox(Inbox inbox){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INBOX_TO, inbox.getInboxName());
        values.put(INBOX_THREAD_ID, inbox.getThreadID());

        //insert row
        long inbox_id = db.insert(TABLE_INBOX, null, values);

        return inbox_id;
    }

    /**
     * Get inbox id
     */
    public String getThreadID(String inbox_name){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_INBOX + " WHERE "
                + INBOX_TO + " = " + "'" + inbox_name +"'";

        Log.e(DATABASE_NAME, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        String threadID = c.getString(c.getColumnIndex(INBOX_THREAD_ID));

        Log.e("getInbox", "id: " + threadID);
        return threadID;
    }

    public void deleteInbox(Inbox inbox){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INBOX, INBOX_TO + " = ?",
                new String[]{String.valueOf(inbox.getInboxName())});
    }

    /**
     * Getting all inbox
     */
    public ArrayList<Inbox> getAllInbox(){
        ArrayList<Inbox> inbox = new ArrayList<Inbox>();
        String selectQuery = "SELECT  * FROM " + TABLE_INBOX;

        Log.e(DATABASE_NAME, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Inbox i = new Inbox(c.getString((c.getColumnIndex(INBOX_TO))),c.getString((c.getColumnIndex(INBOX_THREAD_ID))));

                // adding to todo list
                inbox.add(i);
            } while (c.moveToNext());
        }
        return inbox;
    }

    /**
     * Getting all messages by inbox
     */
    public ArrayList<Message> getAllMessagesByInbox(String threadID) {
        ArrayList<Message> messages = new ArrayList<Message>();

        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGE + " WHERE "
                + MESSAGE_THREAD_ID + " = " + "'" + threadID + "'";

        Log.e(DATABASE_NAME, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Message m = new Message();
                m.setFrom((c.getString(c.getColumnIndex(MESSAGE_FROM))));
                m.setBody(c.getString(c.getColumnIndex(MESSAGE_BODY)));
                m.setTo(c.getString(c.getColumnIndex(MESSAGE_TO)));

                // adding to message list
                messages.add(m);
            } while (c.moveToNext());
        }

        return messages;
    }
    /**
     * create message
     * @param m
     * @param threadID
     * @return
     */

    public long createMessage(Message m, String threadID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MESSAGE_BODY, m.getBody());
        values.put(MESSAGE_FROM, m.getFrom());
        values.put(MESSAGE_TO, m.getTo());
        values.put(MESSAGE_THREAD_ID, threadID);

        // insert row
        long message_id = db.insert(TABLE_MESSAGE, null, values);

        return message_id;
    }

}