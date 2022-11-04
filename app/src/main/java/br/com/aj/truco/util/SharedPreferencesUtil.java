package br.com.aj.truco.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {

    public static  final  String KEY_PARTIDAID_ATIVA = "KEY_PARTIDAID_ATIVA";


    public static SharedPreferences getAppSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
