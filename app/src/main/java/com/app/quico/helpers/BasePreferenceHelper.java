package com.app.quico.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.quico.entities.UserEnt;
import com.app.quico.retrofit.GsonFactory;


public class BasePreferenceHelper extends PreferenceHelper {

    private Context context;

    protected static final String KEY_LOGIN_STATUS = "islogin";
    protected static final String KEY_GUEST_STATUS = "isguest";
    protected static final String KEY_SOCIAL_STATUS = "isSocial";
    protected static final String KEY_LANGUAGE_STATUS = "Islanguage";
    protected static final String TOKEN = "TOKEN";
    protected static final String KEY_USER = "KEY_USER";
    private static final String FILENAME = "preferences";

    protected static final String Firebase_TOKEN = "Firebasetoken";

    protected static final String NotificationCount = "NotificationCount";


    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void setLoginStatus( boolean isLogin ) {
        putBooleanPreference( context, FILENAME, KEY_LOGIN_STATUS, isLogin );
    }

    public boolean isGuestUser() {
        return getBooleanPreference(context, FILENAME, KEY_GUEST_STATUS);
    }


    public void setGuestStatus( boolean isGuest ) {
        putBooleanPreference( context, FILENAME, KEY_GUEST_STATUS, isGuest );
    }

    public boolean isSocialLogin() {
        return getBooleanPreference(context, FILENAME, KEY_SOCIAL_STATUS);
    }


    public void setSocialLogin( boolean isSocial ) {
        putBooleanPreference( context, FILENAME, KEY_SOCIAL_STATUS, isSocial );
    }
    public boolean isLanguageSelected() {
        return getBooleanPreference(context, FILENAME, KEY_LANGUAGE_STATUS);
    }


    public void setLanguageSelected( boolean isSocial ) {
        putBooleanPreference( context, FILENAME, KEY_LANGUAGE_STATUS, isSocial );
    }

    public boolean isLogin() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }


    public String getFirebase_TOKEN() {
        return getStringPreference(context, FILENAME, Firebase_TOKEN);
    }

    public void setFirebase_TOKEN(String _token) {
        putStringPreference(context, FILENAME, Firebase_TOKEN, _token);
    }
    public int getNotificationCount() {
        return getIntegerPreference(context, FILENAME, NotificationCount);
    }

    public void setNotificationCount(int count) {
        putIntegerPreference(context, FILENAME, NotificationCount, count);
    }

    public String get_TOKEN() {
        return getStringPreference(context, FILENAME, TOKEN);
    }

    public void set_TOKEN(String token) {
        putStringPreference(context, FILENAME, TOKEN, token);
    }



    public UserEnt getUser() {
        return GsonFactory.getConfiguredGson().fromJson(getStringPreference(context, FILENAME, KEY_USER), UserEnt.class);
    }

    public void putUser(UserEnt user) {
        putStringPreference(context, FILENAME, KEY_USER, GsonFactory.getConfiguredGson().toJson(user));
    }

}
