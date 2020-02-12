package com.example.cse110_project;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class AccessSharedPrefs {


    public AccessSharedPrefs() {}

    private static SharedPreferences setUp(Context context) {
        return context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    public static void setUserInfo(Context context, String fName, String lName, int feet, int inch ) {
        SharedPreferences prefs = setUp(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("firstname", fName);
        editor.putString("lastname", lName);
        editor.putInt("heightFt", feet);
        editor.putInt("heightInch", inch);
        editor.putBoolean("STORED", false);

        editor.apply();
    }

    public static String getFirstName(Context context) {
        return setUp(context).getString("firstname", "");

    }
    public static String geLasttName(Context context) {
        return setUp(context).getString("firstname", "");

    }

    public static int getHtFeet(Context context) {
        return setUp(context).getInt("heightFt", -1);

    }

    public static int getHtInch(Context context) {
        return setUp(context).getInt("heightInch", -1);

    }

    public static void setWalkStatus(Context context, boolean status) {
        SharedPreferences prefs = setUp(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("walkStatus", status);
        editor.apply();
    }

    public static void setWalkStartTime(Context context, long startTIme) {
        SharedPreferences prefs = setUp(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("walkStartTime", startTIme);
        editor.apply();
    }

    public static long getWalkStartTime(Context context) {
        SharedPreferences prefs = setUp(context);
        return prefs.getLong("walkStartTime", -1);
    }

    public static boolean getWalkStatus(Context context) {
        SharedPreferences prefs = setUp(context);
        if(!prefs.contains("walkStatus")) return false;
        return prefs.getBoolean("walkStatus", false);
    }


    /*public static void setFirstName(Context context) {
        return setUp(context).getString("firstname", "");

    }
    public static void geLasttName(Context context) {
        return setUp(context).getString("firstname", "");

    }

    public static void getHtFeet(Context context) {
        return setUp(context).getInt("heightFt", -1);

    }

    public static void getHtInch(Context context) {
        setUp(context).getInt("heightInch", -1);

    }*/

}
