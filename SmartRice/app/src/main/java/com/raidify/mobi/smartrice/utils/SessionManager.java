package com.raidify.mobi.smartrice.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    /** Shared Preferences*/
     SharedPreferences pref;

     /**Editor for Shared preferences*/
    SharedPreferences.Editor editor;

    /** Context*/
    Context _context;

    /** Shared pref mode*/
    int PRIVATE_MODE = 0;

/** Sharedpref file name*/
private static final String PREF_NAME = "AccountPref";

    /** All Shared Preferences Keys*/
    private static final String IS_LOGIN = "isLoggedIn";
    public static final String KEY_NAME = "name";
    public  static final String  KEY_ROLE = "role";
    public static final String KEY_ID = "id";

/** Constructor*/
public SessionManager(Context context){

    this._context = context;
    pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    editor = pref.edit();
}

    public void createLoginSession(String id, String name, String role ){

// Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        //Store other preference keys
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_ROLE, role);
// commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
// user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
//user id and role
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));
// return user
        return user;
    }

    public String getUserId(){
    return pref.getString(KEY_NAME, null);
    }

    public boolean isLogin(){
// Check login status
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser(){
// Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

}

