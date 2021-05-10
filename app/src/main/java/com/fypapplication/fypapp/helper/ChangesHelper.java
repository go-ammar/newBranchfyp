package com.fypapplication.fypapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fypapplication.fypapp.models.ChangesDue;
import com.fypapplication.fypapp.sharedprefs.SharedPrefs;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChangesHelper {
    private static final String TAG = "ChangesHelper";
    public static String DATE_PATTERN = "yyyy-MM-dd";
    Context context;

    public ChangesHelper(Context context) {
        this.context = context;
    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN, Locale.US);
        return df.format(c.getTime());
    }

    public static int checkDays(String startDate) {

        DateFormat formatter = new SimpleDateFormat(DATE_PATTERN, Locale.US);
        Date startingDate = new Date();
        Date endDate = new Date();
        try {
            startingDate = formatter.parse(startDate);
            endDate = formatter.parse(getDate());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        long different = endDate.getTime() - startingDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        return (int) elapsedDays;
    }


    public void scheduler() {

        SharedPrefs sharedPrefs = new SharedPrefs(context);

        for (int i = 0; i < sharedPrefs.getChangesList().size(); i++) {

            if (checkDays(sharedPrefs.getChangesList().get(i).dateOfChange) > sharedPrefs.getChangesList().get(i).duration) {
                Log.d(TAG, "scheduler: chal beta chuti kr");
                //set notification ke bhaia change krwa lo jo bhi hai
                
            }
        }
    }

//    public boolean addUpdateChange(ChangesDue changesDue) {
//
//
//        if (changesDue.id.equals("-1")) {
//            // New Add Note
//            Gson gson = new Gson();
//            try {
//                changesDue.id = getCurrentTimeInMillis();
//                String str = gson.toJson(changesDue);
//                JSONObject changesObj = new JSONObject(str);
//                changesObj.put(changesDue.typeOfChange, changesObj);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return false;
//            }
//
//        } else {
//            // Update Note
//            Gson gson = new Gson();
//            String str = gson.toJson(changesDue);
//            try {
//                JSONObject notesObj = new JSONObject(str);
//                Log.d(TAG, "addUpdateNote: before " + notesObject);
//
//                notesObject.put(changesDue.getId(), notesObj);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//
//        Log.d(TAG, "addUpdateNote: after " + notesObject);
//        // Finally Saving the Modified Object
//        sharedPrefs.putJsonObject("notes", notesObject);
//
//        return true;
//    }


}
