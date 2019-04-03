package com.app.quico.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.app.quico.activities.MainActivity;
import com.app.quico.entities.UserEnt;
import com.app.quico.retrofit.GsonFactory;

import java.util.Locale;


public class BasePreferenceHelper extends PreferenceHelper {

    private Context context;

    protected static final String KEY_LOGIN_STATUS = "islogin";
    protected static final String KEY_GUEST_STATUS = "isguest";
    protected static final String KEY_SOCIAL_STATUS = "isSocial";
    protected static final String KEY_LANGUAGE_STATUS = "Islanguage";
    protected static final String TOKEN = "TOKEN";
    protected static final String KEY_USER = "KEY_USER";
    private static final String FILENAME = "preferences";
    protected static final String CHAT_KEY = "CHAT_KEY";
    protected static final String Firebase_TOKEN = "Firebasetoken";
    protected static final String CHAT_THREAD_KEY = "chat_thread_key";
    protected static final String NotificationCount = "NotificationCount";
    protected static final String KEY_DEFAULT_LANG = "keyLanguage";


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

    public void setChatScreen( boolean isChat ) {
        putBooleanPreference( context, FILENAME, CHAT_KEY, isChat );
    }

    public boolean isChatScreen() {
        return getBooleanPreference(context, FILENAME, CHAT_KEY);
    }

    public String getChatThreadid() {
        return getStringPreference(context, FILENAME, CHAT_THREAD_KEY);
    }

    public void setChatThreadId(String receiverId) {
        putStringPreference(context, FILENAME, CHAT_THREAD_KEY, receiverId);
    }


    public void putLang(Activity activity, String lang) {
        Log.v("lang", "|" + lang);
        Resources resources = context.getResources();

        if (lang.equals("ar")){
            lang = "ar";}
        else{
            lang = "en";}

        putStringPreference(context, FILENAME, KEY_DEFAULT_LANG, lang);
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = new Locale(lang);
        conf.setLayoutDirection(Locale.ENGLISH);
        resources.updateConfiguration(conf, dm);

        ((MainActivity) activity).restartActivity();

    }



    public String getLang() {
        return getStringPreference(context, FILENAME, KEY_DEFAULT_LANG);
    }

    public boolean isLanguageArabian() {
        return getLang().equalsIgnoreCase("ar");
    }
}
