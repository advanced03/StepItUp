package com.sof.stepitup.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.sof.stepitup.Login;

import java.sql.Array;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int Private_mode = 0;
    //PREF_NAME kan volgens mij alles zijn
    public static final String PREF_NAME = "AndroidHivePref";
    public static final String IS_LOGGED = "isLoggedIn";

    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String ROLE = "role";
//    public static final String STEPS = "steps";

    public SessionManager (Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Private_mode);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String username, String email, String role) {
        editor.putInt(USER_ID, userId);
//        editor.putInt(STEPS, steps);
        editor.putString(USERNAME, username);
        editor.putString(EMAIL, email);
        editor.putString(ROLE, role);
        editor.putBoolean(IS_LOGGED, true);

        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED, false);
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    public Object[] getUserInfo() {
        int id = pref.getInt(USER_ID, 0);
        String username = pref.getString(USERNAME, "NO_USERNAME_FOUND");
        String email = pref.getString(EMAIL, "NO_EMAIL_FOUND");
        String role = pref.getString(ROLE, "NO_ROLE_FOUND");

        return new Object[]{id,username,email,role};
    }

//    public void updateSteps(int steps) {
//        editor.putInt(STEPS, 0);
//        editor.apply();
//    }
}
