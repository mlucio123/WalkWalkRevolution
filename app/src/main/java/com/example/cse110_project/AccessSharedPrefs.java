package com.example.cse110_project;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesClient {


    public SharedPreferencesClient() {}

    private static SharedPreferences setUp(Context context) {
        return context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    public boolean setUserInfo(Context context, String fName, String lName, int feet, int inch ) {
        SharedPreferences prefs = setUp(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("firstname", fName);
        editor.putString("lastname", lName);
        editor.putInt("heightFt", feet);
        editor.putInt("heightInch", inch);
        editor.putBoolean("STORED", false);

        return editor.commit();
    }

}
