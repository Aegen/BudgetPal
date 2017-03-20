package com.example.aegis.budgpal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Harrison on 2/25/2017.
 */

public class Event {


    private long eventID = -1;
    private long userID;
    private String lastModified;
    private String startDate;
    private String endDate;
    private String description;
    private boolean deleted;
    private DatabaseHandler handler;

    public Event(Context context){this.handler = new DatabaseHandler(context, "database", null, 1);}

    public Event(long UID, String start, String end, String Description, Context context){
        this.userID = UID;
        this.lastModified = StatUtils.GetCurrentDate();
        this.startDate = start;
        this.endDate = end;
        this.description = Description;
        this.deleted = false;
        this.handler = new DatabaseHandler(context, "database", null, 1);
    }


    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void pushToDatabase(){
        if(eventID == -1) {
            this.handler.addEvent(this);
        }else{
            this.handler.updateEvent(this);
        }
    }

    public static ArrayList<Event> getEventsByUserID(Context context, long UserID){
        SQLiteDatabase db = StatUtils.GetDatabase(context);
        ArrayList<Event> output = new ArrayList<Event>();

        Cursor curs = db.rawQuery("SELECT * FROM Event WHERE UserID = " + UserID + " AND Deleted = 0 ORDER BY StartDate DESC", null);

        if(curs.getCount() > 0) {
            curs.moveToFirst();
            while(!curs.isAfterLast()){
                Event tempEv = new Event(context);
                tempEv.setEventID(curs.getLong(curs.getColumnIndex("EventID")));
                tempEv.setDeleted(false);
                tempEv.setLastModified(curs.getString(curs.getColumnIndex("LastModified")));
                tempEv.setDescription(curs.getString(curs.getColumnIndex("Description")));
                tempEv.setUserID(UserID);
                tempEv.setStartDate(curs.getString(curs.getColumnIndex("StartDate")));
                tempEv.setEndDate(tempEv.getStartDate());
                curs.moveToNext();

                output.add(tempEv);
            }
        }

        curs.close();

        return output;
    }

    public static Event getEventByEventID(Context context, long EventID){
        SQLiteDatabase db = StatUtils.GetDatabase(context);

        Cursor curs = db.rawQuery("SELECT * FROM Event WHERE EventID = " + EventID, null);
        curs.moveToFirst();
        Log.d("other id:", Long.toString(EventID));
        Event tempEv = new Event(context);
        tempEv.setEventID(EventID);
        tempEv.setDeleted(false);
        tempEv.setLastModified(curs.getString(curs.getColumnIndex("LastModified")));
        tempEv.setDescription(curs.getString(curs.getColumnIndex("Description")));
        tempEv.setUserID(curs.getLong(curs.getColumnIndex("UserID")));
        tempEv.setStartDate(curs.getString(curs.getColumnIndex("StartDate")));
        tempEv.setEndDate(tempEv.getStartDate());
        Log.d("Description", tempEv.getDescription());
        curs.close();
        return tempEv;
    }

}
