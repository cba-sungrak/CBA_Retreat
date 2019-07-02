package kr.or.sungrak.cba.cba_retreat.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import kr.or.sungrak.cba.cba_retreat.models.MyInfo;

public class CBAUtil {
    private static final String TAG = "CBA/CBAUtil";


    public static MyInfo loadMyInfo(Context context) {
        Gson gson = new Gson();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String json = pref.getString("MyInfo", "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Log.i(TAG, "MyInfo " + json);
        return gson.fromJson(json, MyInfo.class);
    }


    public static void removeAllPreferences(Context context) {
        SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().remove("MyInfo").commit();
        pref.edit().remove("GBSInfo").commit();
    }

    public static void signOut(Context context) {
        removeAllPreferences(context);
        FirebaseAuth.getInstance().signOut();
    }

    public static String getCurrentDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        fmt.setCalendar(calendar);
        return fmt.format(calendar.getTime());
//        return String.format("%d-%d-%d", mYear, mMonth + 1, mDay);
    }

    public static void setRetreatTitle(Context context, String s){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Tag.RETREAT_TITLE, s);
        editor.commit();
    }

    public static String getRetreatTitle(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Tag.RETREAT_TITLE, "");
    }

}
