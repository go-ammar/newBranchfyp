package com.electrosoft.electrosoftnew.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.electrosoft.electrosoftnew.models.Login;
import com.electrosoft.electrosoftnew.models.User;
import com.google.gson.Gson;

public class SharedPrefs {

    private static final String MY_PREFS_NAME = "com.sudoware.seedcode.delivery-25-3-2020";

    private static SharedPreferences sharedPreferences;

    private String masterKeyAlias;


    public String getKey() {
        return sharedPreferences.getString("key", null);
    }

    public void setKey(String key) {
        sharedPreferences.edit().putString("key", key).apply();

    }


    public SharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }


    public String getStrings(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void putUser(Login user, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        sharedPreferences.edit().putString(key, json).apply();

    }

    public Login getUser(String key) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        return gson.fromJson(json, Login.class);

    }

    public void clearSpecificKey(String key) {
        sharedPreferences.edit().remove(key).apply();

    }

}
