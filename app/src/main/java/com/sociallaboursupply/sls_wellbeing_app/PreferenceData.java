package com.sociallaboursupply.sls_wellbeing_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceData
{
    static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";
    static final String PREF_USER_TYPE = "logged_in_type";
    static final String PREF_USER_ID = "user_id";
    static final String USER_TYPE_ADMIN = "admin";
    static final String USER_TYPE_MENTOR = "mentor";
    static final String USER_TYPE_USER = "user";

    public static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserLoggedInStatus(Context ctx, boolean status)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
        editor.apply();
    }

    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }


    public static void setUserTypeAdmin(Context ctx) {
        setUserType(ctx, USER_TYPE_ADMIN);
    }
    public static void setUserTypeMentor(Context ctx) {
        setUserType(ctx, USER_TYPE_MENTOR);
    }
    public static void setUserTypeUser(Context ctx) {
        setUserType(ctx, USER_TYPE_USER);
    }
    public static void setUserType(Context ctx, String type)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_TYPE, type);
        editor.apply();
    }

    public static Boolean userIsAdmin(Context ctx) {
        return getUserType(ctx).contentEquals(USER_TYPE_ADMIN);
    }
    public static Boolean userIsMentor(Context ctx) {
        return getUserType(ctx).contentEquals(USER_TYPE_MENTOR);
    }
    public static Boolean userIsUser(Context ctx) {
        return getUserType(ctx).contentEquals(USER_TYPE_USER);
    }
    public static String getUserType(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_TYPE, USER_TYPE_USER);
    }

    public static void setUserID(Context ctx, int id)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_USER_ID, id);
        editor.apply();
    }
    public static int getUserID(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_USER_ID, 0);
    }
    public static void clearPreferences(Context ctx){
        Editor editor =getSharedPreferences(ctx).edit();
        editor.clear();
        editor.apply();
    }
}