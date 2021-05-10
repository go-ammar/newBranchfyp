package com.fypapplication.fypapp.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.transition.ChangeBounds;

import com.fypapplication.fypapp.models.ChangesDue;
import com.fypapplication.fypapp.models.Login;
import com.fypapplication.fypapp.models.MechMyAccount;
import com.fypapplication.fypapp.models.MechServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefs {

    private static final String MY_PREFS_NAME = "com.sudoware.seedcode.delivery-25-3-2020";

    private static SharedPreferences sharedPreferences;

    private String masterKeyAlias;


    public SharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getKey() {
        return sharedPreferences.getString("key", null);
    }

    public void setKey(String key) {
        sharedPreferences.edit().putString("key", key).apply();
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

    public void putUser(Login user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        sharedPreferences.edit().putString("userKey", json).apply();
    }

    public Login getUser() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("userKey", null);
        return gson.fromJson(json, Login.class);

    }

    public ArrayList<ChangesDue> getChangesList() {
        ArrayList<ChangesDue> changesDues;
        Gson gson = new Gson();
        String json = sharedPreferences.getString("changes", "");
        assert json != null;
        if (json.isEmpty()) {
            changesDues = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<ChangesDue>>() {
            }.getType();
            changesDues = gson.fromJson(json, type);
        }
        return changesDues;
    }

    public void putChangesList(ArrayList<ChangesDue> changesDues) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(changesDues);
        prefsEditor.putString("changes", json);
        prefsEditor.apply();
    }

    public ArrayList<MechServices> getMechServices() {
        ArrayList<MechServices> mechServices;
        Gson gson = new Gson();
        String json = sharedPreferences.getString("mechservices", "");
        assert json != null;
        if (json.isEmpty()) {
            mechServices = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<MechServices>>() {
            }.getType();
            mechServices = gson.fromJson(json, type);
        }
        return mechServices;
    }

    public void putMechServicesList(ArrayList<MechServices> mechServices) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mechServices);
        prefsEditor.putString("mechservices", json);
        prefsEditor.apply();
    }

    public ArrayList<MechMyAccount> getMechMyAccount() {
        ArrayList<MechMyAccount> mechMyAccounts;
        Gson gson = new Gson();
        String json = sharedPreferences.getString("mechaccount", "");
        assert json != null;
        if (json.isEmpty()) {
            mechMyAccounts = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<MechMyAccount>>() {
            }.getType();
            mechMyAccounts = gson.fromJson(json, type);
        }
        return mechMyAccounts;
    }

    public void putMechAcountList(ArrayList<MechMyAccount> mechServices) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mechServices);
        prefsEditor.putString("mechaccount", json);
        prefsEditor.apply();
    }


    public void clearSpecificKey(String key) {
        sharedPreferences.edit().remove(key).apply();

    }

}
