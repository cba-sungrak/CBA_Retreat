package kr.or.sungrak.cba.cba_camp.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import kr.or.sungrak.cba.cba_camp.models.MyInfo;
import retrofit2.Response;

public class CBAUtil {
    private static final String TAG = "CBA/CBAUtil";
    private static final String RETREAT_TITLE = "Retreat_Title";
    private static final String ADMIN = "check_admin";
    private static final String CURRENT_DEPARTMENT = "Current_Department";

    public static MyInfo loadMyInfo(Context context) {
        Gson gson = new Gson();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String json = pref.getString("MyInfo", "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, MyInfo.class);
    }

    public static void saveMyInfo(Context context, Response<MyInfo> response) {
        String myInfo = new Gson().toJson(response.body());
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("MyInfo", myInfo).apply();
    }

    public static void removeMyInfo(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove("MyInfo").apply();
    }

    public static void removeAllPreferences(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove("MyInfo")
                .remove("GBSInfo")
                .remove(ADMIN)
                .apply();
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
    }

    public static void setRetreat(Context context, String s) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(RETREAT_TITLE, s).apply();
    }

    public static String getRetreat(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(RETREAT_TITLE, "");
    }

    public static void setCurrentDepartment(Context context, String s) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(CURRENT_DEPARTMENT, s).apply();
    }

    public static String getCurrentDepartment(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(CURRENT_DEPARTMENT, "");
    }

    public static void setAdmin(Context context, boolean isAdmin) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(ADMIN, isAdmin).apply();
    }

    public static boolean isAdmin(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ADMIN, false);
    }

    public static String getPhoneNumber(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            return "";

        }
        String phoneNumber;

        TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {

            @SuppressLint("HardwareIds") String tmpPhoneNumber = mgr.getLine1Number();
            phoneNumber = tmpPhoneNumber.replace("+82", "0");

        } catch (Exception e) {
            phoneNumber = "";
        }

        return phoneNumber;

    }

    public static void setPref(Context context, String key, String s) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, s);
        Log.e(TAG, "setPref : key[" + key + "] value[" + s + "]");
        editor.apply();
    }

    public static String getPref(Context context, String key) {
        Log.e(TAG, "getPref : [" + key + "]");
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }

    public static boolean isSunDay(String inputDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = dateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK);
    }
}

